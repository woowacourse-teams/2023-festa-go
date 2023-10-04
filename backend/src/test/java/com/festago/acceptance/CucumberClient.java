package com.festago.acceptance;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class CucumberClient {

    private Map<String, Object> dataStorage = new HashMap<>();
    private Response response;
    private String token;

    public CucumberClient() {
    }

    public void addData(String key, Object value) {
        dataStorage.put(key, value);
    }

    public void addAuthToken(String token) {
        this.token = token;
    }

    public Object getData(String key) {
        return dataStorage.get(key);
    }

    public Response getResponse() {
        return response;
    }

    public String getToken() {
        return token;
    }
}
