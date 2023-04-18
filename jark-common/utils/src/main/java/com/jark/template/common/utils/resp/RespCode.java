package com.jark.template.common.utils.resp;

/**
 * 全局返回状态码
 * 以 1XXXX 开头的为全局返回常量
 * 1XXXX 接口为 "10"+HTTP状态码
 *
 * @author ponder
 */
public interface RespCode {

    /****************************全局异常************************************************/
    /**
     * Continue
     * 初始的请求已经接受，客户应当继续发送请求的其余部分。（HTTP 1.1新）
     */
    int CONTINUE = 10100;

    /**
     * 服务器将遵从客户的请求转换到另外一种协议（HTTP 1.1新）0
     */
    int UPGRADE_PROTO = 10101;

    /**
     * 请求成功
     */
    int OK = 10200;

    /**
     * 已经接受请求，但处理尚未完成。
     */
    int ACCEPTED = 10202;

    /**
     * 客户请求的文档在其他地方
     * 新的URL在Location头中给出
     * 浏览器应该自动地访问新的URL
     */
    int MOVED = 10301;

    /**
     * 请求失败,语法错误
     * 参数不合法
     */
    int PARAM_ERR = 10400;

    /**
     * 鉴权失败
     * 签名校验失败
     */
    int UNAUTHORIZED = 10401;

    /**
     * 禁止重复提交
     */
    int RESUBMIT_ERROR = 10402;

    /**
     * 资源不可用
     */
    int FORBIDDEN = 10403;

    /**
     * 没有此接口
     */
    int NOT_FOUND = 10404;

    /**
     * 不支持的请求方法
     */
    int METHOD_NOT_ALLOW = 10405;

    /**
     * 请求超时
     */
    int TIME_OUT = 10408;

    /**
     * 不支持的媒体类型
     */
    int MEDIA_NOT_SUPPORT = 10415;

    /**
     * 请求过于频繁
     */
    int TOO_MANY_REQUEST = 10429;

    /**
     * 服务器异常
     */
    int SERVER_ERR = 10500;

    /**
     * 服务不可用
     */
    int SERVICE_NOT_WORKING = 10501;

    /**
     * 拒绝服务
     */
    int SERVER_UNAVAILABLE = 10503;

    /****************************全局异常************************************************/
    /**
     * 以下数据以  2 开头  开头 表示特定情况下的错误信息,唯一,通用
     */
    /****************************user异常***********************************************/

    /**
     * 密码错误
     */
    int LOGIN_PASSWORD_ERROR = 20200;

    /**
     * 用户不存在
     */
    int USER_NOT_FOUND = 20201;
    /**
     * 用户未登录
     */
    int USER_NOT_LOGIN = 20202;
}
