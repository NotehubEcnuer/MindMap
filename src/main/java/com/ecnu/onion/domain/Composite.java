package com.ecnu.onion.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author onion
 * @date 2020/2/3 -7:00 下午
 */
@Data
@NoArgsConstructor
public class Composite{
    private boolean leaf;
    private List<Composite> children;
    private String id;
    private String noteId;

    public Composite(String id) {
        this.id = id;
        children = new ArrayList<>();
        leaf = false;
    }

    public Composite(String id, String noteId) {
        this.id = id;
        this.noteId = noteId;
        leaf = true;
    }

    public void addComponent(Composite component) {
        if (leaf) {
            throw new RuntimeException("不是目录");
        }
        children.add(component);
    }
}
