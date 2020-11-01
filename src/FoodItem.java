/**
 * @version: V1.0
 * @author: Team_04
 * @className: FoodItem
 * @description: Entity Class for food item
 * @data: 2020-11-1 9:00
 **/
public class FoodItem {

	private String itemNumber;
	private String foodItemName;
	private String foodType;
	private Double currentPrice;

	/**
	 * Constructor for objects of class FoodItem
	 * have a parameterised constructor
	 */
	public FoodItem(String itemNumber, String foodItemName, String foodType, Double currentPrice) {
		super();
		this.itemNumber = itemNumber;
		this.foodItemName = foodItemName;
		this.foodType = foodType;
		this.currentPrice = currentPrice;
	}
	/**
	 * Constructor for objects of class FoodItem
	 * have a non-parameterised (“default”) constructor
	 */
	public FoodItem() {
		super();
		this.itemNumber = "";
		this.foodItemName = "";
		this.foodType = "";
		this.currentPrice = 0.00;
	}

	/**
	 * Get method
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return itemNumber;
	}

	/**
	 * Get method
	 * @return the foodItemName
	 */
	public String getFoodItemName() {
		return foodItemName;
	}

	/**
	 * Get method
	 * @return the foodType
	 */
	public String getFoodType() {
		return foodType;
	}

	/**
	 * Get method
	 * @return the currentPrice
	 */
	public Double getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * Mutator method
	 * @param   itemNumber   the new itemNumber in the FoodItem
	 */
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	/**
	 * Mutator method
	 * @param   foodItemName   the new food item name in the FoodItem
	 */
	public void setFoodItemName(String foodItemName) {
		this.foodItemName = foodItemName;
	}

	/**
	 * Mutator method
	 * @param   foodType   the new food type in the FoodItem
	 */
	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}


	/**
	 * Mutator method
	 * @param   currentPrice   the new current price in the FoodItem
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
}
