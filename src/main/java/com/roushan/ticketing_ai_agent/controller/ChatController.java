package com.roushan.ticketing_ai_agent.controller;

import org.springframework.web.bind.annotation.*;

import com.roushan.ticketing_ai_agent.config.AgentConfig;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final AgentConfig.ConciergeAgent agent;

    public ChatController(AgentConfig.ConciergeAgent agent) {
        this.agent = agent;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String answer = agent.chat(request.message());
        return new ChatResponse(answer);
    }
}

record ChatRequest(String message) {}
record ChatResponse(String response) {}