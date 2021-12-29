package com.github.eternajunior;

import com.github.eternajunior.model.Account;
import com.github.eternajunior.model.AccountStatus;
import com.github.eternajunior.model.Developer;
import com.github.eternajunior.model.Skill;
import com.github.eternajunior.repository.DeveloperRepository;
import com.github.eternajunior.repository.SkillRepository;
import com.google.gson.Gson;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello! This is CRUD console app.");
        System.out.println("__________________________________");
        System.out.println("Actions:");
        System.out.println("create");
        System.out.println("read");
        System.out.println("update");
        System.out.println("delete");
        System.out.println("__________________________________");
        System.out.println("For exit write \"exit\"");

        Developer developer = new Developer();
        Account account = new Account();
        account.setAccountStatus(AccountStatus.BANNED);
        account.setBalance(1231);
        account.setName("sanya");
        developer.setAccount(account);
        //developer.setId(25L);
        Skill skill = new Skill();
        skill.setSkill("eblan321");
        Set<Skill> skillSet = new HashSet<>();
        skillSet.add(skill);
        developer.setSkills(skillSet);
        Gson gson = new Gson();
        String json = gson.toJson(developer);
        DeveloperRepository developerRepository = new DeveloperRepository("files\\developers.txt", gson);
        Developer developer2 = new Developer();
        developer2.setId(26L);
        developer2.setAccount(account);
        developer2.setSkills(skillSet);
        List<Developer> list = new ArrayList<>();
        list.add(developer);
        list.add(developer2);
//        developerRepository.saveAll(iterable);
        List<Long> listID = new ArrayList<>();
        listID.add(1L);
        listID.add(26L);
        Iterable<Long> iterableId = listID;
        //developerRepository.deleteAll();
        List<Long> longList = new ArrayList<>();
        longList.add(12L);
        longList.add(13L);
        longList.add(14L);
//        developerRepository.save();


        SkillRepository skillRepository = new SkillRepository("files\\skills.txt", gson);
        skillRepository.saveAll(List.of(skill));
        //developerRepository1.deleteById(1L);
        //System.out.println(developerRepository.count());
        //developerRepository.save(developer);
        //Optional<Developer> developerOptional = developerRepository.findById(1L);
        //developerRepository.deleteById(2L);
        // developerRepository.deleteAll();
        //System.out.println("ID: " + developerOptional.get().getId());
        // System.out.println("name: " + developerOptional.get().getAccount().getName());
        //System.out.println(developerRepository.existById(1l));
    }

}
