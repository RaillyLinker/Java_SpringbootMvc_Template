package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_QuartzMetadata_QRTZ_JOB_DETAILS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Db1_QuartzMetadata_QRTZ_JOB_DETAILS_Repository extends JpaRepository<Db1_QuartzMetadata_QRTZ_JOB_DETAILS, Long> {
}