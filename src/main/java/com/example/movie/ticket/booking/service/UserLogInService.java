package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.dto.UserDto;
import com.example.movie.ticket.booking.entity.User;

import java.util.List;

public interface UserLogInService {

    void createUser(UserDto userDto , String roles);

    UserDto findUserByEmail(String email);

    List<UserDto> findAllUsers();

}
