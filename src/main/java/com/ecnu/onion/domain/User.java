package com.ecnu.onion.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onion
 * @date 2020/2/3 -6:33 下午
 */
@Document(collection = "mind")
@Data
public class User {
    @Id
    private String userId;
    private List<String> collectNotes;
    private Map<String, Component> collectIndexes;

    public User(String userId) {
        this.userId = userId;
        collectNotes = new ArrayList<>();
        collectIndexes = new HashMap<>();
    }

    public void add(String name, Component component) {
        collectIndexes.put(name, component);
    }

}
