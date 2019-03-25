package cn.cheirmin.bookstore.category.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cheirmin.bookstore.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

/**
 * 
 * @author Cheirmin
 */

public class CategoryServlet extends BaseServlet{
	private CategoryService categoryService = new CategoryService();

	//查询所有分类
	public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("categoryList", categoryService.findAll());
		return "f:/jsps/left.jsp";
	}

}
