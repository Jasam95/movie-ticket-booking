package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Role;
import com.example.movie.ticket.booking.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @Test
    void saveUserWithRole_shouldPersist() {
        Role role = new Role(null, "ROLE_USER", List.of());
        User user = new User();
        user.setFullName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("encoded");
        user.setRoles(List.of(role));

        userRepository.save(user);

        User found = userRepository.findByEmail("alice@example.com");
        assertThat(found).isNotNull();
        assertThat(found.getRoles().get(0).getName()).isEqualTo("ROLE_USER");
    }
}
