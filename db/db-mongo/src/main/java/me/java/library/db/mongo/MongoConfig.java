package me.java.library.db.mongo;

import me.java.library.db.mongo.impl.BaseMongoRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author :  sylar
 * @FileName :
 * @CreateDate :  2017/11/08
 * @Description :
 * @ReviewedBy :
 * @ReviewedOn :
 * @VersionHistory :
 * @ModifiedBy :
 * @ModifiedDate :
 * @Comments :
 * @CopyRight : COPYRIGHT(c) xxx.com All Rights Reserved
 * *******************************************************************************************
 */
@Configuration
@EnableMongoRepositories(basePackages = "me", repositoryBaseClass = BaseMongoRepositoryImpl.class)
public class MongoConfig {
}
