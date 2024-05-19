package ir.doodmood.mashtframework.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Component;
import ir.doodmood.mashtframework.annotation.Properties;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.exception.CriticalError;
import ir.doodmood.mashtframework.exception.JWTTokenExpiredException;
import ir.doodmood.mashtframework.exception.JWTVerificationFailedException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.random.RandomGenerator;

@Component(singleton = true)
public class JWT {
    private final byte[] key;
    private final Logger logger;
    private final static byte[] hexCharset = "0123456789abcdef".getBytes();

    @Autowired
    private JWT(@Properties("JWT_key") String key, Logger logger) {
        if (key == null) {
            logger.critical("JWT key is not provided.");
            throw new CriticalError();
        }
        try {
            this.key = Base64.getDecoder().decode(key);
        } catch (Exception e) {
            logger.critical("Could not decode the JWT Key as base64 :", e.getMessage());
            throw new CriticalError();
        }
        this.logger = logger;
    }

    public String sign(String header, String payload) {
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            logger.critical("JWT HmacSHA256 algorithm not found :", e.getMessage());
            throw new CriticalError();
        }
        try {
            mac.init(secretKey);
        } catch (InvalidKeyException e) {
            logger.critical("JWT HmacSHA256 invalid key :", e.getMessage());
            throw new CriticalError();
        }
        return Base64.getUrlEncoder().encodeToString(mac.doFinal((header + "." + payload).getBytes())).replace("=", "");
    }

    public long getTime() {
        return System.currentTimeMillis() / 1000;
    }

    public String getToken(Object payload, long lifetime, String csrfToken) {
        JsonObject p = new Gson().toJsonTree(payload).getAsJsonObject();
        long time = getTime();
        if (lifetime >= 0)
            p.addProperty("exp", time + lifetime);
        p.addProperty("iat", time);
        if (csrfToken != null)
            p.addProperty("csrfToken", csrfToken);
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        String payloadb64 = Base64.getUrlEncoder().encodeToString(p.toString().getBytes()).replace("=", "");
        String headerb64 = Base64.getUrlEncoder().encodeToString(header.getBytes()).replace("=", "");
        String signb64 = sign(headerb64, payloadb64);

        return headerb64 + "." + payloadb64 + "." + signb64;
    }

    public String getToken(Object payload, long lifetime) {
        return getToken(payload, lifetime, null);
    }

    public String getToken(Object payload) {
        return getToken(payload, -1, null);
    }

    public boolean verify(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3)
            return false;

        if (!parts[2].equals(sign(parts[0], parts[1])))
            return false;

        while (parts[0].length() % 4 != 0) parts[0] += "=";
        while (parts[1].length() % 4 != 0) parts[1] += "=";

        try {
            Base64.getUrlDecoder().decode(parts[0]);
            Base64.getUrlDecoder().decode(parts[1]);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Object getPayload(Class clazz, String token, String csrf) throws JWTVerificationFailedException, JWTTokenExpiredException {
        if (!verify(token)) {
            logger.error("invalid JWT token received(invalid signature) :", token);
            throw new JWTVerificationFailedException();
        }

        String payload = token.split("\\.")[1];
        while (payload.length() % 4 != 0) payload += "=";
        JsonObject ret = JsonParser.parseString(new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8)).getAsJsonObject();
        long time = getTime();
        if ((ret.get("exp") != null && ret.get("exp").getAsInt() < time) ||
                (ret.get("iat") != null && ret.get("iat").getAsInt() > time)) {
            logger.info("JWT token expired.");
            throw new JWTTokenExpiredException();
        }

        if (ret.get("csrfToken") != null && (csrf == null || !ret.get("csrfToken").getAsString().equals(csrf))) {
            logger.error("invalid JWT token received(invalid csrf token) :", token);
            throw new JWTVerificationFailedException();
        }

        ret.remove("exp");
        ret.remove("iat");
        ret.remove("csrfToken");

        return new Gson().fromJson(ret, clazz);
    }

    public String generateCSRFToken(int length) {
        byte[] ret = new byte[length];
        RandomGenerator.getDefault().nextBytes(ret);

        return encodeHex(ret);
    }

    public String encodeHex(byte[] inp) {
        StringBuilder ret = new StringBuilder();
        for (byte i : inp) {
            ret.append((char) hexCharset[(i >> 4) & 15]);
            ret.append((char) hexCharset[i & 15]);
        }

        return ret.toString();
    }
}
