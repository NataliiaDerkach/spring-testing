package com.example.service;


import com.example.dao.EventRepository;
import com.example.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service

public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> createEvents(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    @Transactional
    public Event getEventById(int id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(Event event) {
        Event oldEvent = null;
        Optional<Event> optionalevent = eventRepository.findById(event.getId());
        if (optionalevent.isPresent()) {
            oldEvent = optionalevent.get();
            oldEvent.setTitle(event.getTitle());
            oldEvent.setLocation(event.getLocation());
            oldEvent.setTicketPrice(event.getTicketPrice());
            eventRepository.save(oldEvent);
        } else {
            return new Event();
        }
        return oldEvent;
    }

    public String deleteEventById(int id) {
        eventRepository.deleteById(id);
        return "Event got deleted";
    }
}
