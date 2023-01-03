/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp.web.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import space.lingu.lamp.web.data.dto.TokenAuthResult;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author RollW
 */
@Service
public class JwtAuthTokenService implements AuthenticationTokenService {
    private static final String TOKEN_HEAD = "Bearer ";

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenService.class);

    private final String secret = "JwtAuthByRollW. Copyright (C) 2023 RollW. Lamp Blog Project maintained by RollW.";
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    // TODO: replace with external setting, and use RSA key pair.

    public JwtAuthTokenService() {
    }

    @Override
    public String generateAuthToken(long userId) {
        String rawToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(getExpirationDateFromNow())
                .setIssuer("Lingu Inc.")
                .signWith(key)
                .compact();
        return TOKEN_HEAD + rawToken;
    }

    @Override
    public TokenAuthResult verifyToken(String token) {
        if (token == null) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        if (!token.startsWith(TOKEN_HEAD)) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        String rawToken = token.substring(TOKEN_HEAD.length());
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(rawToken)
                    .getBody();
            long userId = Long.parseLong(claims.getSubject());
            return TokenAuthResult.success(userId, token);
        } catch (ExpiredJwtException e) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_TOKEN_EXPIRED);
        } catch (NumberFormatException e) {
            logger.error("Invalid jwt token number format: " + rawToken);
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
    }

    private Date getExpirationDateFromNow() {
        long now = System.currentTimeMillis();
        long exp = now + MINUTES_5;
        return new Date(exp);
    }

    //
    private static final long DAYS_7 = 1000 * 60 * 60 * 24 * 7;
    private static final long MINUTES_5 = 1000 * 60 * 5;
}
