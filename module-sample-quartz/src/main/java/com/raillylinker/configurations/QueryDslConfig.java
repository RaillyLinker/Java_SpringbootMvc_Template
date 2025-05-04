package com.raillylinker.configurations;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    public QueryDslConfig(
            @NotNull EntityManager em
    ) {
        this.em = em;
    }

    private final @NotNull EntityManager em;

    @Bean
    public @NotNull JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}
