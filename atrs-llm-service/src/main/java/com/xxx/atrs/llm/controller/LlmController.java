package com.xxx.atrs.llm.controller;

import com.xxx.atrs.common.config.CommonResponse;
import com.xxx.atrs.llm.dto.ChatMessageDTO;
import com.xxx.atrs.llm.service.LlmChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class LlmController {

    private final LlmChatService llmChatService;

    @PostMapping("/chat")
    public CommonResponse<Map<String, String>> chat(@RequestBody ChatMessageDTO dto) {
        String reply = llmChatService.chatSync(dto.getMessage(), dto.getSessionId());
        return CommonResponse.success(Map.of("reply", reply, "sessionId", dto.getSessionId()));
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatMessageDTO dto) {
        return llmChatService.chatStream(dto.getMessage(), dto.getSessionId());
    }

    @DeleteMapping("/session/{sessionId}")
    public CommonResponse<String> clearSession(@PathVariable String sessionId) {
        llmChatService.clearSession(sessionId);
        return CommonResponse.success("会话已清除");
    }
}
