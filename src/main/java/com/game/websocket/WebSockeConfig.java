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

//需要我们把处理器和拦截器注册到spring websocket中
    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
	
	 @Bean  
    public HandshakeInterceptor myInterceptors() {  
        return new WebSocketInterceptor();  
    } 

	public void registerWebSocketHandlers(WebSocketHandlerRegistry arg0) {
		////注册处理拦截器,拦截url为socketServer的请求
		 arg0.addHandler(myHandler(), "/Server/websocket.ws")
		 	.addInterceptors(myInterceptors()).setAllowedOrigins("*");
	
		//访问WebSocket的地址,setAllowedOrigins方法用来设置来自那些域名的请求可访问，默认为localhost
	}

}
