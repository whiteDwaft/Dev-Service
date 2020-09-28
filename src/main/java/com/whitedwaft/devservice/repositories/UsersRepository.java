package com.whitedwaft.devservice.repositories;

import com.whitedwaft.devservice.dao.UserDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends MongoRepository<UserDao, String> {
    UserDao findByEmail(String email);
    UserDao findByEmailAndPassword(String email, String password);

}
