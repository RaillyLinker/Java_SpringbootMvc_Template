package com.raillylinker.jpa_beans.db1_main.repositories_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestManyToOneChild;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.raillylinker.jpa_beans.db1_main.entities.QDb1_Template_FkTestManyToOneChild.db1_Template_FkTestManyToOneChild;
import static com.raillylinker.jpa_beans.db1_main.entities.QDb1_Template_FkTestParent.db1_Template_FkTestParent;

@Repository
public class Db1_Template_FkTestManyToOneChild_RepositoryDsl {
    Db1_Template_FkTestManyToOneChild_RepositoryDsl(
            @NotNull EntityManager entityManager
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @NotNull
    JPAQueryFactory queryFactory;


    // ---------------------------------------------------------------------------------------------
    public @NotNull List<Db1_Template_FkTestManyToOneChild> findChildByParentId(
            @NotNull Long parentUid
    ) {
        return queryFactory
                .selectFrom(db1_Template_FkTestManyToOneChild)
                .leftJoin(db1_Template_FkTestManyToOneChild.fkTestParent, db1_Template_FkTestParent)
                .where(db1_Template_FkTestParent.uid.eq(parentUid))
                .fetch();
    }
}
