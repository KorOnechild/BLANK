package com.project.cafesns.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AwsS3UploadService implements UploadService {

    private final AmazonS3 amazonS3;
    private final S3Component component;
    //파일 업로드
    @Override
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName){
        amazonS3.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream,  objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }
    //파일 주소 받아오기
    @Override
    public String getFileUrl(String fileName){
        return amazonS3.getUrl(component.getBucket(), fileName).toString();
    }

    //파일 삭제하기
    @Override
    public void deleteFile(String filePath){
        amazonS3.deleteObject(component.getBucket(), filePath);
    }
}
