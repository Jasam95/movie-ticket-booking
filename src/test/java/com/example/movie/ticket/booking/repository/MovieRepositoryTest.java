package com.example.movie.ticket.booking.repository;


import com.example.movie.ticket.booking.MovieTicketBookingApplication;
import com.example.movie.ticket.booking.entity.Movie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MovieTicketBookingApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class MovieRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void checkDataSource() throws Exception {
        System.out.println("DB URL: " + dataSource.getConnection().getMetaData().getURL());
    }



    @Test
    void verifyTablesCreated() throws Exception {
        var conn = dataSource.getConnection();
        var rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        System.out.println("🧩 Tables created:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString("TABLE_NAME"));
        }
        conn.close();
    }

    @Test
    void saveAndFindMovie_shouldWork() {
        Movie movie = new Movie(null, "Inception", "Sci-Fi", 148, "poster.jpg", 9.0);
        movieRepository.save(movie);

        Movie found = movieRepository.findById(movie.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Inception");
    }

    @Test
    void searchByTitleOrGenre_shouldReturnMatches() {
        movieRepository.save(new Movie(null, "Titanic", "Romance", 195, "poster1.jpg", 8.5));
        movieRepository.save(new Movie(null, "Avatar", "Sci-Fi", 160, "poster2.jpg", 9.2));

        List<Movie> result = movieRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase("sci", "sci");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Avatar");
    }
}

