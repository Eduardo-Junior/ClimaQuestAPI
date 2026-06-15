package com.climaquest.cqapi.repository;

import com.climaquest.cqapi.entity.SkillUnlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillUnlockRepository extends JpaRepository<SkillUnlock, UUID> {

    List<SkillUnlock> findByPlayerId(UUID playerId);

    boolean existsByPlayerIdAndSkillId(UUID playerId, String skillId);
}
