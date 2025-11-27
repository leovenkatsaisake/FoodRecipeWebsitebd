package klu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Comment {

    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "comment_time")
    private String time;

    public Comment() {}

    public Comment(Long id, String text, String time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
