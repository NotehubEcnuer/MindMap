package com.ecnu.onion.domain;

import lombok.Data;

/**
 * @author onion
 * @date 2020/2/3 -7:00 下午
 */

@Data
public class Leaf implements Component {
    private Boolean isLeaf;
    private String noteId;
    private String id;
    @Override
    public void addComponent(Component component) {
        throw new RuntimeException("🙅");
    }

    public Leaf(String noteId, String id) {
        this.id = id;
        this.noteId = noteId;
        this.isLeaf = true;
    }


}
