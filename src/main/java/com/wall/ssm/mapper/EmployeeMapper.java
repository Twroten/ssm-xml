package com.wall.ssm.mapper;


import com.wall.ssm.domain.Employee;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeMapper {

    List<Employee> query(@Param("minSalary") BigDecimal minSalary);

    List<Employee> query(
            @Param("minSalary") BigDecimal minSalary,//
            @Param("maxSalary") BigDecimal masSalary//
    );

    List<Employee> query(
            @Param("minSalary") BigDecimal minSalary,//
            @Param("maxSalary") BigDecimal masSalary,//
            @Param("deptId") Long deptId
    );
}
