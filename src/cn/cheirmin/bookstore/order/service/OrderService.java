package cn.cheirmin.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.cheirmin.bookstore.order.dao.OrderDao;
import cn.cheirmin.bookstore.order.domain.Order;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	//添加订单，需要处理事务
	public void add(Order order) {
		try {
			//开启事务
			JdbcUtils.beginTransaction();
			
			orderDao.addOrder(order);//插入订单
			orderDao.addOrderItemList(order.getOrderItemList());//插入订单中的所有条目
			
			//提交事务
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			//回滚事务
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				
			}
			throw new RuntimeException(e);
		}
	}

	//我的订单
	public List<Order> myOrders(String uid) {
		return orderDao.findByUid(uid);
	}

	//加载订单
	public Order load(String oid) {
		// TODO Auto-generated method stub
		return orderDao.load(oid);
	}

	//确认收货
	public void confirm(String oid) throws OrderException{
		// 1 校验订单状态，如果不是3，抛出异常,防止直接在地址栏中操作方法
		int state = orderDao.getStateByOid(oid);//获取订单状态
		if(state != 3) throw new OrderException("订单确认失败，坏淫！");
		
		// 2 修改订单状态为4，表示交易成功
		orderDao.updateState(oid,4);
	}
}
