package com.example.movie.ticket.booking.service;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.dto.UserDto;
import com.example.movie.ticket.booking.entity.Role;
import com.example.movie.ticket.booking.entity.User;
import com.example.movie.ticket.booking.repository.RoleRepository;
import com.example.movie.ticket.booking.repository.UserRepository;
import com.example.movie.ticket.booking.service.impl.UserLogInServiceImplemntation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class UserLoginServiceTest {

    @InjectMocks
    private UserLogInServiceImplemntation userService; // the class under test

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDto userDto;
    private User userEntity;

    @BeforeEach
    void setup() {
        userDto = new UserDto();
        userDto.setFullName("Alice");
        userDto.setEmail("alice@example.com");
        userDto.setPassword("plain");

        userEntity = new User();
        userEntity.setFullName("Alice");
        userEntity.setEmail("alice@example.com");
        userEntity.setPassword("encoded");
    }

    @Test
    void createUser_shouldSaveWhenEmailNotExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(modelMapper.map(userDto, User.class)).thenReturn(userEntity);
        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        userService.createUser(userDto, "ROLE_USER");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowIfEmailExists() {
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(userDto, "ROLE_USER"));
        assertEquals("Email already in use", ex.getMessage());
    }

    @Test
    void findUserByEmail_shouldReturnMappedDto() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(userEntity);
        UserDto mappedDto = new UserDto();
        mappedDto.setFullName("Alice");
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(mappedDto);

        UserDto result = userService.findUserByEmail("alice@example.com");

        assertNotNull(result);
        assertEquals("Alice", result.getFullName());
    }

    @Test
    void findAllUsers_shouldMapAll() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        UserDto mapped = new UserDto();
        mapped.setFullName("Alice");
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(mapped);

        var users = userService.findAllUsers();
        assertEquals(1, users.size());
        assertEquals("Alice", users.get(0).getFullName());
    }
}
