package com.github.eternajunior.model;

import lombok.Data;

import java.util.Set;

@Data
public class Developer {
    private Long id;
    private Set<Skill> skills;
    private Account account;
}
