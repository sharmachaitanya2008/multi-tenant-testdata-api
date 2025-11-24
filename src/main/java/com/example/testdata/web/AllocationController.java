package com.example.testdata.web;

import com.example.testdata.dto.EnvironmentData;
import com.example.testdata.service.AllocatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alloc")
public class AllocationController {

    private final AllocatorService allocator;

    public AllocationController(AllocatorService allocator) {
        this.allocator = allocator;
    }

    @PostMapping("/allocate")
    public ResponseEntity<?> allocate(@RequestParam String env,
                                      @RequestParam String category,
                                      @RequestParam String ownerId) {
        EnvironmentData doc = allocator.allocate(env, category, ownerId);
        if (doc == null) return ResponseEntity.status(404).body("No available data");
        return ResponseEntity.ok(doc);
    }

    @PostMapping("/release")
    public ResponseEntity<?> release(@RequestParam String env,
                                     @RequestParam Long memberId,
                                     @RequestParam String ownerId) {
        boolean ok = allocator.release(env, memberId, ownerId);
        return ok ? ResponseEntity.ok("released") : ResponseEntity.status(409).body("release failed or not owner");
    }

    @GetMapping("/{env}/{memberId}")
    public ResponseEntity<?> getById(@PathVariable String env, @PathVariable Long memberId) {
        var doc = allocator.getById(env, memberId);
        return doc != null ? ResponseEntity.ok(doc) : ResponseEntity.status(404).body("not found");
    }
}
