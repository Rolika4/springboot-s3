package com.epam.edp.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3Controller {

    private final S3Service s3Service;

    @Autowired
    public S3Controller(S3Service s3service) {
        this.s3Service = s3service;
    }

    @GetMapping("/")
    ResponseEntity<ResponseData> getDate() {
        return ResponseEntity.ok(new ResponseData(s3Service.getData()));
    }

    private record ResponseData(String content) {
    }
}

