package com.example.testdata.web;

import com.example.testdata.config.EnvironmentContext;
import com.example.testdata.service.SyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class SyncController {
    private final SyncService syncService;
    public SyncController(SyncService syncService){ this.syncService = syncService; }

    @PostMapping("/{env}")
    public ResponseEntity<String> syncNow(@PathVariable String env) {
        try {
            EnvironmentContext.set(env);
            syncService.syncForEnv(env);
            return ResponseEntity.ok("Synced " + env);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Sync failed: " + ex.getMessage());
        } finally {
            EnvironmentContext.clear();
        }
    }
}
