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

package space.lingu.lamp.security.authentication.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import space.lingu.lamp.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.authentication.token.TokenAuthResult;
import space.lingu.lamp.security.authorization.PrivilegedUser;
import space.lingu.lamp.security.authorization.PrivilegedUserProvider;
import space.lingu.lamp.security.authorization.adapter.PrivilegedUserAuthenticationToken;
import space.lingu.lamp.user.UserSignatureProvider;
import tech.rollw.common.web.CommonRuntimeException;

/**
 * @author RollW
 */
public class TokenBasedAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenBasedAuthenticationProvider.class);

    private final AuthenticationTokenService authenticationTokenService;
    private final PrivilegedUserProvider privilegedUserProvider;
    private final UserSignatureProvider userSignatureProvider;

    public TokenBasedAuthenticationProvider(
            AuthenticationTokenService authenticationTokenService,
            PrivilegedUserProvider privilegedUserProvider,
            UserSignatureProvider userSignatureProvider
    ) {
        this.authenticationTokenService = authenticationTokenService;
        this.privilegedUserProvider = privilegedUserProvider;
        this.userSignatureProvider = userSignatureProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenBasedAuthenticationToken tokenBasedAuthenticationToken = (TokenBasedAuthenticationToken) authentication;
        String token = tokenBasedAuthenticationToken.getCredentials();
        Long id = authenticationTokenService.getUserId(token);
        if (id == null) {
            throw new BadCredentialsException("Invalid token");
        }
        try {
            String signature = userSignatureProvider.getSignature(id);
            TokenAuthResult tokenAuthResult = authenticationTokenService.verifyToken(token, signature);
            long userId = tokenAuthResult.userId();
            PrivilegedUser privilegedUser = privilegedUserProvider.loadPrivilegedUserById(userId);
            check(privilegedUser);
            return new PrivilegedUserAuthenticationToken(privilegedUser);
        } catch (CommonRuntimeException e) {
            throw new TokenAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(TokenBasedAuthenticationToken.class);
    }

    private void check(PrivilegedUser user) throws AuthenticationException {
        if (user.isLocked()) {
            logger.debug("Failed to authenticate since user account is locked, user[{}]", user.getUserId());
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            logger.debug("Failed to authenticate since user account is disabled, user[{}]", user.getUserId());
            throw new DisabledException("User account is disabled");
        }
        if (user.isCanceled()) {
            logger.debug("Failed to authenticate since user account has canceled, user[{}]", user.getUserId());
            throw new CanceledException("User account has canceled");
        }
    }
}
