package com.example;


import com.example.dao.TicketRepository;
import com.example.dao.UserAccountRepository;
import com.example.dao.UserRepository;
import com.example.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userAccountRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void givenCreateAndReturnUser() throws Exception {
        User user = User.builder()
                .name("Rony")
                .email("rony@test.com")
                .build();

        ResultActions response = mockMvc.perform(post("/users/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(jsonPath("$.name",
                        is(user.getName())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())));
    }

    @Test
    public void givenCreateAndReturnListOfUsers() throws Exception {
        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(User.builder().name("Nif").email("nifnif@pigs.com").build());
        listOfUsers.add(User.builder().name("Nuf").email("nufnuf@pigs.com").build());
        listOfUsers.add(User.builder().name("Naf").email("nafnaf@pigs.com").build());
        userRepository.saveAll(listOfUsers);

        ResultActions response = mockMvc.perform(get("/users/users"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));
    }

    @Test
    public void givenInvalidUserThenGetAndReturnEmpty() throws Exception {

        User userId =new User();
        userId.setId(1);
        User invalidUser = User.builder()
                .name("Gosha")
                .email("goshatotosha@test.com")
                .build();
        userRepository.save(invalidUser);

        ResultActions response=mockMvc.perform(get("/users/user/{id}", userId.getId()));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
