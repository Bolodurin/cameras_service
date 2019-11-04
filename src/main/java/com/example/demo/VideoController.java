package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VideoController {

    @RequestMapping(value = "/testcloud/{cameraNum}/{dateStr}/")
    public ResponseEntity<?> getListOfVideosOnDay(@PathVariable Integer cameraNum,
                                                  @PathVariable String dateStr){

        System.out.println(cameraNum);
        System.out.println(dateStr);

        File folder = new File("/home/alex/testcloud/camera_" +
            cameraNum + "/" + dateStr);

        //System.out.println(folder.listFiles());

        if(!folder.exists()){
            return null;
        }
        else if(folder.isDirectory()){
            ArrayList<String> filesName = new ArrayList<>();

            for(File file : folder.listFiles()){
                System.out.println(file.getName());
                filesName.add(file.getName());
            }

            return ResponseEntity.ok(filesName);
        }
        else{
            System.out.print("ERROR! It's not directory");
            return null;
        }
    }

    @RequestMapping(value = "/video/{cameraNum}/{dateStr}/{timeStr}/",
            method = RequestMethod.GET)
    public void getVideo(@PathVariable int cameraNum,
                                      @PathVariable String dateStr,
                                      @PathVariable String timeStr,
                                      HttpServletRequest request,
                                      HttpServletResponse response

    ){

        System.out.println(cameraNum);
        System.out.println(dateStr);

        try{

            /*HttpHeaders headers = new HttpHeaders();
            InputStream in = File.getResourceAsStream("file:/home/alex/testcloud/camera_" +
                    cameraNum + "/" + dateStr +
                    "/record_" + dateStr + "_" + timeStr + ".avi");
            byte[] media = IOUtils.toByteArray(in);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;*/

            MultipartFileSender.fromPath(Paths.get("/home/alex/testcloud/camera_" +
                    cameraNum + "/" + dateStr +
                    "/record_" + dateStr + "_" + timeStr + ".avi"))
                    .with(request)
                    .with(response)
                    .serveResource();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

