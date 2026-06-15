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

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthIntegrationTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void deveFazerLoginERetornarTokenValido() throws Exception {
        String codename = "AuthUser_" + UUID.randomUUID();
        var created = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codename":"%s","scientistType":"Climatologista","avatarIndex":2}
                                """.formatted(codename)))
                .andExpect(status().isCreated())
                .andReturn();

        String playerId = created.getResponse().getContentAsString().split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codename\":\"" + codename + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(playerId))
                .andExpect(jsonPath("$.codename").value(codename))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void deveRetornar404ParaCodenameInexistente() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codename\":\"NaoExiste_" + UUID.randomUUID() + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBloquearLoginAposExcederLimitePorIp() throws Exception {
        String codename = "NaoExiste_" + UUID.randomUUID();

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"codename\":\"" + codename + "\"}"))
                    .andExpect(status().isNotFound());
        }

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codename\":\"" + codename + "\"}"))
                .andExpect(status().is(429));
    }
}
