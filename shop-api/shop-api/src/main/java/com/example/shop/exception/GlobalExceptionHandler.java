package com.example.shop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        log.warn("404 {} {} - {}", req.getMethod(), req.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Không tìm thấy tài nguyên");
        pd.setType(URI.create("https://api.shop.example/errors/not-found"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("resource", ex.getResource());
        pd.setProperty("resourceId", ex.getId());
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex, HttpServletRequest req) {
        log.warn("409 {} {} - {}", req.getMethod(), req.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Dữ liệu bị trùng");
        pd.setType(URI.create("https://api.shop.example/errors/duplicate"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.putIfAbsent(fe.getField(), fe.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(ge -> errors.putIfAbsent(ge.getObjectName(), ge.getDefaultMessage()));

        log.warn("400 {} {} - {} field sai", req.getMethod(), req.getRequestURI(), errors.size());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Dữ liệu gửi lên không hợp lệ");
        pd.setTitle("Lỗi kiểm tra dữ liệu");
        pd.setType(URI.create("https://api.shop.example/errors/validation"));
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("errors", errors);
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    // JSON hỏng, sai kiểu dữ liệu — lỗi xảy ra trước cả lớp validate
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("400 {} {} - body không đọc được", req.getMethod(), req.getRequestURI());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Body không đúng định dạng JSON hoặc sai kiểu dữ liệu");
        pd.setTitle("Body không hợp lệ");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return pd;   // KHÔNG đưa ex.getMessage() ra ngoài: nó lộ tên class nội bộ
    }

    // /api/products/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Tham số '%s' không đúng kiểu dữ liệu".formatted(ex.getName()));
        pd.setTitle("Tham số không hợp lệ");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAll(Exception ex, HttpServletRequest req) {
        // 500 mới là sự cố hệ thống -> log error kèm stack trace đầy đủ
        log.error("500 {} {}", req.getMethod(), req.getRequestURI(), ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Đã có lỗi xảy ra. Vui lòng thử lại sau.");
        pd.setTitle("Lỗi hệ thống");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}