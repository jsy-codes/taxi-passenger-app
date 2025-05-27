package com.EONET.eonet.domain;

@PostMapping
public ResponseEntity<TaxiPost> createPost(@RequestBody TaxiPost post) {
        TaxiPost saved = taxiPostRepository.save(post);
        return ResponseEntity.ok(saved);
        }