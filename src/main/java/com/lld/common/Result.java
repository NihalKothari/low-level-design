package com.lld.common;

import java.util.Optional;

/**
 * Uniform success/failure wrapper for service-layer operations.
 */
public final class Result<T> {

    private final T value;
    private final ErrorCode error;

    private Result(T value, ErrorCode error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(ErrorCode error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<ErrorCode> getError() {
        return Optional.ofNullable(error);
    }
}
