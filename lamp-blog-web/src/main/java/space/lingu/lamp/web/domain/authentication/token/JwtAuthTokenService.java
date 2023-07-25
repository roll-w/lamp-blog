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

package space.lingu.lamp.web.domain.authentication.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.rollw.common.web.AuthErrorCode;

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
    public JwtAuthTokenService() {
    }

    @Override
    public String generateAuthToken(long userId, String signature) {
        Key key = Keys.hmacShaKeyFor(signature.getBytes(StandardCharsets.UTF_8));
        String rawToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(getExpirationDateFromNow())
                .setIssuer("Lingu Inc.")
                .signWith(key)
                .compact();
        return TOKEN_HEAD + rawToken;
    }

    @Override
    public TokenAuthResult verifyToken(String token, String signature) {
        if (token == null) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        if (!token.startsWith(TOKEN_HEAD)) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        String rawToken = token.substring(TOKEN_HEAD.length());
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signature.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(rawToken)
                    .getBody();
            long userId = Long.parseLong(claims.getSubject());
            return TokenAuthResult.success(userId, token);
        } catch (ExpiredJwtException e) {
            return TokenAuthResult.failure(AuthErrorCode.ERROR_TOKEN_EXPIRED);
        } catch (NumberFormatException e) {
            logger.error("Invalid jwt token number format: {}", rawToken);
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
            return TokenAuthResult.failure(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
    }

    @Override
    public Long getUserId(String token) {
        if (!token.startsWith(TOKEN_HEAD)) {
            return null;
        }
        try {
            String rawToken = token.substring(TOKEN_HEAD.length());
            Claims claims = tryParseClaims(rawToken);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("all")
    private static Claims tryParseClaims(String token) {
        // https://github.com/jwtk/jjwt/issues/135
        if (!token.contains(".")) {
            throw new IllegalArgumentException("Invalid token format");
        }
        int i = token.lastIndexOf('.');
        String withoutSignature = token.substring(0, i + 1);
        Jwt<Header, Claims> untrusted = Jwts.parserBuilder()
                .setClock(JwtAuthTokenService::getVerifydate)
                .build()
                .parseClaimsJwt(withoutSignature);
        return untrusted.getBody();
    }

    private long expireTimeInSecond = DAYS_7;

    @Override
    public void setTokenExpireTime(long expireTimeInSecond) {
        this.expireTimeInSecond = expireTimeInSecond;
    }


    private static final Date VERIFYDATE = new Date(1);

    private static Date getVerifydate() {
        return VERIFYDATE;
    }

    private Date getExpirationDateFromNow() {
        long now = System.currentTimeMillis();
        long exp = now + expireTimeInSecond * 1000;
        return new Date(exp);
        // TODO: allow set expiration date.
    }

    //
    private static final long DAYS_7 = 60 * 60 * 24 * 7L;
    private static final long MINUTES_5 = 60 * 5L;
    private static final long SECONDS_5 = 5L;
}
