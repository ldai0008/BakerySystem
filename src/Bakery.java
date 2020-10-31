import java.util.ArrayList;
import java.util.List;

public class Bakery {

	private ArrayList<Store> listOfStore;
	/**
	 * Constructor for objects of class Bakery
	 * have a non-parameterised (“default”) constructor
	 */
	public Bakery() {
		this.listOfStore = new ArrayList<>();
	}

	/**
	 * Constructor for objects of class Bakery
	 * have a parameterised constructor
	 */
	public Bakery(ArrayList<Store> listOfStore) {
		this.listOfStore = listOfStore;
	}

	/**
	 * Get method
	 * @return the listOfStore
	 */
	public ArrayList<Store> getListOfStore() {
		return listOfStore;
	}

	/**
	 * Mutator method
	 * @param   listOfStore  the new listOfStore in the bakery
	 */
	public void setListOfStore(ArrayList<Store> listOfStore) {
		this.listOfStore = listOfStore;
	}

}
