package cn.cheirmin.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import cn.cheirmin.bookstore.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/*
 * User持久层
 * @author Cheirmin
*/

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//按用户名查询
	public User findByUsername(String username) {
		try {
			String sql = "select * from t_user where username=?";
			return qr.query(sql, new BeanHandler<User>(User.class),username);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//按邮箱查询
	public User findByEmail(String email) {
		try {
			String sql = "select * from t_user where email=?";
			return qr.query(sql, new BeanHandler<User>(User.class),email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//按激活码查询
		public User findByCode(String code) {
			try {
				String sql = "select * from t_user where code=?";
				return qr.query(sql, new BeanHandler<User>(User.class),code);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	
	//插入User
	public void add(User user) {
		try {
			String sql = "insert into t_user values(?,?,?,?,?,?)";
			Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),
					user.getEmail(),user.getCode(),user.isState()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//修改激活态
	public void updateState(String uid,boolean state) {
		try {
			String sql = "update t_user set state=? where uid=? ";
			qr.update(sql,state,uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
