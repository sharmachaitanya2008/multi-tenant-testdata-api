package com.example.testdata.service;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.example.testdata.dto.QueryAResult;
import com.example.testdata.dto.QueryBResult;
import com.example.testdata.dto.EnvironmentData;
import com.example.testdata.queries.QueryA;
import com.example.testdata.queries.QueryB;

@Service
public class SybaseQueryService {
    private final NamedParameterJdbcTemplate jdbc;
    public SybaseQueryService(NamedParameterJdbcTemplate jdbc){ this.jdbc = jdbc; }
    public <T> List<T> fetchList(String sql, Map<String,Object> params, Class<T> type){ return jdbc.query(sql, params, new BeanPropertyRowMapper<>(type)); }
    public <T> T fetchOne(String sql, Map<String,Object> params, Class<T> type){ return jdbc.queryForObject(sql, params, new BeanPropertyRowMapper<>(type)); }
    public List<QueryAResult> fetchA_Bulk(){ return fetchList(QueryA.BASE, Map.of(), QueryAResult.class); }
    public List<QueryBResult> fetchB_Bulk(){ return fetchList(QueryB.BASE, Map.of(), QueryBResult.class); }
    public QueryAResult fetchA_ById(String id){ return fetchOne(QueryA.byId(), Map.of("id", id), QueryAResult.class); }
    public List<QueryBResult> fetchB_ById(String id){ return fetchList(QueryB.byMasterId(), Map.of("id", id), QueryBResult.class); }
    public Map<String, EnvironmentData> fetchBulkAsMap(){
        List<QueryAResult> a = fetchA_Bulk();
        List<QueryBResult> b = fetchB_Bulk();
        Map<String, QueryAResult> aById = a.stream().collect(Collectors.toMap(QueryAResult::getId, x -> x));
        Map<String, List<QueryBResult>> bById = b.stream().collect(Collectors.groupingBy(QueryBResult::getId));
        Set<String> ids = new HashSet<>(); ids.addAll(aById.keySet()); ids.addAll(bById.keySet());
        Map<String, EnvironmentData> out = new HashMap<>();
        for (String id : ids){
            EnvironmentData d = new EnvironmentData();
            d.setId(id);
            d.setQueryA(aById.get(id));
            d.setQueryB(bById.getOrDefault(id, List.of()));
            d.setSyncedAt(java.time.Instant.now());
            out.put(id, d);
        }
        return out;
    }
    public EnvironmentData fetchSingleRecord(String id){
        EnvironmentData d = new EnvironmentData();
        d.setId(id);
        d.setQueryA(fetchA_ById(id));
        d.setQueryB(fetchB_ById(id));
        d.setSyncedAt(java.time.Instant.now());
        return d;
    }
}
