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

package space.lingu.lamp;

/**
 * @author RollW
 */
public abstract class ErrorCodeFinderChain implements ErrorCodeFinder {
    private ErrorCodeFinderChain next;

    public ErrorCodeFinderChain(ErrorCodeFinderChain next) {
        this.next = next;
    }

    public ErrorCodeFinderChain() {
    }

    @Override
    public ErrorCode fromThrowable(Throwable e) {
        return fromThrowable(e, CommonErrorCode.ERROR_UNKNOWN);
    }

    @Override
    public ErrorCode fromThrowable(Throwable e, ErrorCode defaultErrorCode) {
        ErrorCode errorCode = fromThrowable0(e);
        if (errorCode != null) {
            return errorCode;
        }
        if (next != null) {
            return next.fromThrowable(e);
        }
        return defaultErrorCode;
    }

    @Override
    public ErrorCode findErrorCode(String codeValue) {
        ErrorCode errorCode = findErrorCode0(codeValue);
        if (errorCode != null) {
            return errorCode;
        }
        if (next != null) {
            return next.findErrorCode(codeValue);
        }
        return CommonErrorCode.ERROR_UNKNOWN;
    }

    private void appendNext(ErrorCodeFinderChain codeFinderChain) {
        next = codeFinderChain;
    }

    protected abstract ErrorCode findErrorCode0(String codeValue);

    protected abstract ErrorCode fromThrowable0(Throwable throwable);

    public static ErrorCodeFinderChain of(ErrorCodeFinder... errorCodeFinders) {
        if (errorCodeFinders.length == 0) {
            return EmptyErrorCodeFinderChain.DEFAULT_EMPTY;
        }
        ErrorCodeFinderChain head = fromFinder(errorCodeFinders[0]);
        final ErrorCodeFinderChain result = head;
        for (int i = 1; i < errorCodeFinders.length; i++) {
            head.appendNext(fromFinder(errorCodeFinders[i]));
            head = head.next;
        }
        return result;
    }

    public static ErrorCodeFinderChain start(ErrorCodeFinder... errorCodeFinders) {
        if (errorCodeFinders.length == 0) {
            return FinderChain.COMMON_CHAIN;
        }
        ErrorCodeFinder[] finders = new ErrorCodeFinder[errorCodeFinders.length + 1];
        finders[0] =  FinderChain.COMMON_CHAIN;
        System.arraycopy(errorCodeFinders, 0, finders, 1, errorCodeFinders.length);
        return of(finders);
    }

    private static ErrorCodeFinderChain fromFinder(ErrorCodeFinder finder) {
        if (finder == null) {
            return new EmptyErrorCodeFinderChain();
        }

        if (finder instanceof ErrorCodeFinderChain) {
            return (ErrorCodeFinderChain) finder;
        }
        return new FinderChain(finder);
    }

    private static class FinderChain extends ErrorCodeFinderChain {
        private static final ErrorCodeFinderChain COMMON_CHAIN =
                new FinderChain(CommonErrorCode.SUCCESS);

        private final ErrorCodeFinder errorCodeFinder;

        public FinderChain(ErrorCodeFinder errorCodeFinder) {
            this.errorCodeFinder = errorCodeFinder;
        }

        @Override
        protected ErrorCode findErrorCode0(String codeValue) {
            return errorCodeFinder.findErrorCode(codeValue);
        }

        @Override
        protected ErrorCode fromThrowable0(Throwable throwable) {
            return errorCodeFinder.fromThrowable(throwable);
        }
    }

    private static class EmptyErrorCodeFinderChain extends ErrorCodeFinderChain {
        private static final EmptyErrorCodeFinderChain DEFAULT_EMPTY = new EmptyErrorCodeFinderChain();

        @Override
        protected ErrorCode findErrorCode0(String codeValue) {
            return null;
        }

        @Override
        protected ErrorCode fromThrowable0(Throwable throwable) {
            return null;
        }
    }
}
