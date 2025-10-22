package com.example.movie.ticket.booking.repository;

import com.example.movie.ticket.booking.entity.Booking;
import com.example.movie.ticket.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking , Long> {

    List<Booking> findByUserOrderByBookingTimeDesc(User user);

   List<Booking>  findByShowId(Long showId);

    @Query("SELECT b.seatCodes FROM Booking b WHERE b.show.id = :showId AND b.status <> 'CANCELLED'")
    List<String> findSeatCodesByShowId(@Param("showId") Long showId);
}
