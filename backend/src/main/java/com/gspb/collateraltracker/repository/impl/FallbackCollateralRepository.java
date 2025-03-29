package com.gspb.collateraltracker.repository.impl;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.repository.CollateralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Primary
public class FallbackCollateralRepository implements CollateralRepository {
    private static final Logger logger = LoggerFactory.getLogger(FallbackCollateralRepository.class);
    private static final ConcurrentMap<String, Collateral> collateralStore = new ConcurrentHashMap<>();
    
    @Autowired(required = false)
    private CollateralRepository mongoRepository;
    
    static {
        addSampleCollateral("Sample Collateral 1", "Description for sample collateral 1", "Type A");
        addSampleCollateral("Test Collateral 2", "Description for test collateral 2", "Type B");
        addSampleCollateral("Demo Collateral 3", "Description for demo collateral 3", "Type C");
        addSampleCollateral("Example Collateral 4", "Description for example collateral 4", "Type A");
        addSampleCollateral("Collateral Item 5", "Description for collateral item 5", "Type B");
    }
    
    private static void addSampleCollateral(String name, String description, String type) {
        Collateral collateral = new Collateral();
        collateral.setId(UUID.randomUUID().toString());
        collateral.setName(name);
        collateral.setDescription(description);
        collateral.setType(type);
        collateral.setCreatedAt(LocalDateTime.now());
        collateral.setUpdatedAt(LocalDateTime.now());
        collateralStore.put(collateral.getId(), collateral);
    }

    private boolean useMongoRepository() {
        return mongoRepository != null;
    }

    @Override
    public List<Collateral> findByNameContainingIgnoreCase(String name) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findByNameContainingIgnoreCase(name);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerCaseName = name.toLowerCase();
        return collateralStore.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerCaseName))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Collateral> S save(S entity) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.save(entity);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        collateralStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends Collateral> List<S> saveAll(Iterable<S> entities) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.saveAll(entities);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add(save(entity)));
        return result;
    }

    @Override
    public Optional<Collateral> findById(String s) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findById(s);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return Optional.ofNullable(collateralStore.get(s));
    }

    @Override
    public boolean existsById(String s) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.existsById(s);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return collateralStore.containsKey(s);
    }

    @Override
    public List<Collateral> findAll() {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll();
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return new ArrayList<>(collateralStore.values());
    }

    @Override
    public Iterable<Collateral> findAllById(Iterable<String> strings) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAllById(strings);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        List<Collateral> result = new ArrayList<>();
        strings.forEach(id -> {
            if (collateralStore.containsKey(id)) {
                result.add(collateralStore.get(id));
            }
        });
        return result;
    }

    @Override
    public long count() {
        try {
            if (useMongoRepository()) {
                return mongoRepository.count();
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return collateralStore.size();
    }

    @Override
    public void deleteById(String s) {
        try {
            if (useMongoRepository()) {
                mongoRepository.deleteById(s);
                return;
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        collateralStore.remove(s);
    }

    @Override
    public void delete(Collateral entity) {
        try {
            if (useMongoRepository()) {
                mongoRepository.delete(entity);
                return;
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        if (entity.getId() != null) {
            collateralStore.remove(entity.getId());
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        try {
            if (useMongoRepository()) {
                mongoRepository.deleteAllById(strings);
                return;
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        strings.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Collateral> entities) {
        try {
            if (useMongoRepository()) {
                mongoRepository.deleteAll(entities);
                return;
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        try {
            if (useMongoRepository()) {
                mongoRepository.deleteAll();
                return;
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        collateralStore.clear();
    }

    @Override
    public List<Collateral> findAll(Sort sort) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll(sort);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return findAll();
    }

    @Override
    public Page<Collateral> findAll(Pageable pageable) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll(pageable);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Pagination not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> S insert(S entity) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.insert(entity);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return save(entity);
    }

    @Override
    public <S extends Collateral> List<S> insert(Iterable<S> entities) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.insert(entities);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        return saveAll(entities);
    }

    @Override
    public <S extends Collateral> Optional<S> findOne(Example<S> example) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findOne(example);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> List<S> findAll(Example<S> example) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll(example);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> List<S> findAll(Example<S> example, Sort sort) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll(example, sort);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> Page<S> findAll(Example<S> example, Pageable pageable) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findAll(example, pageable);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> long count(Example<S> example) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.count(example);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral> boolean exists(Example<S> example) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.exists(example);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }

    @Override
    public <S extends Collateral, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        try {
            if (useMongoRepository()) {
                return mongoRepository.findBy(example, queryFunction);
            }
        } catch (Exception e) {
            logger.error("Error using MongoDB repository, falling back to in-memory implementation", e);
        }
        
        throw new UnsupportedOperationException("Example queries not supported in in-memory repository");
    }
}
