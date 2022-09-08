package com.odde.atddv2;

import com.odde.atddv2.entity.User;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.java.Before;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.github.leeonky.dal.Assertions.expect;

public class Api {
    private static String token;
    private String response;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RestTemplate restTemplate;

    public static String getToken() {
        return token;
    }

    @Before("@api-login")
    public void apiLogin() {
        User defaultUser = new User().setUserName("j").setPassword("j");
        userRepo.save(defaultUser);
        token = restTemplate.postForEntity(makeUri("/users/login"), defaultUser, User.class)
                .getHeaders().get("token").get(0);
    }

    public void get(String path) {
        response = restTemplate.exchange(RequestEntity.get(makeUri("/api/" + path))
                .header("Accept", "application/json")
                .build(), String.class).getBody();
    }

    @SneakyThrows
    public void responseShouldMatchJson(String json) {
        try {
            String responseBodyNoNewLine = json.replace('\n', ' ');
            JSONAssert.assertEquals("[" + responseBodyNoNewLine + "]", "[" + response + "]", false);
        } catch (Throwable t) {
            System.err.println("Expect:");
            System.err.println(json);
            System.err.println("Actual:");
            System.err.println(response);
            throw t;
        }
    }

    public void responseShouldMatchJsonDAL(String json) {
        expect(response).should(json);
    }

    public void post(String path, Object body) {
        response = restTemplate.exchange(RequestEntity.post(makeUri("/api/" + path))
                .header("Content-Type", "application/json")
                .body(body), String.class).getBody();
    }

    @SneakyThrows
    private URI makeUri(String path) {
        return URI.create(String.format("http://localhost:%s%s", 10081, path));
    }
}