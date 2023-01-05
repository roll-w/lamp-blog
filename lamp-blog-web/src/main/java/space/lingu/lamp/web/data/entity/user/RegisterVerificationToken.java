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

package space.lingu.lamp.web.data.entity.user;

import space.lingu.lamp.web.data.entity.VerifiableToken;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.LightConfiguration;
import space.lingu.light.PrimaryKey;

import java.util.Date;

/**
 * Register Verification Token.
 *
 * @author RollW
 */
@DataTable(tableName = "register_verification_token",
        indices = @Index(value = "user_id"))
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120")
public record RegisterVerificationToken(
        @DataColumn(name = "token")
        @PrimaryKey
        String token,

        @DataColumn(name = "user_id")
        long userId,

        @DataColumn(name = "expiry_time")
        long expiryTime,// timestamp

        @DataColumn(name = "used")
        boolean used) implements VerifiableToken {

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    private static final int EXPIRATION = 60 * 24;// min

    public static long calculateExpiryDate(int expiryTimeInMinutes) {
        long now = System.currentTimeMillis();
        long millis = expiryTimeInMinutes * 60 * 1000L;
        return now + millis;
    }

    public static long calculateExpiryDate() {
        return calculateExpiryDate(EXPIRATION);
    }

    public Date toDate() {
        return new Date(expiryTime);
    }

}
