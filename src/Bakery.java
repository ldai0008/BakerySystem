import java.util.ArrayList;
import java.util.List;
/**
 * @version: V1.0
 * @author: Team_04
 * @className: Bakery
 * @description: Entity Class for bakery
 * @data: 2020-11-1 9:00
 **/
public class Bakery {
	// The list of store for Bakery
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
