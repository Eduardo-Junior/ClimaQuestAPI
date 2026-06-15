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
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void deveCriarJogadorEBuscarPorId() throws Exception {
        String codename = "DrClima_" + UUID.randomUUID();
        String body = """
                {
                    "codename": "%s",
                    "scientistType": "Climatologista",
                    "avatarIndex": 3
                }
                """.formatted(codename);

        var result = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codename").value(codename))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = response.split("\"id\":\"")[1].split("\"")[0];

        String token = login(codename);

        mockMvc.perform(get("/api/players/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codename").value(codename))
                .andExpect(jsonPath("$.xp").value(0));
    }

    @Test
    void deveImpedirCodenamesDuplicados() throws Exception {
        String body = """
                {
                    "codename": "%s",
                    "scientistType": "Climatologista",
                    "avatarIndex": 3
                }
                """.formatted("DrClima_" + UUID.randomUUID());

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar401AoBuscarJogadorSemToken() throws Exception {
        String codename = "SemToken_" + UUID.randomUUID();
        var result = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codename":"%s","scientistType":"Climatologista","avatarIndex":1}
                                """.formatted(codename)))
                .andExpect(status().isCreated())
                .andReturn();

        String id = result.getResponse().getContentAsString().split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/players/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar401AoBuscarJogadorComTokenInvalido() throws Exception {
        String codename = "TokenInvalido_" + UUID.randomUUID();
        var result = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"codename":"%s","scientistType":"Climatologista","avatarIndex":1}
                                """.formatted(codename)))
                .andExpect(status().isCreated())
                .andReturn();

        String id = result.getResponse().getContentAsString().split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/api/players/" + id)
                        .header("Authorization", "Bearer token.invalido.aqui"))
                .andExpect(status().isUnauthorized());
    }

    // Faz login com o codename e devolve o JWT emitido
    private String login(String codename) throws Exception {
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codename\":\"" + codename + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString().split("\"token\":\"")[1].split("\"")[0];
    }
}
