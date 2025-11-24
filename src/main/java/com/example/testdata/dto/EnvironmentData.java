package com.example.testdata.dto;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.List;

public class EnvironmentData {

    @Id
    private Long memberId;      // becomes Mongo _id

    private QueryAResult queryA;
    private List<QueryBResult> queryB;
    private Instant syncedAt;
    private Lease lease;

    public static class Lease {
        private String claimedBy;
        private Instant claimedAt;
        private Instant expiresAt;

        public Lease() {
        }

        public String getClaimedBy() {
            return claimedBy;
        }

        public void setClaimedBy(String claimedBy) {
            this.claimedBy = claimedBy;
        }

        public Instant getClaimedAt() {
            return claimedAt;
        }

        public void setClaimedAt(Instant claimedAt) {
            this.claimedAt = claimedAt;
        }

        public Instant getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
        }
    }

    public EnvironmentData() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public QueryAResult getQueryA() {
        return queryA;
    }

    public void setQueryA(QueryAResult queryA) {
        this.queryA = queryA;
    }

    public List<QueryBResult> getQueryB() {
        return queryB;
    }

    public void setQueryB(List<QueryBResult> queryB) {
        this.queryB = queryB;
    }

    public Instant getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(Instant syncedAt) {
        this.syncedAt = syncedAt;
    }

    public Lease getLease() {
        return lease;
    }

    public void setLease(Lease lease) {
        this.lease = lease;
    }
}
