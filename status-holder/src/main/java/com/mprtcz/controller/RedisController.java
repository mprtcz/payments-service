package com.mprtcz.controller;

import com.mprtcz.config.RedisConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedisController {
    RedisConfig redisConfig;

    @GetMapping("/v1/status/test")
    public ResponseEntity testRedis() {
        redisConfig.testThis();
        return ResponseEntity.ok().build();
    }
}
