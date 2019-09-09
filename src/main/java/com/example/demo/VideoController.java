package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private VideoDao videoDao;
    private VideoContentStore videoContentStore;

    @RequestMapping(value = "/testcloud/{camerasNum}/{dateStr}/")
    public ResponseEntity<?> getListOfVideosOnDay(@PathVariable int cameraNum,
                                                  @PathVariable String dateStr){

        File folder = new File("/home/alex/testcloud/camera_" +
            cameraNum + "/" + dateStr);

        if(!folder.exists()){
            return null;
        }
        else if(folder.isDirectory()){
            ArrayList<String> filesName = new ArrayList<>();
            Assert.isNull(folder.listFiles(), "Has not files");

            for(File file : folder.listFiles()){
                filesName.add(file.getName());
            }

            return ResponseEntity.ok(filesName);
        }
        else{
            System.out.print("ERROR! It's not directory");
            return null;
        }
    }

    @RequestMapping(value = "/testcloud/{cameraNum}/{dateStr}/{timeStr}/",
            method = RequestMethod.GET)
    public ResponseEntity<?> getVideo(@PathVariable int cameraNum,
                                      @PathVariable String dateStr,
                                      @PathVariable String timeStr){

        try{
            /*Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
            Date time = new SimpleDateFormat("HH-mm-ss").parse(timeStr);*/

            UrlResource video = new UrlResource(
                    "file:/home/alex/testcloud/camera_" +
                            cameraNum + "/" + dateStr +
                            "/record_" + dateStr + "_" + timeStr);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).
                    contentType(MediaTypeFactory.getMediaType(video).
                            orElse(MediaType.APPLICATION_OCTET_STREAM)).body(video);
        } catch (MalformedURLException e) {
            System.out.println("ERROR: fragment not found");
            return null;
        }
    }
}
