package com.jark.template.app.config.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.jark.template.common.utils.assertion.BizException;
import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.resp.R;
import com.jark.template.common.utils.resp.RespCode;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 * 统一异常处理
 */
@Slf4j
@ControllerAdvice
@Order(-1)
public final class GlobalExceptionHandler {

    private static final String MESSAGE = "服务器异常";

    public ResponseEntity<R<Void>> buildResp(final R<Void> result) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = requestAttributes.getRequest();
        final String from = request.getHeader(HeaderConst.FROM);
        HttpHeaders headers = new HttpHeaders();
        if (StrUtil.isNotEmpty(from)) {
            headers.add(HeaderConst.FROM, from);
            headers.add(HeaderConst.EXCEPTION, HeaderConst.EXCEPTION);
        }
        return new ResponseEntity(result, headers, HttpStatus.OK);
    }

    //    @ExceptionHandler(FeignException.class)
    //    @ResponseBody
    //    public ResponseEntity<R<Void>> feignExceptionHandler(FeignException exception) {
    //        log.error("feign 请求异常:{}", exception.getMessage(), exception);
    //        return buildResp(R.fail(RespCode.SERVER_ERR, exception.getMessage()));
    //    }



    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> bizExceptionHandler(final BizException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(exception.getCode(), exception.getMessage()));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> illegalArgumentExceptionHandler(final IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(RespCode.PARAM_ERR, exception.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> illegalStateExceptionHandler(final IllegalStateException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(RespCode.PARAM_ERR, exception.getMessage()));
    }

    @ExceptionHandler(MismatchedInputException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> mismatchedInputExceptionHandler(final MismatchedInputException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> bindExceptionExceptionHandler(final BindException exception) {
        log.error(exception.getMessage(), exception);
        StringBuilder sb = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append(";"));
        return buildResp(R.fail(RespCode.PARAM_ERR, sb.substring(0, sb.length() - 1)));
    }

    @ResponseBody
    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<R<Void>> servletRequestBindingExceptionHandler(final ServletRequestBindingException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ResponseBody
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<R<Void>> missingPathVariableExceptionHandler(final MissingPathVariableException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R<Void>> httpRequestMethodNotSupportedExceptionHandler(final HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<R<Void>> httpMediaTypeNotSupportedExceptionHandler(final HttpMediaTypeNotSupportedException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<R<Void>> exceptionHandler(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail(MESSAGE));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> accessDeniedException(final AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return buildResp(R.fail("权限不足"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<R<Void>> maxUploadSizeExceededExceptionHandler(final MaxUploadSizeExceededException exception) {
        return buildResp(R.fail(RespCode.PARAM_ERR, "文件大小超过限制"));
    }

}
