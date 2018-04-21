package com.game.websocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WebSocketInterceptor implements HandshakeInterceptor {

	/**
     * 握手之后
     */
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		// TODO Auto-generated method stub
		System.out.println("after handshake...");
	}
	
	/**
     * 握手之前，若返回false，则不建立链接
     */
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler arg2,
			Map<String, Object> arg3) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("before handshake...");
		if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
            HttpSession session = req.getSession();
            if (session != null){
            	System.out.println(session.toString());
            	//System.out.println(session.getAttributeNames());
            	String userId = (String) session.getAttribute("userId");
            	System.out.println(userId);
            	if (userId != null)
            		arg3.put("USER_ID", userId);
            }
        }
		return true;
	}
//拦截器主要是用于用户登录标识（userId）的记录，便于后面获取指定用户的会话标识并向指定用户发送消息，	
	//我们将HttpSession中我们登录后存储的对象放到WebSocketSession中,以此实现定向发送消息。
	

}
