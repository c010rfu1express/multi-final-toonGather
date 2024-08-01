package com.multi.toonGather.common.exception;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Please explain the class!!
 *
 * @author : seoyun
 * @fileName : GlobalExceptionHandler
 * @since : 2024-07-31
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "common/error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "common/error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "common/error/general";
    }
}
