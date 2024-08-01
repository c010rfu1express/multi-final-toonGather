package com.multi.toonGather.common.exception;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : NotFoundException
 * @since : 2024-07-31
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
