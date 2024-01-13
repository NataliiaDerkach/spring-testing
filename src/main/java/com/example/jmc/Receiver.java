package com.example.jmc;


import com.example.dao.TicketRepository;
import com.example.entity.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.example.jmc.MessagingConfiguration.MESSAGE_QUEUE;


@Component
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    @JmsListener(destination = MESSAGE_QUEUE)
    public void receiveMessage(final Message<Ticket> message) {

        try {
            MessageHeaders headers = message.getHeaders();
            log.info("headers = " + headers);

            Ticket bookedTicket = message.getPayload();
            log.info("Booked ticket = " + bookedTicket);
            ticketRepository.save(bookedTicket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

