package com.epam.dto;

import org.mapstruct.Mapper;

import com.epam.model.User;

@Mapper
public interface UserDtoMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

}
