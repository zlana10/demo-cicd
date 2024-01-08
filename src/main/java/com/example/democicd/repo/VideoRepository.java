package com.example.democicd.repo;


import org.springframework.data.repository.CrudRepository;
import com.example.democicd.entity.Video;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends CrudRepository<Video, Integer> {

}
