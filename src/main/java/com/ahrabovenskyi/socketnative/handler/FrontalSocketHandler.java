package com.ahrabovenskyi.socketnative.handler;

import com.ahrabovenskyi.socketnative.model.DoubleEndSession;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FrontalSocketHandler extends TextWebSocketHandler {

    List<DoubleEndSession> sessions = new ArrayList<>();

    WebSocketClient webSocketClient = new StandardWebSocketClient();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {

        for(DoubleEndSession doubleEndSession : sessions) {
            doubleEndSession.getMiddleEndSession().sendMessage(message);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession frontToMiddleSession) throws Exception {
        WebSocketSession middleToEndSession = webSocketClient.doHandshake(new TextWebSocketHandler() {
            @Override
            public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
                log.info("received message MAYBY FROM DATA - " + message.getPayload());
                for(DoubleEndSession doubleEndSession : sessions) {
                    doubleEndSession.getFrontMiddleSession().sendMessage(message);
                }
            }

            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                log.info("established connection - " + session);
            }
        }, new WebSocketHttpHeaders(), URI.create("ws://localhost:8081/name")).get();

        DoubleEndSession doubleEndSession = new DoubleEndSession(frontToMiddleSession, middleToEndSession);
        sessions.add(doubleEndSession);
    }
}
