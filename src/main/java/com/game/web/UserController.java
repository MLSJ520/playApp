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
		System.out.println("查询该手机是否注册："+phone);
		UserBean userBean = userService.getUserByPhone(phone);
		if(userBean==null){
			System.out.println("手机号没有注册");
			return false;
		}
		else
		{
			return true;
		}
	}
	@RequestMapping(value="/getUserByPwd.action")
	public @ResponseBody UserBean GetUserByPwd(String phone,String pwd){
		System.out.println("登录获取信息："+phone);
		return userService.getUserByPwd(phone, pwd);
	}
	@RequestMapping(value="/regist.action")
	public @ResponseBody void Regist(@RequestBody String json){
		System.out.println("注册");
		Gson gson = new Gson();
		UserBean userBean = gson.fromJson(json,UserBean.class);
		String phone = userBean.getUserPhone();
		String name = (String) phone.subSequence(phone.length()-4, phone.length());
		System.out.println(name);    //名字 = phone后四位+随机生成的5位字母，id在100000-999999之间 
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
		System.out.println("修改用户名");
		userService.modifyName(userID, name);
	}
	@RequestMapping(value="/modifyPhone.action")
	public @ResponseBody void ModifyPhone(int userID,String phone){
		System.out.println("修改手机号: "+userID+" "+phone);
		userService.modifyPhone(userID, phone);
	}
	@RequestMapping(value="/updateUserExp.action")
	public @ResponseBody void UpdateUserExp(int userID,int exp,int lv){
		System.out.println("用户经验");
		if (lv>0){
			System.out.println("用户升级");
			userService.UpdateLv(userID, lv);
		}
		userService.UpdateExp(userID, exp);
	}
}
