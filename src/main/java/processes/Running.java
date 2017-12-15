package processes;

import java.util.LinkedList;

import data.Order;
import data.Product;
import data.Robot;

public class Running extends Thread {

	LinkedList<Robot> robots ;
	 int  SelectRobot ;
	 Product product ;
	 Order order ;
	
	
	public Running(LinkedList<Robot> robots, int  SelectRobot, Product product,Order order ){
   this.robots = robots ;
   this.SelectRobot = SelectRobot ;
   this.product = product ;
   this.order = order ;
	
	}
	
	public void run() {
		RobotRunning rr = new RobotRunning(robots.get(SelectRobot).getId(),robots.get(SelectRobot),product);
		robots.get(SelectRobot).execute(rr);
		

		Thread productTr = new ProductRunning(robots.get(SelectRobot), product, order);
		productTr.start();
	}
}
