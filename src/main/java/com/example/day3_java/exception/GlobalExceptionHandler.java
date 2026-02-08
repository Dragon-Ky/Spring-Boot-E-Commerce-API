package com.example.day3_java.exception;

import com.example.day3_java.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //loi 404
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNotFound(ResourceNotFoundException exception){
        return ApiResponse.fail(exception.getMessage());
    }
    // loi 400 – validate fail xac dinh loi ky thuat
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidation (MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(e->errors.put(e.getField(),e.getDefaultMessage()));

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("xac thuc that bai");
        response.setData(errors);
        return response;
    }

    // loi 400 – tự throw loi nghiep vu
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleBadRequest(BadRequestException exception){
        return ApiResponse.fail(exception.getMessage());
    }
}
