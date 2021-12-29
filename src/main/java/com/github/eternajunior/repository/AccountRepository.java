package com.github.eternajunior.repository;

import com.github.eternajunior.core.AbstractRepository;
import com.github.eternajunior.model.Account;
import com.github.eternajunior.model.Developer;
import com.google.gson.Gson;

public class AccountRepository extends AbstractRepository<Account> {

    public AccountRepository(String path, Gson gson) {
        super(path, Account.class, gson);
    }
}