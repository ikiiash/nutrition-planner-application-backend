package sk.posam.fsa.nutritionplanner.domain.ai;

import java.util.List;

public interface AiProvider {
    String chat(String systemPrompt, List<AiMessage> messages);
    AiAutofillResult autofill(String productName);
}
