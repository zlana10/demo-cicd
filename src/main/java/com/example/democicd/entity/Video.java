package com.example.democicd.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id")
    private Integer id;

    @Column(name = "video_title")
    private String title;

    @Column(name = "video_url")
    private String url;

    public Video() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
