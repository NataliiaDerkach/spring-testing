package com.example;

import com.example.entity.Event;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.entity.UserAccount;
import com.example.service.EventService;
import com.example.service.UserAccountService;
import com.example.service.UserService;
import com.example.service.exeptions.EventNotFoundException;
import com.example.service.exeptions.InsufficientBalanceException;
import com.example.service.exeptions.InsufficientFundsException;
import com.example.service.exeptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

import static com.example.jmc.MessagingConfiguration.MESSAGE_QUEUE;

@Component
@Service
public class BookingFacade {

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    JmsTemplate jmsTemplate;


    @Transactional
    public Ticket bookTicket(User userId, Event eventId, int seat) {

        Optional<User> optionalUser = userService.getUserById(userId.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<Event> optionalEvent = Optional.ofNullable(eventService.getEventById(eventId.getId()));
            if (optionalEvent.isPresent()) {
                Event event = optionalEvent.get();
                BigDecimal ticketPrice = event.getTicketPrice();

                BigDecimal userAccountBalance = userAccountService.getUserBalance(userId);
                if (userAccountBalance.compareTo(ticketPrice) >= 0) {

                    Ticket ticket = new Ticket();
                    ticket.setUserId(user);
                    ticket.setEventId(event);
                    ticket.setSeat(seat);

                    UserAccount userAccount = new UserAccount();
                    BigDecimal x = userAccountBalance.subtract(ticketPrice);
                    userAccount.setBalance(x);
                    userAccount.setUserId(userId);
                    userAccountService.updateBalance(userAccount.getUserId(), userAccount.getBalance());

                    jmsTemplate.convertAndSend(MESSAGE_QUEUE, ticket);

                    return ticket;
                }
                if (userAccountBalance.compareTo(ticketPrice) < 0) {
                    throw new InsufficientBalanceException("Not enough balance to book ticket!");
                } else {
                    throw new InsufficientFundsException("Not enough funds to book the ticket.");
                }
            } else {
                throw new EventNotFoundException("Event not found.");
            }
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }
}
