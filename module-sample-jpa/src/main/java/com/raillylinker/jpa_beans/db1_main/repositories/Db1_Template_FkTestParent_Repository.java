package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.jetbrains.annotations.*;

import java.util.List;

@Repository
public interface Db1_Template_FkTestParent_Repository extends JpaRepository<Db1_Template_FkTestParent, Long> {
    @NotNull
    List<Db1_Template_FkTestParent> findAllByRowDeleteDateStrOrderByRowCreateDate(
            @NotNull String rowDeleteDateStr
    );
}