package org.ExpenseTracker.data.repositories;

import org.ExpenseTracker.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
//    User findByUsername(String username);
    User findByEmail(String email);
}