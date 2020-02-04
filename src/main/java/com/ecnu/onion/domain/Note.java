package com.ecnu.onion.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author onion
 * @date 2020/2/3 -8:47 下午
 */
@Data
public class Note {
    @Id
    private String noteId;
    private String tag;
    private String title;
    @Override
    public boolean equals(Object o){
        if (o instanceof Note) {
            Note note = (Note) o;
            return this.noteId.equals(note.noteId);
        }else {
            return false;
        }
    };
    @Override
    public int hashCode() {
        return Integer.parseInt(noteId);
    }
}
