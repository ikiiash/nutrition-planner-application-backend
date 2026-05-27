package sk.posam.fsa.nutritionplanner.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import sk.posam.fsa.nutritionplanner.domain.ai.AiAutofillResult;
import sk.posam.fsa.nutritionplanner.domain.ai.AiMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.AiProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiAdapter implements AiProvider {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o-mini";

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiAdapter(@Value("${openai.api-key}") String apiKey) {
        this.restClient = RestClient.builder()
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String chat(String systemPrompt, List<AiMessage> messages) {
        List<Map<String, String>> openAiMessages = new ArrayList<>();
        openAiMessages.add(Map.of("role", "system", "content", systemPrompt));
        for (AiMessage msg : messages) {
            openAiMessages.add(Map.of("role", msg.role(), "content", msg.content()));
        }

        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", openAiMessages,
                "temperature", 0.7,
                "max_tokens", 1500
        );

        ChatCompletionResponse response = restClient.post()
                .uri(API_URL)
                .body(requestBody)
                .retrieve()
                .body(ChatCompletionResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new RuntimeException("Empty response from OpenAI");
        }
        return response.choices().get(0).message().content();
    }

    @Override
    public AiAutofillResult autofill(String productName) {
        String prompt = """
                Return the average nutritional values per 100g for the food product: "%s"

                Respond ONLY with a valid JSON object using exactly these keys (all values must be numbers):
                {
                  "calories": 0,
                  "protein": 0,
                  "fat": 0,
                  "carbohydrates": 0,
                  "sodiumMg": 0,
                  "potassiumMg": 0,
                  "magnesiumMg": 0,
                  "ironMg": 0,
                  "calciumMg": 0,
                  "zincMg": 0,
                  "vitaminAMcg": 0,
                  "vitaminCMg": 0,
                  "vitaminDMcg": 0,
                  "vitaminEMg": 0,
                  "vitaminKMcg": 0,
                  "vitaminB1Mg": 0,
                  "vitaminB2Mg": 0,
                  "vitaminB6Mg": 0,
                  "vitaminB9Mcg": 0,
                  "vitaminB12Mcg": 0
                }
                No explanation, no markdown, only raw JSON.
                """.formatted(productName);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "user", "content", prompt)
        );

        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", messages,
                "temperature", 0.1,
                "max_tokens", 400,
                "response_format", Map.of("type", "json_object")
        );

        ChatCompletionResponse response = restClient.post()
                .uri(API_URL)
                .body(requestBody)
                .retrieve()
                .body(ChatCompletionResponse.class);

        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            throw new RuntimeException("Empty response from OpenAI");
        }

        String json = response.choices().get(0).message().content();
        try {
            return objectMapper.readValue(json, AiAutofillResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse autofill response: " + json, e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChatCompletionResponse(List<Choice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Choice(Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Message(String content) {}
}
