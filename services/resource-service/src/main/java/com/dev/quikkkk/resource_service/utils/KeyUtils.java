package com.dev.quikkkk.resource_service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {
    public static PublicKey loadPublicKey(String pemPath) {
        try {
            String key = readKeyFromResource(pemPath)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося завантажити публічний ключ", e);
        }
    }

    private static String readKeyFromResource(String pemPath) throws IOException {
        try (InputStream is = KeyUtils.class.getClassLoader().getResourceAsStream(pemPath)) {
            if (is == null) throw new IllegalArgumentException("Файл ключа не знайдено: " + pemPath);
            return new String(is.readAllBytes());
        }
    }
}