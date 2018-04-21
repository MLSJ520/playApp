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

//�ö����ṩ�˿ͻ�������,�ر�,����,���͵ȷ���,��д�⼸����������ʵ���Զ���ҵ���߼�
//session����������ע�ͻ���id����������ǵ�httpsession�������������������һ����׼���ͺ�ȫ�����ͣ����ǿ�����ô��
// 1.��ȡ�Է���Ϣ
// 2.��ȡ�Է�����
// 3.��ȡ�Լ���������  ����3����Ϣ

// 1.��������ʱ���͵��Լ�����Ϣ  ����2����Ϣ
// 2.����ʱ���͵���Ϣ
public class MyHandler extends TextWebSocketHandler{
	private static final Map<String,WebSocketSession> users = new HashMap<String,WebSocketSession>();
	private static final List<UserBean> waitUsers1 = new ArrayList<>();
	//private Map<Integer,List<UserBean>> waitUsers4 = new HashMap<>();
	private static String USER_ID = "userId";
	private static String GAME_TYPE = "type";
	private static String USER_BEAN = "userBean";
	private UserBean me,rival;
	 // �������ӳɹ�ִ��
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("�ɹ�����scocket����");
	    String userId = (String) session.getAttributes().get(USER_ID);
		users.put(userId,session);     //�������û���
	    if(userId!=null){
	      session.sendMessage(new TextMessage("�����Ѿ��ɹ�����socketͨ����"));
	    }
	    System.out.println("��ʼѰ�Ҷ���...");
	    searchRival(session);
	    System.out.println("����Ѱ����ɣ�����client...");
	    Gson gson = new Gson();
	    session.sendMessage(new TextMessage(gson.toJson(rival)));   // ��������Ϣ�����Է� ,
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
					waitUsers1.remove(rival);// ɾ��wait����˫����Ϣ
				}
				// ��Ѱ�����֣�����Ϣ�ƽ�ս��
			}
			break;
		case 4:
			break;
		default:
			break;
				
		}
	}
    // ��ȡ���Է���session;
	public WebSocketSession getRival(String id){
		String rivalId;
		if(id == String.valueOf(me.getUserId()))
			rivalId = String.valueOf(rival.getUserId());
		else
			rivalId = String.valueOf(me.getUserId());
		return users.get(rivalId);
	}
	/*
	 * ��д���෽��handlerTextMessage(),ÿ���ͻ��˷�����Ϣ����������������������ղ�����
	 */
	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // ...
		System.out.println(message.getPayload());
    }
	//������Խ���¼�û����浽�����У�Ȼ�����ʵ�ֵ�Ե���Ϣ���͡����������û��ȹ��ܡ�
	
	/**
     * �����������û�������Ϣ
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
     * ��ĳ���û�������Ϣ
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
		System.out.println("�����ѹرա�");
		users.remove(session);
		
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("���ӳ���");
		if(session.isOpen()){
			session.close();
	    }
	    users.remove(session);
	}
	
	
}
