package com.game.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.game.bean.UserBean;
import com.google.gson.Gson;

//该对象提供了客户端连接,关闭,错误,发送等方法,重写这几个方法即可实现自定义业务逻辑
//session可以用来标注客户端id，相对于我们的httpsession，这样，如果我们想做一个精准推送和全部推送，我们可以这么做
// 1.获取对方信息
// 2.获取对方牌面
// 3.获取自己的所有牌  共有3种消息

// 1.请求连接时发送的自己的信息  共有2种信息
// 2.出牌时发送的信息
public class MyHandler extends TextWebSocketHandler{
	private static final Map<String,WebSocketSession> users = new HashMap<String,WebSocketSession>();
	private static final List<UserBean> waitUsers1 = new ArrayList<>();
	//private Map<Integer,List<UserBean>> waitUsers4 = new HashMap<>();
	private static String USER_ID = "userId";
	private static String GAME_TYPE = "type";
	private static String USER_BEAN = "userBean";
	private UserBean me,rival;
	 // 初次连接成功执行
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("成功建立scocket连接");
	    String userId = (String) session.getAttributes().get(USER_ID);
		users.put(userId,session);     //将连接用户保
	    if(userId!=null){
	      session.sendMessage(new TextMessage("我们已经成功建立socket通信了"));
	    }
	    System.out.println("开始寻找对手...");
	    searchRival(session);
	    System.out.println("对手寻找完成，发送client...");
	    Gson gson = new Gson();
	    session.sendMessage(new TextMessage(gson.toJson(rival)));   // 将对手信息发给对方 ,
	    WebSocketSession rivalSession = getRival(userId);
	    rivalSession.sendMessage(new TextMessage(gson.toJson(me)));  
	   /* socket.send(JSON.stringify({
	        'msg': 'payload'
	    }));*/
	}
	public void searchRival(WebSocketSession session){
		int gameType = (int) session.getAttributes().get(GAME_TYPE);
		me = (UserBean) session.getAttributes().get(USER_BEAN);
		int Min = 1000;
		switch(gameType){
		case 1:
			if (waitUsers1.isEmpty()){
				waitUsers1.add(me);
			} else {
				for(UserBean user:waitUsers1){
					int gap = Math.abs(user.getUserLv()-me.getUserLv());
					if(gap < Min){
						rival = user;
						Min = gap;
					}
					if (Min<3)
						break;
					waitUsers1.remove(rival);// 删除wait池中双方信息
				}
				// 找寻到对手，将信息移进战斗
			}
			break;
		case 4:
			break;
		default:
			break;
				
		}
	}
    // 获取到对方的session;
	public WebSocketSession getRival(String id){
		String rivalId;
		if(id == String.valueOf(me.getUserId()))
			rivalId = String.valueOf(rival.getUserId());
		else
			rivalId = String.valueOf(me.getUserId());
		return users.get(rivalId);
	}
	/*
	 * 重写父类方法handlerTextMessage(),每当客户端发送信息过来，都会由这个函数接收并处理。
	 */
	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // ...
		System.out.println(message.getPayload());
    }
	//这里可以将登录用户保存到对象中，然后可以实现点对点消息发送、发送所有用户等功能。
	
	/**
     * 给所有在线用户发送消息
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users.values()) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
    	WebSocketSession user = users.get(userId);
        try {
            if (user.isOpen()) {
                user.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	 @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("连接已关闭。");
		users.remove(session);
		
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("连接出错。");
		if(session.isOpen()){
			session.close();
	    }
	    users.remove(session);
	}
	
	
}
