package com.epam.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.epam.dto.UserDto;
import com.epam.model.Role;
import com.epam.model.User;
import com.epam.repository.RoleRepository;
import com.epam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void shouldFindAllUsers() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDto> actual = sut.findAll();

        assertThat(actual, is(Arrays.asList(userDto)));
    }

    @Test
    void shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setPassword("test");

        User user = new User();
        user.setPassword("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");

        UserDto expected = new UserDto();
        expected.setPassword("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");

        when(passwordEncoder.encode("test")).thenReturn("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");
        when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto actual = sut.add(userDto);

        assertThat(actual, is(expected));
    }

    @Test
    void shouldFindUserByName() {
        Optional<User> expected = Optional.of(new User());
        String name = "Name";
        when(userRepository.findByUsername(name)).thenReturn(expected);

        Optional<User> actual = sut.findByUsername(name);

        assertThat(actual, is(expected));
    }

    @Test
    void shouldFindUserById() {
        Long id = 1L;
        Optional<User> expected = Optional.of(new User());
        Optional<UserDto> expectedDto = Optional.of(new UserDto());
        when(userRepository.findById(id)).thenReturn(expected);

        Optional<UserDto> actual = sut.findById(id);

        assertThat(actual, is(expectedDto));
    }

    @Test
    void shouldDeleteUserById() {
        Long id = 1L;
        Optional<User> optionalUser = Optional.of(new User());
        when(userRepository.findById(id)).thenReturn(optionalUser);
        doNothing().when(userRepository).deleteById(id);

        sut.deleteById(id);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldUpdateUserWhenIdFound() {
        UserDto userDto = new UserDto();
        Long id = 1L;

        User user = new User();
        Optional<UserDto> expected = Optional.of(new UserDto());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<UserDto> actual = sut.update(userDto, id);

        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<UserDto> actual = sut.update(new UserDto(), id);

        assertThat(actual, is(Optional.empty()));
    }

}
