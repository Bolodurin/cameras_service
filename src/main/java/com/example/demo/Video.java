package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Video {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Date created = new Date();

    @Getter
    @Setter
    private String summary;

    @ContentId
    @Getter
    @Setter
    private String contentId;

    @ContentLength
    @Getter
    @Setter
    private long contentLength;

    @Getter
    @Setter
    private String path;
}
