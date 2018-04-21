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
     * ����֮��
     */
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		// TODO Auto-generated method stub
		System.out.println("after handshake...");
	}
	
	/**
     * ����֮ǰ��������false���򲻽�������
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
//��������Ҫ�������û���¼��ʶ��userId���ļ�¼�����ں����ȡָ���û��ĻỰ��ʶ����ָ���û�������Ϣ��	
	//���ǽ�HttpSession�����ǵ�¼��洢�Ķ���ŵ�WebSocketSession��,�Դ�ʵ�ֶ�������Ϣ��
	

}
