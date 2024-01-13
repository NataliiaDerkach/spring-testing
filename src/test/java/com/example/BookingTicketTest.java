package com.example;

import com.example.dao.EventRepository;
import com.example.dao.TicketRepository;
import com.example.dao.UserAccountRepository;
import com.example.dao.UserRepository;
import com.example.entity.Event;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.entity.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookingTicketTest {

    private static final Logger logger = LoggerFactory.getLogger(BookingTicketTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    BookingFacade bookingFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userAccountRepository.deleteAll();
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void verifyWithdrowMoneyFromUserAccountBalance() throws Exception {

        User userForBook = User.builder()
                .name("Book")
                .email("wouldLikebookTicket@test")
                .build();

        Event event = Event.builder()
                .title("DanceParty")
                .location("NightClub")
                .ticketPrice(BigDecimal.valueOf(75))
                .build();

        UserAccount userAccount = UserAccount.builder()
                .userId(userForBook)
                .balance(BigDecimal.valueOf(575))
                .build();
        userRepository.save(userForBook);
        eventRepository.save(event);
        userAccountRepository.save(userAccount);

        bookingFacade.bookTicket(userForBook, event, 55);

        ResultActions response= mockMvc.perform(get("/userAccount/balance/{userId}", userForBook.getId()));

        response.andDo(print())
                .andExpect(content().string("500.00"));

    }

    @Test
    public void verifyReceiverMessage() throws Exception {
        User userForBook = User.builder()
                .name("Message")
                .email("messagebookTicket@test")
                .build();

        Event event = Event.builder()
                .title("DanceParty")
                .location("NightClub")
                .ticketPrice(BigDecimal.valueOf(75))
                .build();

        UserAccount userAccount = UserAccount.builder()
                .userId(userForBook)
                .balance(BigDecimal.valueOf(1075))
                .build();
        userRepository.save(userForBook);
        eventRepository.save(event);
        userAccountRepository.save(userAccount);

       Ticket newBookedTicket = bookingFacade.bookTicket(userForBook, event, 88);


        ResultActions response= mockMvc.perform(get("/tickets/ticketByUser/{userId}", userForBook.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId.id",
                        is(newBookedTicket.getEventId().getId())))
                .andExpect(jsonPath("$.eventId.title",
                        is(newBookedTicket.getEventId().getTitle())))
                .andExpect(jsonPath("$.userId.id",
                        is(newBookedTicket.getUserId().getId())))
                .andExpect(jsonPath("$.userId.email",
                        is(newBookedTicket.getUserId().getEmail())));

       logger.info("New Ticket booked successfully for User's email: " + newBookedTicket.getUserId().getEmail());

    }
}
