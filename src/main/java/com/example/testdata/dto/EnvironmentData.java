package com.example.testdata.dto;

import java.time.Instant;

public class EnvironmentData {
    private String id;
    private QueryAResult queryA;
    private QueryBResult queryB;
    private Instant syncedAt;
    private Lease lease;

    public static class Lease {
        private String claimedBy;
        private Instant claimedAt;
        private Instant expiresAt;
        public Lease(){}
        public String getClaimedBy(){return claimedBy;} public void setClaimedBy(String s){this.claimedBy=s;}
        public Instant getClaimedAt(){return claimedAt;} public void setClaimedAt(Instant i){this.claimedAt=i;}
        public Instant getExpiresAt(){return expiresAt;} public void setExpiresAt(Instant i){this.expiresAt=i;}
    }

    public EnvironmentData(){}
    public String getId(){return id;} public void setId(String id){this.id=id;}
    public QueryAResult getQueryA(){return queryA;} public void setQueryA(QueryAResult q){this.queryA=q;}
    public QueryBResult getQueryB(){return queryB;} public void setQueryB(QueryBResult q){this.queryB=q;}
    public Instant getSyncedAt(){return syncedAt;} public void setSyncedAt(Instant i){this.syncedAt=i;}
    public Lease getLease(){return lease;} public void setLease(Lease l){this.lease=l;}
}
