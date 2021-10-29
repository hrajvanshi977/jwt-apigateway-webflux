package com.jwtgateway.project.dto.mapper;

import com.jwtgateway.project.dto.UserDto;
import com.jwtgateway.project.model.user.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface UserMapper {
    UserDto map(User user);

    @InheritInverseConfiguration
    User map(UserDto userDto);
}