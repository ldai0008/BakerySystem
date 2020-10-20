package com.bakery.model;

import java.util.ArrayList;
import java.util.List;

public class BakerySystem {

	private Bakery bakery;
	private List<FoodItem> foodList;

	public BakerySystem() {
		super();
		this.bakery = new Bakery();
		this.foodList = new ArrayList<>();
	}

	public Bakery getBakery() {
		return bakery;
	}

	public void setBakery(Bakery bakery) {
		this.bakery = bakery;
	}

	public List<FoodItem> getFoodList() {
		return foodList;
	}

	public void setFoodList(List<FoodItem> foodList) {
		this.foodList = foodList;
	}

}
