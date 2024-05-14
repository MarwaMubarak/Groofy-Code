package com.groofycode.GroofyCode.utilities;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class FirebaseManager {
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${firebase.bucketName}")
    private String bucketName;

    public String uploadPhoto(MultipartFile file, String folderName) throws Exception {
        try {
            String fileName = UUID.randomUUID() + file.getOriginalFilename();
            storage.create(BlobInfo.newBuilder(bucketName, folderName + "/" + fileName).build(), file.getBytes());
            return "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" + folderName + "%2F" + fileName + "?alt=media";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deletePhoto(String photoUrl, String folderName) throws Exception {
        try {
            String fileName = photoUrl.substring(photoUrl.lastIndexOf("%2F") + 3, photoUrl.lastIndexOf("?"));
            BlobId blobId = BlobId.of(bucketName, folderName + "/" + fileName);
            storage.delete(blobId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
