package com.github.eternajunior.model;

import lombok.Data;

@Data
public class Account {
    private int id;
    private AccountStatus accountStatus;
    private String name;
    private int balance;
}
