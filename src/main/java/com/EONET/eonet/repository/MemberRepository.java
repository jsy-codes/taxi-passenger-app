package com.EONET.eonet.repository;
import com.EONET.eonet.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor



public class MemberRepository  {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByUsername(String  id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findById(String id) {
        return em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
    }
    public Optional<Member> findOptionalById(String id) {
        return em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst(); // Optional<Member> 반환
    }

}
