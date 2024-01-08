package com.example.democicd.controller;

import com.example.democicd.entity.Video;
import com.example.democicd.repo.VideoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class VideoController {

    private final VideoRepository videoRepository;

    public VideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostMapping(path = "/create")
    public @ResponseBody String createNewVideo(@RequestParam String title, @RequestParam String url){
        Video video = new Video();
        video.setTitle(title);
        video.setUrl(url);
        videoRepository.save(video);
        return "Saved";
    }

    @GetMapping(path = "/videos")
    public @ResponseBody Iterable<Video> getVideos(){
        return videoRepository.findAll();
    }
}
