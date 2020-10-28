package com.bakery.controller;

import com.bakery.model.*;
import com.bakery.view.UserInterface;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class MainController {

	public static void main(String args[]) {
		while (true) {
			BakerySystem bakerySystem = new BakerySystem();
			boolean check;
			UserInterface.displayBakeShop();
			System.out.println("***Please enter your user credential***");
			check = login(bakerySystem);
			while (!check) {
				UserInterface.displayLoginError();
				check = login(bakerySystem);
			}
			User currentUser = bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0);
			mainOption(currentUser, bakerySystem);
		}

	}



	public static void addOrderInDB(Order aOrder, Bakery bakery) {
		BufferedWriter out = null;
		String fileName = "order.csv";
		try {
			ArrayList<String> itemName = new ArrayList<>();
			ArrayList<String> itemPrice = new ArrayList<>();
			ArrayList<String> itemQuantity = new ArrayList<>();
			for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
				itemName.add(entry.getKey().getFoodItemName());
				itemPrice.add(String.valueOf(entry.getKey().getCurrentPrice()));
				itemQuantity.add(String.valueOf(entry.getValue()));
			}
			String n = "";
			String q = "";
			String p = "";
			int length = itemName.size();
			for (int i = 0; i < length; i++) {
				if (i == length - 1) {
					n += itemName.get(i);
					q += itemQuantity.get(i);
					p += itemPrice.get(i);
				} else {
					n += itemName.get(i) + "|";
					q += itemQuantity.get(i) + "|";
					p += itemPrice.get(i) + "|";
				}
			}
			out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(bakery.getListOfStore().get(0).getStoreId() + "," + aOrder.getOrderId() + ","
					+ bakery.getListOfStore().get(0).getListOfUser().get(0).getUserId() + "," + n + "," + q + "," + p
					+ "," + aOrder.getTotalCost() + "," + aOrder.getOrderDate() + "," + aOrder.getOrderTime() + ","
					+ aOrder.getNameOfCustomer() + "," + aOrder.getOrderStatus() + "," + aOrder.getCustomerPhone() + ","
					+ aOrder.getLastModifiedBy() + "," + aOrder.getLastModifiedDate() + ","
					+ aOrder.getLastModifiedTime() + "\n");
			out.close();

		} catch (FileNotFoundException e) {
			System.out.println(fileName + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double calTotalCost(Order aOrder, BakerySystem bakerySystem) {
		double totalCost = 0;
		for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
			for (FoodItem foodItem : bakerySystem.getFoodList()) {
				double currentPrice;
				if (foodItem.equals(entry.getKey())) {
					currentPrice = foodItem.getCurrentPrice();
					totalCost += entry.getValue() * currentPrice;
					break;
				}
			}
		}
		return totalCost;
	}
	public static void createNewAdvanceOrder(BakerySystem bakerySystem) {
		String itemName;
		int itemQuantity = 0;
		boolean quantityCheck = true;
		Order aOrder = new Order();
		String option;
		FoodItem aFoodItem = new FoodItem();
		do {
			UserInterface.displayBakeShop();
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			for (FoodItem foodItem : bakerySystem.getFoodList())
			{
				if (foodItem.getFoodItemName().equals("roast coffee beans")){
					aFoodItem = foodItem;
					break;
				}
			}
			aOrder.getQuantity().put(aFoodItem, itemQuantity);
			String itemNumber = aFoodItem.getItemNumber();
			displayCurrentItem(aOrder);

			do {
				if (!quantityCheck) {
					System.out.println("!Error: The item quantity is not valid!");
					System.out.println("The current quantity in inventory for this item is:");
					System.out.println(getFoodItemQuantity(itemNumber, bakerySystem.getBakery()));
					System.out.println(
							"****************************************\n" + "Please try enter the item quantity again.");
				} else
					System.out.println("-- Please enter the item's quantity:");
				Scanner console = new Scanner(System.in);
				String s = console.nextLine();
				quantityCheck = validateQuantityCheck(itemNumber, s, bakerySystem.getBakery());
				if (quantityCheck) {
					itemQuantity = Integer.parseInt(s);
				}
			} while (!quantityCheck);

			aOrder.getQuantity().put(aFoodItem, itemQuantity);
			aOrder.setTotalCost(calTotalCost(aOrder, bakerySystem));
			UserInterface.displayBakeShop();
			displayCurrentItem(aOrder);
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			option = UserInterface.displayCreateAdvanceOrderOption();
		} while (option.equals("1"));

		if (option.equals("2")) {
			Scanner console = new Scanner(System.in);
			System.out.println("--  Enter the name of the customer:");
			String name = console.nextLine();
			System.out.println("--  Enter the phone number of the customer:");
			String phoneNumber = console.nextLine();
			boolean isNumeric;
			do {
				isNumeric = true;
				if (phoneNumber.length() == 0){
					System.out.println("The phone number cannot be blank");
					isNumeric = false;
				} else if (!UserInterface.isNumeric(phoneNumber)){
					System.out.println("Invalid input");
					isNumeric = false;
				} else if (phoneNumber.length() != 10 && phoneNumber.length() != 9){
					System.out.println("Invalid input");
					isNumeric = false;
				}
				if (!isNumeric){
					System.out.println("Please enter again: ");
					phoneNumber = console.nextLine();
				}
			} while (!isNumeric);
			String date = getDate();
			String time = getTime();
			aOrder.setOrderDate(date);
			aOrder.setOrderTime(time);
			aOrder.setLastModifiedBy(String
					.valueOf(bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId()));
			aOrder.setLastModifiedDate(date);
			aOrder.setLastModifiedTime(time);
			aOrder.setLastModifiedBy(String
					.valueOf(bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId()));
			aOrder.setOrderStatus("Confirmed");
			aOrder.setNameOfCustomer(name);
			aOrder.setCustomerPhone(phoneNumber);
			bakerySystem.getBakery().getListOfStore().get(0).getListOfOrder().add(aOrder);
			String orderId = createOrderId(aOrder);
			aOrder.setOrderId(orderId);
			addOrderInDB(aOrder, bakerySystem.getBakery());
			UserInterface.displayBakeShop();
			displayCurrentItem(aOrder);
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			System.out.println("****************************************");
			System.out.println("Order id: " + orderId);
			System.out.println("Order date: " + date);
			System.out.println("Order time: " + time);
			System.out.println("Store id: " + bakerySystem.getBakery().getListOfStore().get(0).getStoreId());
			System.out.println("Employee id: "
					+ bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId());
			System.out.println("Customer name: " + name);
			System.out.println("Customer phone number: " + phoneNumber);
			System.out.println("****************************************");
			System.out.println("The order has been successfully created! ");
		}

	}

	public static void createNewOrder(BakerySystem bakerySystem) {
		String itemName;
		int itemQuantity = 0;
		boolean nameCheck = true;
		boolean quantityCheck = true;
		Order aOrder = new Order();
		String option;
		FoodItem aFoodItem;
		do {
			UserInterface.displayBakeShop();
			displayCurrentItem(aOrder);
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			List<String> inventories = UserInterface.readFile("inventory.csv");
			bakerySystem.getBakery().getListOfStore().get(0).getListOfInventory().clear();
			for (String inventory : inventories) {
				String[] i = inventory.split(",");
				Inventory aInventory = new Inventory(i[0], Integer.parseInt(i[1]), i[2]);
				bakerySystem.getBakery().getListOfStore().get(0).getListOfInventory().add(aInventory);
			}
			do {
				if (!nameCheck) {
					System.out.println("!Error: The item name is not valid!");
					System.out.println(
							"****************************************\n" + "Please try enter the item name again.");
				} else
					System.out.println("-- Please enter the item's name:");
				Scanner console = new Scanner(System.in);
				itemName = console.nextLine();
				ArrayList<FoodItem> foodItems = searchItems(itemName, bakerySystem);
				if (foodItems.size() == 0) {
					nameCheck = false;
					continue;
				}
				nameCheck = true;
				String selection = selectItem(foodItems);
				if (Integer.parseInt(selection) != (foodItems.size() + 1)) {
					aFoodItem = foodItems.get(Integer.parseInt(selection) - 1);
					break;
				}
			} while (true);
			String itemNumber = aFoodItem.getItemNumber();
			do {
				if (!quantityCheck) {
					System.out.println("!Error: The item quantity is not valid!");
					System.out.println("The current quantity in inventory for this item is:");
					System.out.println(getFoodItemQuantity(itemNumber, bakerySystem.getBakery()));
					System.out.println(
							"****************************************\n" + "Please try enter the item quantity again.");
				} else
					System.out.println("-- Please enter the item's quantity:");
				Scanner console = new Scanner(System.in);
				String s = console.nextLine();
				quantityCheck = validateQuantityCheck(itemNumber, s, bakerySystem.getBakery());
				if (quantityCheck) {
					itemQuantity = Integer.parseInt(s);
				}
			} while (!quantityCheck);

			aOrder.getQuantity().put(aFoodItem, itemQuantity);
			aOrder.setTotalCost(calTotalCost(aOrder, bakerySystem));
			UserInterface.displayBakeShop();
			displayCurrentItem(aOrder);
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			option = UserInterface.displayCreateOrderOption();
			if (option.equals("2")) {
				System.out.println("--  Enter the name of the item you want to cancel:");
				Scanner console = new Scanner(System.in);
				String name = console.nextLine();
				for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
					for (FoodItem foodItem : bakerySystem.getFoodList()) {
						if (name.equals(entry.getKey().getFoodItemName())) {
							aOrder.getQuantity().remove(foodItem);
							break;
						}
					}
				}
				aOrder.setTotalCost(calTotalCost(aOrder, bakerySystem));
			}

		} while (option.equals("1") || option.equals("2"));

		if (option.equals("3")) {
			Scanner console = new Scanner(System.in);
			System.out.println("--  Enter the name of the customer:");
			String name = console.nextLine();
			String date = getDate();
			String time = getTime();
			aOrder.setOrderDate(date);
			aOrder.setOrderTime(time);
			aOrder.setLastModifiedBy(String
					.valueOf(bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId()));
			aOrder.setLastModifiedDate(date);
			aOrder.setLastModifiedTime(time);
			aOrder.setLastModifiedBy(String
					.valueOf(bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId()));
			aOrder.setOrderStatus("Confirmed");
			aOrder.setNameOfCustomer(name);
			bakerySystem.getBakery().getListOfStore().get(0).getListOfOrder().add(aOrder);
			String orderId = createOrderId(aOrder);
			aOrder.setOrderId(orderId);
			addOrderInDB(aOrder, bakerySystem.getBakery());
			UserInterface.displayBakeShop();
			displayCurrentItem(aOrder);
			System.out.println("            Total cost:" + aOrder.getTotalCost());
			System.out.println("****************************************");
			System.out.println("Order id: " + orderId);
			System.out.println("Order date: " + date);
			System.out.println("Order time: " + time);
			System.out.println("Store id: " + bakerySystem.getBakery().getListOfStore().get(0).getStoreId());
			System.out.println("Employee id: "
					+ bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0).getUserId());
			System.out.println("Customer name: " + name);
			System.out.println("****************************************");
			System.out.println("The order has been successfully created! ");
		}

	}

	public static String createOrderId(Order aOrder) {
		List<String> orders = UserInterface.readFile("order.csv");
		int biggest = 0;
		for (String order : orders) {
			String[] o = order.split(",");
			String orderID = o[1];
			String[] subOrderId = orderID.split("-");
			int idNumber = Integer.parseInt(subOrderId[0]);
			if (idNumber > biggest)
				biggest = idNumber;
		}
		int currentIdNumber = biggest + 1;
		String str = String.format("%06d", currentIdNumber);
		String year = aOrder.getOrderDate().split("-")[0];
		String month = aOrder.getOrderDate().split("-")[1];
		String orderId = str + "-" + month + year;
		return orderId;
	}

	public static void displayCurrentItem(Order aOrder) {
		System.out.println("Id    " + "Name               " + "Quantity " + "Cost");
		for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
			System.out.printf("%-6s", entry.getKey().getItemNumber());
			System.out.printf("%-19s", entry.getKey().getFoodItemName());
			System.out.printf("%-9s", entry.getValue());
			System.out.print(entry.getKey().getCurrentPrice());
			System.out.println();
		}
	}

	public static String getDate() {
		return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public static String getTime() {
		return LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
	}

	public static int getFoodItemQuantity(String itemNumber, Bakery bakery) {
		for (Inventory inventory : bakery.getListOfStore().get(0).getListOfInventory()) {
			if (itemNumber.equals(inventory.getItemNumber())) {
				return inventory.getQuantity();
			}
		}
		return 0;
	}

	public static ArrayList<FoodItem> searchItems(String s, BakerySystem bakerySystem) {
		ArrayList<FoodItem> items = new ArrayList<>();
		s = s.strip();
		for (FoodItem aFoodItem : bakerySystem.getFoodList()) {
			if (aFoodItem.getFoodItemName().strip().contains(s)) {
				String itemNumber = aFoodItem.getItemNumber();
				for (Inventory inventory : bakerySystem.getBakery().getListOfStore().get(0).getListOfInventory()) {
					if (itemNumber.equals(inventory.getItemNumber()))
						items.add(aFoodItem);
				}
			}
		}
		return items;
	}

	public static String selectItem(ArrayList<FoodItem> foodItems) {
		String selection;
		while (true) {
			System.out.println("-- Please select the item you want:");
			int index = 1;
			for (FoodItem foodItem : foodItems) {
				System.out.println(index + ". " + foodItem.getFoodItemName());
				index += 1;
			}
			System.out.println(index + ". " + "re-enter the food item name");
			Scanner console = new Scanner(System.in);
			selection = console.nextLine();
			selection = selection.strip();
			if (UserInterface.isNumeric(selection) && Integer.parseInt(selection) > index) {
				System.out.println("!Error: Your selection is not valid!");
				System.out
						.println("****************************************\n" + "Please try selecting a option again.");
				continue;
			}
			return selection;
		}
	}

	public static void updateInventory(Order aOrder, Bakery bakery) {
		List<String> inventories = UserInterface.readFile("inventory.csv");
		bakery.getListOfStore().get(0).getListOfInventory().clear();
		for (String inventory : inventories) {
			String[] i = inventory.split(",");
			Inventory aInventory = new Inventory(i[0], Integer.parseInt(i[1]), i[2]);
			bakery.getListOfStore().get(0).getListOfInventory().add(aInventory);
		}
		for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
			for (Inventory aInventory : bakery.getListOfStore().get(0).getListOfInventory()) {
				if (aInventory.getItemNumber().equals(entry.getKey())) {
					int originQuantity = aInventory.getQuantity();
					int finalQuantity = originQuantity - entry.getValue();
					aInventory.setQuantity(finalQuantity);
				}
			}
		}

		BufferedWriter out = null;
		String fileName = "inventory.csv";
		try {
			ArrayList<String> itemName = new ArrayList<>();
			ArrayList<String> itemPrice = new ArrayList<>();
			ArrayList<String> itemQuantity = new ArrayList<>();
			for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
				itemName.add(entry.getKey().getFoodItemName());
				itemPrice.add(String.valueOf(entry.getKey().getCurrentPrice()));
				itemQuantity.add(String.valueOf(entry.getValue()));
			}
			String n = "";
			String q = "";
			String p = "";
			int length = itemName.size();
			for (int i = 0; i < length; i++) {
				if (i == length - 1) {
					n += itemName.get(i);
					q += itemQuantity.get(i);
					p += itemPrice.get(i);
				} else {
					n += itemName.get(i) + "|";
					q += itemQuantity.get(i) + "|";
					p += itemPrice.get(i) + "|";
				}
			}
			out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(bakery.getListOfStore().get(0).getStoreId() + "," + aOrder.getOrderId() + ","
					+ bakery.getListOfStore().get(0).getListOfUser().get(0).getUserId() + "," + n + "," + q + "," + p
					+ "," + aOrder.getTotalCost() + "," + aOrder.getOrderDate() + "," + aOrder.getOrderTime() + ","
					+ aOrder.getNameOfCustomer() + "," + aOrder.getOrderStatus() + "," + aOrder.getCustomerPhone() + ","
					+ aOrder.getLastModifiedBy() + "," + aOrder.getLastModifiedDate() + ","
					+ aOrder.getLastModifiedTime() + "\n");
			out.close();

		} catch (FileNotFoundException e) {
			System.out.println(fileName + " not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean validateQuantityCheck(String itemNumber, String s, Bakery bakery) {
		boolean check = false;
		check = UserInterface.isNumeric(s);
		if (check) {
			if (Integer.parseInt(s) <= 0)
				check = false;
		} else
			return false;
		for (Inventory inventory : bakery.getListOfStore().get(0).getListOfInventory()) {
			if (itemNumber.equals(inventory.getItemNumber())) {
				int currentNumber = inventory.getQuantity();
				if (Integer.parseInt(s) <= currentNumber)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	public static Store chooseStore(Bakery bakery){
		System.out.println("Please enter the number of store you want to check: ");
		Scanner sc = new Scanner(System.in);
		String storeChose = sc.nextLine();
		boolean isNumeric;
		do {
			isNumeric = true;
			if (storeChose.length() == 0){
				System.out.println("The storeID cannot be blank");
				isNumeric = false;
			} else if (!UserInterface.isNumeric(storeChose)){
				System.out.println("Invalid input");
				isNumeric = false;
			} else if (Integer.parseInt(storeChose) < 1 || Integer.parseInt(storeChose) > 10){
				System.out.println("Invalid input");
				isNumeric = false;
			}
			if (!isNumeric){
				System.out.println("Please enter again: ");
				storeChose = sc.nextLine();
			}
		} while (!isNumeric);
		for (Store store: bakery.getListOfStore()){
			if (storeChose.equals(store.getStoreId())){
				return store;
			}
		}
		return new Store();
	}

	public static boolean login(BakerySystem bakerySystem) {
		Scanner console = new Scanner(System.in);
		System.out.println("--Enter your employee id or email:");
		System.out.println("--Enter your password:");
		String account = console.nextLine();
		System.out.println("--Enter your employee id or email:" + account);
		System.out.println("--Enter your password:");
		String password = console.nextLine();
		System.out.println("--Enter your employee id or email:" + account);
		System.out.println("--Enter your password:" + password);

		if (UserInterface.validateUser(account, password, bakerySystem))
			return true;
		else
			return false;

	}

	public static void mainOption(User currentUser, BakerySystem bakerySystem) {
		List<FoodItem> foodList = bakerySystem.getFoodList();
		Bakery bakery = bakerySystem.getBakery();
		boolean isContinue = true;
		Scanner console = new Scanner(System.in);
		String currentUserName = currentUser.getUserName();
		String currentUserType = currentUser.getUserType();
		Store store = new Store();
		boolean firstLogin = true;
		while (isContinue) {
//            BakeryUtils.displayHomeScreen(currentUserName, currentUserType);
			String selection = "";
			if (currentUserType.equals("Staff") || currentUserType.equals("Manager")) {
				UserInterface.displayHomeScreen(currentUserName, currentUserType);
				selection = console.nextLine();
				switch (selection) {
					case "1":
						createNewOrder(bakerySystem);
						break;
					case "2":
						createNewAdvanceOrder(bakerySystem);
						break;
					case "0":
						isContinue = false;
						System.out.println("good bye!!!");
						System.exit(1);
//                        break;
					default:
						System.out.println("!Error: Your selection is not valid!");
						System.out.println(
								"****************************************\n" + "Please select the correct option.");
				}
			} else if (currentUserType.equals("Owner")) {
				if (firstLogin) {
					store = chooseStore(bakery);
					firstLogin = false;
				}
				UserInterface.displayHomeScreen(currentUserName, currentUserType);
				selection = console.nextLine();
				switch (selection) {
					case "1":
						createNewOrder(bakerySystem);
						break;
					case "2":
						createNewAdvanceOrder(bakerySystem);
						break;
					case "8":
						generateReport(currentUser, bakerySystem, store);
						break;
					case "0":
						isContinue = false;
						System.out.println("good bye!!!");
						System.exit(1);
//                        break;
					default:
						System.out.println("!Error: Your selection is not valid!");
						System.out.println(
								"****************************************\n" + "Please select the correct option.");
				}
			} else {
				switch (selection) {
					case "0":
						isContinue = false;
						System.out.println("good bye!!!");
						System.exit(1);
//                        break;
					default:
						System.out.println("!Error: Your selection is not valid!");
						System.out.println(
								"****************************************\n" + "Please select the correct option.");
				}
			}
		}

	}


	public static void previousOption(User currentUser, BakerySystem bakerySystem, Consumer<User> callback) {
		System.out.println("");
		System.out.println("Please enter your choice: ");
		System.out.println("1. Back to previous menu");
		System.out.println("2. Back to home screen");
		Scanner sc = new Scanner(System.in);
		String option = sc.nextLine();
		if (option.equals("1")) {
			callback.accept(currentUser);
		} else if (option.equals("2")) {
			mainOption(currentUser, bakerySystem);
		}
	}


	public static boolean checkIfInLastMonth(String dates) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		String time = sdf.format(cal.getTime());
		Date d = null;
		Date now = null;
		try {
			d = f.parse(dates);
			now = sdf.parse(time);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		assert now != null;
		return !now.after(d);
	}

	public static int chooseReport(){
		UserInterface.displayTrackBusinessOption();
		Scanner sc = new Scanner(System.in);
		String option = sc.nextLine();
		boolean isNumeric;
		do {
			isNumeric = true;
			if (option.length() == 0){
				System.out.println("The option cannot be blank");
				isNumeric = false;
			} else if (!UserInterface.isNumeric(option)){
				System.out.println("Invalid input");
				isNumeric = false;
			} else if (Integer.parseInt(option) > 7 && Integer.parseInt(option) < 1){
				System.out.println("Invalid input");
				isNumeric = false;
			}
			if (!isNumeric){
				System.out.println("Please enter again: ");
				option = sc.nextLine();
			}
		} while (!isNumeric);
		return Integer.parseInt(option);
	}

	public static Map<String, Double> originDays(){
		Map<String, Double> daysSold = new HashMap<>();
		daysSold.put("Monday",0.0);
		daysSold.put("Tuesday",0.0);
		daysSold.put("Wednesday",0.0);
		daysSold.put("Thursday",0.0);
		daysSold.put("Friday",0.0);
		daysSold.put("Saturday",0.0);
		daysSold.put("Sunday",0.0);
		return daysSold;
	}

	public static int setLowInventoryBar(){
		System.out.println("Please enter the number to set the bar for low inventory items: ");
		Scanner sc = new Scanner(System.in);
		String barForLowInventory = sc.nextLine();
		boolean isNumeric;
		do {
			isNumeric = true;
			if (barForLowInventory.length() == 0){
				System.out.println("The standard of low inventory cannot be blank");
				isNumeric = false;
			} else if (!UserInterface.isNumeric(barForLowInventory)){
				System.out.println("The standard should be a positive number");
				isNumeric = false;
			}
			if (!isNumeric){
				System.out.println("Please enter again: ");
				barForLowInventory = sc.nextLine();
			} else {
				System.out.println("You set the bar for low inventory at " + barForLowInventory);
			}
		} while (!isNumeric);
		return Integer.parseInt(barForLowInventory);
	}

	public static void generateReportOfDaysMadeTheMostSold(String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		Map<String, Double> daysSold = originDays();
		for (String order: orders){
			String[] quantities = order.split(",");
			String amount = quantities[6];
			String date = quantities[7];
			String orderStoreId = quantities[0];

			if (storeId.trim().equalsIgnoreCase(orderStoreId)) {
				if (checkIfInLastMonth(date)) {
					String day = getWeek(date);
					for (Map.Entry<String, Double> entry : daysSold.entrySet()) {
						if (entry.getKey().equals(day)) {
							double currentTotal = entry.getValue();
							double finalTotal = currentTotal + Double.parseDouble(amount);
							daysSold.put(day, finalTotal);
							break;
						}
					}
				}
			}
		}
		ArrayList<String> topDays = new ArrayList<>();
		double topTotal = 0;
		for (Map.Entry<String, Double> entry : daysSold.entrySet()){
			if (entry.getValue() > topTotal){
				topTotal = entry.getValue();
				topDays.clear();
				topDays.add(entry.getKey());
			}
			else if (entry.getValue() == topTotal){
				topDays.add(entry.getKey());
			}
		}
		StringBuilder topDay = new StringBuilder();
		for (String s : topDays){
			topDay.append(s).append("\n");
		}

		System.out.println("Days of the week that made the most sale in the last month is: ");
		System.out.println(topDay);
		System.out.println("The total revenue on that days of week is: ");
		System.out.println(topTotal);
	}


	public static void generateReportOfLowInventory(Store store, List<FoodItem> foodList, int lowInventory){
		System.out.println("Food items low in inventory: ");
		System.out.println("Id    " + "Name                    " + "Quantity ");
		for (int i = 0; i < store.getListOfInventory().size(); i++) {
			if (store.getListOfInventory().get(i).getQuantity() < lowInventory) {
				String itemNumber = store.getListOfInventory().get(i).getItemNumber();
				int itemQuantity = store.getListOfInventory().get(i).getQuantity();
				for (FoodItem item : foodList) {
					if (itemNumber.equals(item.getItemNumber())) {
						String itemName = item.getFoodItemName();
						System.out.printf("%-6s", itemNumber);
						System.out.printf("%-24s", itemName);
						System.out.printf("%-9s", itemQuantity);
						System.out.println();
					}

				}
			}
		}

	}


	public static void generateReportOfSoldCoffee(List<FoodItem> foodList, String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		int totalNum = 0;
		for (String order: orders){
			String[] quantities = order.split(",");
			String[] items = quantities[3].split("\\|");
			String[] quantity = quantities[4].split("\\|");
			String date = quantities[7];
			String orderStoreId = quantities[0];

			if (orderStoreId.trim().equalsIgnoreCase(storeId.trim())) {
				if (checkIfInLastMonth(date)) {
					int index = 0;
					for (String item : items) {
						boolean foodCheck = false;
						for (FoodItem foodItem : foodList) {
							if (foodItem.getFoodItemName().equals(item.strip()) && foodItem.getFoodType().equals("Coffee")) {
								foodCheck = true;
								break;
							}
						}
						if (foodCheck) {
							totalNum += Integer.parseInt(quantity[index]);
						}
						index += 1;
					}
				}
			}
		}
		System.out.println("Total number of coffee sold in last month is: " + totalNum);
	}

	public static void generateReportOfSoldCoffeeBean(String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		int totalNum = 0;
		for (String order: orders) {
			String[] quantities = order.split(",");
			String[] items = quantities[3].split("\\|");
			String[] quantity = quantities[4].split("\\|");
			String date = quantities[7];
			String orderStoreId = quantities[0];
			if (storeId.trim().equalsIgnoreCase(orderStoreId)) {
				if (checkIfInLastMonth(date)) {
					int index = 0;
					for (String item : items) {
						boolean foodCheck = false;
						if (item.equals("roast coffee beans")) {
							foodCheck = true;
						}
						if (foodCheck) {
							totalNum += Integer.parseInt(quantity[index]);
						}
						index += 1;
					}
				}
			}
		}
		System.out.println("Total number of coffee bean sold in last month is: " + totalNum);
	}

	public static void generateReportOfSoldFoodItem(String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		int totalNum = 0;
		for (String order: orders) {
			String[] quantities = order.split(",");
			String[] quantity = quantities[4].split("\\|");
			String date = quantities[7];
			String orderStoreId = quantities[0];

			if (orderStoreId.trim().equalsIgnoreCase(storeId)) {
				if (checkIfInLastMonth(date)) {
					for (String q : quantity) {
						totalNum += Integer.parseInt(q);
					}
				}
			}
		}
		System.out.println("Total number of sold items in last month is: " + totalNum);
	}

	public static void generateReportOfTotalSold(String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		double totalSale = 0;
		String orderStoreId = "0";
		for (String order: orders){
			String[] quantities = order.split(",");
			String date = quantities[7];
			orderStoreId = quantities[0];

			if (orderStoreId.trim().equalsIgnoreCase(storeId)){
				if (checkIfInLastMonth(date)) {
					totalSale += Double.parseDouble(quantities[6]);
				}
			}

		}
		System.out.println("Total Sale Made in dollars: ");
		System.out.println(totalSale);
	}

	public static void generateReportOfTypeOfCoffeeSoldMost(List<FoodItem> foodList, String storeId){
		List<String> orders = UserInterface.readFile("order.csv");
		Map<String, Integer> coffeeSold = new HashMap<>();
		for (String order: orders) {
			String[] quantities = order.split(",");
			String[] items = quantities[3].split("\\|");
			String[] quantity = quantities[4].split("\\|");
			String date = quantities[7];
			String orderStoreId = quantities[0];
			if (orderStoreId.trim().equalsIgnoreCase(storeId.trim())) {
				if (checkIfInLastMonth(date)) {
					int index = 0;
					for (String item : items) {
						boolean foodCheck = false;
						for (FoodItem foodItem : foodList) {
							if (foodItem.getFoodItemName().equals(item.strip()) && foodItem.getFoodType().equals("Coffee")) {
								foodCheck = true;
								break;
							}
						}
						if (foodCheck) {
							int currentQuantity = 0;
							for (Map.Entry<String, Integer> entry : coffeeSold.entrySet()) {
								if (entry.getKey().equals(item)) {
									currentQuantity += entry.getValue();
								}
							}
							int finalQuantity = currentQuantity + Integer.parseInt(quantity[index]);
							coffeeSold.put(item, finalQuantity);
						}
						index += 1;
					}
				}
			}
		}
		ArrayList<String> topItem = new ArrayList<>();
		int topQuantity = 0;
		for (Map.Entry<String, Integer> entry : coffeeSold.entrySet()){
			if (entry.getValue() > topQuantity){
				topQuantity = entry.getValue();
				topItem.clear();
				topItem.add(entry.getKey());
			}
			else if (entry.getValue() == topQuantity){
				topItem.add(entry.getKey());
			}
		}
		StringBuilder topItems = new StringBuilder();
		for (String s : topItem){
			topItems.append(s).append("\n");
		}
		System.out.println("Type of coffee sold the most per store in the last month: ");
		System.out.println(topItems);
		System.out.println("The total quantity of sold is:");
		System.out.println(topQuantity);
	}

	public static void generateReport(User currentUser, BakerySystem bakerySystem, Store store){
		int choice = chooseReport();
		if (choice == 1){
			Report reportOfLowInventory = new Report(LocalDate.now(), "items low in inventory",
					"inventory report", store);
			int lowInventory = setLowInventoryBar();
			UserInterface.displayReportTitle(reportOfLowInventory, store);
			generateReportOfLowInventory(store, bakerySystem.getFoodList(), lowInventory);
		} else if (choice == 2){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of item sold in last month",
					"business report", store);
			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfSoldFoodItem(store.getStoreId());
		} else if (choice == 3){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of coffee sold in last month",
					"business report", store);
			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfSoldCoffee(bakerySystem.getFoodList(), store.getStoreId());
		} else if (choice == 4){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(),
					"Number of coffee bean sold in last month",
					"business report", store);
			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfSoldCoffeeBean(store.getStoreId());
		} else if (choice == 5){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(),
					"Type of coffee sold the most per store in the last month",
					"business report", store);
			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfTypeOfCoffeeSoldMost(bakerySystem.getFoodList(),store.getStoreId());
		} else if (choice == 6){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(),
					"Days of the week that made the most sale in the last month per store",
					"business report", store);
			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfDaysMadeTheMostSold(store.getStoreId());
		} else if (choice == 7){
			Report reportOfSoldFoodItem = new Report(LocalDate.now(),
					"Total sale made in dollars in the last month per store",
					"business report", store);

			UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
			generateReportOfTotalSold(store.getStoreId());
		}
		previousOption(currentUser, bakerySystem, u -> generateReport(currentUser, bakerySystem, store));
	}

	public static String getWeek(String dates) {
		String strWeek = "";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = f.parse(dates);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		assert d != null;
		cal.setTime(d);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 0) {
			w = 7;
		}
		switch (w) {
			case 1 :
				strWeek = "Monday";
				break;
			case 2 :
				strWeek = "Tuesday";
				break;
			case 3 :
				strWeek = "Wednesday";
				break;
			case 4 :
				strWeek = "Thursday";
				break;
			case 5 :
				strWeek = "Friday";
				break;
			case 6 :
				strWeek = "Saturday";
				break;
			case 7 :
				strWeek = "Sunday";
				break;
			default :
				strWeek = "";
		};


		return strWeek;
	}
}
