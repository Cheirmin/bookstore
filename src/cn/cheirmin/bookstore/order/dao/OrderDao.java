package cn.cheirmin.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cheirmin.bookstore.order.domain.Order;
import cn.cheirmin.bookstore.order.domain.OrderItem;
import cn.cheirmin.bookstore.book.domain.Book;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//添加订单
	public void addOrder(Order order) {
		try {
			String sql = "insert into orders values(?,?,?,?,?,?)";
			/**
			 * 处理util的date转换成SQL的Timestamp
			 */
			Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
			Object[] params = {order.getOid(),timestamp,order.getTotal(),
					order.getState(),order.getOwner().getUid(),order.getAddress()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//添加订单条目，需要批处理
	public void addOrderItemList(List<OrderItem> orderItemsList) {
		/**
		 * 批处理
		 * QueryRunner类的batch(String sql,Object[][] params)
		 * 其中params是多个一维数组!
		 * 每个一维数组都与sql执行一次，多个执行多次
		 */
		try {
			String sql = "insert into orderitem values(?,?,?,?,?)";
			/**
			 * 把orderItemsList转化为二维数组
			 * 	把一个orderItem转换为要给一维数组
			 */
			Object[][] params = new Object[orderItemsList.size()][];
			//循环遍历orderItemsList，使用每个orderItem对象为params中每个一维数组赋值
			for (int i = 0; i < orderItemsList.size(); i++) {
				OrderItem item = orderItemsList.get(i);
				params[i] = new Object[]{item.getIid(),item.getCount(),
						item.getSubtotal(),item.getOrder().getOid(),
						item.getBook().getBid()};
			}
			qr.batch(sql, params);//执行批处理
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//按照uid查询订单
	public List<Order> findByUid(String uid) {
		/**
		 * 1 通过uid查询当前用户所有List<Order>
		 * 2 循环遍历每个Order,为其加载他的所有OrderItem
		 */
		try {
			//得到该uid的所有订单
			String sql = "select * from orders where uid=? order by ordertime desc";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
			
			//循环遍历每一个订单，加载其所有订单条目
			for(Order order :orderList) {
				loadOrderItems(order);//为订单加载其所有订单条目
			}
			
			//3. 返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//为指定订单加载其所有订单条目
	private void loadOrderItems(Order order) throws SQLException {
		//查询两张表orderitem,book 
		String sql = "select * from orderitem o,book b where o.bid=b.bid and oid=?";
		
		//因为一行结果集对应不再是一个JavaBean，所以不能再使用BeanListHandler,而是MapListHandler
		//mapList是多个Map，每个对应一行结果集
		List<Map<String,Object>> mapList= qr.query(sql, new MapListHandler(),order.getOid());
		 
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}
	
	//把mapList中每个Map转换成两个对象，并建立关系
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map : mapList) {
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}
	
	//把一个Map转换成一个OrderItem对象
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	//加载订单
	public Order load(String oid) {
		try {
			//得到该oid的所有订单
			String sql = "select * from orders where oid=?";
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
			
			//为订单加载其所有订单条目
			loadOrderItems(order);
			
			//返回订单
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//通过oid查询订单状态
	public int getStateByOid(String oid) {
		try {
			String sql = "select state from orders where oid=?";
			return (Integer)qr.query(sql, new ScalarHandler(),oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//通过oid设置订单状态
	public void updateState(String oid,int state) {
		try {
			String sql = "update orders set state=? where oid=?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
