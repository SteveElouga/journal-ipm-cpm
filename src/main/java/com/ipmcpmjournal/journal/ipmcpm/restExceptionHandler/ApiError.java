package com.ipmcpmjournal.journal.ipmcpm.restExceptionHandler;

public record ApiError(int code, String message, String cause) {
}
