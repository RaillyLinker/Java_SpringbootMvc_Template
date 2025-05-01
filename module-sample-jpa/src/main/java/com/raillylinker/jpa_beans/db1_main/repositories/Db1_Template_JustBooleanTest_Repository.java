package com.raillylinker.jpa_beans.db1_main.repositories;

import com.raillylinker.jpa_beans.db1_main.entities.Db1_Template_JustBooleanTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.jetbrains.annotations.*;

@Repository
public interface Db1_Template_JustBooleanTest_Repository extends JpaRepository<Db1_Template_JustBooleanTest, Long> {
    @Query(
            nativeQuery = true,
            value = """
                    SELECT 
                    true AS normalBoolValue, 
                    (TRUE = :inputVal) AS funcBoolValue, 
                    IF
                    (
                        (TRUE = :inputVal), 
                        TRUE, 
                        FALSE
                    ) AS ifBoolValue, 
                    (
                        CASE 
                            WHEN 
                                (TRUE = :inputVal) 
                            THEN 
                                TRUE 
                            ELSE 
                                FALSE 
                        END
                    ) AS caseBoolValue, 
                    (
                        SELECT 
                        bool_value 
                        FROM 
                        template.just_boolean_test 
                        WHERE 
                        uid = 1
                    ) AS tableColumnBoolValue
                    """
    )
    @NotNull
    MultiCaseBooleanReturnTestOutputVo multiCaseBooleanReturnTest(
            @Param("inputVal")
            @NotNull Boolean inputVal
    );

    interface MultiCaseBooleanReturnTestOutputVo {
        @NotNull
        Long getNormalBoolValue();

        @NotNull
        Long getFuncBoolValue();

        @NotNull
        Long getIfBoolValue();

        @NotNull
        Long getCaseBoolValue();

        @NotNull
        Boolean getTableColumnBoolValue();
    }
}
