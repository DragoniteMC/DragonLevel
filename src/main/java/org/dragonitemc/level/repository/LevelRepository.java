package org.dragonitemc.level.repository;

import org.dragonitemc.level.db.Levels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LevelRepository extends JpaRepository<Levels, UUID> {
}
