package com.groofycode.GroofyCode.utilities;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.UUID;

@Component
public class FirebaseManager {
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${firebase.bucketName}")
    private String bucketName;

    @Value("${groofycode.credentials}")
    private String accountAuth;

    public String uploadPhoto(MultipartFile file, String folderName) throws Exception {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(accountAuth));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, folderName + "/" + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getBytes());
            return storage.get(blobId).signUrl(15, java.util.concurrent.TimeUnit.MINUTES).toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deletePhoto(String photoUrl, String folderName) throws Exception {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(accountAuth));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            BlobId blobId = BlobId.of(bucketName, folderName + "/" + photoUrl);
            storage.delete(blobId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}