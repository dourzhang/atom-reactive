package com.watent.reactive;

import com.watent.reactive.bean.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class AnnotationTest {


    @Test
    public void testCreate() {
        final User user = new User();
        user.setId("1231");
        user.setName("Test");
        user.setMail("test@demo.org");
        final WebClient client = WebClient.create("http://localhost:8080/user");

        final Mono<User> createdUser = client.post()
                .uri("")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .flatMap(response -> response.bodyToMono(User.class));
        System.out.println(createdUser.block());
    }

}
