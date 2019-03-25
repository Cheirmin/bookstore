package cn.cheirmin.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cheirmin.bookstore.book.domain.Book;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//查询所有图书
	public List<Book> findAll() {
		try {
			String sql = "select * from book where del=0";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//按分类查找图书
	public List<Book> findByCategory(String cid) {
		try {
			String sql = "select * from book where cid=?";
			return qr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//加载书本详细页面
	public Book findByBid(String bid) {
		try {
			String sql = "select * from book where bid=?";
			return qr.query(sql, new BeanHandler<Book>(Book.class),bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//查询指定分类下的图书本数
	public int getCountByCid(String cid) {
		try {
			String sql = "select count(*) from book where cid=?";
			Number cnt = (Number)qr.query(sql, new ScalarHandler(), cid);
			return cnt.intValue();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//添加图书
	public void add(Book book) {
		try {
			String sql = "insert into book values(?,?,?,?,?,?)";
			Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), book.getCategory().getCid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//删除图书信息
	public void delete(String bid) {
		try {
			String sql = "update book set del=true where bid=?";
			qr.update(sql, bid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//修改图书信息
	public void edit(Book book) {
		try {
			String sql = "update book set bname=?, price=?,author=?, image=?, cid=? where bid=?";
			Object[] params = {book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), 
					book.getCategory().getCid(), book.getBid()};
			qr.update(sql, params);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
