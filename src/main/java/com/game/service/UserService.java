package com.game.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.game.bean.UserBean;
import com.game.dao.UserDao;

@Service(value="userService")
public class UserService {

	@Resource(name="userDao")
	private UserDao userDao;
	
	public UserBean getUserByPhone(String phone){
		return userDao.GetUserByPhone(phone);
	}
	public UserBean getUserByPwd(String phone,String pwd){
		return userDao.GetUserByPwd(phone,pwd);
	}
	public void Regist(UserBean userBean){
		userDao.Regist(userBean);
	}
	public void modifyName(int userID,String name){
		userDao.ModifyName(userID, name);
	}
	public void modifyPhone(int userID,String phone){
		userDao.ModifyPhone(userID, phone);
	}
	public void UpdateExp(int userID,int exp){
		userDao.UpdateExp(userID,exp);
	}
	public void UpdateLv(int userID,int lv){
		userDao.UpdateLv(userID, lv);
	}
	
	public int getUserNum(){
		return userDao.GetUserNum();
	}
}
