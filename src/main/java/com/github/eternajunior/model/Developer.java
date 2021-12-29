package com.github.eternajunior.model;

import com.github.eternajunior.core.HasId;
import lombok.Data;

import java.util.Set;

@Data
public class Developer implements HasId<Long> {
    private Long id;
    private Set<Skill> skills;
    private Account account;
}
