package com.game.dao;

import org.springframework.stereotype.Repository;

import com.game.bean.UserBean;

@Repository("userDao")
public interface UserDao {
   //��¼����ѯ�û���Ϣ�������û���Ϣ
	public UserBean GetUserByPhone(String phone);
	public UserBean GetUserByPwd(String phone,String pwd);
	public void Regist(UserBean userBean);
	public void ModifyName(int userID,String name);
	public void ModifyPhone(int userID,String phone);
	public void UpdateExp(int userID,int exp);
	public void UpdateLv(int userID,int lv);
	public int GetUserNum();
	
}
