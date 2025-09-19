package com.papatoncio.taskapi.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
        boolean success,
        Object data,
        String message,
        Map<String, Object> meta
) {
}
