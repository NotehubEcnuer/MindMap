package com.ecnu.onion.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.onion.dao.UserDao;
import com.ecnu.onion.domain.Composite;
import com.ecnu.onion.domain.Note;
import com.ecnu.onion.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author onion
 * @date 2020/2/3 -6:35 下午
 */
@RestController
@Slf4j
public class UserController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserDao userDao;

    /**
     * 新建一个Index并且保存
     * map键值对：(key:value)
     * userId: "xxx"
     * name: "xxx" 字符串，字符串为json格式
     * 不传NoteId，后台则处理为目录
     * 举例：
     * {
     *           "children": [
     *              {
     *                   "id": "mvc",
     *                   "children": []
     *               },
     *               {
     *                   "id": "spring",
     *                   "children": []
     *               }
     *           ],
     *           "id": "spring学习路线"
     * }
     * @param map
     * @return
     */
    @PostMapping("/addIndex")
    public Object addIndex(@RequestParam Map<String, String> map) {
        String id = map.get("userId");
        String name = map.get("name");
        User user = userDao.findById(id).get();
        Composite composite = JSON.parseObject(name, Composite.class);
        user.add(composite);
        log.info("user:{}", user);
        return userDao.save(user);
    }

    /**
     * 查找某个Index
     * @param userId
     * @param num 类似于索引ID，自增，以Default为0开始计算。底层数据结构为数组。
     * @return
     */
    @GetMapping("/findOneIndex")
    public Object getOneIndex(@RequestParam String userId, @RequestParam int num) {
        User user = userDao.findById(userId).get();
        log.info("user:{}",user);
        return user.getCollectIndexes().get(num);
    }

    /**
     * 删除某个Index，真实删除
     * @param userId
     * @param num
     * @return
     */
    @GetMapping("/deleteOneIndex")
    public Object deleteOneIndex(@RequestParam String userId, @RequestParam int num) {
        User user = userDao.findById(userId).get();
        user.getCollectIndexes().remove(num);
        log.info("user:{}", user);
        return userDao.save(user);
    }

    /**
     * 更新某个Index，可能是更新Tag名或者放入笔记到Index里
     * map键值对格式
     * 不传noteId默认处理为目录，传noteId处理为笔记
     * userId:
     * name: json格式的字符串
     * num: 索引下标，以default为0计数
     *
     * json字符串格式举例：
     * {
     *           "children": [
     *              {
     *                   "children": [
     *                      {
     *                           "id": "Filter",
     * 	                         "noteId": "101",
     *                           "leaf": true
     *                      }
     *                   ],
     *                   "id": "mvc"
     *               },
     *               {
     *                   "children": [
     *                       {
     *                           "id": "Spring AOP",
     * 		                     "noteId": "102",
     *                           "leaf": true
     *                       },
     *                       {
     *                           "id": "Spring IOC",
     *                           "noteId": "103",
     *                           "leaf": true
     *                       }
     *                   ],
     *                   "id": "spring"
     *               }
     *           ],
     *           "id": "spring学习路线"
     * }
     * @param map
     * @return
     */
    @PostMapping("/updateIndex")
    public Object updateIndex(@RequestParam Map<String, String> map) {
        String id = map.get("userId");
        String name = map.get("name");
        String sub = map.get("num");
        User user = userDao.findById(id).get();
        Composite composite = JSON.parseObject(name, Composite.class);
        user.set(Integer.parseInt(sub), composite);
        log.info("user:{}", user);
        return userDao.save(user);
    }

    /**
     * 收藏某篇笔记，需要自动加入default索引中，递归算法
     * Note的三个字段
     *  noteId
     *  tag
     *  title
     * @param userId
     * @param note
     * @return
     */
    @PostMapping("/collectNote")
    public Object collectNote(@RequestParam String userId,
                              @RequestBody Note note) {
        User user = userDao.findById(userId).get();
        user.getCollectNotes().add(note);
        String tag = note.getTag();
        moveToMenu(tag, note.getNoteId(), note.getTitle(), user.getCollectIndexes().get(0));
        log.info("user:{}",user);
        return userDao.save(user);
    }

    /**
     * 取消对笔记的收藏，所有的Index都会删除相应的笔记，递归算法
     * @param userId
     * @param noteId
     * @return
     */
    @GetMapping("cancelCollectNote")
    public Object cancelCollectNote(@RequestParam String userId, @RequestParam String noteId) {
        User user = userDao.findById(userId).get();
        List<Composite> list = user.getCollectIndexes();
        for (Composite composite : list) {
            removeCollectNote(noteId, composite);
        }
        user.setCollectIndexes(list);
        user.removeCollectNotes(noteId);
        log.info("user:{}", user);
        return userDao.save(user);
    }

    private void removeCollectNote(String noteId, Composite composite) {
        if (composite.isLeaf()) {
            return;
        }
        List<Composite> children = composite.getChildren();
        Iterator<Composite> iterator = children.iterator();
        while (iterator.hasNext()) {
            Composite child = iterator.next();
            if (child.isLeaf() && child.getNoteId().equals(noteId)) {
                iterator.remove();
            } else if (!child.isLeaf()) {
                removeCollectNote(noteId, child);
            }
        }
    }

    private void moveToMenu(String tag, String noteId, String titile, Composite composite) {
        if (composite.isLeaf()) {
            return;
        }
        if (composite.getId().equals(tag)) {
            composite.addComponent(new Composite(titile, noteId));
            return;
        }
        for (int i = 0; i < composite.getChildren().size(); i++) {
            moveToMenu(tag, noteId, titile, composite.getChildren().get(i));
        }
    }

}
