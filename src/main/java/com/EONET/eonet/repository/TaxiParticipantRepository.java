package com.EONET.eonet.repository;
import com.EONET.eonet.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository

public interface TaxiParticipantRepository extends JpaRepository<TaxiParticipant, Long> {
    int countByPost(TaxiPost post);
    boolean existsByPostAndMember(TaxiPost post, Member member);
}
