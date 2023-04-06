package com.jark.upms.sdk.resp;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Ponder
 */
@Mapper
public interface RespBasicConvert {

    /**
     *
     */
    RespBasicConvert INSTANCE = Mappers.getMapper(RespBasicConvert.class);


}



