package com.jark.upms.sdk.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Ponder
 */
@Mapper
public interface EntityConvert {

    /**
     *
     */
    EntityConvert INSTANCE = Mappers.getMapper(EntityConvert.class);


}



