package com.github.eternajunior.repository;

import com.github.eternajunior.core.AbstractRepository;
import com.github.eternajunior.model.Developer;
import com.google.gson.Gson;

public class DeveloperRepository extends AbstractRepository<Developer> {

    public DeveloperRepository(String path, Gson gson) {
        super(path, Developer.class, gson);
    }
}
