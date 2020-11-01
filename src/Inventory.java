/**
 * @version: V1.0
 * @author: Team_04
 * @className: Inventory
 * @description: Entity Class for inventory
 * @data: 2020-11-1 9:00
 **/
public class Inventory {

	private String itemNumber;
	private Integer quantity;
	private String dateAdded;

	/**
	 * Constructor for objects of class Inventory
	 * have a non-parameterised (“default”) constructor
	 */
	public Inventory() {
		this.itemNumber = "";
		this.quantity = 0;
		this.dateAdded = "";
	}

	/**
	 * Constructor for objects of class Inventory
	 * have a parameterised constructor
	 */
	public Inventory(String itemNumber, int quantity, String dateAdded) {
		this.itemNumber = itemNumber;
		this.quantity = quantity;
		this.dateAdded = dateAdded;
	}

	/**
	 * Get method
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return itemNumber;
	}

	/**
	 * Mutator method
	 * @param   itemNumber   the new itemNumber in the Inventory
	 */
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	/**
	 * Get method
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * Mutator method
	 * @param   quantity   the new quantity in the Inventory
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get method
	 * @return the dateAdded
	 */
	public String getDateAdded() {
		return dateAdded;
	}

	/**
	 * Mutator method
	 * @param   dateAdded   the new data in the Inventory
	 */
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

}
