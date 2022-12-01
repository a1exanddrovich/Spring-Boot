package com.epam.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.dto.UserDto;
import com.epam.dto.UserDtoMapper;
import com.epam.model.Status;
import com.epam.model.User;
import com.epam.repository.RoleRepository;
import com.epam.repository.UserRepository;

@Service
public class UserService {

    private static final String USER_ROLE_NAME = "USER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final UserDtoMapper dtoMapper = Mappers.getMapper(UserDtoMapper.class);

    public UserDto add(UserDto userDto) {
        User user = dtoMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName(USER_ROLE_NAME)));
        user.setStatus(Status.ACTIVE);

        return dtoMapper.userToUserDto(userRepository.save(user));
    }

    public List<UserDto> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(dtoMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserDto> findById(Long id) {
        return userRepository
                .findById(id)
                .map(dtoMapper::userToUserDto);

    }

    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    public Optional<UserDto> update(UserDto userDto, Long id) {
        Optional<User> optionalUserDto = userRepository.findById(id);
        if (optionalUserDto.isPresent()) {
            User user = dtoMapper.userDtoToUser(userDto);
            user.setId(id);
            return Optional.of(dtoMapper.userToUserDto(userRepository.save(user)));
        }
        return Optional.empty();
    }

}
