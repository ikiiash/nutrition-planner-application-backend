package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.ai.AiAutofillResult;
import sk.posam.fsa.nutritionplanner.domain.ai.AiMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;
import sk.posam.fsa.nutritionplanner.domain.ai.service.AiAssistantFacade;
import sk.posam.fsa.nutritionplanner.rest.api.AiApi;
import sk.posam.fsa.nutritionplanner.rest.dto.AiAutofillRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.AiAutofillResponseDto;
import sk.posam.fsa.nutritionplanner.rest.dto.AiChatRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.AiChatResponseDto;
import sk.posam.fsa.nutritionplanner.rest.dto.ChatMessageItemDto;
import sk.posam.fsa.nutritionplanner.rest.dto.ChatSessionDetailDto;
import sk.posam.fsa.nutritionplanner.rest.dto.ChatSessionSummaryDto;
import sk.posam.fsa.nutritionplanner.rest.dto.SendChatMessageRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.SendChatMessageResponseDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@RestController
public class AiRestController implements AiApi {

    private final AiAssistantFacade aiAssistantFacade;
    private final CurrentUserProvider currentUserProvider;

    public AiRestController(AiAssistantFacade aiAssistantFacade,
                             CurrentUserProvider currentUserProvider) {
        this.aiAssistantFacade = aiAssistantFacade;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<AiChatResponseDto> aiChat(AiChatRequestDto request) {
        List<AiMessage> messages = request.getMessages() == null
                ? Collections.emptyList()
                : request.getMessages().stream()
                .map(m -> new AiMessage(m.getRole(), m.getContent()))
                .toList();

        String content = aiAssistantFacade.chat(currentUserProvider.getUserId(), messages);

        AiChatResponseDto response = new AiChatResponseDto();
        response.setContent(content);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AiAutofillResponseDto> aiAutofill(AiAutofillRequestDto request) {
        AiAutofillResult result = aiAssistantFacade.autofill(request.getProductName());

        AiAutofillResponseDto dto = new AiAutofillResponseDto();
        dto.setCalories(result.getCalories());
        dto.setProtein(result.getProtein());
        dto.setFat(result.getFat());
        dto.setCarbohydrates(result.getCarbohydrates());
        dto.setSodiumMg(result.getSodiumMg());
        dto.setPotassiumMg(result.getPotassiumMg());
        dto.setMagnesiumMg(result.getMagnesiumMg());
        dto.setIronMg(result.getIronMg());
        dto.setCalciumMg(result.getCalciumMg());
        dto.setZincMg(result.getZincMg());
        dto.setVitaminAMcg(result.getVitaminAMcg());
        dto.setVitaminCMg(result.getVitaminCMg());
        dto.setVitaminDMcg(result.getVitaminDMcg());
        dto.setVitaminEMg(result.getVitaminEMg());
        dto.setVitaminKMcg(result.getVitaminKMcg());
        dto.setVitaminB1Mg(result.getVitaminB1Mg());
        dto.setVitaminB2Mg(result.getVitaminB2Mg());
        dto.setVitaminB6Mg(result.getVitaminB6Mg());
        dto.setVitaminB9Mcg(result.getVitaminB9Mcg());
        dto.setVitaminB12Mcg(result.getVitaminB12Mcg());
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<ChatSessionSummaryDto>> listChatSessions() {
        String userId = currentUserProvider.getUserId();
        List<ChatSession> sessions = aiAssistantFacade.listSessions(userId);
        List<ChatSessionSummaryDto> dtos = sessions.stream().map(s -> {
            ChatSessionSummaryDto dto = new ChatSessionSummaryDto();
            dto.setId(s.getId());
            dto.setTitle(s.getTitle());
            if (s.getCreatedAt() != null) {
                dto.setCreatedAt(s.getCreatedAt().atOffset(ZoneOffset.UTC));
            }
            dto.setMessageCount(s.getMessages().size());
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<ChatSessionDetailDto> createChatSession() {
        String userId = currentUserProvider.getUserId();
        ChatSession session = aiAssistantFacade.createSession(userId);
        return ResponseEntity.status(201).body(toDetailDto(session));
    }

    @Override
    public ResponseEntity<ChatSessionDetailDto> getChatSession(Long chatId) {
        String userId = currentUserProvider.getUserId();
        ChatSession session = aiAssistantFacade.getSession(userId, chatId);
        return ResponseEntity.ok(toDetailDto(session));
    }

    @Override
    public ResponseEntity<Void> deleteChatSession(Long chatId) {
        aiAssistantFacade.deleteSession(currentUserProvider.getUserId(), chatId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SendChatMessageResponseDto> sendChatMessage(Long chatId,
                                                                       SendChatMessageRequestDto request) {
        String userId = currentUserProvider.getUserId();
        String aiResponse = aiAssistantFacade.sendMessage(userId, chatId, request.getContent());
        SendChatMessageResponseDto dto = new SendChatMessageResponseDto();
        dto.setContent(aiResponse);
        return ResponseEntity.ok(dto);
    }

    private ChatSessionDetailDto toDetailDto(ChatSession session) {
        ChatSessionDetailDto dto = new ChatSessionDetailDto();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        if (session.getCreatedAt() != null) {
            dto.setCreatedAt(session.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        List<ChatMessageItemDto> msgDtos = session.getMessages().stream().map(m -> {
            ChatMessageItemDto msgDto = new ChatMessageItemDto();
            msgDto.setId(m.getId());
            msgDto.setRole(m.getRole());
            msgDto.setContent(m.getContent());
            if (m.getCreatedAt() != null) {
                msgDto.setCreatedAt(m.getCreatedAt().atOffset(ZoneOffset.UTC));
            }
            return msgDto;
        }).toList();
        dto.setMessages(msgDtos);
        return dto;
    }
}
