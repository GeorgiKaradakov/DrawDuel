package com.drawduel.infrastructure.persistence.jpa.repositories;

import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameJpaRepository extends JpaRepository<JpaGameEntity, UUID> {}
