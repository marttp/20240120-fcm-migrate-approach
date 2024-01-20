package dev.tpcoder.pushnotification.firebaseadmin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FcmConfiguration {

    @Bean
    @Primary
    FirebaseMessaging firebaseMessaging(
//            @Qualifier("file") GoogleCredentials googleCredentials
            @Qualifier("base64") GoogleCredentials googleCredentials
    ) {
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
        return FirebaseMessaging.getInstance(app);
    }

}
