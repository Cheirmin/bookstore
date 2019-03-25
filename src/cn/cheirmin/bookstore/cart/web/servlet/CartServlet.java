package cn.cheirmin.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cheirmin.bookstore.book.domain.Book;
import cn.cheirmin.bookstore.book.service.BookService;
import cn.cheirmin.bookstore.cart.domain.Cart;
import cn.cheirmin.bookstore.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;

/**
 * 购物车类
 * @author Cheirmin
 */

public class CartServlet extends BaseServlet {

	//添加购物条目
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1 得到车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		/**
		 * 2 得到条目
		 * >得到图书和数量
		 * >得到图书的bid,然后通过bid查询数据库得到Book
		 * >数量表单中得到
		 */
		String bid = request.getParameter("bid");
		Book book = new BookService().load(bid);
		int count = Integer.parseInt(request.getParameter("count"));
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		//3 把条目添加到车里面
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}

	//清除购物条目
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//得到车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		//调用车的clear
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	
	//删除指定购物条目
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//得到车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		//得到要删除的bid
		String bid = request.getParameter("bid");
		//调用车的delete
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
