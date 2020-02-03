package com.ecnu.onion.test;

import com.ecnu.onion.MindMapApplication;
import com.ecnu.onion.dao.UserDao;
import com.ecnu.onion.domain.Component;
import com.ecnu.onion.domain.Composite;
import com.ecnu.onion.domain.Leaf;
import com.ecnu.onion.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author onion
 * @date 2020/2/3 -7:36 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MindMapApplication.class)
public class MyTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void test() {
        User user = userDao.findById("969023014@qq.com").get();
        Component component = new Composite("spring学习路线");
        Component mvc = new Composite("mvc");
        Component spring = new Composite("spring");
        component.addComponent(mvc);
        component.addComponent(spring);
        Component aop = new Leaf("10", "Spring AOP");
        Component ioc = new Leaf("11", "Spring IOC");
        Component filter = new Leaf("12", "Filter");
        mvc.addComponent(filter);
        spring.addComponent(aop);
        spring.addComponent(ioc);
        user.add("spring学习路线", component);
        userDao.save(user);
    }
}
