package com.bakery.model;

import java.util.List;

public class Bakery {

	public Bakery() {
		super();
	}

	public Bakery(List<Store> listOfStore) {
		super();
		this.listOfStore = listOfStore;
	}

	private List<Store> listOfStore;

	public List<Store> getListOfStore() {
		return listOfStore;
	}

	public void setListOfStore(List<Store> listOfStore) {
		this.listOfStore = listOfStore;
	}

}
