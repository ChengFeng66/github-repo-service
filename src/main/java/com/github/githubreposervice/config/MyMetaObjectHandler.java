package com.github.githubreposervice.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus auto-fill handler for fields
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    /**
     * Fill on insert
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("Starting insert fill...");
        
        // Fill create time
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        
        // Fill update time
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
    
    /**
     * Fill on update
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("Starting update fill...");
        
        // Fill update time
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
