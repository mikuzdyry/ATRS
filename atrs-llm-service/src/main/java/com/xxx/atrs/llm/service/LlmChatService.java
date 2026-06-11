package com.xxx.atrs.llm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.*;

@Slf4j
@Service
public class LlmChatService {

    @Value("${atrs.llm.api-key:sk-placeholder}")
    private String apiKey;

    @Value("${atrs.llm.api-url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${atrs.llm.model:deepseek-chat}")
    private String model;

    // 简单的内存对话历史
    private final Map<String, List<Map<String, String>>> sessionHistory = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<Map<String, String>>> eldest) {
            return size() > 1000; // 最多保留 1000 个会话
        }
    };

    private static final String SYSTEM_PROMPT = """
            你是 ATRS 航空票务预订系统的智能客服助手。你的职责包括：
            1. 帮助用户查询航班信息（出发城市、到达城市、日期）
            2. 解答关于订票流程、支付、退改签的问题
            3. 介绍舱位等级（经济舱 ECONOMY、商务舱 BUSINESS、头等舱 FIRST）
            4. 解答常见问题（行李额度、值机时间、证件要求等）
            5. 提供航班状态查询指引

            回答要求：
            - 专业、礼貌、简洁
            - 遇到不确定的问题，引导用户联系人工客服
            - 涉及具体订单操作，提示用户登录后查看
            - 使用中文回答""";

    public Flux<String> chatStream(String message, String sessionId) {
        // 获取或创建会话历史
        List<Map<String, String>> history = sessionHistory.computeIfAbsent(sessionId, k -> {
            List<Map<String, String>> h = new ArrayList<>();
            h.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
            return h;
        });

        // 添加用户消息
        history.add(Map.of("role", "user", "content", message));

        // 构建请求体
        List<Map<String, String>> messages = new ArrayList<>(history);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("stream", true);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        StringBuilder fullResponse = new StringBuilder();

        return client.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .map(chunk -> {
                    // 解析 SSE 格式数据
                    if (chunk.startsWith("data: ")) {
                        String data = chunk.substring(6).trim();
                        if ("[DONE]".equals(data)) {
                            // 流结束，保存完整回复到历史
                            history.add(Map.of("role", "assistant", "content", fullResponse.toString()));
                            return "[DONE]";
                        }
                        try {
                            // 简单 JSON 解析提取 content
                            int idx = data.indexOf("\"content\":\"");
                            if (idx > 0) {
                                int start = idx + 11;
                                int end = data.indexOf("\"", start);
                                if (end > start) {
                                    // 处理转义字符
                                    String content = data.substring(start, end)
                                            .replace("\\n", "\n")
                                            .replace("\\\"", "\"")
                                            .replace("\\\\", "\\");
                                    fullResponse.append(content);
                                    return content;
                                }
                            }
                        } catch (Exception e) {
                            log.debug("解析 SSE 数据失败: {}", data);
                        }
                    }
                    return "";
                })
                .filter(s -> !s.isEmpty());
    }

    public String chatSync(String message, String sessionId) {
        List<Map<String, String>> history = sessionHistory.computeIfAbsent(sessionId, k -> {
            List<Map<String, String>> h = new ArrayList<>();
            h.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
            return h;
        });
        history.add(Map.of("role", "user", "content", message));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", new ArrayList<>(history));
        requestBody.put("stream", false);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        WebClient client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

        Map<String, Object> response = client.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String reply = "";
        if (response != null && response.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> msg = (Map<String, Object>) choices.get(0).get("message");
                reply = (String) msg.get("content");
            }
        }

        history.add(Map.of("role", "assistant", "content", reply));
        return reply;
    }

    public void clearSession(String sessionId) {
        sessionHistory.remove(sessionId);
    }
}
