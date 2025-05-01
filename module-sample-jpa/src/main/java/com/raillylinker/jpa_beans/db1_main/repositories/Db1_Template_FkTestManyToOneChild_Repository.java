package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestManyToOneChild;
import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestParent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Db1_Template_FkTestManyToOneChild_Repository extends JpaRepository<Db1_Template_FkTestManyToOneChild, Long> {
    // 외래키 변수로 검색시, 테이블 컬럼명이 아닌 클래스 변수명을 기준으로 하며, 데이터 타입도 부모 테이블의 클래스 타입을 선언해야합니다.
    @Valid
    @NotNull
    @org.jetbrains.annotations.NotNull
    List<Db1_Template_FkTestManyToOneChild> findAllByFkTestParentAndRowDeleteDateStrOrderByRowCreateDate(
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            Db1_Template_FkTestParent fkTestParent,
            @Valid @NotNull @org.jetbrains.annotations.NotNull
            String rowDeleteDateStr
    );
}