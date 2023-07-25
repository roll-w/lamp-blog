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

package space.lingu.lamp.web.util;

import space.lingu.lamp.web.domain.article.common.ArticleErrorCode;
import space.lingu.lamp.web.domain.comment.common.CommentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.review.common.ReviewErrorCode;
import tech.rollw.common.web.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to check duplicate error codes.
 *
 * @author RollW
 */
public class ErrorCodeDuplicateChecker {
    public static void main(String[] args) {
        List<Class<? extends ErrorCode>> errorCodeClasses = List.of(
                CommonErrorCode.class, AuthErrorCode.class, IoErrorCode.class,
                UserErrorCode.class, WebCommonErrorCode.class, DataErrorCode.class,
                ContentErrorCode.class, ReviewErrorCode.class, ArticleErrorCode.class,
                CommentErrorCode.class
        );
        List<Conflict> conflicts = checkDuplicate(errorCodeClasses);
        if (conflicts.isEmpty()) {
            System.out.println("No conflicts found.");
            return;
        }
        System.out.println("Conflicts: " + conflicts.size());
        System.out.println("========================================");
        for (Conflict conflict : conflicts) {
            System.out.println("-\t" + conflict);
        }
    }

    private static List<Conflict> checkDuplicate(List<Class<? extends ErrorCode>> errorCodeClasses) {
        List<Conflict> conflicts = new ArrayList<>();
        for (int i = 0; i < errorCodeClasses.size(); i++) {
            Class<? extends ErrorCode> first = errorCodeClasses.get(i);
            ErrorCode[] firstErrorCodes = first.getEnumConstants();
            conflicts.addAll(selfCheck(firstErrorCodes));
            for (int j = i + 1; j < errorCodeClasses.size(); j++) {
                Class<? extends ErrorCode> second = errorCodeClasses.get(j);

                ErrorCode[] secondErrorCodes = second.getEnumConstants();
                conflicts.addAll(checkWith(firstErrorCodes, secondErrorCodes));
            }
        }
        return conflicts;
    }

    private static List<Conflict> selfCheck(ErrorCode[] codes) {
        List<Conflict> conflicts = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            ErrorCode first = codes[i];
            if (first.success()) {
                continue;
            }
            for (int j = i + 1; j < codes.length; j++) {
                ErrorCode second = codes[j];
                if (first.getCode().equals(second.getCode())) {
                    conflicts.add(new Conflict(
                            new ErrorCodePair(first.getClass(), first),
                            new ErrorCodePair(second.getClass(), second)
                    ));
                }
            }
        }
        return conflicts;
    }

    private static List<Conflict> checkWith(ErrorCode[] codes1, ErrorCode[] codes2) {
        List<Conflict> conflicts = new ArrayList<>();
        for (ErrorCode code1 : codes1) {
            if (code1.success()) {
                continue;
            }
            for (ErrorCode code2 : codes2) {
                if (code1.getCode().equals(code2.getCode())) {
                    conflicts.add(new Conflict(
                            new ErrorCodePair(code1.getClass(), code1),
                            new ErrorCodePair(code2.getClass(), code2)
                    ));
                }
            }
        }
        return conflicts;
    }

    private record ErrorCodePair(
            Class<? extends ErrorCode> errorCodeClass,
            ErrorCode errorCode
    ) {
    }

    private record Conflict(
            ErrorCodePair first,
            ErrorCodePair second
    ) {
        @Override
        public String toString() {
            return String.format("%s#%s(%s) <-> %s#%s(%s)",
                    first.errorCodeClass.getSimpleName(), first.errorCode.getName(),
                    first.errorCode.getCode(),
                    second.errorCodeClass.getSimpleName(), second.errorCode.getName(),
                    second.errorCode.getCode()
            );
        }
    }
}
