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

	/**
	 * Constructor for objects of class Order
	 * have a non-parameterised (“default”) constructor
	 */
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

	/**
	 * Constructor for objects of class Order
	 * have a parameterised constructor
	 */
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

	/**
	 * Get method
	 * @return the “default”
	 */
	public String getCustomerPhone(){
		return "";
	}

	/**
	 * Get method
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * Mutator method
	 * @param   orderDate   the order data in the Order
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * Get method
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * Mutator method
	 * @param   orderId   the new orderId in the Order
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * Get method
	 * @return the orderTime
	 */
	public String getOrderTime() {
		return orderTime;
	}

	/**
	 * Mutator method
	 * @param   orderTime   the new order time in the Order
	 */
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * Get method
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Mutator method
	 * @param   orderStatus   the new orderStatus in the Order
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Get method
	 * @return the quantity
	 */
	public Map<FoodItem, Integer> getQuantity() {
		return quantity;
	}

	/**
	 * Mutator method
	 * @param   quantity   the new quantity in the Order
	 */
	public void setQuantity(Map<FoodItem, Integer> quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get method
	 * @return the totalCost
	 */
	public Double getTotalCost() {
		return totalCost;
	}

	/**
	 * Mutator method
	 * @param   totalCost   the new totalCost in the Order
	 */
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * Get method
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Mutator method
	 * @param   lastModifiedDate   the new lastModifiedDate in the Order
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Get method
	 * @return the lastModifiedTime
	 */
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * Mutator method
	 * @param   lastModifiedTime   the new lastModifiedTime in the Order
	 */
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * Get method
	 * @return the nameOfCustomer
	 */
	public String getNameOfCustomer() {
		return nameOfCustomer;
	}

	/**
	 * Mutator method
	 * @param   nameOfCustomer   the new nameOfCustomer in the Order
	 */
	public void setNameOfCustomer(String nameOfCustomer) {
		this.nameOfCustomer = nameOfCustomer;
	}

	/**
	 * Get method
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * Mutator method
	 * @param   lastModifiedBy   the new last modify in the Order
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
