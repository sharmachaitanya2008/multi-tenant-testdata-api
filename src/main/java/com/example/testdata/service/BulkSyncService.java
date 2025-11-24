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
public class BulkSyncService {
    private static final Logger log = LoggerFactory.getLogger(BulkSyncService.class);

    private final Environment springEnv;
    private final SybaseQueryService sybase;
    private final MongoStorageService storage;

    public BulkSyncService(Environment springEnv, SybaseQueryService sybase, MongoStorageService storage) {
        this.springEnv = springEnv;
        this.sybase = sybase;
        this.storage = storage;
    }

    @Scheduled(cron = "${app.sync.cron}")
    public void syncAll() {
        List<Map<String, String>> envs = Binder.get(springEnv).bind("app.environments", List.class).orElse(null);
        if (envs == null) return;

        for (Map<String, String> cfg : envs) {
            String envKey = cfg.get("key").trim();
            try {
                log.info("Bulk sync start for env={}", envKey);
                EnvironmentContext.set(envKey);
                syncForEnv(envKey);
            } catch (Exception ex) {
                log.error("Bulk sync failed for env={} : {}", envKey, ex.getMessage(), ex);
            } finally {
                EnvironmentContext.clear();
            }
        }
    }

    public void syncForEnv(String envKey) {
        storage.ensureCollectionAndIndexes(envKey);
        var map = sybase.fetchBulkAsMap();
        log.info("Env={} - {} docs to upsert", envKey, map.size());
        for (var e : map.entrySet()) {
            try {
                storage.upsertEnvironmentDoc(envKey, e.getValue());
            } catch (Exception ex) {
                log.error("Failed upsert memberId={} env={} : {}", e.getKey(), envKey, ex.getMessage(), ex);
            }
        }
    }
}
