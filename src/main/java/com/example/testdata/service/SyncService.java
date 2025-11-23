package com.example.testdata.service;

import com.example.testdata.config.EnvironmentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SyncService {
    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private final Environment springEnv;
    private final com.example.testdata.repository.QueryRepository queryRepository;
    private final AssemblerService assembler;
    private final MongoStorageService storage;

    public SyncService(Environment springEnv, com.example.testdata.repository.QueryRepository repository, AssemblerService assembler, MongoStorageService storage){
        this.springEnv = springEnv;
        this.queryRepository = repository;
        this.assembler = assembler;
        this.storage = storage;
    }

    @Scheduled(cron = "${app.sync.cron}")
    public void syncAll() {
        List<Map<String,String>> envs = Binder.get(springEnv).bind("app.environments", List.class).orElse(null);
        if (envs == null) return;
        for (Map<String,String> cfg : envs) {
            String envKey = cfg.get("key").trim();
            try {
                log.info("Sync start for env={}", envKey);
                EnvironmentContext.set(envKey);
                syncForEnv(envKey);
            } catch (Exception ex) {
                log.error("Sync failure env={} : {}", envKey, ex.getMessage(), ex);
            } finally {
                EnvironmentContext.clear();
            }
        }
    }

    public void syncForEnv(String envKey) {
        storage.ensureCollectionAndIndexes(envKey);
        List<String> ids = queryRepository.loadIdsToSync();
        log.info("Env={} - {} ids to sync", envKey, ids.size());
        for (String id : ids) {
            try {
                var doc = assembler.assembleForId(envKey, id);
                storage.upsertEnvironmentDoc(envKey, doc);
            } catch (Exception e) {
                log.error("Failed to assemble/upsert id={} env={} : {}", id, envKey, e.getMessage(), e);
            }
        }
    }
}
