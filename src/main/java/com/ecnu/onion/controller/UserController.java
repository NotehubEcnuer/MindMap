package com.ecnu.onion.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.onion.dao.UserDao;
import com.ecnu.onion.domain.Component;
import com.ecnu.onion.domain.Composite;
import com.ecnu.onion.domain.User;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author onion
 * @date 2020/2/3 -6:35 下午
 */
@RestController
public class UserController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserDao userDao;
    @PostMapping("/addMap")
    public String addMap(@RequestParam Map<String, String> map) {
        String id = map.get("userId");
        String name = map.get("name");
        User user = userDao.findById(id).get();
        Composite composite = JSON.parseObject(name, Composite.class);
        user.add(name, composite);
        userDao.save(user);
        return "success";
    }
    @GetMapping("/findOneIndex")
    public String getOneIndex(@RequestParam String userId, @RequestParam String key) {
        User user = userDao.findById(userId).get();
        Component component = user.getCollectIndexes().get(key);
        return JSON.toJSONString(component);
    }
    @GetMapping("/deleteOneIndex")
    public String deleteOneIndex(@RequestParam String userId, @RequestParam String key) {
        User user = userDao.findById(userId).get();
        user.getCollectIndexes().remove(key);
        User user1 = userDao.save(user);
        return JSON.toJSONString(user1);
    }
    @GetMapping("/collectNote")
    public String collectNote(@RequestParam String noteId, @RequestParam String userId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update();
        update.addToSet("collectNotes",noteId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        return result.toString();
    }
    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam String noteId, @RequestParam String userId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update();
        update.pull("collectNotes",noteId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);
        return result.toString();
    }
    @GetMapping("/addToMenu")
    public String addToMenu(@RequestParam String userId, @RequestParam String json) {
        User user = userDao.findById(userId).get();
        Component component = user.getCollectIndexes().get("key");
        return null;
    }

}
