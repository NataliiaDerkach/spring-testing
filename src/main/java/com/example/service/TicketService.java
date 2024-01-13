package com.example.service;

import com.example.dao.EventRepository;
import com.example.dao.TicketRepository;
import static com.example.jmc.MessagingConfiguration.MESSAGE_QUEUE;
import com.example.dao.UserRepository;
import com.example.entity.Event;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.entity.UserAccount;
import com.example.service.exeptions.EventNotFoundException;
import com.example.service.exeptions.InsufficientBalanceException;
import com.example.service.exeptions.InsufficientFundsException;
import com.example.service.exeptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public String deleteTicketById(int id) {
        ticketRepository.deleteById(id);
        return "Ticket got deleted";
    }

    public Optional<Ticket> getTicketByUser(User user) {
         List<Ticket> tickets= ticketRepository.findAll();
       for(Ticket t : tickets){
             if(t.getUserId().getId()==user.getId()){
                 return Optional.of(t);
             }
        }
       return Optional.empty();
    }

    public Ticket getTicketByEvent(Event event) {
        return ticketRepository.findById(event.getId()).orElse(null);
    }
}