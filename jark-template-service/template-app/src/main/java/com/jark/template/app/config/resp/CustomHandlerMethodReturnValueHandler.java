package com.jark.template.app.config.resp;

import java.io.PrintWriter;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.json.JsonUtil;
import com.jark.template.common.utils.resp.R;
import com.jark.template.common.utils.resp.custom.CustomReturn;
import com.jark.template.common.utils.resp.custom.ReturnValue;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * @author ponder
 */
public class CustomHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(final MethodParameter returnType) {
        final Class<?> containingClass = returnType.getContainingClass();
        return containingClass.isAnnotationPresent(RestController.class) && containingClass.getName().startsWith("com.jark");
    }

    @Override
    public void handleReturnValue(final Object returnValue, final MethodParameter returnType, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest)
        throws Exception {
        mavContainer.setRequestHandled(true);

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = requestAttributes.getRequest();
        final String from = request.getHeader(HeaderConst.FROM);
        final HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/json;charset=UTF-8");
        final PrintWriter out = response.getWriter();
        final ReturnValue customReturnData = returnType.getMethodAnnotation(ReturnValue.class);

        Object data = null;
        if (ObjectUtil.isNotEmpty(customReturnData)) {
            response.setContentType(customReturnData.contentType());
            final Class<? extends CustomReturn> clazz = customReturnData.clazz();
            final CustomReturn customReturn = clazz.getDeclaredConstructor().newInstance();
            data = customReturn.of(returnValue);
        } else if (StrUtil.isEmpty(from)) {
            if (returnValue instanceof R) {
                data = returnValue;
            } else {
                data = R.ok(returnValue);
            }
        }
        out.write(Objects.requireNonNull(JsonUtil.toJSON(data)));
        out.close();
    }
}
