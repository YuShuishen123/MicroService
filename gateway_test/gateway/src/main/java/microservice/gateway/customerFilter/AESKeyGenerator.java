package microservice.gateway.customerFilter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESKeyGenerator {
    public static String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // 128位密钥
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        // 使用Base64编码将字节数组转换为字符串
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void main(String[] args) {
        try {
            String key = generateAESKey();
            System.out.println(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
