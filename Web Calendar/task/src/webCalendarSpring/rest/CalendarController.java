package webCalendarSpring.rest;


import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import webCalendarSpring.business.EventService;
import webCalendarSpring.model.Event;

import java.time.LocalDate;
import java.util.*;

@RestController
public class CalendarController {
    EventService eventService;

    public CalendarController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event/today")
    public ResponseEntity<List<Event>> getTodayEvents() {

//        List<Event> events = eventService.findAll(); // or findByDate
//        // Always return 200
//        return ResponseEntity.ok(events); // If empty, returns []

//        LocalDate today = LocalDate.now();  // or a fixed date if your test is pinned to 2025-02-21
//        List<Event> events = eventService.findByDate(today);
//        if (events.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }

        LocalDate today = LocalDate.now();
        List<Event> events = eventService.findByDate(today);
        return ResponseEntity.ok(events);
//        return ResponseEntity.ok(events);
    }

    @PostMapping("/event")
    public ResponseEntity<Map<String,Object>> addEvent(@Valid @RequestBody Event event, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

       // List<Event> events = new ArrayList<>();
      // event.setMessage("The event has been added!");
        eventService.save(event);

        // Manually build JSON to return "message"
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "The event has been added!");
        response.put("event", event.getEvent());
        response.put("date", event.getDate()); // or toString()

        return ResponseEntity.ok(response);
    }

    @GetMapping("/event")
    public ResponseEntity<List<Event>> getEvents(){
        List<Event> events = eventService.findAll();
        if(events.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }
    @GetMapping("/event/{id}")
    public ResponseEntity<Object> getEventByID(@PathVariable("id") Long id){
        Optional<Event> eventById = eventService.findById(id);
        return eventById.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "The event doesn't exist!")));

    }
    @DeleteMapping("/event/{id}")
    public ResponseEntity<Object> deleteEventById(@PathVariable("id") long id){
        Optional<Event> eventById = eventService.findById(id);
        if(eventById.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The event doesn't exist!"));
        }
        eventService.deleteEventById(id);
        return ResponseEntity.ok(eventById.get());
    }

    @GetMapping(value = "/event", params = {"start_time", "end_time"})
    public ResponseEntity<List<Event>> getEvents(
            @RequestParam("start_time") String startTimeStr,
            @RequestParam("end_time") String endTimeStr) {

        // If both params are missing => return all events
        if (startTimeStr == null && endTimeStr == null) {
            List<Event> allEvents = eventService.findAll();
            if (allEvents.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allEvents);
        }

        // Otherwise parse them to LocalDate
        LocalDate start = LocalDate.parse(startTimeStr);
        LocalDate end = LocalDate.parse(endTimeStr);

        // Query for the range
        List<Event> rangeEvents = eventService.findByDateBetween(start, end);
        if (rangeEvents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rangeEvents);
    }


}
