package com.ys.myhome.controller;


import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.ys.myhome.model.Board;
import com.ys.myhome.model.QUser;
import com.ys.myhome.model.User;
import com.ys.myhome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
class UserAPIController {

    @Autowired
    private UserRepository userRepository;


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method,
                   @RequestParam(required = false) String text) {

        Iterable<User> users = null;
//        log.debug("Before calling users.get(0).getBoards().size()");
//        log.debug("Calling users.get(0).getBoards().size(): {}", users.get(0).getBoards().size());
//        log.debug("After calling users.get(0).getBoards().size()");

        if("query".equals(method)){
            users = userRepository.findByUsernameQuery(text);

        } else if("nativeQuery".equals(method)){
            users = userRepository.findByUsernameNativeQuery(text);

        } else if("querydsl".equals(method)){

            QUser user = QUser.user;

            BooleanExpression b = user.username.contains(text);

            if(true){
                b =b.and(user.username.eq("urea"));
            }

            Predicate predicate = user.username.contains(text);


            users = userRepository.findAll(b);
        } else if("querydslCustom".equals(method)){
            users = userRepository.findByUsernameCustom(text);

        }  else if("jdbc".equals(method)){

            users = userRepository.findByUsernameJdbc(text);

        } else {
            users = userRepository.findAll();
        }


        return users;

    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());

                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    user.setBoards(newUser.getBoards());
                    for (Board board: user.getBoards()){
                        board.setUser(user);
                    }

                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}