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

package space.lingu.lamp.web.domain.user.repository;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.RegisterVerificationTokenDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.user.RegisterVerificationToken;
import space.lingu.lamp.web.domain.user.UserIdentity;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

/**
 * @author RollW
 */
@Repository
public class RegisterVerificationTokenRepository extends AutoPrimaryBaseRepository<RegisterVerificationToken> {
    private final RegisterVerificationTokenDao registerVerificationTokenDao;

    public RegisterVerificationTokenRepository(LampDatabase lampDatabase,
                                               ContextThreadAware<PageableContext> pageableContextThreadAware,
                                               CacheManager cacheManager) {
        super(lampDatabase.getRegisterVerificationTokenDao(), pageableContextThreadAware, cacheManager);
        this.registerVerificationTokenDao = lampDatabase.getRegisterVerificationTokenDao();
    }

    @Override
    protected Class<RegisterVerificationToken> getEntityClass() {
        return RegisterVerificationToken.class;
    }

    public RegisterVerificationToken findByToken(String token) {
        return cacheResult(
                registerVerificationTokenDao.findByToken(token)
        );
    }

    public RegisterVerificationToken findByUser(UserIdentity user) {
        return findByUser(user.getUserId());
    }

    public RegisterVerificationToken findByUser(long id) {
        return cacheResult(
                registerVerificationTokenDao.findByUserId(id)
        );
    }

    public void makeTokenVerified(RegisterVerificationToken verificationToken) {
        if (verificationToken == null) {
            throw new NullPointerException("Null entity 'RegisterVerificationToken' in makeTokenVerified.");
        }
        RegisterVerificationToken updated = verificationToken.toBuilder()
                .setUsed(true)
                .build();
        update(updated);
    }
}