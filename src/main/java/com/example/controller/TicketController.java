package com.example.controller;


import com.example.entity.Event;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/addTicket")
    public Ticket addTicket(@RequestBody Ticket event) {
        return ticketService.saveTicket(event);
    }

    @DeleteMapping("/ticket/{id}")
    public String cancelTicket(@PathVariable int id) {
        return ticketService.deleteTicketById(id);
    }

    @GetMapping("/ticketByUser/{userId}")
    public ResponseEntity<Ticket> getTicketByUser(@PathVariable ("userId") User userId) {
        return ticketService.getTicketByUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ticketByEvent/{event}")
    public Ticket getTicketByEvent(@PathVariable Event event) {
        return ticketService.getTicketByEvent(event);
    }


}
