package com.jark.template.common.web.config.exception;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.resp.R;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Slf4j
@Controller
public final class ErrorController extends AbstractErrorController {


    public ErrorController(final ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping("error")
    public ResponseEntity<Map<String, Object>> error(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestId = response.getHeader(HeaderConst.REQUEST_ID);
        final HttpStatus status = getStatus(request);
        log.error("用户请求异常:{},参数:{},status:{}", request.getRequestURI(), request.getQueryString(), status);
        final R<Void> result = R.fail(Integer.parseInt("10" + status.value()), status.name());
        result.setRequestId(requestId);
        return ResponseEntity.ok(result.toMap());
    }


}
