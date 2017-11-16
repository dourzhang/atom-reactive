package com.watent.reactive.annotation;//package com.watent.reactive.controller;
//
//import com.watent.reactive.User;
//import com.watent.reactive.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.config.ResourceNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.Objects;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(final UserService userService) {
//        this.userService = userService;
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public void notFound() {
//    }
//
//    @GetMapping("")
//    public Flux<User> list() {
//        return this.userService.list();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<User> getById(@PathVariable("id") final String id) {
//        return this.userService.getById(id);
//    }
//
//    @PostMapping
//    public Flux<User> create(@RequestBody final Flux<User> users) {
//        return this.userService.createOrUpdate(users);
//    }
//
//    @PutMapping("/{id}")
//    public Mono<User> update(@PathVariable("id") final String id, @RequestBody final User user) {
//        Objects.requireNonNull(user);
//        user.setId(id);
//        return this.userService.createOrUpdate(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<User> delete(@PathVariable("id") final String id) {
//        return this.userService.delete(id);
//    }
//
//    @GetMapping("/test")
//    public Mono<ServerResponse> getById() {
//        Mono<ServerResponse> result = ServerResponse.ok().contentType(APPLICATION_JSON).body(userService.test(), User.class);
//        return result;
//    }
//}