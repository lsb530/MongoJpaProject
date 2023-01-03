package com.example.mongojpapractice.util;

import com.example.mongojpalogic.common.config.var.BusinessLogicConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageUtil {

    private static BusinessLogicConfig businessLogicConfig;

    private MessageUtil(BusinessLogicConfig businessLogicConfig) {
        MessageUtil.businessLogicConfig = businessLogicConfig;
    }

    /**
     * slack에 게시 **각 채널별로 다른 token을 사용하기 때문에 현재 slack/ops 채널에만 게시가 가능하다
     *
     * @param message 메세지 내용
     */
    public static void sendSlackMessage(String message) {
        String slackApiUrl = businessLogicConfig.getSlackBot();

        if (slackApiUrl != null && (!slackApiUrl.equals(""))) {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, String> messages = new HashMap<>();
            messages.put("text", message);

            try {
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest httpRequest =
                    HttpRequest.newBuilder()
                        .uri(URI.create(slackApiUrl))
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(messages)))
                        .build();
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
