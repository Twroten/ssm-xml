package com.wall.ssm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private BigDecimal salary;
}
