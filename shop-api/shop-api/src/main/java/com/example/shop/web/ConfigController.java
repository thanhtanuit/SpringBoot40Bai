package com.example.shop.web;

import com.example.shop.config.ShopProperties;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    private final ShopProperties props;
    private final Environment env;

    public ConfigController(ShopProperties props, Environment env) {
        this.props = props;
        this.env = env;
    }

    @GetMapping
    public Map<String, Object> config() {
        return Map.of(
                "shop", props,
                "activeProfiles", Arrays.asList(env.getActiveProfiles())
        );
    }
}
