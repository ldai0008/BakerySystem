import java.util.HashMap;
import java.util.Map;

public class Order {

	private String orderDate;
	private String orderId;
	private String orderTime;
	private String orderStatus;
	private Map<FoodItem, Integer> quantity;
	private Double totalCost;
	private String lastModifiedDate;
	private String lastModifiedTime;
	private String nameOfCustomer;
	private String lastModifiedBy;

	public Order() {
		this.orderDate = "";
		this.orderTime = "";
		this.orderStatus = "";
		this.quantity = new HashMap<>();
		this.totalCost = 0.0;
		this.lastModifiedDate = "";
		this.lastModifiedTime = "";
		this.nameOfCustomer = "";
		this.lastModifiedBy = "";
	}

	public Order(String orderDate, String orderId, String orderTime, String orderStatus,
			Map<FoodItem, Integer> quantity, Double totalCost, String lastModifiedDate, String lastModifiedTime,
			String nameOfCustomer, String lastModifiedBy) {
		this.orderDate = orderDate;
		this.orderId = orderId;
		this.orderTime = orderTime;
		this.orderStatus = orderStatus;
		this.quantity = quantity;
		this.totalCost = totalCost;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedTime = lastModifiedTime;
		this.nameOfCustomer = nameOfCustomer;
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getCustomerPhone(){
		return "";
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Map<FoodItem, Integer> getQuantity() {
		return quantity;
	}

	public void setQuantity(Map<FoodItem, Integer> quantity) {
		this.quantity = quantity;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getNameOfCustomer() {
		return nameOfCustomer;
	}

	public void setNameOfCustomer(String nameOfCustomer) {
		this.nameOfCustomer = nameOfCustomer;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
