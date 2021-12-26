package com.github.eternajunior.repository;

import com.github.eternajunior.core.CrudRepository;
import com.github.eternajunior.model.Developer;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
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
        long maxId = developerMap.keySet().stream().max((o1, o2) -> (int) (o1 - o2)).orElse(0L);
        if (saveDeveloper.getId() == null) {
            saveDeveloper.setId(maxId + 1L);
        }
        developerMap.put(saveDeveloper.getId(), saveDeveloper);
        List<Developer> resultList = new ArrayList<>(developerMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return saveDeveloper;
    }

    @Override
    public <S extends Developer> Iterable<S> saveAll(Iterable<S> developers) {
        Map<Long, Developer> developerMap = getAllDevelopers().stream()
                .collect(Collectors.toMap(Developer::getId, developer -> developer));
        long maxId = developerMap.keySet().stream().max((o1, o2) -> (int) (o1 - o2)).orElse(0L);
        for (Developer developer : developers) {
            if (developer.getId() == null) {
                developer.setId(maxId + 1L);
                maxId++;
            }
            developerMap.put(developer.getId(), developer);
        }
        List<Developer> resultList = new ArrayList<>(developerMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return developers;
    }

    @Override
    public Optional<Developer> findById(Long id) {
        return getAllDevelopers().stream().filter(developer -> Objects.equals(developer.getId(), id)).findFirst();
    }

    @Override
    public boolean existById(Long id) {
        return getAllDevelopers().stream().anyMatch(developer -> Objects.equals(developer.getId(), id));
    }

    @Override
    public Iterable<Developer> findAll() {
        return getAllDevelopers();
    }

    @Override
    public Iterable<Developer> findAllById(Iterable<Long> id) {
        List<Developer> developerList = new ArrayList<>();
        for (Long developerId : id) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_DEVELOPERS))) {
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    Developer developer = gson.fromJson(str, Developer.class);
                    if (Objects.equals(developer.getId(), developerId)) {
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
        List<Developer> developersList = getAllDevelopers();
        List<Developer> deleteDeveloperList = new ArrayList<>();
        developers.forEach(deleteDeveloperList::add);
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
