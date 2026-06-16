package com.climaquest.cqapi.repository;

import com.climaquest.cqapi.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, UUID> {

    List<Achievement> findByPlayerId(UUID playerId);

    boolean existsByPlayerIdAndAchievementId(UUID playerId, String achievementId);
}
