package mamonova.com.events.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    @Value("${app.token.expiration.hours:24}")
    private int tokenExpirationHours;

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        Date expirationDate = new Date(System.currentTimeMillis() + tokenExpirationHours * 60 * 60 * 1000);

        tokenStore.put(token, new TokenInfo(userId, expirationDate));
        return token;
    }

    public Long getUserIdFromToken(String token) {
        TokenInfo tokenInfo = tokenStore.get(token);

        if (tokenInfo == null) {
            return null;
        }

        if (tokenInfo.getExpirationDate().before(new Date())) {
            tokenStore.remove(token);
            return null;
        }

        return tokenInfo.getUserId();
    }

    public void invalidateToken(String token) {
        tokenStore.remove(token);
    }

    public boolean isValidToken(String token) {
        TokenInfo tokenInfo = tokenStore.get(token);

        if (tokenInfo == null) {
            return false;
        }

        boolean valid = !tokenInfo.getExpirationDate().before(new Date());

        if (!valid) {
            tokenStore.remove(token);
        }

        return valid;
    }

    // Внутренний класс для хранения информации о токене
    @Data
    @AllArgsConstructor
    private static class TokenInfo {
        private Long userId;
        private Date expirationDate;
    }
}