package com.game.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSockeConfig implements WebSocketConfigurer  {

//��Ҫ���ǰѴ�������������ע�ᵽspring websocket��
    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
	
	 @Bean  
    public HandshakeInterceptor myInterceptors() {  
        return new WebSocketInterceptor();  
    } 

	public void registerWebSocketHandlers(WebSocketHandlerRegistry arg0) {
		////ע�ᴦ��������,����urlΪsocketServer������
		 arg0.addHandler(myHandler(), "/Server/websocket.ws")
		 	.addInterceptors(myInterceptors()).setAllowedOrigins("*");
	
		//����WebSocket�ĵ�ַ,setAllowedOrigins������������������Щ����������ɷ��ʣ�Ĭ��Ϊlocalhost
	}

}
