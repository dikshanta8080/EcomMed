package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.request.RegistrationRequest;
import com.acharya.dikshanta.EcomMed.dto.response.UserResponse;
import com.acharya.dikshanta.EcomMed.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(RegistrationRequest request);

    UserResponse toResponse(User user);
}
