<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
	<link href="<c:url value='/jsps/user/css/style.css'/>" rel='stylesheet' type='text/css'/>
	
  </head>
  
  <body>

 <!--SIGN UP-->
   <h1>登录系统</h1>
	<div class="login-form">
			<form action="<c:url value='/UserServlet'/>" method="post">
				<div class="close"> </div>
				<div class="clear"> </div><p><br/></p>
				<div class="avtar"></div>
				<input type="hidden" name="method" value="login"/>
				<div class="key">
					<p style="color: red;font-weight: 900">${msg }</p>
					<font color="white">账 号：</font><input type="text"  name="username" value="${form.username }" />
					<p style="color: red;font-weight: 900">${errors.username }</p><br/>
					<font color="white">密 码：</font><input type="password" name="password" value="${form.password }" />
					<p style="color: red;font-weight: 900">${errors.username }</p><br/>
				</div>
				<div class="signin">
					<input type="submit" value="Login" />
				</div>
			</form>
		</div>
		
<script>$(document).ready(function(c) {
		$('.close').on('click', function(c){
			$('.login-form').fadeOut('slow', function(c){
		  		$('.login-form').remove();
			});
		});	  
	});
</script>
	</body>
</html>
