package com.jark.template.common.utils.tuple;

import java.util.List;

/**
 * 两个参数的Tuple
 */
public final class Tuple2 implements Tuple {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public List<?> toList() {
        return null;
    }
}


