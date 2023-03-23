package com.jark.template.common.utils.exception;

import java.io.Serializable;


/**
 * @author ponder
 */
public abstract class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -129608967045158032L;

    public BaseException(final String message) {
        super(message);
    }

    public BaseException(final Exception exception) {
        super(exception);
    }

    public BaseException(final String message, final Exception exception) {
        super(message, exception);
    }

}
