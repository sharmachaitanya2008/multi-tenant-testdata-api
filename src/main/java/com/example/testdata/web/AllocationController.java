package com.example.testdata.web;

import com.example.testdata.config.EnvironmentContext;
import com.example.testdata.dto.EnvironmentData;
import com.example.testdata.service.AllocatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alloc")
public class AllocationController {

    private final AllocatorService allocator;

    public AllocationController(AllocatorService allocator){ this.allocator = allocator; }

    @PostMapping("/allocate")
    public ResponseEntity<?> allocate(@RequestParam("env") String env,
                                      @RequestParam("category") String category,
                                      @RequestParam("ownerId") String ownerId) {
        try {
            EnvironmentContext.set(env);
            EnvironmentData doc = allocator.allocate(env, category, ownerId);
            if (doc == null) return ResponseEntity.status(404).body("No available data");
            return ResponseEntity.ok(doc);
        } finally {
            EnvironmentContext.clear();
        }
    }

    @PostMapping("/release")
    public ResponseEntity<?> release(@RequestParam("env") String env,
                                     @RequestParam("id") String id,
                                     @RequestParam("ownerId") String ownerId) {
        try {
            EnvironmentContext.set(env);
            boolean ok = allocator.release(env, id, ownerId);
            return ok ? ResponseEntity.ok("released") : ResponseEntity.status(409).body("release failed or not owner");
        } finally {
            EnvironmentContext.clear();
        }
    }

    @GetMapping("/{env}/{id}")
    public ResponseEntity<?> getById(@PathVariable String env, @PathVariable String id) {
        try {
            EnvironmentContext.set(env);
            var doc = allocator.getById(env, id);
            return doc != null ? ResponseEntity.ok(doc) : ResponseEntity.status(404).body("not found");
        } finally {
            EnvironmentContext.clear();
        }
    }
}
