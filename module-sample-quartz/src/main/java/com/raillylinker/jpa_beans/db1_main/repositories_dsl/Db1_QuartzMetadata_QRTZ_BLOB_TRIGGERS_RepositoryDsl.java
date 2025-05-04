package com.raillylinker.jpa_beans.db1_main.repositories_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class Db1_QuartzMetadata_QRTZ_BLOB_TRIGGERS_RepositoryDsl {
    Db1_QuartzMetadata_QRTZ_BLOB_TRIGGERS_RepositoryDsl(
            @NotNull EntityManager entityManager
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @NotNull
    JPAQueryFactory queryFactory;


    // ---------------------------------------------------------------------------------------------
//    public @NotNull List<Db1_Template_FkTestManyToOneChild> findChildByParentId(
//            @NotNull Long parentUid
//    ) {
//        return queryFactory
//                .selectFrom(db1_Template_FkTestManyToOneChild)
//                .leftJoin(db1_Template_FkTestManyToOneChild.fkTestParent, db1_Template_FkTestParent)
//                .where(db1_Template_FkTestParent.uid.eq(parentUid))
//                .fetch();
//    }
}
