package cn.cheirmin.bookstore.user.service;

import cn.cheirmin.bookstore.user.dao.UserDao;
import cn.cheirmin.bookstore.user.domain.User;

/*
 * User的业务层
 * @author Cheirmin
*/

public class UserService {
	private UserDao userDao = new UserDao();
	
	//注册功能
	public void regist(User form) throws UserException{
		
		//校验用户名
		User user = userDao.findByUsername(form.getUsername());
		if(user != null)  throw new UserException("用户名已被注册");
		
		//校验邮箱
		user = userDao.findByEmail(form.getEmail());
		if(user != null)  throw new UserException("邮箱已被注册");
		
		//插入用户到数据库中
		userDao.add(form);
	}
	
	//激活功能
	public void active(String code) throws UserException {
		//1、使用code查询数据库，得到user
		User user = userDao.findByCode(code);
		//2 如果user不存在，说明激活码错误
		if(user == null ) throw new UserException("激活码无效");
		//3 校验用户状态是否为未激活状态，2次激活抛异常
		if(user.isState()) throw new UserException("已经激活过了，不需要再激活了");
		//4 修改用户状态
		userDao.updateState(user.getUid(), true);
	}
	
	//登陆功能
	public User login(User form) throws UserException {
		/**
		 * 1 使用username查询，得到User
		 * 2 如果form为null，抛出异常（用户不存在）
		 * 3 比较form的user密码是否一直，不同抛出异常（密码错误）
		 * 4 查看用户激活态，若为false，抛出异常（尚未激活）
		 * 5 返回user
		 */
		
		User user = userDao.findByUsername(form.getUsername());
		if( user == null) throw new UserException("该用户不存在");
		if(!user.getPassword().equals(form.getPassword()))
			throw new UserException("密码错误");
		if(!user.isState()) throw new UserException("该用户尚未激活");
		
		return user;
	}
}
