package com.example.ticket;

import com.example.ticket.infrastructure.adapter.in.web.TicketController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class TicketControllerTest {

    @Autowired
    private TicketController ticketController;

    @Test
    void testVersionEndpoint() {
        String version = ticketController.version();
        assertNotNull(version);
    }
}
