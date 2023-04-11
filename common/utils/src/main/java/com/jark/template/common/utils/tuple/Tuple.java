package com.jark.template.common.utils.tuple;


import java.util.List;

/**
 *  思路来自 var Tuple
 */
public sealed interface Tuple permits Tuple2 {

    int arity();

    List<?> toList();


}


