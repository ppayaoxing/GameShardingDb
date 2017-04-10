package com.snowcattle.game.db.async;

import com.snowcattle.game.db.cache.redis.RedisService;
import com.snowcattle.game.db.service.config.DbConfig;
import com.snowcattle.game.db.util.ExecutorUtil;
import com.snowcattle.game.thread.executor.NonOrderedQueuePoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by jiangwenping on 17/4/10.
 * 异步执行更新中心
 */
@Service
public class AsyncDbOperationCenter {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DbConfig dbConfig;

    /**
     * 执行db落得第线程数量
     */
    private NonOrderedQueuePoolExecutor operationExecutor;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void start(){
        int coreSize =  dbConfig.getAsyncDbOperationWorkerSize();
        operationExecutor = new NonOrderedQueuePoolExecutor(coreSize);
    }

    public void stop(){
        if(operationExecutor != null){
            ExecutorUtil.shutdownAndAwaitTermination(operationExecutor, 60, TimeUnit.SECONDS);
        }
    }

    public DbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }
}