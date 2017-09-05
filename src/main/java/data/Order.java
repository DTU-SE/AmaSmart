package data;

import java.util.LinkedList;

public class Order {

	private LinkedList<Product> order ;
	private int orderID ;
	private int status ;
	
	
	
	public Order(int orderID) {
		super();
		this.orderID = orderID;
	}
	public LinkedList<Product> getOrder() {
		return order;
	}
	public void setOrder(LinkedList<Product> order) {
		this.order = order;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
}
