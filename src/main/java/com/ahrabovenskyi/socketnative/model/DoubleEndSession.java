package com.ahrabovenskyi.socketnative.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
public class DoubleEndSession {

    WebSocketSession frontMiddleSession;

    WebSocketSession middleEndSession;
}
