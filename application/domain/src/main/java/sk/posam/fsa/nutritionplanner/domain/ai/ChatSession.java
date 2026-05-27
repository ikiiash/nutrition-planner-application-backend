package sk.posam.fsa.nutritionplanner.domain.ai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatSession {

    private Long id;
    private String ownerUserId;
    private String title;
    private LocalDateTime createdAt;
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatSession() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ChatMessage> getMessages() { return messages; }
    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }
}
