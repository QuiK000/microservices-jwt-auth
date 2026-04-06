package com.dev.quikkkk.auth_service.mapper;

import com.dev.quikkkk.auth_service.dto.request.RegistrationRequest;
import com.dev.quikkkk.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegistrationRequest request);
}
