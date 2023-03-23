package com.jark.template.sdk.param;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Ponder
 */
@Mapper
public interface ParamConvert {

    /**
     *
     */
    ParamConvert INSTANCE = Mappers.getMapper(ParamConvert.class);


}



