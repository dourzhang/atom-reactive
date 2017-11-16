package com.watent.reactive.function.cfg;//package com.watent.reactive.service;

import com.watent.reactive.bean.User;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> data = new ConcurrentHashMap<>();

    public Flux<User> list() {
        return Flux.fromIterable(this.data.values());
    }

    public Flux<User> getById(final Flux<String> ids) {
        return ids.flatMap(id -> Mono.justOrEmpty(this.data.get(id)));
    }

    public Mono<User> getById(final String id) {
        return Mono.justOrEmpty(this.data.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(id, null)));
    }

    public Flux<User> createOrUpdate(final Flux<User> users) {
//        Flux<User> userFlux = users.doOnNext(user -> this.data.put(user.getId(), user));
        return users;
    }

    public Mono<User> createOrUpdate(final User user) {
        this.data.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> delete(final String id) {
        return Mono.justOrEmpty(this.data.remove(id));
    }

    public Mono<User> test() {

        User user = new User();
        user.setId("123");
        user.setName("test");
        user.setMail("test@ytx.com");

        return Mono.just(user);
    }

}