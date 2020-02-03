package com.ecnu.onion.dao;

import com.ecnu.onion.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author onion
 * @date 2020/2/3 -6:35 下午
 */
public interface UserDao extends MongoRepository<User, String> {
}
