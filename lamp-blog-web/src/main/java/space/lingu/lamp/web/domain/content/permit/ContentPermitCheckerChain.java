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

package space.lingu.lamp.web.domain.content.permit;

import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessCredential;

/**
 * @author RollW
 */
public abstract class ContentPermitCheckerChain implements ContentPermitChecker {
    private ContentPermitCheckerChain next;

    public ContentPermitCheckerChain(ContentPermitCheckerChain next) {
        this.next = next;
    }

    private ContentPermitCheckerChain() {
    }

    public ContentPermitCheckerChain getNext() {
        return next;
    }

    private void append(ContentPermitCheckerChain next) {
        this.next = next;
    }

    @NonNull
    @Override
    public ContentPermitResult checkPermit(@NonNull Content content, @NonNull ContentAccessCredential credential) {
        if (!credential.getType().needsAuth()) {
            // no need to check.
            return ContentPermitResult.permit();
        }
        ContentPermitResult result = checkPermitInternal(content, credential);
        if (next == null) {
            return result;
        }
        return result.plus(
                next.checkPermit(content, credential)
        );
    }

    @NonNull
    protected abstract ContentPermitResult checkPermitInternal(@NonNull Content content,
                                                               @NonNull ContentAccessCredential credential);

    public static ContentPermitChecker of(ContentPermitChecker... filters) {
        if (filters == null || filters.length == 0) {
            return EmptyCheckerChain.INSTANCE;
        }
        ContentPermitCheckerChain head = buildChain(filters[0]);
        ContentPermitCheckerChain cur = head;
        for (int i = 1; i < filters.length; i++) {
            ContentPermitCheckerChain next = buildChain(filters[i]);
            cur.append(next);
            cur = next;
        }
        return head;
    }

    private static ContentPermitCheckerChain buildChain(ContentPermitChecker filter) {
        if (filter instanceof CheckerChain) {
            return (CheckerChain) filter;
        }
        return new CheckerChain(filter);
    }

    private static class CheckerChain extends ContentPermitCheckerChain {
        private final ContentPermitChecker filter;

        private CheckerChain(ContentPermitChecker filter) {
            this.filter = filter;
        }

        @NonNull
        @Override
        protected ContentPermitResult checkPermitInternal(@NonNull Content content,
                                                          @NonNull ContentAccessCredential credential) {
            return filter.checkPermit(content, credential);
        }
    }

    private static class EmptyCheckerChain extends ContentPermitCheckerChain {
        private static final EmptyCheckerChain INSTANCE = new EmptyCheckerChain();

        @NonNull
        @Override
        protected ContentPermitResult checkPermitInternal(@NonNull Content content, @NonNull ContentAccessCredential credential) {
            return ContentPermitResult.permit();
        }
    }
}
