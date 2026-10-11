package com.roushan.ticketing_ai_agent.service;

import org.springframework.stereotype.Service;

import com.roushan.ticketing_ai_agent.repository.TicketQueryRepo;
import java.util.List;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import com.roushan.ticketing_ai_agent.entity.TicketQueryEntity;

@Service
@RequiredArgsConstructor
public class TicketingService {

    private final TicketQueryRepo eventRepository;

    @Tool("Search for events by city and optionally by genre (e.g., Rock, Comedy).")
    public List<TicketQueryEntity> searchEvents(String city, String genre) {
        if (genre != null && !genre.isEmpty()) {
           return eventRepository.findByCityIgnoreCaseAndGenreIgnoreCase(city, genre);
        }
      return eventRepository.findByCityIgnoreCase(city);
    }

    @Tool("Check if enough seats are available for a specific event ID.")
    public boolean checkAvailability(Long eventId, int requiredSeats) {
        return eventRepository.findById(eventId)
                .map(event -> event.getAvailableSeats() >= requiredSeats)
                .orElse(false);
    }

    @Tool("Book tickets for an event. Returns a booking confirmation string.")
    public String bookTickets(Long eventId, int quantity, String customerName) {
        TicketQueryEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        if (event.getAvailableSeats() >= quantity) {
            event.setAvailableSeats(event.getAvailableSeats() - quantity);
            eventRepository.save(event);
            return "SUCCESS: Booking confirmed for " + customerName + ". ID: TKT-" + Math.random();
        }
        return "FAILED: Not enough seats available.";
    }
}

