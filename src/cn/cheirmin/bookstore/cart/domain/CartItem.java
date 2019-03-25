package cn.cheirmin.bookstore.cart.domain;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import cn.cheirmin.bookstore.book.domain.Book;

/**
 * 购物车条目类
 * @author Cheirmin
 */

public class CartItem {
	private Book book;//商品
	private int count;//数量
	
	public double getSubtotal() {//小计方法，没有对应的成员
		/**
		 * 采用了BigDecimal运算方法
		 * 可以处理二进制的十分之一和十进制的三分之一计算误差的问题
		 * 本来误差在允许范围内，
		 * 因为涉及到钱的问题，所以必须精确
		 */
		BigDecimal d1 = new BigDecimal(book.getPrice()+"");
		BigDecimal d2 = new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
