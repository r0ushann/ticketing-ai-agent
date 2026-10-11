package com.roushan.ticketing_ai_agent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roushan.ticketing_ai_agent.entity.TicketQueryEntity;


@Repository
public interface TicketQueryRepo extends JpaRepository<TicketQueryEntity, Long> {

    List<TicketQueryEntity> findByCityIgnoreCaseAndGenreIgnoreCase(String city, String genre);
    List<TicketQueryEntity> findByCityIgnoreCase(String city);
}
