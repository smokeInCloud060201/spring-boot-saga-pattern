package org.example.invertory.exception;
import org.example.common.dtos.BaseResponse;
import org.example.common.exception.BadOrderRequestException;
import org.example.common.util.BuilderUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerGlobal {

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleGlobalException(Exception e) {
        return BuilderUtil.buildResponse( e, "SERVER_ERROR");
    }

    @ExceptionHandler(BadOrderRequestException.class)
    public BaseResponse<Void> handleGlobalException(BadOrderRequestException e) {
        return BuilderUtil.buildResponse(e, "BAD_REQUEST");
    }
}
