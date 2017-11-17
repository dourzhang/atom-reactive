package com.watent.reactive;

import com.watent.reactive.bean.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClientOptions;

import java.util.function.Consumer;

public class FunctionTest {

    @Test
    public void calculator() {

        WebClient client = WebClient.create("http://localhost:8080/calculator");
        Mono<String> result = client.post()
                .uri("?operator=add&v1=1&v2=2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(String.class));

        System.out.println("-----------------");
        System.out.println(result.block());
        System.out.println("-----------------");
    }


    @Test
    private static void testGet() {

        final User user = new User();
        user.setName("Test");
        user.setMail("test@example.org");

        final WebClient client = WebClient.create("http://localhost:8080/user");
        final Mono<User> createdUser = client.get()
                .uri("/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(User.class));

        System.out.println(createdUser.block());

        System.out.println("--------------------------");
    }


}
