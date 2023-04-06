package com.jark.template.sdk.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Ponder
 */
@Mapper
public interface DtoBasicConvert {

    /**
     *
     */
    DtoBasicConvert INSTANCE = Mappers.getMapper(DtoBasicConvert.class);


}



