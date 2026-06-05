package com.climaquest.cqapi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlayerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void deveCriarJogadorEBuscarPorId() throws Exception {
        String body = """
                {
                    "codename": "DrClima",
                    "scientistType": "Climatologista",
                    "avatarIndex": 3
                }
                """;

        var result = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codename").value("DrClima"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/players/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codename").value("DrClima"))
                .andExpect(jsonPath("$.xp").value(0));
    }

    @Test
    void deveImpedirCodenamesDuplicados() throws Exception {
        String body = """
                {
                    "codename": "DrClima",
                    "scientistType": "Climatologista",
                    "avatarIndex": 3
                }
                """;

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }
}