package com.example.movie.ticket.booking.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.example.movie.ticket.booking.dto.UserDto;
import com.example.movie.ticket.booking.entity.Role;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.RoleRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.UserLogInService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;


@Service
@AllArgsConstructor
public class UserLogInServiceImplemntation implements UserLogInService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;



    public void createUser(UserDto userDto , String roleUser) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = createRoleIfNotExist(roleUser);

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    // Create role if it does not exist
    public Role createRoleIfNotExist(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    @Override
    public UserDto findUserByEmail(String email) {
//        User user = userRepository.findByEmail(userDetails.getUsername());
        User findUser =  userRepository.findByEmail(email);
        if(findUser == null ){
            return  null;
        }
        return modelMapper.map(findUser, UserDto.class);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

}
