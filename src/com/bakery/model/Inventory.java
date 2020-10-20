package com.bakery.model;

public class Inventory {

	private String itemNumber;
	private Integer quantity;
	private String dateAdded;

	public Inventory() {
		super();
		this.itemNumber = "";
		this.quantity = 0;
		this.dateAdded = "";
	}

	public Inventory(String itemNumber, int quantity, String dateAdded) {
		super();
		this.itemNumber = itemNumber;
		this.quantity = quantity;
		this.dateAdded = dateAdded;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

}
