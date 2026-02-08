package com.example.day3_java.service;

import com.example.day3_java.dto.request.OrderCreateRequest;
import com.example.day3_java.dto.request.OrderItemRequest;
import com.example.day3_java.dto.response.OrderItemResponse;
import com.example.day3_java.dto.response.OrderResponse;
import com.example.day3_java.entity.*;
import com.example.day3_java.exception.BadRequestException;
import com.example.day3_java.exception.ResourceNotFoundException;
import com.example.day3_java.repository.OrderRepository;
import com.example.day3_java.repository.ProductRepository;
import com.example.day3_java.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private Map<Long, Integer> mergeItems(List<OrderItemRequest> itemRequests){
        Map<Long,Integer> merged = new HashMap<>();

        for (OrderItemRequest orderItemRequest : itemRequests){
            merged.put(orderItemRequest.getProductId(),
                    merged.getOrDefault(orderItemRequest.getProductId(),0)+orderItemRequest.getQuantity());
            // cộng dồn quantity theo productId
        }
        return merged;
    }
    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCreatedAt(order.getCreatedAt());
        response.setStatus(order.getStatus().name());
        // order.getStatus(): enum OrderStatus
        // .name(): chuyển enum -> String ("NEW", "PAID", "CANCELED")
        double total = 0;

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem orderItem : order.getItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setProductId(orderItem.getProduct().getId());
            orderItemResponse.setProductName(orderItem.getProduct().getName());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setUnitPrice(orderItem.getUnitPrice());


            double lineTotal = orderItem.getUnitPrice() * orderItem.getQuantity();
            orderItemResponse.setLineTotal(lineTotal);

            total += lineTotal;
            itemResponses.add(orderItemResponse);
        }
        response.setItem(itemResponses);
        response.setTotal(total);
        return response;
    }

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository= userRepository;
    }

    @Transactional
    // @Transactional: nếu có lỗi giữa chừng -> rollback toàn bộ
    // tránh trường hợp trừ stock xong nhưng order chưa lưu
    public OrderResponse create(OrderCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("ko tìm thấy user: "+username));

        Order order = new Order();
        order.setUser(user);
        List<OrderItem> items = new ArrayList<>();

        // 1) gộp quantity theo productId
        Map<Long, Integer> merged = mergeItems(request.getItems());
        // 2) loop theo merged để check stock 1 lần/product
        for (Map.Entry<Long, Integer> entry : merged.entrySet()) {
            Long productId = entry.getKey();
            int buyQty = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Ko tìm thấy productId " + productId));
            //check gia
            if (product.getStock() < buyQty) {
                throw new BadRequestException(
                        "Ko đủ số lượng của productId=" + productId
                                + ", còn=" + product.getStock()
                                + ", yêu cầu=" + buyQty
                );
            }

            // trừ stock
            product.setStock(product.getStock() - buyQty);
            // do product đang managed trong transaction -> cuối tx sẽ update DB
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(buyQty);
            orderItem.setUnitPrice(product.getPrice());

            items.add(orderItem);
        }
        order.setItems(items);
        order.setStatus(OrderStatus.NEW);
        Order saved = orderRepository.save(order);
        // cascade ALL -> sẽ lưu luôn order_items
        // product stock update -> cũng được flush cuối transaction

        return toResponse(saved);
    }
    public OrderResponse getById(Long id){
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ko tìm thấy id giao dịch : "+id));
        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancel(Long orderId){
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Ko tìm thấy order : "+orderId));

        if (order.getStatus() == OrderStatus.CANCELED){
            throw  new BadRequestException("Order đã hủy: "+orderId);
        }

        for (OrderItem item : order.getItems()){
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            // product là managed entity trong transaction -> cuối tx update DB
        }
        // đổi trạng thái
        order.setStatus(OrderStatus.CANCELED);
        // save không bắt buộc vì entity managed, nhưng để rõ ràng vẫn ok
        Order saved = orderRepository.save(order);

        return toResponse(saved);
    }
    public List<OrderResponse> myOrders(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return orderRepository.findAllByUsernameWithItems(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}

