package com.github.eternajunior.core;

import com.github.eternajunior.model.Developer;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T extends HasId<Long>> implements CrudRepository<T, Long> {
    private final Class<T> tClass;
    private final String file_path;
    private final Gson gson;

    public AbstractRepository(String path, Class<T> tClass, Gson gson) {
        file_path = path;
        this.tClass = tClass;
        this.gson = gson;
        try {
            if (!(new File(file_path).exists())) {
                new File(file_path).createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <S extends T> S save(S entity) {
        Map<Long, T> entityMap =getAllEntityFromFile().stream()
                .collect(Collectors.toMap(HasId::getId, entitySave -> entitySave));
        Long maxId = entityMap.keySet().stream().max(Comparable::compareTo).orElse(0L);
        if (entity.getId() == null) {
            entity.setId(maxId + 1L);
        }
        entityMap.put(entity.getId(), entity);
        List<T> resultList = new ArrayList<>(entityMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        Map<Long, T> entityMap = getAllEntityFromFile().stream()
                .collect(Collectors.toMap(HasId::getId, entity -> entity));
        Long maxId = entityMap.keySet().stream().max(Comparable::compareTo).orElse(0L);
        for (T entity : entities) {
            if (entity.getId() == null) {
                maxId++;
                entity.setId(maxId);

            }
            entityMap.put(entity.getId(), entity);
        }
        List<T> resultList = new ArrayList<>(entityMap.values());
        resultList.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        printCollectionInFile(resultList);
        return entities;
    }

    @Override
    public Optional<T> findById(Long id) {
        return getAllEntityFromFile().stream().filter(entity -> Objects.equals(entity.getId(), id)).findFirst();
    }

    @Override
    public boolean existById(Long id) {
        return getAllEntityFromFile().stream().anyMatch(entity -> Objects.equals(entity.getId(), id));
    }

    @Override
    public Iterable<T> findAll() {
        return getAllEntityFromFile();
    }

    @Override
    public Iterable<T> findAllById(Iterable<Long> ids) {
        List<T> entityList = new ArrayList<>();
        for (Long entityId : ids) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path))) {
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    T entity = gson.fromJson(str, tClass);
                    if (Objects.equals(entity.getId(), entityId)) {
                        entityList.add(entity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entityList;
    }

    @Override
    public long count() {
        return getAllEntityFromFile().size();
    }

    @Override
    public void deleteById(Long id) {
        List<T> allEntities = getAllEntityFromFile();
        allEntities.removeIf(entity -> Objects.equals(entity.getId(), id));
        printCollectionInFile(allEntities);
    }

    @Override
    public void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        List<T> entityList = getAllEntityFromFile();
        List<T> deleteEntityList = new ArrayList<>();
        entities.forEach(deleteEntityList::add);
        entityList.removeAll(deleteEntityList);
        printCollectionInFile(entityList);
    }

    @Override
    public void deleteAll() {
        File file = new File(file_path);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<T> getAllEntityFromFile() {
        List<T> entity = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file_path))) {
            entity = reader.lines()
                    .map(s -> gson.fromJson(s, tClass))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }


    private void printCollectionInFile(List<T> entities) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file_path))) {
            for (T entity : entities) {
                bufferedWriter.write(gson.toJson(entity) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
