package org.example.crpyto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class config {
    @Bean
    public int generateKeyRSA() throws NoSuchAlgorithmException, IOException, InterruptedException {
        RSAUtils rsaUtils = new RSAUtils();
        rsaUtils.generateKey();
        return 0;
    }
}
