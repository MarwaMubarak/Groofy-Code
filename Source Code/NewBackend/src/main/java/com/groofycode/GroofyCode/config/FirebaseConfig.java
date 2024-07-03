package com.groofycode.GroofyCode.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
public class FirebaseConfig {
    @Value("${groofycode.credentials}")
    private Resource credentialsPath;

    @PostConstruct
    public void InitializeFirebase() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(Paths.get(credentialsPath.getURI()).toFile());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
    }
}
