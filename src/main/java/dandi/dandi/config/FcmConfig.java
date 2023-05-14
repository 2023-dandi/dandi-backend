package dandi.dandi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import dandi.dandi.pushnotification.exception.FCMInitializationFailedException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FcmConfig {

    private final String fcmCredential;

    public FcmConfig(@Value("${fcm.credential}") String fcmCredential) {
        this.fcmCredential = fcmCredential;
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        initialize();
        return FirebaseMessaging.getInstance();
    }

    private void initialize() {
        ClassPathResource resource = new ClassPathResource(fcmCredential);
        try (InputStream inputStream = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();
            initializeWithOptions(options);
        } catch (IOException e) {
            throw new FCMInitializationFailedException();
        }
    }

    private void initializeWithOptions(FirebaseOptions options) {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
