package com.github.eternajunior.repository;

import com.github.eternajunior.core.CrudRepository;
import com.github.eternajunior.model.Developer;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DeveloperRepository implements CrudRepository<Developer, Long> {
    {
        try {
            if (!(new File("files\\developers.txt").exists())) {
                new File("files\\developers.txt").createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static String FILE_DEVELOPERS = "files\\developers.txt";
    private Gson gson = new Gson();

    @Override
    public <S extends Developer> S save(S saveDeveloper) {
        Map<Long, Developer> developerMap = getAllDevelopers().stream()
                .collect(Collectors.toMap(Developer::getId, developer -> developer));
        List<Developer> developersList = getAllDevelopers();
        if (saveDeveloper.getId() == null) {
            saveDeveloper.setId(developersList.stream()
                    .max(Comparator.comparing(Developer::getId))
                    .get()
                    .getId() + 1L);
            developerMap.put(saveDeveloper.getId(), saveDeveloper);
            developersList.add(saveDeveloper);
        } else {
            developerMap.put(saveDeveloper.getId(), saveDeveloper);
        }
        List<Developer> resultList = new ArrayList<Developer>(developerMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return saveDeveloper;
    }

    @Override
    public <S extends Developer> Iterable<S> saveAll(Iterable<S> developers) {
        List<Developer> developersSaveList = new ArrayList<>();
        for (Developer developer : developers) {
            developersSaveList.add(developer);
        }
        Map<Long, Developer> developerMap = developersSaveList.stream()
                .collect(Collectors.toMap(Developer::getId, developer -> developer));
        List<Developer> developersList = getAllDevelopers();
        for (Developer developer : developersSaveList) {
            if (developer.getId() == null) {
                developer.setId(developersList.stream()
                        .max(Comparator.comparing(Developer::getId))
                        .get()
                        .getId() + 1L);
                developerMap.put(developer.getId(), developer);
                developersList.add(developer);
            } else {
                developerMap.put(developer.getId(), developer);
            }
        }
        List<Developer> resultList = new ArrayList<Developer>(developerMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return developers;
    }

    @Override
    public Optional<Developer> findById(Long id) {
        List<Developer> developerList = getAllDevelopers();
        return developerList.stream().filter(developer -> developer.getId() == id).findFirst();
    }

    @Override
    public boolean existById(Long id) {
        List<Developer> developerList = getAllDevelopers();
        return developerList.stream().anyMatch(developer -> developer.getId() == id);
    }

    @Override
    public Iterable<Developer> findAll() {
        return getAllDevelopers();
    }

    @Override
    public Iterable<Developer> findAllById(Iterable<Long> developers) {
        List<Developer> developerList = new ArrayList<>();
        for (Long id : developers) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_DEVELOPERS))) {
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    Developer developer = gson.fromJson(str, Developer.class);
                    if (developer.getId() == id) {
                        developerList.add(developer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return developerList;
    }

    @Override
    public long count() {
        return getAllDevelopers().size();
    }

    @Override
    public void deleteById(Long id) {
        List<Developer> allDevelopers = getAllDevelopers();
        allDevelopers.removeIf(developer -> Objects.equals(developer.getId(), id));
        printCollectionInFile(allDevelopers);
    }

    @Override
    public void delete(Developer developer) {
        deleteById(developer.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends Developer> developers) {
        List<Developer> developersList = new ArrayList<>();
        List<Developer> deleteDeveloperList = new ArrayList<>();
        developers.forEach(deleteDeveloperList::add);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_DEVELOPERS))) {
            while (bufferedReader.ready()) {
                String str = bufferedReader.readLine();
                Developer developer = gson.fromJson(str, Developer.class);
                developersList.add(developer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        developersList.removeAll(deleteDeveloperList);
        printCollectionInFile(developersList);
    }

    @Override
    public void deleteAll() {
        File file = new File(FILE_DEVELOPERS);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Developer> getAllDevelopers() {
        List<Developer> developers = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_DEVELOPERS))) {
            developers = reader.lines()
                    .map(s -> gson.fromJson(s, Developer.class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return developers;
    }

    private void printCollectionInFile(List<Developer> developers) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_DEVELOPERS))) {
            for (Developer developer : developers) {
                bufferedWriter.write(gson.toJson(developer) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
