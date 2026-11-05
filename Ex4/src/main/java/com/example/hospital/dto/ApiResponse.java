package com.example.hospital.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "code", "message", "data", "meta"})
public class ApiResponse<T> {

    /**
     * Trạng thái thực thi: "success" hoặc "error"
     */
    private String status;

    /**
     * HTTP Status code (200, 201, 400, 404, 500, ...)
     */
    private int code;

    /**
     * Thông điệp giải thích thêm (nếu có)
     */
    private String message;

    /**
     * Payload dữ liệu trả về (Object, Array, Map, ...)
     */
    private T data;

    /**
     * Thông tin siêu dữ liệu (Metadata / Phân trang)
     */
    private MetaData meta;

    public static <T> ApiResponse<T> success(T data, MetaData meta) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .data(data)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(code)
                .message(message)
                .build();
    }
}
