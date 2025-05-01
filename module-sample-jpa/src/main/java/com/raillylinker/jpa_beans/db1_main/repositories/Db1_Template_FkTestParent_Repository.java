package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_FkTestParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db1_Template_FkTestParent_Repository extends JpaRepository<Db1_Template_FkTestParent, Long> {
//    @Valid
//    @NotNull
//    @org.jetbrains.annotations.NotNull
//    List<Db1_Template_FkTestParent> findAllByRowDeleteDateStrOrderByRowCreateDate(
//            @Valid @NotNull @org.jetbrains.annotations.NotNull
//            String rowDeleteDateStr
//    );
}