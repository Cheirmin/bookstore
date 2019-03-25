<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<link href="<c:url value='/jsps/user/css/style.css'/>" rel='stylesheet' type='text/css'/>

  </head>
  
  <body>
   <script>$(document).ready(function(c) {
		$('.close').on('click', function(c){
			$('.login-form').fadeOut('slow', function(c){
		  		$('.login-form').remove();
			});
		});	  
	});
 </script>
 
  <h1>注册</h1>
  <div class="login-form">
		<div class="close"> </div>
			<form action="<c:url value='/UserServlet'/>" method="post">
				<input type="hidden" name="method" value="regist"/><br/><br/>
				<p style="color: red;font-weight: 900">${msg }</p>
				<font color="white">账 户：</font><input type="text" name="username" value="${form.username }"/>
				<p style="color: red;font-weight: 900">${errors.username }</p><br/>
				<font color="white">邮 箱：</font><input type="text" name="email" value="${form.email }"/>
				<p style="color: red;font-weight: 900">${errors.email }</p><br/>
				<font color="white">密 码：</font><input type="password" name="password" value="${form.password }"/>
				<p style="color: red;font-weight: 900">${errors.password }</p><br/>
				<div class="signin">
					<input type="submit" value="Register" >
				</div>
			</form>
	</div>
  </body>
</html>
