package com.mprtcz.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;

@Component
@Slf4j
public class RedisConfig {
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

    @Bean
    public Jedis getRedisResource() {
        try (JedisSentinelPool pool = new JedisSentinelPool("master1", jedisSentinelHosts)) {
            return pool.getResource();
        }
    }
}
