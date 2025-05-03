package com.raillylinker.jpa_beans.db1_main.repositories_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class Db1_RaillyLinkerCompany_TotalAuthMember_RepositoryDsl {
    Db1_RaillyLinkerCompany_TotalAuthMember_RepositoryDsl(
            @NotNull EntityManager entityManager
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @NotNull
    JPAQueryFactory queryFactory;


    // ---------------------------------------------------------------------------------------------
}
