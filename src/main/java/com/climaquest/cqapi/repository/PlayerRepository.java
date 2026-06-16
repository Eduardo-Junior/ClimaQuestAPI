package com.climaquest.cqapi.repository;

import com.climaquest.cqapi.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    //Encontra jogador por codinome
    //SELECT * FROM players WHERE codename = ?
    Optional<Player> findByCodename(String codename);

    //Lista os jogadores ordenados por XP (o limite é aplicado no service)
    //SELECT * FROM players ORDER BY xp DESC
    List<Player> findAllByOrderByXpDesc();

    boolean existsByCodename(String codename);
}
