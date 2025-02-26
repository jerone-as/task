package com.mykare.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.mykare.task.bean.ResponseBean;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice(basePackages = "com.mykare")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    private ResponseEntity<ResponseBean> throwGlobalException(GlobalException ex) {
        ResponseBean response = ResponseBean.builder()
                .status(ResponseBean.STATUS.FAILED)
                .statusCode(ex.status.value())
                .message(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(ex.status).body(response);
    }

    @ExceptionHandler(value = GlobalException.UnAuthorized.class)
    private ResponseEntity<ResponseBean> throwUnAuthoriseException(GlobalException.UnAuthorized ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ResponseBean response = ResponseBean.builder()
                .status(ResponseBean.STATUS.FAILED)
                .statusCode(httpStatus.value())
                .message(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(value = GlobalException.Forbidden.class)
    private ResponseEntity<ResponseBean> throwForbiddenException(GlobalException.Forbidden ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ResponseBean response = ResponseBean.builder()
                .status(ResponseBean.STATUS.FAILED)
                .statusCode(httpStatus.value())
                .message(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(value = GlobalException.InternalServerError.class)
    private ResponseEntity<ResponseBean> throwInternalServerException(GlobalException.InternalServerError ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseBean response = ResponseBean.builder()
                .status(ResponseBean.STATUS.FAILED)
                .statusCode(httpStatus.value())
                .message(ex.getLocalizedMessage())
                .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
