package com.EONET.eonet.repository;

// 3. TaxiPostRepository (src/main/java/com/example/project/repository/TaxiPostRepository.java)
package com.example.project.repository;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiPost;
import com.example.project.domain.TaxiPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TaxiPostRepository extends JpaRepository<TaxiPost, Long> {
}
