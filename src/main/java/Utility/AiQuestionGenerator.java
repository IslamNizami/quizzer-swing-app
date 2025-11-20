package Utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.errors.RateLimitException;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import model.Question;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AiQuestionGenerator {

    private OpenAIClient client;
    private final Gson gson = new Gson();

    public AiQuestionGenerator() {
        try {
            client = OpenAIOkHttpClient.fromEnv();
        } catch (Exception e) {
            client = null;
            System.err.println("⚠️ OpenAI client not initialized. Using fallback mode.");
        }
    }

    public List<Question> generateQuestions(String topic, int count) {
        if (client == null) {
            return mockQuestions(topic);
        }

        try {
            String json = generateQuizJSON(topic, count);
            Type listType = new TypeToken<List<Question>>() {}.getType();
            List<Question> questions = gson.fromJson(json, listType);
            if (questions == null || questions.isEmpty()) return mockQuestions(topic);
            return questions;
        } catch (RateLimitException e) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ OpenAI API quota exceeded.\nUsing local fallback questions.",
                    "AI Service Error", JOptionPane.WARNING_MESSAGE);
            return mockQuestions(topic);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "❌ AI generation failed: " + e.getMessage() + "\nUsing fallback mode.",
                    "AI Error", JOptionPane.ERROR_MESSAGE);
            return mockQuestions(topic);
        }
    }

    private String generateQuizJSON(String topic, int count) {
        String prompt = """
            Generate %d multiple-choice quiz questions about "%s".
            Each question MUST have exactly 4 options (A, B, C, D)
            and a field "correct" giving the 0-based index of the correct option.
            Respond STRICTLY in this JSON format:
            [
              {
                "question": "...",
                "options": ["...", "...", "...", "..."],
                "correct": 0
              }
            ]
            """.formatted(count, topic);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .addSystemMessage("You are a helpful quiz generator.")
                .addUserMessage(prompt)
                .temperature(0.7)
                .maxTokens(800)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        if (completion.choices().isEmpty() ||
                completion.choices().get(0).message().content().isEmpty()) {
            throw new RuntimeException("Empty response from OpenAI");
        }

        return completion.choices().get(0).message().content().get();
    }

    // Local fallback mock questions
    private List<Question> mockQuestions(String topic) {
        List<Question> list = new ArrayList<>();
        list.add(new Question("What is 2 + 2?", List.of("3", "4", "5", "6"), 1));
        list.add(new Question("What is 5 × 3?", List.of("15", "10", "5", "20"), 0));
        list.add(new Question("What is 9 − 3?", List.of("3", "9", "6", "5"), 2));
        list.add(new Question("What is 10 ÷ 2?", List.of("2", "4", "5", "8"), 2));
        JOptionPane.showMessageDialog(null,
                "💡 Using local mock AI questions for topic: " + topic,
                "AI Offline Mode", JOptionPane.INFORMATION_MESSAGE);
        return list;
    }
}
