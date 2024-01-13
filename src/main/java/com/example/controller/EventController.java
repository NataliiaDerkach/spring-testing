package com.example.controller;


import com.example.entity.Event;
import com.example.entity.User;
import com.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Transactional
    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PostMapping("/addEvents")
    public List<Event> addEvents(@RequestBody List<Event> events) {
        return eventService.createEvents(events);
    }

    @Transactional
    @GetMapping("/event/{id}")
    public Event getEventById(@PathVariable int id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.getEvents();
    }

    @PutMapping("/updateevent")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @DeleteMapping("/event/{id}")
    public String cancelEvent(@PathVariable int id) {
        return eventService.deleteEventById(id);
    }


}
