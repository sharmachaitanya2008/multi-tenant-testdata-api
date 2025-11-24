package com.example.testdata.service;

import com.example.testdata.dto.EnvironmentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AllocatorService {
    private static final Logger log = LoggerFactory.getLogger(AllocatorService.class);

    private final MongoTemplate mongo;
    private final int leaseMinutes;

    public AllocatorService(MongoTemplate mongo, org.springframework.core.env.Environment env) {
        this.mongo = mongo;
        this.leaseMinutes = Integer.parseInt(env.getProperty("app.lease.minutes", "15"));
    }

    private String coll(String env) {
        return "test_data_" + env.toLowerCase();
    }

    public EnvironmentData allocate(String env, String category, String ownerId) {
        String c = coll(env);
        Query q = Query.query(Criteria.where("queryA.category").is(category).and("lease.claimedBy").is(null));
        Update u = new Update()
                .set("lease.claimedBy", ownerId)
                .set("lease.claimedAt", Instant.now())
                .set("lease.expiresAt", Instant.now().plus(leaseMinutes, ChronoUnit.MINUTES));
        FindAndModifyOptions opts = FindAndModifyOptions.options().returnNew(true);
        try {
            return mongo.findAndModify(q, u, opts, EnvironmentData.class, c);
        } catch (Exception ex) {
            log.error("Allocation error env={} category={} : {}", env, category, ex.getMessage(), ex);
            return null;
        }
    }

    public boolean release(String env, Long memberId, String ownerId) {
        String c = coll(env);
        Query q = Query.query(Criteria.where("_id").is(memberId).and("lease.claimedBy").is(ownerId));
        Update u = new Update().set("lease.claimedBy", null).set("lease.claimedAt", null).set("lease.expiresAt", null);
        var res = mongo.updateFirst(q, u, c);
        return res.getModifiedCount() > 0;
    }

    public boolean renewLease(String env, Long memberId, String ownerId) {
        String c = coll(env);
        Query q = Query.query(Criteria.where("_id").is(memberId).and("lease.claimedBy").is(ownerId));
        Update u = new Update().set("lease.expiresAt", Instant.now().plus(leaseMinutes, ChronoUnit.MINUTES));
        var res = mongo.updateFirst(q, u, c);
        return res.getModifiedCount() > 0;
    }

    public EnvironmentData getById(String env, Long memberId) {
        return mongo.findById(memberId, EnvironmentData.class, coll(env));
    }

    public void reclaimExpired(String env) {
        String c = coll(env);
        Query q = Query.query(Criteria.where("lease.expiresAt").lt(Instant.now()).and("lease.claimedBy").ne(null));
        Update u = new Update().set("lease.claimedBy", null).set("lease.claimedAt", null).set("lease.expiresAt", null);
        var res = mongo.updateMulti(q, u, c);
        log.info("Reclaimed {} docs in env={}", res.getModifiedCount(), env);
    }
}
