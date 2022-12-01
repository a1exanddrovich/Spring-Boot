package com.epam.controller;

import lombok.SneakyThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.epam.dto.UserDto;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@ContextConfiguration(classes = AdminController.class)
@WebMvcTest
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void shouldFindAllUsers() {
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        UserDto userDto3 = new UserDto();

        userDto1.setId(1L);
        userDto2.setId(2L);
        userDto3.setId(3L);

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2, userDto3);

        when(userService.findAll()).thenReturn(userDtos);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                        .with(user("username"))
                        .with(csrf())
                        .content(String.valueOf(userDtos))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        UserDto[] userDtosUnmapped = mapFromJson(content, UserDto[].class);
        assertThat(userDtosUnmapped[0], is(userDto1));
        assertThat(userDtosUnmapped[1], is(userDto2));
        assertThat(userDtosUnmapped[2], is(userDto3));
        assertTrue(userDtosUnmapped.length > 0);

    }

    @Test
    @SneakyThrows
    void shouldDeleteUser() {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);

        doNothing().when(userService).deleteById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/users/1")
                        .with(user("username"))
                        .with(csrf())
                        .content(String.valueOf(userDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @SneakyThrows
    private <T> T mapFromJson(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

}
