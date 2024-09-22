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

package space.lingu.lamp.web;

import org.hibernate.HibernateException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * @author RollW
 */
public class HibernateFailureAnalyzer extends AbstractFailureAnalyzer<HibernateException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, HibernateException cause) {
        return new FailureAnalysis(
                "[Data] Hibernate error: " + cause.getMessage(),
                "Check the configuration of database, ensure the database is running properly and accessible.",
                cause
        );
    }
}
