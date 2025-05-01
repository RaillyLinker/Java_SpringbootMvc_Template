package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_LogicalDeleteUniqueData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db1_Template_LogicalDeleteUniqueData_Repository extends JpaRepository<Db1_Template_LogicalDeleteUniqueData, Long> {
//    @Valid @NotNull @org.jetbrains.annotations.NotNull
//    Optional<Db1_Template_LogicalDeleteUniqueData> findByUidAndRowDeleteDateStr(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            Long uid,
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
//
//    @Valid @NotNull @org.jetbrains.annotations.NotNull
//    List<Db1_Template_LogicalDeleteUniqueData> findAllByRowDeleteDateStrOrderByRowCreateDate(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
//
//    @Valid @NotNull @org.jetbrains.annotations.NotNull
//    List<Db1_Template_LogicalDeleteUniqueData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
//
//    @Valid @NotNull @org.jetbrains.annotations.NotNull
//    Optional<Db1_Template_LogicalDeleteUniqueData> findByUniqueValueAndRowDeleteDateStr(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            Integer uniqueValue,
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
}