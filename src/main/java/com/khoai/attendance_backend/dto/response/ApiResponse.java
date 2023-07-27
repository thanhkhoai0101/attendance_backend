package com.khoai.attendance_backend.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @param <T> Type of Data
 */
@Builder
@Getter
@Setter
public class ApiResponse<T> implements Serializable {
    @Schema(description = "Response code, 0 is success, 422 is validation error, etc...")
    @Builder.Default
    private String code = "0";
    @Schema(description = "Response message, may be null if request success")
    private String message;
    @Schema(description = "Response errors if any, may be null if request success")
    private Map<String, String> errors;
    @Schema(description = "Response data")
    private T data;
}
