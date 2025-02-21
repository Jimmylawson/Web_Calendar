package webCalendarSpring.business;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import webCalendarSpring.model.Event;
import webCalendarSpring.persistence.EventRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
    public List<Event> findAll() {
        return eventRepository.findAll();
    }
//    public List<Event> findAll(Sort sort) {
//        return eventRepository.findAll(sort);
//    }

    public List<Event> findByDate(LocalDate today) {
        return eventRepository.findByDate(today);
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> deleteEventById(Long id) {
       Optional<Event> existing = eventRepository.findById(id);
//        if(existing.isPresent()) {
//            eventRepository.deleteById(id);
//        }
        existing.ifPresent(event->eventRepository.deleteById(id));

        /// Return the entity that was deleted if not found
        return existing;

    }
    public List<Event> findByDateBetween(LocalDate start, LocalDate end) {
        return eventRepository.findByDateBetween(start, end);
    }
}
