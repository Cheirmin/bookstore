package cn.cheirmin.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author Cheirmin
 */

public class Cart {
	private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>();
	
	//合计钱币数,等于所有条目小计和
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem:map.values()) {
			/**
			 * 采用了BigDecimal运算方法
			 * 可以处理二进制的十分之一和十进制的三分之一计算误差的问题
			 * 本来误差在允许范围内，
			 * 因为涉及到钱的问题，所以必须精确
			 */
			BigDecimal subtotal = new BigDecimal(cartItem.getSubtotal()+"");
			total = total.add(subtotal);
		}
		return total.doubleValue();
	}
	
	//添加购物车条目
	public void add(CartItem cartItem) {
		//判断条目本身是否存在，存在合并，不存在新增
		if(map.containsKey(cartItem.getBook().getBid())){//存在返回原条目
			CartItem _cartItem = map.get(cartItem.getBook().getBid());
			//设置老条目数量为原数量加新数量
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
			map.put(cartItem.getBook().getBid(),_cartItem);
		}else {//不存在
			map.put(cartItem.getBook().getBid(),cartItem);
		}
	}
	
	//清空购物车条目
	public void clear() {
		map.clear();
	}
	
	//删除购物车条目
	public void delete(String bid) {
		map.remove(bid);
	}
	
	//获取所有条目
	public Collection<CartItem> getCartItems() {
		return map.values();
	}
	
}
