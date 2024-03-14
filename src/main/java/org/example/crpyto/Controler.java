package org.example.crpyto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@CrossOrigin(origins = "*")
@RestController
public class Controler {
    @Value("${public.file.path}")
    private String publicKeyFilePath;
    @Value("${private.file.path}")
    private String privateKeyFilePath;
    @Value("${secret.file.path}")
    private String secretFilePath;
    @PostMapping("/key")
    public HttpStatus test(@RequestBody String secretKeyEncrypted ) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        // khởi tạo privateKey để giải mã
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        File privateKeyFile = new File (privateKeyFilePath);
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // khởi tạo đối tượng Cipher để giải mã
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);

        //chuyển secretKey thành mảng byte
        byte[] test = Base64.getDecoder().decode(secretKeyEncrypted);

        // giải mã
        byte[] decrypted = cipher.doFinal(test);
        String secretKey = new String(decrypted, StandardCharsets.UTF_8);

        // Lưu trữ secret Key
        try (FileOutputStream fos = new FileOutputStream(secretFilePath)) {
            fos.write(secretKey.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpStatus.OK;
    }


    @GetMapping("/key")
    public String getPublicKey() throws IOException {
        File publicKeyFile = new File(publicKeyFilePath);
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        String publicKeyTest = Base64.getEncoder().encodeToString(publicKeyBytes);
        return publicKeyTest;
    }

    @PostMapping("/message")
    public String getReturnBody(@RequestBody String mess){
        System.out.println("mess: " +mess);
        return  mess;
    }


}

