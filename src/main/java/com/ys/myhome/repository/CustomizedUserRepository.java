package com.ys.myhome.repository;

import com.ys.myhome.model.User;

import java.util.List;

public interface CustomizedUserRepository {

    List<User> findByUsernameCustom(String username);

    List<User> findByUsernameJdbc(String username);


}
