package com.EONET.eonet.repository;

// 3. TaxiPostRepository (src/main/java/com/example/project/repository/TaxiPostRepository.java)

import com.EONET.eonet.domain.TaxiPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiPostRepository extends JpaRepository<TaxiPost, Long> {
}
