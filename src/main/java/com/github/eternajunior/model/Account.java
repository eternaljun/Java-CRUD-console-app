package com.github.eternajunior.model;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private AccountStatus accountStatus;
    private String name;
    private Integer balance;
}
