package com.happiness.core.config;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.happiness.common.utils.JsonUtil;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	@Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.dbnum}")
    private int redisDatabase;
    @Value("${redis.maxWaitMillis}")
    private long redisMaxWaitMillis;

    @Value("${redis.max.total}")
    private int redisMaxTotal;
    @Value("${redis.max.idle}")
    private int redisMaxIdle;
    @Value("${redis.min.idle}")
    private int redisMinIdle;
    @Value("${redis.min.evictable.idle.time.millis}")
    private long minEvictableIdleTimeMillis;
    @Value("${redis.eviction.run.time}")
    private long redisEvictionRunTime;
    @Value("${redis.evictable.idle.time}")
    private long redisEvictableIdleTime;

    @Value("${redis.msg.channel}")
    private String redisMsgChannel;
    
    @PostConstruct
    public void init() {
    	System.out.println("初始化redisConfig：redisHost = " + redisHost);
    }

    /**
     * redis connection factory config
     *
     * @return redis connection factory
     */
    @Bean(destroyMethod = "destroy")
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(redisPassword);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        poolConfig.setMaxWaitMillis(redisMaxWaitMillis);
        //最大连接数, 默认8个
        poolConfig.setMaxTotal(redisMaxTotal);
        //最大空闲连接数, 默认8个
        poolConfig.setMaxIdle(redisMaxIdle);
        //是否启用后进先出, 默认true
        poolConfig.setLifo(true);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        //最小空闲连接数, 默认0
        poolConfig.setMinIdle(redisMinIdle);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(redisEvictionRunTime);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        poolConfig.setSoftMinEvictableIdleTimeMillis(redisEvictableIdleTime);
        //在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        //在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(true);
        //每次逐出检查时 逐出的最大数目
        poolConfig.setNumTestsPerEvictionRun(redisMaxIdle / 2);
        jedisConnectionFactory.setPoolConfig(poolConfig);
        // 生产环境去掉，测试只用一个DB就
        jedisConnectionFactory.setDatabase(redisDatabase);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    /**
     * redis template
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
            StringRedisSerializer stringRedisSerializer,
            GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setDefaultSerializer(stringRedisSerializer);
        template.setStringSerializer(stringRedisSerializer);
        template.setEnableDefaultSerializer(true);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        // 允许事务（mutlti）
        template.setEnableTransactionSupport(true);

        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());
        // 允许事务（mutlti）
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean(name = "valueOps")
    public ValueOperations<?, ?> valueOperations(RedisTemplate<?, ?> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean(name = "stringValueOps")
    public ValueOperations<?, ?> stringValueOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    @Bean(name = "listOps")
    public ListOperations<?, ?> listOperations(RedisTemplate<?, ?> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean(name = "stringListOps")
    public ListOperations<?, ?> stringListOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForList();
    }

    @Bean(name = "setOps")
    public SetOperations<?, ?> setOperations(RedisTemplate<?, ?> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean(name = "stringSetOps")
    public SetOperations<?, ?> stringSetOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForSet();
    }

    @Bean(name = "hashOps")
    public HashOperations<?, ?, ?> hashOperations(RedisTemplate<?, ?> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean(name = "stringHashOps")
    public HashOperations<?, ?, ?> stringHashOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForHash();
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer(Charset.forName("UTF-8"));
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(JsonUtil.objectMapper);
        return serializer;
    }
}
