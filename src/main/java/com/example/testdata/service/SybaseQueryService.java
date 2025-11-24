package com.example.testdata.service;

import com.example.testdata.dto.EnvironmentData;
import com.example.testdata.dto.QueryAResult;
import com.example.testdata.dto.QueryBResult;
import com.example.testdata.queries.QueryA;
import com.example.testdata.queries.QueryB;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SybaseQueryService {

    private final NamedParameterJdbcTemplate jdbc;

    public SybaseQueryService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public <T> List<T> fetchList(String sql, Map<String, ?> params, Class<T> type) {
        return jdbc.query(sql, params, new BeanPropertyRowMapper<>(type));
    }

    public <T> T fetchOne(String sql, Map<String, ?> params, Class<T> type) {
        return jdbc.queryForObject(sql, params, new BeanPropertyRowMapper<>(type));
    }

    // Bulk fetch
    public List<QueryAResult> fetchA_Bulk() {
        return fetchList(QueryA.BASE, Map.of(), QueryAResult.class);
    }

    public List<QueryBResult> fetchB_Bulk() {
        return fetchList(QueryB.BASE, Map.of(), QueryBResult.class);
    }

    // Single ID fetch
    public QueryAResult fetchA_ByMemberId(Long memberId) {
        return fetchOne(QueryA.byMemberId(), Map.of("memberId", memberId), QueryAResult.class);
    }

    public List<QueryBResult> fetchB_ByMemberId(Long memberId) {
        return fetchList(QueryB.byMemberId(), Map.of("memberId", memberId), QueryBResult.class);
    }

    // Bulk assemble -> Map<memberId, EnvironmentData>
    public Map<Long, EnvironmentData> fetchBulkAsMap() {
        List<QueryAResult> aList = fetchA_Bulk();
        List<QueryBResult> bList = fetchB_Bulk();

        Map<Long, QueryAResult> aByKey = aList.stream().collect(Collectors.toMap(QueryAResult::getMemberId, x -> x));
        Map<Long, List<QueryBResult>> bByKey = bList.stream().collect(Collectors.groupingBy(QueryBResult::getMemberId));

        Set<Long> keys = new HashSet<>();
        keys.addAll(aByKey.keySet());
        keys.addAll(bByKey.keySet());

        Map<Long, EnvironmentData> out = new HashMap<>();
        for (Long memberId : keys) {
            EnvironmentData d = new EnvironmentData();
            d.setMemberId(memberId);
            d.setQueryA(aByKey.get(memberId));
            d.setQueryB(bByKey.getOrDefault(memberId, List.of()));
            d.setSyncedAt(java.time.Instant.now());
            d.setLease(new EnvironmentData.Lease());
            out.put(memberId, d);
        }
        return out;
    }

    // Single record assemble for selective refresh
    public EnvironmentData fetchSingleRecord(Long memberId) {
        EnvironmentData d = new EnvironmentData();
        d.setMemberId(memberId);
        d.setQueryA(fetchA_ByMemberId(memberId));
        d.setQueryB(fetchB_ByMemberId(memberId));
        d.setSyncedAt(java.time.Instant.now());
        d.setLease(new EnvironmentData.Lease());
        return d;
    }
}
