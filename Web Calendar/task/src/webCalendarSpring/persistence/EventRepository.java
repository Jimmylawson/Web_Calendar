package webCalendarSpring.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webCalendarSpring.model.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDate(LocalDate date);

    List<Event> findByDateBetween(LocalDate start,LocalDate end);


}
