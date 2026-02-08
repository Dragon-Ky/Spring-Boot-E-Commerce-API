package com.example.day3_java.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> oke (T data){
        ApiResponse<T> response = new ApiResponse<>();

        response.success=true;
        response.message="oke ko bug";
        response.data = data;
        return  response;
    }
    public static <T> ApiResponse<T> fail (String message){
        ApiResponse<T> response= new ApiResponse<>();

        response.success=false;
        response.message= message;
        response.data=null;
        return  response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
