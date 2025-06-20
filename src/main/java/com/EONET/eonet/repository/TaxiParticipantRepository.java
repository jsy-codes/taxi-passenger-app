package com.EONET.eonet.repository;
import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiParticipant;
import com.EONET.eonet.domain.TaxiPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TaxiParticipantRepository extends JpaRepository<TaxiParticipant, Long> {

    int countByPost(TaxiPost post);
    boolean existsByPostAndMember(TaxiPost post, Member member);
}

