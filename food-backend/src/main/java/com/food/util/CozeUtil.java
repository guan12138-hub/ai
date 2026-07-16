package com.food.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.food.config.AiConfig;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class CozeUtil {

    private final AiConfig aiConfig;
    private final OkHttpClient client;

    public CozeUtil(AiConfig aiConfig) {
        this.aiConfig = aiConfig;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // === Coze Bot API ===
    public String chatWithCoze(String userMessage, String sessionId) {
        JSONObject body = new JSONObject();
        body.put("bot_id", aiConfig.getCoze().getBotId());
        body.put("user", sessionId != null ? sessionId : "default_user");
        body.put("query", userMessage);
        body.put("stream", false);

        Request request = new Request.Builder()
                .url(aiConfig.getCoze().getApiUrl())
                .addHeader("Authorization", "Bearer " + aiConfig.getCoze().getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body.toJSONString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject result = JSON.parseObject(response.body().string());
                if (result.containsKey("messages")) {
                    JSONArray msgs = result.getJSONArray("messages");
                    for (int i = 0; i < msgs.size(); i++) {
                        JSONObject msg = msgs.getJSONObject(i);
                        if ("assistant".equals(msg.getString("role"))) {
                            return msg.getString("content");
                        }
                    }
                }
                return result.toJSONString();
            }
            return "Coze API error: " + response.code();
        } catch (IOException e) {
            return "Coze API exception: " + e.getMessage();
        }
    }

    // === DeepSeek / OpenAI 兼容 LLM API ===
    public String chatWithLLM(String systemPrompt, String userPrompt) {
        JSONObject body = new JSONObject();
        body.put("model", "deepseek-chat");

        JSONArray messages = new JSONArray();
        JSONObject sysMsg = new JSONObject();
        sysMsg.put("role", "system");
        sysMsg.put("content", systemPrompt);
        messages.add(sysMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userPrompt);
        messages.add(userMsg);

        body.put("messages", messages);
        body.put("temperature", 0.7);
        body.put("max_tokens", 4096);
        body.put("stream", false);

        Request request = new Request.Builder()
                .url(aiConfig.getDeepseek().getApiUrl())
                .addHeader("Authorization", "Bearer " + aiConfig.getDeepseek().getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body.toJSONString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String respBody = response.body().string();
                JSONObject result = JSON.parseObject(respBody);
                JSONArray choices = result.getJSONArray("choices");
                if (choices != null && !choices.isEmpty()) {
                    return choices.getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                }
                return result.toJSONString();
            }
            String errBody = response.body() != null ? response.body().string() : "null";
            return "LLM API error: " + response.code() + " - " + errBody;
        } catch (IOException e) {
            return "LLM API exception: " + e.getMessage();
        }
    }

    // === 文生图 API (智谱 CogView) ===
    public String generateImage(String prompt) {
        JSONObject body = new JSONObject();
        body.put("model", "cogview-3-flash");
        body.put("prompt", prompt);

        Request request = new Request.Builder()
                .url(aiConfig.getVolc().getImageApiUrl())
                .addHeader("Authorization", "Bearer " + aiConfig.getVolc().getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body.toJSONString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject result = JSON.parseObject(response.body().string());
                if (result.containsKey("data")) {
                    return result.getJSONArray("data").getJSONObject(0).getString("url");
                }
                return null;
            }
            String errBody = response.body() != null ? response.body().string() : "null";
            System.err.println("Image API error: " + response.code() + " - " + errBody);
            return null;
        } catch (IOException e) {
            System.err.println("Image API exception: " + e.getMessage());
            return null;
        }
    }
}
