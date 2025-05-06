package com.raillylinker.mongodb_beans.mdb1_main.repositories;

import com.raillylinker.mongodb_beans.mdb1_main.documents.Mdb1_TestData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Repository
public interface Mdb1_TestData_Repository extends MongoRepository<Mdb1_TestData, String> {
    @NotNull
    Long countByRowDeleteDateStr(
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    Page<Mdb1_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr,
            @NotNull Pageable pageable
    );

    @NotNull
    List<Mdb1_TestData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @Nullable
    Mdb1_TestData findByUidAndRowDeleteDateStr(
            @NotNull String uid,
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Mdb1_TestData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );
}
