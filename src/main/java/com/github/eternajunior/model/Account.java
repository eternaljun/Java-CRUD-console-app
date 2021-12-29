package com.github.eternajunior.model;

import com.github.eternajunior.core.HasId;
import lombok.Data;

@Data
public class Account implements HasId<Long> {
    private Long id;
    private AccountStatus accountStatus;
    private String name;
    private Integer balance;
}
