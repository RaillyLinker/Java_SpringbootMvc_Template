package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_LogicalDeleteUniqueData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Db1_Template_LogicalDeleteUniqueData_Repository extends JpaRepository<Db1_Template_LogicalDeleteUniqueData, Long> {
    @Nullable
    Db1_Template_LogicalDeleteUniqueData findByUidAndRowDeleteDateStr(
            @NotNull Long uid,
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Db1_Template_LogicalDeleteUniqueData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Db1_Template_LogicalDeleteUniqueData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @Nullable
    Db1_Template_LogicalDeleteUniqueData findByUniqueValueAndRowDeleteDateStr(
            @NotNull Integer uniqueValue,
            @NotNull String rowDeleteDateStr
    );
}