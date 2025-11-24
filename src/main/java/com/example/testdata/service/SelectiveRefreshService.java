package com.example.testdata.service;

import com.example.testdata.config.EnvironmentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SelectiveRefreshService {
    private static final Logger log = LoggerFactory.getLogger(SelectiveRefreshService.class);

    private final SybaseQueryService sybase;
    private final MongoStorageService storage;

    public SelectiveRefreshService(SybaseQueryService sybase, MongoStorageService storage) {
        this.sybase = sybase;
        this.storage = storage;
    }

    public void refreshOne(String env, Long memberId) {
        try {
            EnvironmentContext.set(env);
            var doc = sybase.fetchSingleRecord(memberId);
            storage.upsertEnvironmentDoc(env, doc);
        } catch (Exception ex) {
            log.error("Refresh failed env={} memberId={} : {}", env, memberId, ex.getMessage(), ex);
        } finally {
            EnvironmentContext.clear();
        }
    }
}
