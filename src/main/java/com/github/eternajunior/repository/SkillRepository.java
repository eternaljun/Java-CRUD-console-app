package com.github.eternajunior.repository;

import com.github.eternajunior.core.AbstractRepository;
import com.github.eternajunior.model.Skill;
import com.google.gson.Gson;

public class SkillRepository extends AbstractRepository<Skill> {

    public SkillRepository(String path, Gson gson) {
        super(path, Skill.class, gson);
    }
}
