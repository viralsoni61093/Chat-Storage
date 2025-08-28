package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource(properties = "rate.limit=10")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatStorageIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String API_KEY_VALUE = "local-dev-api-key";

    private int getRateLimit() {
        String value = System.getProperty("rate.limit");
        if (value == null) {
            value = System.getenv("RATE_LIMIT");
        }
        if (value == null) {
            value = "100";
        }
        return Integer.parseInt(value);
    }

    private String getUniqueIp() {
        return "127.0.0." + (int)(Math.random() * 200 + 1);
    }

    @Test
    void swaggerAccessibleWithoutApiKey() throws Exception {
        int statusHtml = mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui.html")
                .header("X-Forwarded-For", getUniqueIp()))
                .andReturn().getResponse().getStatus();
        int statusIndex = mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui/index.html")
                .header("X-Forwarded-For", getUniqueIp()))
                .andReturn().getResponse().getStatus();
        boolean ok = (statusHtml >= 200 && statusHtml < 400) || (statusIndex >= 200 && statusIndex < 400);
        Assertions.assertTrue(ok, "Swagger UI should be accessible (status: /swagger-ui.html=" + statusHtml + ", /swagger-ui/index.html=" + statusIndex + ")");
    }

    @Test
    void createSessionAddMessageRetrieveMessage() throws Exception {
        String userId = UUID.randomUUID().toString();
        String sessionName = "TestSession";
        MultiValueMap<String, String> sessionParams = new LinkedMultiValueMap<>();
        sessionParams.add("userId", userId);
        sessionParams.add("name", sessionName);
        String clientIp = getUniqueIp();
        MvcResult sessionResult = mockMvc.perform(MockMvcRequestBuilders.post("/sessions")
                .params(sessionParams)
                .header(API_KEY_HEADER, API_KEY_VALUE)
                .header("X-Forwarded-For", clientIp))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String sessionJson = sessionResult.getResponse().getContentAsString();
        JsonNode sessionNode = objectMapper.readTree(sessionJson);
        String sessionId = sessionNode.has("id") ? sessionNode.get("id").asText() : null;
        Assertions.assertNotNull(sessionId);

        MultiValueMap<String, String> msgParams = new LinkedMultiValueMap<>();
        msgParams.add("sender", "user");
        msgParams.add("content", "Hello, world!");
        MvcResult msgResult = mockMvc.perform(MockMvcRequestBuilders.post("/sessions/" + sessionId + "/messages")
                .params(msgParams)
                .header(API_KEY_HEADER, API_KEY_VALUE)
                .header("X-Forwarded-For", clientIp))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String msgJson = msgResult.getResponse().getContentAsString();
        Assertions.assertTrue(msgJson.contains("id"));

        mockMvc.perform(MockMvcRequestBuilders.get("/sessions/" + sessionId + "/messages")
                .param("page", "0")
                .param("size", "20")
                .header(API_KEY_HEADER, API_KEY_VALUE)
                .header("X-Forwarded-For", clientIp))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("content")));
    }

    /*@Test
    void rateLimitValidation() throws Exception {
        String userId = UUID.randomUUID().toString();
        String sessionName = "RateLimitSession";
        MultiValueMap<String, String> sessionParams = new LinkedMultiValueMap<>();
        sessionParams.add("userId", userId);
        sessionParams.add("name", sessionName);
        String clientIp = getUniqueIp();
        MvcResult sessionResult = mockMvc.perform(MockMvcRequestBuilders.post("/sessions")
                .params(sessionParams)
                .header(API_KEY_HEADER, API_KEY_VALUE)
                .header("X-Forwarded-For", clientIp))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String sessionJson = sessionResult.getResponse().getContentAsString();
        JsonNode sessionNode = objectMapper.readTree(sessionJson);
        String sessionId = sessionNode.has("id") ? sessionNode.get("id").asText() : null;
        Assertions.assertNotNull(sessionId);

        MultiValueMap<String, String> msgParams = new LinkedMultiValueMap<>();
        msgParams.add("sender", "user");
        msgParams.add("content", "test");
        int rateLimit = getRateLimit();
        int lastStatus = 0;
        for (int i = 0; i < rateLimit; i++) {
            lastStatus = mockMvc.perform(MockMvcRequestBuilders.post("/sessions/" + sessionId + "/messages")
                    .params(msgParams)
                    .header(API_KEY_HEADER, API_KEY_VALUE)
                    .header("X-Forwarded-For", clientIp))
                    .andReturn().getResponse().getStatus();
            Assertions.assertEquals(200, lastStatus, "Expected 200 for request " + (i+1));
        }
        lastStatus = mockMvc.perform(MockMvcRequestBuilders.post("/sessions/" + sessionId + "/messages")
                .params(msgParams)
                .header(API_KEY_HEADER, API_KEY_VALUE)
                .header("X-Forwarded-For", clientIp))
                .andReturn().getResponse().getStatus();
        Assertions.assertEquals(429, lastStatus, "Expected 429 after exceeding rate limit (actual: " + lastStatus + ")");
    }*/
}
