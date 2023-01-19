package com.booster.cliclient;

import com.booster.cliclient.console.UserInputReader;
import com.booster.cliclient.launcher.BoosterCliLauncher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class BaseIntegrationTest {

    protected static ClientAndServer mockServer;

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected BoosterCliLauncher boosterCliLauncher;
    @MockBean
    protected UserInputReader adapter;

    @BeforeAll
    public static void beforeAll() {
        mockServer = ClientAndServer.startClientAndServer(8081);
    }

    @AfterAll
    public static void afterAll() {
        mockServer.stop();
    }

}
