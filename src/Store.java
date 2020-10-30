import java.util.ArrayList;
import java.util.List;

public class Store {

	private String storeId;
	private String storeAddress;
	private String storeContactNumber;
	private List<User> listOfUser;
	private List<Order> listOfOrder;
	private List<Inventory> listOfInventory;

	public Store() {
		super();
		storeId = "";
		storeAddress = "";
		storeContactNumber = "";
		listOfUser = new ArrayList<>();
		listOfOrder = new ArrayList<>();
		listOfInventory = new ArrayList<>();
	}

	public Store(String storeId, String storeAddress, String storeContactNumber, List<User> listOfUser,
			List<Order> listOfOrder, List<Inventory> listOfInventory) {
		super();
		this.storeId = storeId;
		this.storeAddress = storeAddress;
		this.storeContactNumber = storeContactNumber;
		this.listOfUser = listOfUser;
		this.listOfOrder = listOfOrder;
		this.listOfInventory = listOfInventory;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreContactNumber() {
		return storeContactNumber;
	}

	public void setStoreContactNumber(String storeContactNumber) {
		this.storeContactNumber = storeContactNumber;
	}

	public List<User> getListOfUser() {
		return listOfUser;
	}

	public void setListOfUser(List<User> listOfUser) {
		this.listOfUser = listOfUser;
	}

	public List<Order> getListOfOrder() {
		return listOfOrder;
	}

	public void setListOfOrder(List<Order> listOfOrder) {
		this.listOfOrder = listOfOrder;
	}

	public List<Inventory> getListOfInventory() {
		return listOfInventory;
	}

	public void setListOfInventory(List<Inventory> listOfInventory) {
		this.listOfInventory = listOfInventory;
	}

}
