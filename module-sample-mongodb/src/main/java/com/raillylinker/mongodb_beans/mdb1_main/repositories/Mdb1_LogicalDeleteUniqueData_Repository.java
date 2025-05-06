package com.raillylinker.mongodb_beans.mdb1_main.repositories;

import com.raillylinker.mongodb_beans.mdb1_main.documents.Mdb1_LogicalDeleteUniqueData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Repository
public interface Mdb1_LogicalDeleteUniqueData_Repository extends MongoRepository<Mdb1_LogicalDeleteUniqueData, String> {
    @NotNull
    List<Mdb1_LogicalDeleteUniqueData> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @Nullable
    Mdb1_LogicalDeleteUniqueData findByUidAndRowDeleteDateStr(
            @NotNull String uid,
            @NotNull String rowDeleteDateStr
    );

    @NotNull
    List<Mdb1_LogicalDeleteUniqueData> findAllByRowDeleteDateStrNotOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );

    @Nullable
    Mdb1_LogicalDeleteUniqueData findByUniqueValueAndRowDeleteDateStr(
            @NotNull Integer uniqueValue,
            @NotNull String rowDeleteDateStr
    );
}
