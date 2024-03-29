package com.example.stepdefinitions;

import com.example.BookingFacade;
import com.example.entity.Event;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.entity.UserAccount;
import com.example.service.EventService;
import com.example.service.TicketService;
import com.example.service.UserAccountService;
import com.example.service.UserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;


public class Stepdefinitions {

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    BookingFacade bookingFacade;

    @Autowired
    TicketService ticketService;

    private User user;
    private Event event;
    int seat;

    public Stepdefinitions(UserService userService) {
        this.userService = new UserService();
    }


    @Before
    public void setUp() {
        User  user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        userService.createUser(user);

        Event  event = new Event();
        event.setTitle("Concert");
        event.setLocation("Terrace Centr");
        event.setTicketPrice(BigDecimal.valueOf(50));
        eventService.createEvent(event);
    }

    @And("an event titled {string} with a ticket price of ${int}")
    public void anEventTitledWithATicketPriceOf$(String title, int price) {
        event.setTitle("Concert");
        event.setLocation("Terrace Centr");
        event.setTicketPrice(BigDecimal.valueOf(price));
        eventService.createEvent(event);
    }

    @Given("ser with name {string} and email {string} and account balance ${int}")
    public void serWithNameAndEmailAndAccountBalance$(String name, String email, int balance) {
        user.setName(name);
        user.setEmail(email);
        userService.createUser(user);

        UserAccount userAccount= new UserAccount();
        userAccount.setUserId(user);
        userAccount.setBalance(BigDecimal.valueOf(balance));
        userAccountService.saveUserAccount(userAccount);
    }

    @When("the user books a ticket for the event")
    public void theUserBooksATicketForTheEvent() {
        seat= 77;
        bookingFacade.bookTicket(user, event, seat);
    }

    @Then("the booking should be successful")
    public void theBookingShouldBeSuccessful() {
        Optional<Ticket> bookedTicket = ticketService.getTicketByUser(user);
        Assert.assertTrue(bookedTicket.isPresent());
    }
}
