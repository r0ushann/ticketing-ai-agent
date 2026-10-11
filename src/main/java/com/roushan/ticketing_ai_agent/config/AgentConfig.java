package com.roushan.ticketing_ai_agent.config;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.roushan.ticketing_ai_agent.service.TicketingService;

@Configuration
public class AgentConfig {

    public interface ConciergeAgent {
        @SystemMessage("""
            You are a helpful event booking concierge. 
            Use tools to search for events, check availability, and book tickets.
            Be conversational, polite, and format pricing nicely.
            """)
        String chat(String userMessage);
    }

    @Bean
    public ChatLanguageModel groqChatModel(@Value("${groq.api.key}") String apiKey) {
        return OpenAiChatModel.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .apiKey(apiKey)
                .modelName("openai/gpt-oss-120b")
                .temperature(0.0) // Keep temperature 0 for reliable tool calling
                .build();
    }

    @Bean
    public ConciergeAgent conciergeAgent(ChatLanguageModel chatModel, TicketingService tools) {
        return AiServices.builder(ConciergeAgent.class)
                .chatLanguageModel(chatModel)
                .tools(tools) // Injects the Spring Bean as agent tools
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}