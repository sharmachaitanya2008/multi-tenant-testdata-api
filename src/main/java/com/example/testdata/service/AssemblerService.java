package com.example.testdata.service;

import com.example.testdata.dto.*;
import com.example.testdata.repository.QueryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AssemblerService {

    private final QueryRepository repo;

    public AssemblerService(QueryRepository repo){
        this.repo = repo;
    }

    public EnvironmentData assembleForId(String env, String id){
        QueryAResult a = repo.queryA(id);
        QueryBResult b = repo.queryB(id);

        EnvironmentData doc = new EnvironmentData();
        doc.setId(id);
        doc.setQueryA(a);
        doc.setQueryB(b);
        doc.setSyncedAt(Instant.now());
        doc.setLease(new EnvironmentData.Lease());
        return doc;
    }
}
