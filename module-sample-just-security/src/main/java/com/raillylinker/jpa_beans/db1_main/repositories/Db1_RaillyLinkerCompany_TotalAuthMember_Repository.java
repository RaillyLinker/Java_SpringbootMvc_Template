package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_RaillyLinkerCompany_TotalAuthMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db1_RaillyLinkerCompany_TotalAuthMember_Repository extends JpaRepository<Db1_RaillyLinkerCompany_TotalAuthMember, Long> {
    @Nullable
    Db1_RaillyLinkerCompany_TotalAuthMember findByUidAndRowDeleteDateStr(
            @NotNull Long uid,
            @NotNull String rowDeleteDateStr
    );
}