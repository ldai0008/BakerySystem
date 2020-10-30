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

    /*

     */
    public void addOrderInDB(Order aOrder, Store currentStore) {
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
            out.write(currentStore.getStoreId() + "," + aOrder.getOrderId() + ","
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

    public double calTotalCost(Order aOrder) {
        double totalCost = 0;
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


    public void createNewAdvanceOrder(Store currentStore) {
        AdvanceOrder aOrder = new AdvanceOrder();
        int itemQuantity = 0;
        boolean quantityCheck = true;
        String option;
        FoodItem aFoodItem = new FoodItem();
        do {
            UserInterface.displayBakeShop();
            UserInterface.updateNewestInventory(currentStore);
            for (FoodItem foodItem : foodList) {
                if (foodItem.getFoodItemName().equals("roast coffee beans")) {
                    aFoodItem = foodItem;
                    break;
                }
            }
            aOrder.getQuantity().put(aFoodItem, itemQuantity);
            String itemNumber = aFoodItem.getItemNumber();
            displayCurrentItem(aOrder);
            System.out.println("            Total cost:" + aOrder.getTotalCost());

            do {
                if (!quantityCheck) {
                    System.out.println("!Error: The item quantity is not valid!");
                    System.out.println("The current quantity in inventory for this item is:");
                    System.out.println(getFoodItemQuantity(itemNumber, bakery, currentStore));
                    System.out.println(
                            "****************************************\n" + "-- Please try enter the item quantity again.");
                } else
                    System.out.println("--  Please enter the item's quantity:");
                Scanner console = new Scanner(System.in);
                String s = console.nextLine();
                if (s.length() == 0){
                    quantityCheck = false;
                    continue;
                }
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
                if (phoneNumber.length() == 0) {
                    System.out.println("!Error:The phone number cannot be blank");
                    isNumeric = false;
                } else if (!UserInterface.isNumeric(phoneNumber)) {
                    System.out.println("!Error:The phone number must only contains number");
                    isNumeric = false;
                }
                if (!isNumeric) {
                    System.out.println("***************************************");
                    System.out.println("Please enter the phone number of the customer again: ");
                    phoneNumber = console.nextLine();
                }
            } while (!isNumeric);
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
            System.out.println("Customer phone number: " + phoneNumber);
            System.out.println("****************************************");
            System.out.println("The order has been successfully created! ");
        }

    }

    public void createNewOrder(Store currentStore) {
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
            UserInterface.updateNewestInventory(currentStore);
            do {
                if (!nameCheck) {
                    System.out.println("!Error: The item name is not valid!");
                    System.out.println(
                            "****************************************\n" + "Please try enter the item name again.");
                } else
                    System.out.println("-- Please enter the item's name:");
                Scanner console = new Scanner(System.in);
                itemName = console.nextLine();
                ArrayList<FoodItem> foodItems = searchItems(itemName, currentStore);
                if (foodItems.size() == 0) {
                    nameCheck = false;
                    continue;
                }
                nameCheck = true;
                System.out.println("-- Please select the item you want:");
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
                    System.out.println(getFoodItemQuantity(itemNumber, bakery, currentStore));
                    System.out.println(
                            "****************************************\n" + "Please try enter the item quantity again.");
                } else
                    System.out.println("-- Please enter the item's quantity:");
                Scanner console = new Scanner(System.in);
                String s = console.nextLine();
                if (s.length() == 0){
                    System.out.println("!Error: The item quantity can not be empty!");
                    System.out.println(
                            "****************************************\n" + "Please try enter the item quantity again.");
                    quantityCheck = true;
                    continue;
                }
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
                    for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
                        aOrder.getQuantity().remove(f);
                        break;
                    }
                    aOrder.setTotalCost(calTotalCost(aOrder));
                    UserInterface.displayBakeShop();
                    displayCurrentItem(aOrder);
                }

            } while(option.equals("2"));

        } while (option.equals("1") || option.equals("2"));

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

    public String createOrderId(Order aOrder) {
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

    public void displayCurrentItem(Order aOrder) {
        System.out.println("Id    " + "Name               " + "Quantity " + "Cost");
        for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
            System.out.printf("%-6s", entry.getKey().getItemNumber());
            System.out.printf("%-19s", entry.getKey().getFoodItemName());
            System.out.printf("%-9s", entry.getValue());
            System.out.print(entry.getKey().getCurrentPrice());
            System.out.println();
        }
    }

    public String getDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public int getFoodItemQuantity(String itemNumber, Bakery bakery, Store currentStore) {
        for (Inventory inventory : currentStore.getListOfInventory()) {
            if (itemNumber.equals(inventory.getItemNumber())) {
                return inventory.getQuantity();
            }
        }
        return 0;
    }

    public ArrayList<FoodItem> searchItems(String s, Store currentStore) {
        ArrayList<FoodItem> items = new ArrayList<>();
        s = s.strip();
        for (FoodItem aFoodItem : foodList) {
            if (aFoodItem.getFoodItemName().strip().toLowerCase().contains(s)
                    && !aFoodItem.getFoodItemName().strip().equals("roast coffee beans")) {
                String itemNumber = aFoodItem.getItemNumber();
                for (Inventory inventory : currentStore.getListOfInventory()) {
                    if (itemNumber.equals(inventory.getItemNumber()) && inventory.getQuantity() > 0) {
                        items.add(aFoodItem);
                    }
                }
            }
        }
        return items;
    }

    public String selectItem(ArrayList<FoodItem> foodItems) {
        String selection;
        while (true) {

            int index = 1;
            for (FoodItem foodItem : foodItems) {
                System.out.println(index + ". " + foodItem.getFoodItemName());
                index += 1;
            }
            System.out.println(index + ". " + "Back to previous menu");
            Scanner console = new Scanner(System.in);
            selection = console.nextLine();
            selection = selection.strip();
            if (!UserInterface.isNumeric(selection) || Integer.parseInt(selection) > index + 1||
                    Integer.parseInt(selection) < 1 || selection.length() == 0) {
                System.out.println("!Error: Your selection is not valid!");
                System.out.println("****************************************\n" + "Please try selecting a option again.");
                continue;
            }
            return selection;
        }
    }

    public boolean updateInventoryInDB(Order aOrder, Store currentStore) {
        List<String> inventories = UserInterface.readFile("inventory.csv");
        ArrayList<String> storeIds = new ArrayList<>();
        ArrayList<String> itemIds = new ArrayList<>();
        ArrayList<String> itemQuantity = new ArrayList<>();
        ArrayList<String> itemAddedD = new ArrayList<>();

        for (String inventory:inventories){
            String[] column = inventory.split(",");
            storeIds.add(column[0]);
            itemIds.add(column[1]);
            itemQuantity.add(column[2]);
            itemAddedD.add(column[3]);
        }

        int a = 0;
        for (Map.Entry<FoodItem, Integer> entry : aOrder.getQuantity().entrySet()) {
            for (int index = 0; index < storeIds.size(); index++){
                if (itemIds.get(index).equals(entry.getKey().getItemNumber()) &&
                storeIds.get(index).equals(currentStore.getStoreId())){
                    int currentQuantity = Integer.parseInt(itemQuantity.get(index));
                    int changeQuantity = entry.getValue();
                    int finalQuantity = currentQuantity - changeQuantity;
                    if (finalQuantity < 0){
                        return false;
                    }
                    itemQuantity.set(index, "" + finalQuantity);
                    break;
                };
            }
        }
        BufferedWriter out = null;
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
            System.out.println(fileName + " not found");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    public boolean validateQuantityCheck(String itemNumber, String s, Store currentStore) {
        boolean check = UserInterface.isNumeric(s);
        if (!check) {
            return false;
        }
        if (Integer.parseInt(s) <= 0){
            return false;
        }
        for (Inventory inventory : currentStore.getListOfInventory()) {
            if (itemNumber.equals(inventory.getItemNumber())) {
                int currentNumber = inventory.getQuantity();
                return Integer.parseInt(s) <= currentNumber;
            }
        }
        return false;
    }

    public Store chooseStore() {
        System.out.println("-- Please enter the ID of store you want to check: ");
        Scanner sc = new Scanner(System.in);
        String storeChose = sc.nextLine();
        boolean isNumeric;
        do {
            isNumeric = true;
            if (storeChose.length() == 0) {
                System.out.println("!Error: The storeID cannot be blank");
                isNumeric = false;
            } else if (!UserInterface.isNumeric(storeChose)) {
                System.out.println("!Error: Invalid input");
                isNumeric = false;
            } else if (Integer.parseInt(storeChose) < 1 ||
                    Integer.parseInt(storeChose) > bakery.getListOfStore().size()) {
                System.out.println("!Error: Invalid store ID");
                isNumeric = false;
            }
            if (!isNumeric) {
                System.out.println("***************************************");
                System.out.println("-- Please enter the ID of store again: ");
                storeChose = sc.nextLine();
            }
        } while (!isNumeric);
        for (Store store : bakery.getListOfStore()) {
            if (storeChose.equals(store.getStoreId())) {
                return store;
            }
        }
        return new Store();
    }


    public void mainOption(User currentUser, BakerySystem bakerySystem) {
        ;
        boolean isContinue = true;
        Scanner console = new Scanner(System.in);
        String currentUserName = currentUser.getUserName();
        String currentUserType = currentUser.getUserType();
        Store currentStore = bakery.getListOfStore().get(0);
        boolean firstLogin = true;
        while (isContinue) {
            String selection = "";
            if (currentUserType.equals("Staff") || currentUserType.equals("Manager")) {
                UserInterface.displayHomeScreen(currentUserName, currentUserType, currentStore);
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
                    default:
                        System.out.println("!Error: Your selection is not valid or still in development!");
                        System.out.println(
                                "****************************************\n" + "Please select the correct option.");
                }
            }
            else if (currentUserType.equals("Owner")) {
                if (firstLogin) {
                    currentStore = chooseStore();
                    firstLogin = false;
                }
                UserInterface.displayHomeScreen(currentUserName, currentUserType, currentStore);
                selection = console.nextLine();
                switch (selection) {
                    case "1":
                        createNewOrder(currentStore);
                        break;
                    case "2":
                        createNewAdvanceOrder(currentStore);
                        break;
                    case "8":
                        generateReport(currentUser, currentStore);
                        break;
                    case "0":
                        isContinue = false;
                        break;
                    case "C":
                        currentStore = chooseStore();
                        break;
                    default:
                        System.out.println("!Error: Your selection is not valid or still in development!");
                        System.out.println(
                                "****************************************\n" + "Please select the correct option.");
                }
            }

        }

    }


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
            if (option.length() == 0){
                check = false;
            }
            else check = option.equals("1") || option.equals("2");
        }while (!check);
        if (option.equals("1")) {
            callback.accept(currentUser);
        }
    }


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
            } else if (!UserInterface.isNumeric(option)) {
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
            } else if (!UserInterface.isNumeric(barForLowInventory)) {
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

    public void generateReportOfDaysMadeTheMostSold(String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
        Map<String, Double> daysSold = originDays();
        for (String order : orders) {
            String[] quantities = order.split(",");
            String amount = quantities[6];
            String date = quantities[7];
            String orderStoreId = quantities[0];

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

    public void generateReportOfSoldCoffee(List<FoodItem> foodList, String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
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

    public void generateReportOfSoldCoffeeBean(String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
        int totalNum = 0;
        for (String order : orders) {
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

    public void generateReportOfSoldFoodItem(String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
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

    public void generateReportOfTotalSold(String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
        double totalSale = 0;
        String orderStoreId = "0";
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

    public void generateReportOfTypeOfCoffeeSoldMost(List<FoodItem> foodList, String storeId) {
        List<String> orders = UserInterface.readFile("order.csv");
        Map<String, Integer> coffeeSold = new HashMap<>();
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
                                    foodItem.getFoodType().equals("Coffee")) {
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

    public void generateReport(User currentUser, Store store) {
        int choice = chooseReport();
        UserInterface.updateNewestInventory(store);
        if (choice == 1) {
            Report reportOfLowInventory = new Report(LocalDate.now(), "items low in inventory",
                    "inventory report", store);
            int lowInventory = setLowInventoryBar();
            UserInterface.displayReportTitle(reportOfLowInventory, store);
            generateReportOfLowInventory(store, foodList, lowInventory);
        } else if (choice == 2) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of item sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldFoodItem(store.getStoreId());
        } else if (choice == 3) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of coffee sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldCoffee(foodList, store.getStoreId());
        } else if (choice == 4) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Number of coffee bean sold in last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldCoffeeBean(store.getStoreId());
        } else if (choice == 5) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Type of coffee sold the most per store in the last month",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfTypeOfCoffeeSoldMost(foodList, store.getStoreId());
        } else if (choice == 6) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Days of the week that made the most sale in the last month per store",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfDaysMadeTheMostSold(store.getStoreId());
        } else if (choice == 7) {
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Total sale made in dollars in the last month per store",
                    "business report", store);
            UserInterface.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfTotalSold(store.getStoreId());
        }  else{
            return;
        }

        previousOption(currentUser, u -> generateReport(currentUser, store));
    }

    public String getWeek(String dates) {
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
            case 1:
                strWeek = "Monday";
                break;
            case 2:
                strWeek = "Tuesday";
                break;
            case 3:
                strWeek = "Wednesday";
                break;
            case 4:
                strWeek = "Thursday";
                break;
            case 5:
                strWeek = "Friday";
                break;
            case 6:
                strWeek = "Saturday";
                break;
            case 7:
                strWeek = "Sunday";
                break;
            default:
                strWeek = "";
        }
        ;


        return strWeek;
    }

}
