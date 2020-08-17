package com.example.esdemo.mapper;

import com.example.esdemo.pojo.User;

import java.util.List;

public interface UserMapper {

    User findById(Long id);

    List<User> findAll();
}
