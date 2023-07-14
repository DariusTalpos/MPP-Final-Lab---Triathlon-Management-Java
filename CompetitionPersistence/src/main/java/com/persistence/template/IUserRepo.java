package com.persistence.template;

import com.model.User;

public interface IUserRepo extends IGenericRepo<Long, User> {
    public User findUserWithNameAndPassword(String username,String password);
}
