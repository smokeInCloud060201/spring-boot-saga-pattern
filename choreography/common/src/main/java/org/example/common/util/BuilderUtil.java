package org.example.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.common.dtos.BaseResponse;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuilderUtil {

    private static <T> BaseResponse<T> buildResponse(T data, Exception exception, String errorCode) {
        BaseResponse.BaseResponseBuilder<T> baseResponseBuilder = BaseResponse.builder();
        if (exception != null) {
            baseResponseBuilder.errorCode(errorCode);
            baseResponseBuilder.timeStamp(LocalDateTime.now().toString());
            baseResponseBuilder.errorMessage(exception.getMessage());
        }

        baseResponseBuilder.data(data);

        return baseResponseBuilder.build();
    }

    public static <T> BaseResponse<T> buildResponse(Exception exception, String errorCode) {
        return buildResponse(null, exception, errorCode);

    }

    public static <T> BaseResponse<T> buildResponse(T data) {
        return buildResponse(data, null, null);
    }
}
