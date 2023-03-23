package com.jark.template.common.utils.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ponder
 */
@Slf4j
public final class NotImplementedException extends BaseException {

    private static final long serialVersionUID = -1741997652245186385L;

    public NotImplementedException(final Exception ex) {
        super(ex);
    }

    public NotImplementedException(final String message, final Exception ex) {
        super(message, ex);
    }

    public NotImplementedException(final String message) {
        super(message);
    }
}
