package com.papatoncio.taskapi.utils;

import com.papatoncio.taskapi.dto.common.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ResponseFactory {
    // ------------------- ÉXITO -------------------
    public static ApiResponse success(Object data) {
        return success(data, "Operación exitosa", null);
    }

    public static ApiResponse success(Object data, String message) {
        return success(data, message, null);
    }

    public static ApiResponse success(Object data, String message, Map<String, Object> meta) {
        return new ApiResponse(true, data, message, meta);
    }

    public static ApiResponse created(Object data, String message) {
        return new ApiResponse(true, data, message, null);
    }

    // ------------------- ERROR -------------------
    public static ApiResponse error(String message) {
        return error(null, message, null, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse error(String message, HttpStatus status) {
        return error(null, message, null, status);
    }

    public static ApiResponse error(Object data, String message, Map<String, Object> meta, HttpStatus status) {
        return new ApiResponse(false, data, message, meta);
    }

    // ------------------- PAGINACIÓN -------------------
    public static ApiResponse paginated(Object data, Map<String, Object> meta) {
        return new ApiResponse(true, data, "Operación exitosa", meta);
    }
}
