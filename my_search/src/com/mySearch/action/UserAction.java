package com.mySearch.action;

import com.mySearch.domain.User;
import com.mySearch.service.UserService;

public class UserAction extends BaseAction{

	private static final long serialVersionUID = 7672561492034859828L;
	
	private User user;
	
	private UserService userService;
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public String execute() {
		try {
			this.userService.addObject(this.user);
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
	
}
