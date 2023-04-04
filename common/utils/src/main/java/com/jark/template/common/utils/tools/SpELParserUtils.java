package com.jark.template.common.utils.tools;

import java.lang.reflect.Method;
import java.util.Objects;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * SpEL 工具类
 */
@Slf4j
public final class SpELParserUtils {

    private static final String EXPRESSION_PREFIX = "#{";

    private static final String EXPRESSION_SUFFIX = "}";

    /**
     * 表达式解析器
     */
    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * 参数名解析器，用于获取参数名
     */
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


    private SpELParserUtils() {
    }

    /**
     * 解析spel表达式
     *
     * @param method 方法
     * @param args 参数值
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     * @param defaultResult 默认结果
     *
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(final Method method, final Object[] args, final String spelExpression, final Class<T> clz, final T defaultResult) {
        if (args == null || args.length == 0) {
            return defaultResult;
        }
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        T result = getResult(context, spelExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;
    }

    /**
     * 解析spel表达式
     *
     * @param method 方法
     * @param args 参数值
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     *
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(final Method method, final Object[] args, final String spelExpression, final Class<T> clz) {
        String[] params = parameterNameDiscoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return getResult(context, spelExpression, clz);
    }

    /**
     * 解析spel表达式
     *
     * @param param 参数名
     * @param paramValue 参数值
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     *
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(final String param, final Object paramValue, final String spelExpression, final Class<T> clz) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        return getResult(context, spelExpression, clz);
    }


    /**
     * 解析spel表达式
     *
     * @param param 参数名
     * @param paramValue 参数值
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     * @param defaultResult 默认结果
     *
     * @return 执行spel表达式后的结果
     */
    public static <T> T parse(final String param, final Object paramValue, final String spelExpression, final Class<T> clz, final T defaultResult) {
        EvaluationContext context = new StandardEvaluationContext();
        //设置上下文变量
        context.setVariable(param, paramValue);
        T result = getResult(context, spelExpression, clz);
        if (Objects.isNull(result)) {
            return defaultResult;
        }
        return result;

    }


    /**
     * 获取spel表达式后的结果
     *
     * @param context 解析器上下文接口
     * @param spelExpression 表达式
     * @param clz 返回结果的类型
     *
     * @return 执行spel表达式后的结果
     */
    private static <T> T getResult(final EvaluationContext context, final String spelExpression, final Class<T> clz) {
        try {
            //解析表达式
            Expression expression = parseExpression(spelExpression);
            //获取表达式的值
            return expression.getValue(context, clz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 解析表达式
     *
     * @param spelExpression spel表达式
     */
    private static Expression parseExpression(final String spelExpression) {
        // 如果表达式是一个#{}表达式，需要为解析传入模板解析器上下文
        if (spelExpression.startsWith(EXPRESSION_PREFIX) && spelExpression.endsWith(EXPRESSION_SUFFIX)) {
            return expressionParser.parseExpression(spelExpression, new TemplateParserContext());
        }
        return expressionParser.parseExpression(spelExpression);
    }

}

