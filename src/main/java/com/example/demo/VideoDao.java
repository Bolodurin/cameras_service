package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "/home/alex/test", collectionResourceRel = "/home/alex/test")
public interface VideoDao extends JpaRepository<Video, String> {
    Video findVideo();
}
