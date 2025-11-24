package com.example.testdata.web;

import com.example.testdata.service.BulkSyncService;
import com.example.testdata.service.SelectiveRefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class SyncController {

    private final BulkSyncService bulk;
    private final SelectiveRefreshService refresh;

    public SyncController(BulkSyncService bulk, SelectiveRefreshService refresh) {
        this.bulk = bulk;
        this.refresh = refresh;
    }

    @PostMapping("/{env}")
    public ResponseEntity<String> syncEnv(@PathVariable String env) {
        bulk.syncForEnv(env);
        return ResponseEntity.ok("Bulk sync started for " + env);
    }

    @PostMapping("/{env}/id/{memberId}")
    public ResponseEntity<String> refreshId(@PathVariable String env, @PathVariable Long memberId) {
        refresh.refreshOne(env, memberId);
        return ResponseEntity.ok("Refreshed memberId=" + memberId);
    }
}
