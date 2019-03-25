package cn.cheirmin.bookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cheirmin.bookstore.cart.domain.Cart;
import cn.cheirmin.bookstore.user.domain.User;
import cn.cheirmin.bookstore.user.service.UserException;
import cn.cheirmin.bookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

/**
 * User表述层
 * @author Cheirmin
 */

public class UserServlet extends BaseServlet {
	/**
	 * 
	 */
	private UserService userService = new UserService();
	
	//退出功能，干掉session
	public String quit(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException {
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
	
	//登陆功能
	public String login(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException {
		/**
		 * 	1 封装表单数据到form
		 * 2 输入校验（省略）
		 * 3 调用service完成激活
		 * >保存错误信息、form到request，转发到login.jsp
		 * 4 保存用户信息到session中，然后重定向到index.jsp	
		 */
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		try {
			User user = userService.login(form);
			request.getSession().setAttribute("session_user", user);
			//给用户添加一辆购物车，即向session中保存一个Cart对象
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";//r重定向
		} catch (UserException e) {
			// TODO Auto-generated catch block
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:jsps/user/login.jsp";//f转发
		}
		
	}
	
	//激活功能
	public String active(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException{
		//1 获取参数激活码
		//2 调用service方法完成激活
		// >报错异常信息到request域，转发到msg.jsp
		//3 保存成功信息到request域，转发到msg.jsp
		String code = request.getParameter("code");
		try {
			userService.active(code);
			request.setAttribute("msg", "激活成功，请登陆！");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}
	
	//注册功能
	public String regist(HttpServletRequest request,HttpServletResponse response) 
		throws ServletException,IOException {
	/*	
	 * 1 封装表单数据到form对象中
	 * 2 补全：uid、 code
	 * 3 输入校验
	 *  > 保存错误信息、form到request域，转发到registe
	 * 4 调用service方法完成注册
	 *  > 保存错误信息、form到request域，转发到registe
	 * 5 发邮件
	 * 6 保存成功信息转发到msg.jsp
	*/
		//封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		//补全
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		/*
		 *  输入检验
		 *  1 创建一个map，用来封装错误信息，其中key单表字段名称，值为错误信息
		 */
		Map<String, String> errors = new HashMap<String,String>();
		
		/*
		 * 2 获取form中username、password、Email经行检验
		 */
		String username = form.getUsername();
		if(username == null ||username.trim().isEmpty()) { //为空或者空串
			errors.put("username", "用户名不能为空");
		}else if(username.length()<3||username.length()>10 ){
			errors.put("username", "用户名长度必须在3-10之间");
		}
		
		String password = form.getPassword();
		if(password == null ||password.trim().isEmpty()) { //为空或者空串
			errors.put("password", "密码不能为空");
		}else if(password.length()<6||password.length()>10 ){
			errors.put("password", "密码长度必须在6-10之间");
		}
		
		String email = form.getEmail();
		if(email == null ||email.trim().isEmpty()) { //为空或者空串
			errors.put("email", "邮箱不能为空");
		}else if(!email.matches("\\w+@\\w+\\.\\w+")){
			errors.put("email", "邮箱格式错误");
		}
		
		/*
		 * 3 判断是否存在错误信息
		 */
		if(errors.size()>0) {
			//1 报错错误信息
			//2 保存表单数据
			//3 转发到regist.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		/*
		 * 调用service的request方法
		 */
		try {
			userService.regist(form);
			/*
			 * 执行到这，说明userService执行成功，没有抛出异常
			 */

		} catch (UserException e) {
			/*
			 * 1 报错错误信息
			 * 2 保存表单数据
			 * 3 转发到regist.jsp
			*/
			request.setAttribute("msg",e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}

		/*
		 * 发邮件验证
		 * 准备配置文件
		*/
		//获取配置文件内容
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");//获取主机名
		String uname = props.getProperty("uname");//获取用户名
		String pwd = props.getProperty("pwd");//获取密码
		String from = props.getProperty("from");//获取发件人邮箱
		String to = form.getEmail();//获取收件人邮箱
		String subject = props.getProperty("subject");//获取主题
		String content = props.getProperty("content");//获取内容
		content= MessageFormat.format(content, form.getCode());//去除内容中暂未符
		
		Session session = MailUtils.createSession(host, uname, pwd);//得到session
		Mail mail = new Mail(from, to, subject, content);//创建邮件对象
		try {
			System.out.println("准备发送邮件");
			MailUtils.send(session, mail);//发邮件
			System.out.println("邮件已经发送");
		} catch (MessagingException e) {
			System.out.println("邮件发送失败");
			// TODO: handle exception
		}
		
		/*
		 * 1 保存成功信息
		 * 2 转发到msg.jsp
		*/
		request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱激活");
		return "f:/jsps/msg.jsp";

	}
		
} 
