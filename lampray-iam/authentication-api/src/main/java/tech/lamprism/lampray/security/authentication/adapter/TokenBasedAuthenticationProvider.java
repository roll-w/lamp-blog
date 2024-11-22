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

package tech.lamprism.lampray.security.authentication.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import tech.lamprism.lampray.authentication.token.AuthenticationTokenService;
import tech.lamprism.lampray.authentication.token.TokenAuthResult;
import tech.lamprism.lampray.security.authorization.PrivilegedUser;
import tech.lamprism.lampray.security.authorization.PrivilegedUserProvider;
import tech.lamprism.lampray.security.authorization.adapter.PrivilegedUserAuthenticationToken;
import tech.lamprism.lampray.user.UserSignatureProvider;
import tech.rollw.common.web.CommonRuntimeException;

/**
 * @author RollW
 */
public class TokenBasedAuthenticationProvider extends PrivilegedUserBasedAuthenticationProvider {
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
    protected Logger getLogger() {
        return logger;
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
}
