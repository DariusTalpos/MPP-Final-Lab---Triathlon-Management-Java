package com.server.service;

import com.model.User;
import com.persistence.template.IUserRepo;

public class UserService {
    private IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User userExists(String username,String password)
    {
         return userRepo.findUserWithNameAndPassword(username,password);
    }

}
