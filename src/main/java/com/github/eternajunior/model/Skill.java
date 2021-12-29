package com.github.eternajunior.model;

import com.github.eternajunior.core.HasId;
import lombok.Data;

@Data
public class Skill implements HasId<Long> {
    private Long id;
    private String skill;
}
