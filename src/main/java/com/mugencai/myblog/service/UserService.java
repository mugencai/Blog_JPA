package com.mugencai.myblog.service;

import com.mugencai.myblog.pojo.User;

public interface UserService {

    User checkUser(String username, String password);

}
