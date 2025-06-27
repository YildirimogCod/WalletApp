package com.yildirimog.walletapi.auth.mapper;

import com.yildirimog.walletapi.auth.dto.RegisterRequest;
import com.yildirimog.walletapi.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * RegisterRequest DTO'sunu User entity'sine dönüştürür.
 * MapStruct ile otomatik implementasyon üretilir.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest dto);
}
