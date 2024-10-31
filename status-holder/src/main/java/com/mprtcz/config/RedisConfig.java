package com.mprtcz.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisSentinelPool;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;

@Component
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisCluster cluster;
    private Set<String> jedisSentinelHosts;

    @PostConstruct
    public void startRedis() {
        cluster = RedisCluster.builder().ephemeral().sentinelCount(1).quorumSize(1)
                .replicationGroup("master1", 1)
                .build();
        cluster.start();

        jedisSentinelHosts = JedisUtil.sentinelHosts(cluster);
    }

    @PreDestroy
    public void stopRedis() {
        cluster.stop();
    }

    // Sample redis i/o
    public void testThis() {
        try (JedisSentinelPool pool = new JedisSentinelPool("master1", jedisSentinelHosts)) {
            var r = pool.getResource();
            r.set("test", "value");
            var result = r.get("test");
            log.info("Result is {}", result);
        }
    }
}
