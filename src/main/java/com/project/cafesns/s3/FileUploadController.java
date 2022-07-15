package com.project.cafesns.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/api/upload")
    public String uploadImage(@RequestPart MultipartFile file){
        return fileUploadService.uploadImage(file, "");
    }

    @PostMapping("api/list")
    public List<String> uploadImageList(@RequestPart(value = "file") MultipartFile [] fileList){
        return fileUploadService.uploadImageList(fileList, "post");
    }
}
