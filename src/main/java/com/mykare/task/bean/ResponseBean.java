package com.mykare.task.bean;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ResponseBean {

    @Builder.Default
    private STATUS status = STATUS.SUCCESS;

    @Builder.Default
    private int statusCode = 200;

    private String message;

    private Object data;

    private String token;

    @Builder.Default
    private Date timeStamp = new Date();

    public static enum STATUS {
        SUCCESS,
        FAILED,
        WARNING,
    }
}
