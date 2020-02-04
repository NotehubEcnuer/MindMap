package com.ecnu.onion.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * @author onion
 * @date 2020/2/3 -6:33 下午
 */
@Document(collection = "mind")
@Data
public class User {
    @Id
    private String userId;
    private Set<Note> collectNotes;
    private List<Composite> collectIndexes;

    public User(String userId) {
        this.userId = userId;
        collectNotes = new HashSet<>();
        collectIndexes = new ArrayList<>();
    }

    public void add(Composite component) {
        collectIndexes.add(component);
    }

    public void removeCollectNotes(String noteId) {
        collectNotes.removeIf(e->noteId.equals(e.getNoteId()));
    }
    public void set(int num, Composite composite) {
        collectIndexes.set(num, composite);
    }

}
