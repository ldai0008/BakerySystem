import java.util.ArrayList;
import java.util.List;
/**
 * @version: V1.0
 * @author: Team_04
 * @className: Store
 * @description: Entity Class for store
 * @data: 2020-11-1 9:00
 **/
public class Store {

	private String storeId;
	private String storeAddress;
	private String storeContactNumber;
	private List<User> listOfUser;
	private List<Order> listOfOrder;
	private List<Inventory> listOfInventory;

	/**
	 * Constructor for objects of class Store
	 * have a non-parameterised (“default”) constructor
	 */
	public Store() {
		super();
		storeId = "";
		storeAddress = "";
		storeContactNumber = "";
		listOfUser = new ArrayList<>();
		listOfOrder = new ArrayList<>();
		listOfInventory = new ArrayList<>();
	}

	/**
	 * Constructor for objects of class Store
	 * have a parameterised constructor
	 */
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

	/**
	 * Get method
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * Mutator method
	 * @param   storeId   the new store ID in the Store
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * Get method
	 * @return the storeAddress
	 */
	public String getStoreAddress() {
		return storeAddress;
	}

	/**
	 * Mutator method
	 * @param   storeAddress   the new storeAddress in the Store
	 */
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	/**
	 * Get method
	 * @return the storeContactNumber
	 */
	public String getStoreContactNumber() {
		return storeContactNumber;
	}

	/**
	 * Mutator method
	 * @param   storeContactNumber   the new storeContactNumber in the Store
	 */
	public void setStoreContactNumber(String storeContactNumber) {
		this.storeContactNumber = storeContactNumber;
	}

	/**
	 * Get method
	 * @return the listOfUser
	 */
	public List<User> getListOfUser() {
		return listOfUser;
	}

	/**
	 * Mutator method
	 * @param   listOfUser   the new list of user in the Store
	 */
	public void setListOfUser(List<User> listOfUser) {
		this.listOfUser = listOfUser;
	}

	/**
	 * Get method
	 * @return the listOfOrder
	 */
	public List<Order> getListOfOrder() {
		return listOfOrder;
	}

	/**
	 * Mutator method
	 * @param   listOfOrder   the new list of order in the Store
	 */
	public void setListOfOrder(List<Order> listOfOrder) {
		this.listOfOrder = listOfOrder;
	}

	/**
	 * Get method
	 * @return the listOfInventory
	 */
	public List<Inventory> getListOfInventory() {
		return listOfInventory;
	}

	/**
	 * Mutator method
	 * @param   listOfInventory   the new list of inventory in the Store
	 */
	public void setListOfInventory(List<Inventory> listOfInventory) {
		this.listOfInventory = listOfInventory;
	}

}
