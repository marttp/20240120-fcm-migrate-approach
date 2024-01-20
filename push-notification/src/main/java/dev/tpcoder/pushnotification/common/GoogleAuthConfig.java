package dev.tpcoder.pushnotification.common;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Configuration
public class GoogleAuthConfig {

    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/firebase.messaging"
    );

    private final String firebaseServiceAccount;

    public GoogleAuthConfig(@Value("${firebase.service.account}") String firebaseServiceAccount) {
        this.firebaseServiceAccount = firebaseServiceAccount;
    }

    @Bean
    @Primary
    @Qualifier("file")
    GoogleCredentials credentialFromFile() throws IOException {
        // With classpath resource - This way need to attach service-account.json to container
        ClassPathResource serviceAccount = new ClassPathResource("service-account.json");
        return GoogleCredentials.fromStream(serviceAccount.getInputStream())
                .createScoped(SCOPES);
    }

    @Bean
    @Qualifier("base64")
    GoogleCredentials credentialFromBase64() throws IOException {
        byte[] decodedServiceAccount = Base64.getDecoder().decode(firebaseServiceAccount);
        return GoogleCredentials.fromStream(new ByteArrayInputStream(decodedServiceAccount))
                .createScoped(SCOPES);
    }
}
