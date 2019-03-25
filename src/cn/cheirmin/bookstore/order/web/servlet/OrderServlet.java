package cn.cheirmin.bookstore.order.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cheirmin.bookstore.cart.domain.Cart;
import cn.cheirmin.bookstore.cart.domain.CartItem;
import cn.cheirmin.bookstore.order.domain.Order;
import cn.cheirmin.bookstore.order.domain.OrderItem;
import cn.cheirmin.bookstore.order.service.OrderException;
import cn.cheirmin.bookstore.order.service.OrderService;
import cn.cheirmin.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	//确认收货，调用service的方法即可
	public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 1 得到oid参数
		 * 2 调用service方法
		 * 	>有异常，保存失败信息，转发到msp.jsp
		 * 3 保存成功信息，转发到msp.jsp
		 */
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "收货成功");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 1 得到oid参数
		 * 2 使用oid 调用service方法得到order
		 * 3 保存到request域，转发到/jsps/order/desc.jsp
		 */
		request.setAttribute("order", orderService.load(request.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}
	
	//我的订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 1 从session 中得到uid 
		 * 2 使用uid调用OrderService#myOrders(uid)得到该用户的所有订单List<Order>
		 * 3 把订单列表保存到request域中，转发到/jsps/order/list.jsp
		 */
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOrders(user.getUid());
		request.setAttribute("orderList", orderList);
		
		return "f:/jsps/order/list.jsp";
	}

	// 添加订单，把session中的车生成order对象
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 1 从session 中得到cart 
		 * 2 使用cart中的order对象 
		 * 3 调用 service方法完成添加订单
		 * 4保存order到request域中，转发到/jsps/order/desc.jsp
		 */

		// 1 从session 中得到cart
		Cart cart = (Cart) request.getSession().getAttribute("cart");

		// 2 把cart转换成order对象
		Order order = new Order();
		order.setOid(CommonUtils.uuid());// 设置编号
		order.setOrdertime(new Date());// 设置下单时间
		order.setState(1);// 订单状态 默认1 ，表示未付款
		User user = (User) request.getSession().getAttribute("session_user");
		order.setOwner(user);// 订单所有者
		order.setTotal(cart.getTotal());// 订单总金额，从cart中获取

		//创建订单条目集合
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		// 便利cart中的所有cartItem，使每一个cartItem对象，并添加到集合中
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem oi = new OrderItem();// 创建订单条目

			oi.setIid(CommonUtils.uuid());// 设置条目id
			oi.setCount(cartItem.getCount());// 设置条目数量
			oi.setBook(cartItem.getBook());// 设置条目的图书
			oi.setSubtotal(cartItem.getSubtotal());// 设置条目小计
			oi.setOrder(order);// 设置所属订单
			orderItemList.add(oi);// 把订单条目添加到集合中
		}
		// 把所有的订单条目添加到订单中
		order.setOrderItemList(orderItemList);
		
		//清空购物车
		cart.clear();

		// 3 调用 service方法完成添加订单
		orderService.add(order);

		// 4 保存order到request域中，转发到/jsps/order/desc.jsp
		request.setAttribute("order", order);
		return "/jsps/order/desc.jsp";
	}

}
