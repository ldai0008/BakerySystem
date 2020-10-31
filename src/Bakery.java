import java.util.ArrayList;
import java.util.List;

public class Bakery {

	private ArrayList<Store> listOfStore;

	public Bakery() {
		this.listOfStore = new ArrayList<>();
	}
	public Bakery(ArrayList<Store> listOfStore) {
		this.listOfStore = listOfStore;
	}

	public ArrayList<Store> getListOfStore() {
		return listOfStore;
	}

	public void setListOfStore(ArrayList<Store> listOfStore) {
		this.listOfStore = listOfStore;
	}

}
