package com.ecnu.onion.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author onion
 * @date 2020/2/3 -7:00 下午
 */
@Data
public class Composite implements Component {
    private Boolean isLeaf;
    private List<Component> children;
    private String id;

    public Composite(String id) {
        this.id = id;
        children = new ArrayList<>();
        isLeaf = false;
    }

    @Override
    public void addComponent(Component component) {
        children.add(component);
    }
}
