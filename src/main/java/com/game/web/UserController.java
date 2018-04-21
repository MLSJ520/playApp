package com.game.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.game.service.UserService;
import com.google.gson.Gson;
import com.game.bean.UserBean;

@Controller
@RequestMapping(value="/userController")
public class UserController {
	@Resource(name="userService")
	private UserService userService;
	private static final String STR = "abcdefghigklmnopqrstuvwxyz";
	
	@RequestMapping(value="/getUserByPhone.action")
	public @ResponseBody boolean GetUserByPhone(String phone){
		System.out.println("��ѯ���ֻ��Ƿ�ע�᣺"+phone);
		UserBean userBean = userService.getUserByPhone(phone);
		if(userBean==null){
			System.out.println("�ֻ���û��ע��");
			return false;
		}
		else
		{
			return true;
		}
	}
	@RequestMapping(value="/getUserByPwd.action")
	public @ResponseBody UserBean GetUserByPwd(String phone,String pwd){
		System.out.println("��¼��ȡ��Ϣ��"+phone);
		return userService.getUserByPwd(phone, pwd);
	}
	@RequestMapping(value="/regist.action")
	public @ResponseBody void Regist(@RequestBody String json){
		System.out.println("ע��");
		Gson gson = new Gson();
		UserBean userBean = gson.fromJson(json,UserBean.class);
		String phone = userBean.getUserPhone();
		String name = (String) phone.subSequence(phone.length()-4, phone.length());
		System.out.println(name);    //���� = phone����λ+������ɵ�5λ��ĸ��id��100000-999999֮�� 
		for(int i = 0;i<5;i++){
			name += STR.charAt((int) (Math.random()*26));
		}
		//userBean.setUserPic(String.valueOf(phone.charAt(phone.length())));
		int ID = userService.getUserNum();
		userBean.setUserId(ID+1);
		userBean.setUserName(name);
		userService.Regist(userBean);
	}
	@RequestMapping(value="/modifyName.action")
	public @ResponseBody void ModifyName(int userID,String name){
		System.out.println("�޸��û���");
		userService.modifyName(userID, name);
	}
	@RequestMapping(value="/modifyPhone.action")
	public @ResponseBody void ModifyPhone(int userID,String phone){
		System.out.println("�޸��ֻ���: "+userID+" "+phone);
		userService.modifyPhone(userID, phone);
	}
	@RequestMapping(value="/updateUserExp.action")
	public @ResponseBody void UpdateUserExp(int userID,int exp,int lv){
		System.out.println("�û�����");
		if (lv>0){
			System.out.println("�û�����");
			userService.UpdateLv(userID, lv);
		}
		userService.UpdateExp(userID, exp);
	}
}
