package com.example.shop.web;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello/{ten}")
    public String hello(@PathVariable String ten){
        return "Xin chao "+ten;
    }

    // Trả về object Java, KHÔNG tự ghép chuỗi JSON.
    // Spring sẽ tự chuyển KetQua -> JSON (xem giải thích ở bước 6).
    @GetMapping("/tinh")
    public KetQua tinh(@RequestParam int a, @RequestParam int b) {
        return new KetQua(a, b, a + b);
    }
}
