package com.raillylinker.configurations.mongodb_configs;

import com.raillylinker.const_objects.ModuleConst;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.jetbrains.annotations.NotNull;

// [MongoDB 설정]
@Configuration
@EnableMongoRepositories(
        basePackages = {
                ModuleConst.PACKAGE_NAME + ".mongodb_beans." + Mdb1MainConfig.MONGO_DB_DIRECTORY_NAME + ".repositories"
        },
        mongoTemplateRef = Mdb1MainConfig.MONGO_DB_DIRECTORY_NAME
)
@EnableTransactionManagement
public class Mdb1MainConfig extends AbstractMongoClientConfiguration {
    // !!!application.yml 의 datasource-mongodb 안에 작성된 이름 할당하기!!!
    public static final @NotNull String MONGO_DB_CONFIG_NAME = "mdb1-main";

    // !!!data_sources/mongo_db_sources 안의 서브 폴더(documents, repositories 를 가진 폴더)의 이름 할당하기!!!
    public static final @NotNull String MONGO_DB_DIRECTORY_NAME = "mdb1_main";

    // Database 트랜젝션 이름 변수
    // 트랜젝션을 적용할 함수 위에, @CustomMongoDbTransactional 어노테이션과 결합하여,
    // @CustomMongoDbTransactional([MongoDbConfig.TRANSACTION_NAME])
    // 위와 같이 적용하세요.
    public static final @NotNull String TRANSACTION_NAME = MONGO_DB_DIRECTORY_NAME + "_PlatformTransactionManager";

    // ---------------------------------------------------------------------------------------------
    @Value("${datasource-mongodb." + MONGO_DB_CONFIG_NAME + ".uri}")
    private String mongoDbUri;

    private SimpleMongoClientDatabaseFactory mongoClientFactory;

    @PostConstruct
    public void init() {
        this.mongoClientFactory = new SimpleMongoClientDatabaseFactory(mongoDbUri);
    }

    @Bean(name = MONGO_DB_DIRECTORY_NAME)
    public @NotNull MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClientFactory);
    }

    @Bean(name = TRANSACTION_NAME)
    public @NotNull MongoTransactionManager customTransactionManager() {
        return new MongoTransactionManager(mongoClientFactory);
    }

    @Override
    protected @NotNull String getDatabaseName() {
        return MONGO_DB_CONFIG_NAME;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }

    @Override
    public @NotNull MongoDatabaseFactory mongoDbFactory() {
        return mongoClientFactory;
    }
}
