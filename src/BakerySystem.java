import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

/**
 * @version: V1.0
 * @author: Team_04
 * @className: BakerySystem
 * @description: Controller Class
 * @data: 2020-11-1 9:00
 **/
public class BakerySystem {

    // bakery which contains all stores
    private Bakery bakery;
    // list of food items
    private List<FoodItem> foodList;

    /**
     * Constructor for objects of BakerySystem
     * Default constructor
     */
    public BakerySystem() {
        this.bakery = new Bakery();
        this.foodList = new ArrayList<>();
    }

    /**
     * Get Bakery
     * @return bakery
     */
    public Bakery getBakery() {
        return bakery;
    }

    /**
     * Set Bakery
     * @param bakery
     */
    public void setBakery(Bakery bakery) {
        this.bakery = bakery;
    }

    /**
     * get FoodList
     * @return foodList
     */
    public List<FoodItem> getFoodList() {
        return foodList;
    }

    /**
     * set FoodList
     * @param foodList
     */
    public void setFoodList(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    /**
     * Add the data of new order in database(CSV file)
     * @param aOrder
     *        The Order object of a order
     * @param currentStore
     *        The Store object of store generating the order
     */
    public void addOrderInDB(Order aOrder, Store currentStore) {
        BufferedWriter out = null;
        String fileName = "order.csv";
        try {
            ArrayList<String> itemName = new ArrayList<>();
            ArrayList<String> itemPrice = new ArrayList<>();
            ArrayList<String> itemQuantity = new ArrayList<>();
            // Storing the name of item, price of item and quantity of item in three Arraylist
            for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
                itemName.add(entry.getKey().getFoodItemName());
                itemPrice.add(String.valueOf(entry.getKey().getCurrentPrice()));
                itemQuantity.add(String.valueOf(entry.getValue()));
            }
            StringBuilder n = new StringBuilder();
            StringBuilder q = new StringBuilder();
            StringBuilder p = new StringBuilder();
            int length = itemName.size();
            // Generate the format of database, using "|" to split the information of different items
            for (int i = 0; i < length; i++) {
                if (i == length - 1) {
                    n.append(itemName.get(i));
                    q.append(itemQuantity.get(i));
                    p.append(itemPrice.get(i));
                } else {
                    n.append(itemName.get(i)).append("|");
                    q.append(itemQuantity.get(i)).append("|");
                    p.append(itemPrice.get(i)).append("|");
                }
            }
            // Writing the data in CSV format
            out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(currentStore.getStoreId() + "," + aOrder.getOrderId() + ","
                    + currentStore.getListOfUser().get(0).getUserId() + "," + n + "," + q + "," + p
                    + "," + aOrder.getTotalCost() + "," + aOrder.getOrderDate() + "," + aOrder.getOrderTime() + ","
                    + aOrder.getNameOfCustomer() + "," + aOrder.getOrderStatus() + "," + aOrder.getCustomerPhone() + ","
                    + aOrder.getLastModifiedBy() + "," + aOrder.getLastModifiedDate() + ","
                    + aOrder.getLastModifiedTime() + "\n");
            // close the file
            out.close();

        } catch (FileNotFoundException e) {
            System.out.println("Necessary file " + fileName + " not found");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Unexpected I/O exception occur");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        }
    }

    /**
     * Calculate the total price of a order
     * @param aOrder
     *        The Order object of new order
     * @return the current total cost of a order
     */
    public double calTotalCost(Order aOrder) {
        double totalCost = 0;
        // Plus the price of each item in a order
        for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
            for (FoodItem foodItem : foodList) {
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

    /**
     * Create a new advance order for roast coffee beans
     * @param currentStore
     *        The Store object of store generating the order
     */
    public void createNewAdvanceOrder(Store currentStore) {
        AdvanceOrder aOrder = new AdvanceOrder();
        int itemQuantity = 0;
        boolean quantityCheck = true;
        String option;
        FoodItem aFoodItem = new FoodItem();
        do {
            // Display the standard message
            UserInterface.displayBakeShop();
            // Update the inventory
            updateNewestInventory(currentStore);
            // Find the information of roast coffee beans
            for (FoodItem foodItem : foodList) {
                if (foodItem.getFoodItemName().equals("roast coffee beans")) {
                    aFoodItem = foodItem;
                    break;
                }
            }
            // Store the roast coffee beans coffee beans with the quantity
            aOrder.getQuantity().put(aFoodItem, itemQuantity);
            String itemNumber = aFoodItem.getItemNumber();
            // Display the information of current order
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());

            do {
                // Check if the quantity is valid
                if (!quantityCheck) {
                    System.out.println("!Error: The item quantity is not valid!");
                    System.out.println("The current quantity in inventory for this item is:");
                    System.out.println(getFoodItemQuantity(itemNumber, currentStore));
                    System.out.println(
                            "****************************************\n" + "-- Please try enter the item quantity again.");
                } else{
                    System.out.println("--  Please enter the item's quantity:");
                }
                Scanner console = new Scanner(System.in);
                String s = console.nextLine();
                // Check if the input of entity is blank
                if (s.length() == 0){
                    quantityCheck = false;
                    continue;
                }
                // Validate if the quantity of the item in inventory is enough
                quantityCheck = validateQuantityCheck(itemNumber, s, currentStore);
                if (quantityCheck) {
                    itemQuantity = Integer.parseInt(s);
                }
            } while (!quantityCheck);
            aOrder.getQuantity().put(aFoodItem, itemQuantity);
            aOrder.setTotalCost(calTotalCost(aOrder));
            UserInterface.displayBakeShop();
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());
            // Ask the user about the next step
            option = UserInterface.displayCreateAdvanceOrderOption();
        } while (option.equals("1")); // Check if the user wants to change the item number

        // If the user choose to confirm the order
        if (option.equals("2")) {
            Scanner console = new Scanner(System.in);
            System.out.println("--  Enter the name of the customer:");
            String name = console.nextLine();
            System.out.println("--  Enter the phone number of the customer:");
            String phoneNumber = console.nextLine();
            boolean isNumeric;
            do {
                isNumeric = true;
                // Check if the phone number is valid
                if (phoneNumber.length() == 0) {
                    System.out.println("!Error:The phone number cannot be blank");
                    isNumeric = false;
                } else if (!isNumeric(phoneNumber)) {
                    System.out.println("!Error:The phone number must only contains number");
                    isNumeric = false;
                }
                if (!isNumeric) {
                    System.out.println("***************************************");
                    System.out.println("Please enter the phone number of the customer again: ");
                    phoneNumber = console.nextLine();
                }
            } while (!isNumeric);
            // The order is confirmed, change the fields of the order
            String date = getDate();
            String time = getTime();
            aOrder.setOrderDate(date);
            aOrder.setOrderTime(time);
            aOrder.setLastModifiedBy(String
                    .valueOf(currentStore.getListOfUser().get(0).getUserId()));
            aOrder.setLastModifiedDate(date);
            aOrder.setLastModifiedTime(time);
            aOrder.setLastModifiedBy(String
                    .valueOf(currentStore.getListOfUser().get(0).getUserId()));
            aOrder.setOrderStatus("Confirmed");
            aOrder.setNameOfCustomer(name);
            aOrder.setCustomerPhone(phoneNumber);
            currentStore.getListOfOrder().add(aOrder);
            String orderId = createOrderId(aOrder);
            aOrder.setOrderId(orderId);
            // Add the order into order database
            addOrderInDB(aOrder, currentStore);
            // Update the inventory
            updateInventoryInDB(aOrder, currentStore);
            UserInterface.displayBakeShop();
            // Display the successful message
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());
            System.out.println("****************************************");
            System.out.println("Order id: " + orderId);
            System.out.println("Order date: " + date);
            System.out.println("Order time: " + time);
            System.out.println("Store id: " + currentStore.getStoreId());
            System.out.println("Employee id: "
                    + currentStore.getListOfUser().get(0).getUserId());
            System.out.println("Customer name: " + name);
            System.out.println("Customer phone number: " + phoneNumber);
            System.out.println("****************************************");
            System.out.println("The order has been successfully created! ");
        }

    }

    /**
     * Create the normal order
     * @param currentStore
     *        The Store object of store generating the order
     */
    public void createNewOrder(Store currentStore) {
        String itemName;
        int itemQuantity = 0;
        boolean nameCheck = true;
        boolean quantityCheck = true;
        Order aOrder = new Order();
        String option;
        FoodItem aFoodItem;
        do {
            // Display the standard message
            UserInterface.displayBakeShop();
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());
            // Update the current inventory
            updateNewestInventory(currentStore);
            do {
                if (!nameCheck) {
                    System.out.println("!Error: The item name is not valid!");
                    System.out.println(
                            "****************************************\n" + "Please try enter the item name again.");
                } else
                    System.out.println("-- Please enter the item's name:");
                Scanner console = new Scanner(System.in);
                itemName = console.nextLine();
                // search the item names contains the word user enter
                ArrayList<FoodItem> foodItems = searchItems(itemName, currentStore);
                if (foodItems.size() == 0) {
                    nameCheck = false;
                    continue;
                }
                nameCheck = true;
                System.out.println("-- Please select the item you want:");
                // According to the selection of user, return different item
                String selection = selectItem(foodItems);
                if (Integer.parseInt(selection) != (foodItems.size() + 1)) {
                    aFoodItem = foodItems.get(Integer.parseInt(selection) - 1);
                    break;
                }
            } while (true);
            String itemNumber = aFoodItem.getItemNumber();
            do {
                // Error message for invalid quantity
                if (!quantityCheck) {
                    System.out.println("!Error: The item quantity is not valid!");
                    System.out.println("The current quantity in inventory for this item is:");
                    System.out.println(getFoodItemQuantity(itemNumber, currentStore));
                    System.out.println(
                            "****************************************\n" + "Please try enter the item quantity again.");
                } else
                    System.out.println("-- Please enter the item's quantity:");
                Scanner console = new Scanner(System.in);
                String s = console.nextLine();
                // Check if the length of quantity is 0
                if (s.length() == 0){
                    System.out.println("!Error: The item quantity can not be empty!");
                    System.out.println(
                            "****************************************\n" + "Please try enter the item quantity again.");
                    quantityCheck = true;
                    continue;
                }
                // check if the quantity is enough
                quantityCheck = validateQuantityCheck(itemNumber, s, currentStore);
                if (quantityCheck) {
                    itemQuantity = Integer.parseInt(s);
                }
            } while (!quantityCheck);

            // store the current item with its quantity
            aOrder.getQuantity().put(aFoodItem, itemQuantity);
            aOrder.setTotalCost(calTotalCost(aOrder));
            UserInterface.displayBakeShop();
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());
            // remove the item in order the user select
            do{
                option = UserInterface.displayCreateOrderOption();
                if (option.equals("2")) {
                    System.out.println("-- Please select the item you want to cancel:");
                    ArrayList<FoodItem> foodItems = new ArrayList<>();
                    for (FoodItem f : aOrder.getQuantity().keySet()) {
                        foodItems.add(f);
                    }
                    String selection = selectItem(foodItems);
                    FoodItem f = new FoodItem();
                    if (Integer.parseInt(selection) != (foodItems.size() + 1)) {
                        f = foodItems.get(Integer.parseInt(selection) - 1);
                    }
                    // Delete the item in order
                    for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
                        aOrder.getQuantity().remove(f);
                        break;
                    }
                    aOrder.setTotalCost(calTotalCost(aOrder));
                    UserInterface.displayBakeShop();
                    displayCurrentItem(aOrder);
                }

            } while(option.equals("2"));

        } while (option.equals("1"));

        // user confirms the order
        if (option.equals("3")) {
            Scanner console = new Scanner(System.in);
            System.out.println("-- Enter the name of the customer:");
            String name = console.nextLine();
            String date = getDate();
            String time = getTime();
            aOrder.setOrderDate(date);
            aOrder.setOrderTime(time);
            aOrder.setLastModifiedBy(String
                    .valueOf(currentStore.getListOfUser().get(0).getUserId()));
            aOrder.setLastModifiedDate(date);
            aOrder.setLastModifiedTime(time);
            aOrder.setLastModifiedBy(String
                    .valueOf(currentStore.getListOfUser().get(0).getUserId()));
            aOrder.setOrderStatus("Confirmed");
            aOrder.setNameOfCustomer(name);
            currentStore.getListOfOrder().add(aOrder);
            String orderId = createOrderId(aOrder);
            aOrder.setOrderId(orderId);
            addOrderInDB(aOrder, currentStore);
            updateInventoryInDB(aOrder, currentStore);
            UserInterface.displayBakeShop();
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());
            System.out.println("****************************************");
            System.out.println("Order id: " + orderId);
            System.out.println("Order date: " + date);
            System.out.println("Order time: " + time);
            System.out.println("Store id: " + currentStore.getStoreId());
            System.out.println("Employee id: "
                    + currentStore.getListOfUser().get(0).getUserId());
            System.out.println("Customer name: " + name);
            System.out.println("****************************************");
            System.out.println("The order has been successfully created! ");
        }
    }

    /**
     * generate the order id using the format "xxxxxx-mmyyyy"
     * @param aOrder
     *        The order recently created which needs a new order id
     * @return
     *        a string represent the order ID
     */
    public String createOrderId(Order aOrder) {
        // read the CSV file
        List<String> orders = readFile("order.csv");
        int biggest = 0;
        // get the biggest number in database
        for (String order : orders) {
            String[] o = order.split(",");
            String orderID = o[1];
            String[] subOrderId = orderID.split("-");
            int idNumber = Integer.parseInt(subOrderId[0]);
            if (idNumber > biggest)
                biggest = idNumber;
        }
        // generate current order id
        int currentIdNumber = biggest + 1;
        String str = String.format("%06d", currentIdNumber);
        String year = aOrder.getOrderDate().split("-")[0];
        String month = aOrder.getOrderDate().split("-")[1];
        return str + "-" + month + year;
    }

    /**
     * Display the message of current item in a order
     * @param aOrder
     *        a Order object
     */
    public void displayCurrentItem(Order aOrder) {
        System.out.println("Id    " + "Name               " + "Quantity " + "Cost");
        // ensure the output is in same format
        for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
            System.out.printf("%-6s", entry.getKey().getItemNumber());
            System.out.printf("%-19s", entry.getKey().getFoodItemName());
            System.out.printf("%-9s", entry.getValue());
            System.out.print(entry.getKey().getCurrentPrice());
            System.out.println();
        }
    }

    /**
     * get the current date
     * @return
     *        a string represents the current date
     */
    public String getDate(){
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sf.format(date);
        return dateString;
    }

    /**
     * get the current time
     * @return
     *        a string represents the current time
     */
    public String getTime(){
        Date time = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        String timeString = sf.format(time);
        return timeString;
    }

    /**
     * Return the quantity of a food item in current store
     * @param itemNumber
     *        the ID of a item
     * @param currentStore
     *        the store is creating a new order
     * @return
     *        the quantity of a item in a particular store's inventory
     */
    public int getFoodItemQuantity(String itemNumber, Store currentStore) {
        for (Inventory inventory : currentStore.getListOfInventory()) {
            if (itemNumber.equals(inventory.getItemNumber())) {
                return inventory.getQuantity();
            }
        }
        return 0;
    }

    /**
     * Read the data for a csv file, and creating FoodItem object for each row in the file
     */
    public void initializeFoodItem() {
        List<String> foodItems = readFile("foodItem.csv");
        for (String foodItem : foodItems) {
            String[] f = foodItem.split(",");
            if (!f[2].strip().equals("Material")) {
                FoodItem aFoodItem = new FoodItem();
                aFoodItem.setItemNumber(f[0]);
                aFoodItem.setFoodItemName(f[1]);
                aFoodItem.setFoodType(f[2]);
                aFoodItem.setCurrentPrice(Double.parseDouble(f[3]));
                foodList.add(aFoodItem);
            }
        }
    }

    /**
     * Check if a string is a correct number
     * @param s
     *        a string
     * @return
     *        the result of checking
     */
    public boolean isNumeric(String s) {
        for (int j = 0; j < s.length(); j++) {
            if (!(s.charAt(j) >= 48 && s.charAt(j) <= 57)) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method to read a file
     * @param fileName
     *        the name of the file
     * @return
     *        a string list contains the full content of the file, each element is one line in the file
     */
    public static List<String> readFile(String fileName) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            try (FileReader inputFile = new FileReader(fileName)) {
                Scanner parser = new Scanner(inputFile);
                parser.nextLine();
                while (parser.hasNextLine()) {
                    String line = parser.nextLine();
                    if (line.isEmpty())
                        continue;
                    strings.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Necessary file" + fileName + " not found");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Unexpected I/O exception occur");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        }
        return strings;
    }

    /**
     * Search the item whose name contains the input string
     * @param s
     *        a string supposed to be part of the item name
     * @param currentStore
     *        THe current store in operation
     * @return
     *        an ArrayList contains all food item whose name contains the string input
     */
    public ArrayList<FoodItem> searchItems(String s, Store currentStore) {
        ArrayList<FoodItem> items = new ArrayList<>();
        s = s.strip();
        // find the item name containing the string from the food list
        for (FoodItem aFoodItem : foodList) {
            // the roast coffee beans cannot shown in normal order, it is just for advance order
            if (aFoodItem.getFoodItemName().strip().toLowerCase().contains(s)
                    && !aFoodItem.getFoodItemName().strip().equals("roast coffee beans")) {
                String itemNumber = aFoodItem.getItemNumber();
                // double check, make sure the item is in both the food list and inventory
                for (Inventory inventory : currentStore.getListOfInventory()) {
                    if (itemNumber.equals(inventory.getItemNumber()) && inventory.getQuantity() > 0) {
                        items.add(aFoodItem);
                    }
                }
            }
        }
        return items;
    }

    /**
     * Ask the user to select the item from the
     * @param foodItems
     *        A arraylist contains all items need to be selected
     * @return
     *        a string represents the option the user selects
     */
    public String selectItem(ArrayList<FoodItem> foodItems) {
        String selection;
        while (true) {

            int index = 1;
            // print out the message, each line for each item
            for (FoodItem foodItem : foodItems) {
                System.out.println(index + ". " + foodItem.getFoodItemName());
                index += 1;
            }
            // last line for the back to previous menu option
            System.out.println(index + ". " + "Back to previous menu");
            Scanner console = new Scanner(System.in);
            selection = console.nextLine();
            selection = selection.strip();
            // validate the selection
            if (!isNumeric(selection) || Integer.parseInt(selection) > index + 1||
                    Integer.parseInt(selection) < 1 || selection.length() == 0) {
                System.out.println("!Error: Your selection is not valid!");
                System.out.println("****************************************\n" + "Please try selecting a option again.");
                continue;
            }
            return selection;
        }
    }

    /**
     * update the inventory object using the newest dataset
     * @param currentStore
     *        the store is operated now
     */
    public void updateNewestInventory(Store currentStore) {
        List<String> inventories = readFile("inventory.csv");
        // clear all inventory object
        currentStore.getListOfInventory().clear();
        for (String inventory : inventories) {
            String[] i = inventory.split(",");
            // create new inventory object
            if (i[0].equals(currentStore.getStoreId())) {
                Inventory aInventory = new Inventory(i[1], Integer.parseInt(i[2]), i[3]);
                currentStore.getListOfInventory().add(aInventory);
            }
        }
    }

    /**
     * Update the inventory objects into database
     * @param aOrder
     *        a Order object
     * @param currentStore
     *        a Store object represents the current operated store
     */
    public void updateInventoryInDB(Order aOrder, Store currentStore) {
        List<String> inventories = readFile("inventory.csv");
        // four arrayList storing four column
        ArrayList<String> storeIds = new ArrayList<>();
        ArrayList<String> itemIds = new ArrayList<>();
        ArrayList<String> itemQuantity = new ArrayList<>();
        ArrayList<String> itemAddedD = new ArrayList<>();

        // add the data into arrayList
        for (String inventory:inventories){
            String[] column = inventory.split(",");
            storeIds.add(column[0]);
            itemIds.add(column[1]);
            itemQuantity.add(column[2]);
            itemAddedD.add(column[3]);
        }

        // change the old quantity of items using new quantity
        for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
            for (int index = 0; index < storeIds.size(); index++){
                if (itemIds.get(index).equals(entry.getKey().getItemNumber()) &&
                storeIds.get(index).equals(currentStore.getStoreId())){
                    int currentQuantity = Integer.parseInt(itemQuantity.get(index));
                    int changeQuantity = entry.getValue();
                    int finalQuantity = currentQuantity - changeQuantity;
                    if (finalQuantity < 0){
                        return;
                    }
                    itemQuantity.set(index, "" + finalQuantity);
                    break;
                };
            }
        }

        //write into file
        BufferedWriter out;
        String fileName = "Inventory.csv";
        try {
        out = new BufferedWriter(new FileWriter(fileName));
        StringBuilder s = new StringBuilder();
        s.append("store id,item id,quantity,date added\n");
        for (int index = 0; index < storeIds.size(); index++){
            s.append(storeIds.get(index)).append(",");
            s.append(itemIds.get(index)).append(",");
            s.append(itemQuantity.get(index)).append(",");
            s.append(itemAddedD.get(index)).append("\n");
        }
        out.write(String.valueOf(s));
        out.close();

        } catch (FileNotFoundException e) {
            System.out.println("Necessary file" + fileName + " not found");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Unexpected I/O exception occur");
            System.out.println("Please check the data file, then restart the program");
            System.exit(1);
        }
    }

    /**
     * Check if the quantity of items is enough in one store
     * when a new order is creating
     * @param itemNumber
     *        the ID of item need to be checked
     * @param s
     *        a string type number represents the quantity of item needs to be removed form inventory
     * @param currentStore
     *        a Store object which is operated
     * @return
     *        a boolean type represents if the quantity input is valid
     */
    public boolean validateQuantityCheck(String itemNumber, String s, Store currentStore) {
        // first check if the string is a number
        boolean check = isNumeric(s);
        if (!check) {
            return false;
        }
        // next ensure it is positive
        if (Integer.parseInt(s) <= 0){
            return false;
        }
        // check if the item is enough in inventory
        for (Inventory inventory : currentStore.getListOfInventory()) {
            if (itemNumber.equals(inventory.getItemNumber())) {
                int currentNumber = inventory.getQuantity();
                return Integer.parseInt(s) <= currentNumber;
            }
        }
        return false;
    }

    /**
     * Ask the owner to choose a store he/she wants to manage
     * @return
     *        a Store object represents the owner's choice
     */
    public Store chooseStore() {
        // ask the owner input a id
        System.out.println("-- Please enter the ID of store you want to check: ");
        Scanner sc = new Scanner(System.in);
        String storeChose = sc.nextLine();
        // check if this id is valid
        boolean isNumeric;
        do {
            isNumeric = true;
            if (storeChose.length() == 0) {
                System.out.println("!Error: The storeID cannot be blank");
                isNumeric = false;
            } else if (!isNumeric(storeChose)) {
                System.out.println("!Error: Invalid input");
                isNumeric = false;
            } else if (Integer.parseInt(storeChose) < 1 ||
                    Integer.parseInt(storeChose) > bakery.getListOfStore().size()) {
                System.out.println("!Error: Invalid store ID");
                isNumeric = false;
            }
            // if invalid, ask the owner to enter again
            if (!isNumeric) {
                System.out.println("***************************************");
                System.out.println("-- Please enter the ID of store again: ");
                storeChose = sc.nextLine();
            }
        } while (!isNumeric);
        // get the Store object
        for (Store store : bakery.getListOfStore()) {
            if (storeChose.equals(store.getStoreId())) {
                return store;
            }
        }
        return new Store();
    }


    /**
     * The main menu of the program, according to different type of user, the menu can be different
     * @param currentUser
     *        User object represent the current user of the program
     *
     */
    public void mainOption(User currentUser) {
        // get the choice of the user
        boolean isContinue = true;
        Scanner console = new Scanner(System.in);
        String currentUserName = currentUser.getUserName();
        String currentUserType = currentUser.getUserType();
        Store currentStore = bakery.getListOfStore().get(0);
        boolean firstLogin = true;
        while (isContinue) {
            String selection = "";
            // for staff and Manager
            if (currentUserType.equals("Staff") || currentUserType.equals("Manager")) {
                UserInterface.displayHomeScreen(currentUserName, currentUserType, currentStore.getStoreId());
                currentStore = bakery.getListOfStore().get(0);
                selection = console.nextLine();
                switch (selection) {
                    case "1":
                        createNewOrder(currentStore);
                        break;
                    case "2":
                        createNewAdvanceOrder(currentStore);
                        break;
                    case "0":
                        isContinue = false;
                    // if the user choose other option
                    default:
                        System.out.println("!Error: Your selection is not valid or still in development!");
                        System.out.println(
                                "****************************************\n" + "Please select the correct option.");
                }
            }
            // for the owner, only the first time when logging needs to choose a store
            else if (currentUserType.equals("Owner")) {
                if (firstLogin) {
                    currentStore = chooseStore();
                    firstLogin = false;
                }
                UserInterface.displayHomeScreen(currentUserName, currentUserType, currentStore.getStoreId());
                selection = console.nextLine();
                switch (selection) {
                    case "1" -> createNewOrder(currentStore);
                    case "2" -> createNewAdvanceOrder(currentStore);
                    case "8" -> generateReport(currentUser, currentStore);
                    case "0" -> isContinue = false;
                    case "C" -> currentStore = chooseStore();
                    // if the user choose invalid option
                    default -> {
                        System.out.println("!Error: Your selection is not valid or still in development!");
                        System.out.println(
                                "****************************************\n" + "Please select the correct option.");
                    }
                }
            }

        }

    }

    /**
     * This function is used to call back to previous Option
     * @param currentUser
     *        User object represent the current user of the program
     * @param callback
     *        callback function
     */
    public void previousOption(User currentUser, Consumer<User> callback) {
        System.out.println("");
        System.out.println("-- Please enter your choice: ");
        System.out.println("1. Back to previous menu");
        System.out.println("2. Back to home screen");
        boolean check = true;
        String option;
        do {
            if (!check){
                System.out.println("!Error: invalid input");
                System.out.println("***************************************");
                System.out.println("-- Please enter the correct choice again: ");
                System.out.println("1. Back to previous menu");
                System.out.println("2. Back to home screen");
            }
            Scanner sc = new Scanner(System.in);
            option = sc.nextLine();
            // enter validation
            if (option.length() == 0){
                check = false;
            }
            else check = option.equals("1") || option.equals("2");
        }while (!check);
        if (option.equals("1")) {
            callback.accept(currentUser);
        }
    }

    /**
     * This method is used to check if the date is in the last month from now.
     * @param dates
     *        date inserted
     * @return boolean
     *         if it is in the last month, return true; if not, return false.
     */
    public boolean checkIfInLastMonth(String dates) {
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

    /**
     * This method is used for user to choose report they want to check
     * @return a integer of the report
     */
    public int chooseReport() {
        UserInterface.displayTrackBusinessOption();
        Scanner sc = new Scanner(System.in);
        String option = sc.nextLine();
        boolean isNumeric;
        do {
            isNumeric = true;
            if (option.length() == 0) {
                System.out.println("!Error: The option cannot be blank");
                isNumeric = false;
            } else if (!isNumeric(option)) {
                System.out.println("!Error: Please enter a number to select a option");
                isNumeric = false;
            } else if (Integer.parseInt(option) > 8 || Integer.parseInt(option) < 1) {
                System.out.println("!Error: Please enter a number to select a option");
                isNumeric = false;
            }
            if (!isNumeric) {
                System.out.println("***************************************");
                System.out.println("-- Please enter the option again: ");
                option = sc.nextLine();
            }
        } while (!isNumeric);
        return Integer.parseInt(option);
    }

    /**
     * This method is used to define a map, which the key is the every day of the week,
     * and the value is the sale of the day
     * @return a map interface
     */
    public Map<String, Double> originDays() {
        Map<String, Double> daysSold = new HashMap<>();
        daysSold.put("Monday", 0.0);
        daysSold.put("Tuesday", 0.0);
        daysSold.put("Wednesday", 0.0);
        daysSold.put("Thursday", 0.0);
        daysSold.put("Friday", 0.0);
        daysSold.put("Saturday", 0.0);
        daysSold.put("Sunday", 0.0);
        return daysSold;
    }

    /**
     * This method is used to validate user
     * @param account
     *        account is the userId or userEmail inserted by the user
     * @param password
     *        the password entered by the user
     * @return a boolean
     *         if the user information is right, return true; if not, return false
     */
    public boolean validateUser(String account, String password) {
        List<String> users = readFile("user.csv");
        for (String user : users) {
            String[] u = user.split(",");
            // u[0] is the employee ID, u[2] is the employ Email, u[3] is the password
            if ((u[0].toLowerCase().equals(account.toLowerCase()) || u[2].equals(account))
                    && u[3].equals(password)) {
                int userId = Integer.parseInt(u[0]);
                // create the object of the user based on the user information
                User aUser = new User(userId, u[1], u[2], u[3], u[4], u[5], u[6], u[7], u[8]);
                List<String> stores = readFile("store.csv");
                ArrayList<Store> storeList = new ArrayList<>();
                bakery.setListOfStore(storeList);
                for (String store : stores) {
                    String[] s = store.split(",");
                    String[] storeIds = u[9].split("\\|");
                    for (String storeId : storeIds) {
                        // s[0] is the store ID
                        if (s[0].equals(storeId)) {
                            // create the object of the store which the user belongs to
                            Store aStore = new Store();
                            aStore.setStoreId(s[0]);
                            aStore.setStoreAddress(s[1]);
                            aStore.setStoreContactNumber(s[2]);
                            ArrayList<User> userList = new ArrayList<>();
                            userList.add(aUser);
                            aStore.setListOfUser(userList);
                            List<String> inventory = readFile("inventory.csv");
                            // create the object of all inventory of the current store
                            ArrayList<Inventory> inventoryList = new ArrayList<>();
                            for (String item : inventory) {
                                String[] i = item.split(",");
                                Inventory anItem = new Inventory();
                                if (i[0].equals(s[0])) {
                                    anItem.setItemNumber(i[1]);
                                    anItem.setQuantity(Integer.parseInt(i[2]));
                                    anItem.setDateAdded(i[3]);
                                    inventoryList.add(anItem);
                                }
                            }
                            aStore.setListOfInventory(inventoryList);
                            storeList.add(aStore);
                        }
                    }
                }
                initializeFoodItem();
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to let the user to set the bar of low inventory
     * @return a integer of the low inventory bar
     */
    public int setLowInventoryBar() {
        System.out.println("-- Please enter the number to set the bar for low inventory items: ");
        Scanner sc = new Scanner(System.in);
        String barForLowInventory = sc.nextLine();
        boolean isNumeric;
        do {
            isNumeric = true;
            if (barForLowInventory.length() == 0) {
                System.out.println("!Error: The standard of low inventory cannot be blank");
                isNumeric = false;
            } else if (!isNumeric(barForLowInventory)) {
                System.out.println("!Error: The standard should be a positive number");
                isNumeric = false;
            }
            if (!isNumeric) {
                System.out.println("***************************************");
                System.out.println("-- Please enter the quantity again: ");
                barForLowInventory = sc.nextLine();
            } else {
                System.out.println("You set the bar for low inventory at " + barForLowInventory);
            }
        } while (!isNumeric);
        return Integer.parseInt(barForLowInventory);
    }

    /**
     * This method is used to generate the report of days made the most sold in the last month
     * @param storeId
     *        the Id of the store which need to generate the report
     */
    public void generateReportOfDaysMadeTheMostSold(String storeId) {
        List<String> orders = readFile("order.csv");
        Map<String, Double> daysSold = originDays();
        for (String order : orders) {
            String[] quantities = order.split(",");
            String amount = quantities[6];
            String date = quantities[7];
            String orderStoreId = quantities[0];

            // calculate the sale of the everyday in the week
            if (storeId.strip().equalsIgnoreCase(orderStoreId)) {
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
        // find the day that has the most sale
        ArrayList<String> topDays = new ArrayList<>();
        double topTotal = 0;
        for (Map.Entry<String, Double> entry : daysSold.entrySet()) {
            if (entry.getValue() > topTotal) {
                topTotal = entry.getValue();
                topDays.clear();
                topDays.add(entry.getKey());
            } else if (entry.getValue() == topTotal) {
                topDays.add(entry.getKey());
            }
        }
        StringBuilder topDay = new StringBuilder();
        for (String s : topDays) {
            topDay.append(s).append("\n");
        }

        System.out.println("Days of the week that made the most sale in the last month is: ");
        System.out.println(topDay);
        System.out.println("The total revenue on that days of week is: ");
        System.out.println(topTotal);
    }

    /**
     * This method is used to generate report of the food items that are low in the inventory
     * @param store the store which need generate report
     * @param foodList list of the food items
     * @param lowInventory the bar of low inventory
     */
    public void generateReportOfLowInventory(Store store, List<FoodItem> foodList, int lowInventory) {
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

    /**
     * This method is used to generate the number of the coffee sold in the last month
     * @param foodList list of the food items
     * @param storeId the Id of the store which need generate report
     */
    public void generateReportOfSoldCoffee(List<FoodItem> foodList, String storeId) {
        List<String> orders = readFile("order.csv");
        int totalNum = 0;
        for (String order : orders) {
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
            String orderStoreId = quantities[0];

            if (orderStoreId.strip().equalsIgnoreCase(storeId.strip())) {
                if (checkIfInLastMonth(date)) {
                    int index = 0;
                    for (String item : items) {
                        boolean foodCheck = false;
                        for (FoodItem foodItem : foodList) {
                            if (foodItem.getFoodItemName().strip().equals(item.strip()) &&
                                    foodItem.getFoodType().strip().equals("Coffee")) {
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

    /**
     * the method is used to generate the number of coffee bean sold in last month
     * @param storeId the Id of the store which need generate report
     */
    public void generateReportOfSoldCoffeeBean(String storeId) {
        List<String> orders = readFile("order.csv");
        int totalNum = 0;
        for (String order : orders) {
            // split the string using |
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
            String orderStoreId = quantities[0];
            if (storeId.strip().equalsIgnoreCase(orderStoreId)) {
                if (checkIfInLastMonth(date)) {
                    int index = 0;
                    for (String item : items) {
                        boolean foodCheck = false;
                        if (item.strip().equals("roast coffee beans")) {
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

    /**
     * This method is used to generate report that shows the number of food items sold in the last month
     * @param storeId the Id of the store which need generate report
     */
    public void generateReportOfSoldFoodItem(String storeId) {
        List<String> orders = readFile("order.csv");
        int totalNum = 0;
        for (String order : orders) {
            String[] quantities = order.split(",");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
            String orderStoreId = quantities[0];

            if (orderStoreId.strip().equalsIgnoreCase(storeId)) {
                if (checkIfInLastMonth(date)) {
                    for (String q : quantity) {
                        totalNum += Integer.parseInt(q);
                    }
                }
            }
        }
        System.out.println("Total number of sold items in last month is: " + totalNum);
    }

    /**
     * This method is used to generate report of the total sale of last month
     * @param storeId the Id of the store which need generate report
     */
    public void generateReportOfTotalSold(String storeId) {
        List<String> orders = readFile("order.csv");
        double totalSale = 0;
        String orderStoreId;
        for (String order : orders) {
            String[] quantities = order.split(",");
            String date = quantities[7];
            orderStoreId = quantities[0];

            if (orderStoreId.strip().equalsIgnoreCase(storeId)) {
                if (checkIfInLastMonth(date)) {
                    totalSale += Double.parseDouble(quantities[6]);
                }
            }

        }
        System.out.println("Total Sale Made in dollars: ");
        System.out.println(totalSale);
    }

    /**
     * The method is used to generate the report,
     * which shows the type of coffee sold most in the last month
     * @param foodList list of food items
     * @param storeId the Id of the store which need generate report
     */
    public void generateReportOfTypeOfCoffeeSoldMost(List<FoodItem> foodList, String storeId) {
        List<String> orders = readFile("order.csv");
        Map<String, Integer> coffeeSold = new HashMap<>();
        for (String order : orders) {
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
            String orderStoreId = quantities[0];
            // find the store
            if (orderStoreId.strip().equalsIgnoreCase(storeId.strip())) {
                // check if is in last month
                if (checkIfInLastMonth(date)) {
                    int index = 0;
                    // find the items whose type is Coffee
                    for (String item : items) {
                        boolean foodCheck = false;
                        for (FoodItem foodItem : foodList) {
                            if (foodItem.getFoodItemName().strip().equals(item.strip()) &&
                                    foodItem.getFoodType().equals("Coffee")) {
                                foodCheck = true;
                                break;
                            }
                        }
                        // store the item with quantity sold
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
        // find the top coffee
        for (Map.Entry<String, Integer> entry : coffeeSold.entrySet()) {
            if (entry.getValue() > topQuantity) {
                topQuantity = entry.getValue();
                topItem.clear();
                topItem.add(entry.getKey());
            } else if (entry.getValue() == topQuantity) {
                topItem.add(entry.getKey());
            }
        }
        StringBuilder topItems = new StringBuilder();
        for (String s : topItem) {
            topItems.append(s).append("\n");
        }
        System.out.println("Type of coffee sold the most per store in the last month: ");
        System.out.println(topItems);
        System.out.println("The total quantity of sold is:");
        System.out.println(topQuantity);
    }

    /**
     * This method is used to generate right report based on the chose of the user
     * @param currentUser User object represent the current user of the program
     * @param store the store which need generate report
     */
    public void generateReport(User currentUser, Store store) {
        int choice = chooseReport();
        updateNewestInventory(store);
        // According to different option ,generate different report
        if (choice == 1) {
            Report reportOfLowInventory = new Report(LocalDate.now(), "items low in inventory",
                    "inventory report", store);
            int lowInventory = setLowInventoryBar();
            UserInterface.displayReportTitle(reportOfLowInventory.getDateOfReport(),
                    reportOfLowInventory.getNameOfReport(), reportOfLowInventory.getTypeOfReport(), store.getStoreId());
            generateReportOfLowInventory(store, foodList, lowInventory);
        } else if (choice == 2) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of item sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(),
                    reportOfSoldFoodItem.getNameOfReport(), reportOfSoldFoodItem.getTypeOfReport(), store.getStoreId());
            generateReportOfSoldFoodItem(store.getStoreId());
        } else if (choice == 3) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of coffee sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(),
                    reportOfSoldFoodItem.getNameOfReport(), reportOfSoldFoodItem.getTypeOfReport(), store.getStoreId());
            generateReportOfSoldCoffee(foodList, store.getStoreId());
        } else if (choice == 4) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Number of coffee bean sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(),
                    reportOfSoldFoodItem.getNameOfReport(), reportOfSoldFoodItem.getTypeOfReport(),store.getStoreId());
            generateReportOfSoldCoffeeBean(store.getStoreId());
        } else if (choice == 5) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Type of coffee sold the most per store in the last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(), reportOfSoldFoodItem.getNameOfReport()
                    ,reportOfSoldFoodItem.getTypeOfReport(),store.getStoreId());
            generateReportOfTypeOfCoffeeSoldMost(foodList, store.getStoreId());
        } else if (choice == 6) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Days of the week that made the most sale in the last month per store",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(),
                    reportOfSoldFoodItem.getNameOfReport(), reportOfSoldFoodItem.getTypeOfReport(),store.getStoreId());
            generateReportOfDaysMadeTheMostSold(store.getStoreId());
        } else if (choice == 7) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Total sale made in dollars in the last month per store",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem.getDateOfReport(),reportOfSoldFoodItem.getNameOfReport(),
                    reportOfSoldFoodItem.getTypeOfReport(), store.getStoreId());
            generateReportOfTotalSold(store.getStoreId());
        }  else{
            return;
        }

        previousOption(currentUser, u -> generateReport(currentUser, store));
    }

    /**
     * This method is used to get the week based on the date
     * @param dates the date
     * @return the day of week
     */
    public String getWeek(String dates) {
        String strWeek;

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
        strWeek = switch (w) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "";
        };
        return strWeek;
    }

}
