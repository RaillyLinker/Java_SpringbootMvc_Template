package com.raillylinker.jpa_beans.db1_main.repositories_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestParent;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.raillylinker.jpa_beans.db1_main.entities.QDb1_Template_FkTestManyToOneChild.db1_Template_FkTestManyToOneChild;
import static com.raillylinker.jpa_beans.db1_main.entities.QDb1_Template_FkTestParent.db1_Template_FkTestParent;

@Repository
public class Db1_Template_FkTestParent_RepositoryDsl {
    Db1_Template_FkTestParent_RepositoryDsl(
            @NotNull EntityManager entityManager
    ) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @NotNull
    JPAQueryFactory queryFactory;


    // ---------------------------------------------------------------------------------------------
    public @NotNull List<Db1_Template_FkTestParent> findParentWithChildren() {
        return queryFactory
                .selectFrom(db1_Template_FkTestParent)
                .leftJoin(db1_Template_FkTestParent.fkTestManyToOneChildList, db1_Template_FkTestManyToOneChild)
                .fetchJoin() // fetchJoin을 사용하여 자식 엔티티를 함께 가져옴
                .fetch(); // 결과를 가져옴
    }

    public @NotNull List<Db1_Template_FkTestParent> findParentWithChildrenByName(
            @NotNull String parentName
    ) {
        return queryFactory
                .selectFrom(db1_Template_FkTestParent)
                .leftJoin(db1_Template_FkTestParent.fkTestManyToOneChildList, db1_Template_FkTestManyToOneChild)
                .fetchJoin()
                .where(db1_Template_FkTestParent.parentName.like("%" + parentName.replace(" ", "") + "%"))
                .fetch();
    }
}
