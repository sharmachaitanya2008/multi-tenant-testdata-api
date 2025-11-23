package com.example.testdata.repository;

import com.example.testdata.dto.QueryAResult;
import com.example.testdata.dto.QueryBResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Repository
public class QueryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public QueryRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private String loadSql(String name){
        try {
            var r = new ClassPathResource("queries/" + name);
            return new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e){
            throw new RuntimeException("Failed to load SQL: " + name, e);
        }
    }

    public List<String> loadIdsToSync(){
        String sql = loadSql("ids_to_sync.sql");
        return jdbc.queryForList(sql, Map.of(), String.class);
    }

    public QueryAResult queryA(String id){
        String sql = loadSql("queryA.sql");
        return jdbc.queryForObject(sql, Map.of("id", id), new BeanPropertyRowMapper<>(QueryAResult.class));
    }

    public QueryBResult queryB(String id){
        String sql = loadSql("queryB.sql");
        return jdbc.queryForObject(sql, Map.of("id", id), new BeanPropertyRowMapper<>(QueryBResult.class));
    }
}
