package cn.cheirmin.bookstore.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cheirmin.bookstore.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet{
	
	private BookService bookService = new BookService();
	
	//加载书本详细页面
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 1 得到参数bid
		 * 2 查询得到Book
		 * 3 保存，转发到desc.jsp
		 */
		request.setAttribute("book", bookService.load(request.getParameter("bid")));
		return "f:/jsps/book/desc.jsp";
	}

	//查找所有图书
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "f:/jsps/book/list.jsp";
	}
	
	//按分类查找图书
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("bookList", bookService.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
	
}
