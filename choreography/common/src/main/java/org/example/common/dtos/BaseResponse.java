package org.example.common.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse <T> {

    @JsonProperty("data")
    private T data;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_messages")
    private String errorMessage;

    @JsonProperty("time_stamp")
    private String timeStamp;
}
