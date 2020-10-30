import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    public static void displayBakeShop() {
        System.out.println("=======================================");
        System.out.println("|                                     |");
        System.out.println("|               Bake Shop             |");
        System.out.println("|                                     |");
        System.out.println("=======================================");
    }

    public static String displayCreateAdvanceOrderOption() {
        String option;
        boolean optionCheck = true;
        do {
            if (!optionCheck) {
                System.out.println("!Error: Your selection is not valid!");
            }
            System.out.println("****************************************\n"
                    + "-- Please select one option by entering the number:\n" +
                    "1. Change the item quantity\n" + "2. Confirm\n" + "3. Back to home screen");
            Scanner console = new Scanner(System.in);
            option = console.nextLine();
            optionCheck = option.equals("1") || option.equals("2") || option.equals("3");
        } while (!optionCheck);
        return option;
    }

    public static String displayCreateOrderOption() {
        String option;
        boolean optionCheck = true;
        do {
            if (!optionCheck) {
                System.out.println("!Error: Your selection is not valid!");
            }
            System.out.println("****************************************\n"
                    + "-- Please select one option by entering the number:\n" + "1. Continue entering items\n"
                    + "2. Cancel items\n" + "3. Confirm\n" + "4. Back to home screen");
            Scanner console = new Scanner(System.in);
            option = console.nextLine();
            optionCheck = option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4");
        } while (!optionCheck);
        return option;
    }

    public static void displayHomeScreen(String userName, String userType, Store currentStore) {
        displayBakeShop();
        System.out.println("    *****Welcome, " + userName + "(" + userType + ")*****");
        System.out.println("        You are in store *" + currentStore.getStoreId() + "*");
        switch (userType) {
            case "Staff":
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("1. Create new order");
                System.out.println("2. Create new special order for roast coffee bean");
                System.out.println("3. Manage order");
                System.out.println("4. Manage inventory");
                System.out.println("5. View my profile");
                System.out.println("0. Logout");
                break;
            case "Manager":
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("1. Create new order");
                System.out.println("2. Create new special order for roast coffee bean");
                System.out.println("3. Manage order");
                System.out.println("4. Manage inventory");
                System.out.println("5. Manage all advance order");
                System.out.println("6. View my profile");
                System.out.println("0. Logout");
                break;
            case "Owner":
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("1. Create new order");
                System.out.println("2. Create new special order for roast coffee bean");
                System.out.println("3. Manage order");
                System.out.println("4. Manage inventory");
                System.out.println("5. Manage all advance order");
                System.out.println("6. Manage my stores");
                System.out.println("7. Manage employee");
                System.out.println("8. Track my business");
                System.out.println("9. View my profile");
                System.out.println("0. Logout");
                System.out.println("C. Change current Store");
                break;
            default:
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("!Error: User Type is not valid!");
                System.out.println("0. Logout");
        }

    }

    public static void displayLoginError() {
        System.out.println("!Error: User is not valid!");
        System.out.println("***************************************\n"
                + "Please try login again.or contact the owner to reset the password.");
    }

    public static void displayTrackBusinessOption() {
        displayBakeShop();
        System.out.println("-- Please select one option by entering the number:");
        System.out.println("1. Print items low in inventory");
        System.out.println("2. Print total number of items sold in last month in each store");
        System.out.println("3. Print total number of coffee sold in last month in each store");
        System.out.println("4. Total number of coffee beans (in quantity) sold in last month in each store");
        System.out.println("5. Type of coffee sold the most per store in the last month");
        System.out.println("6. Days of the week that made the most sale in the last month per store");
        System.out.println("7. Total sale made in dollars in the last month per store");
        System.out.println("8. Back to home screen");
    }

    public static boolean isNumeric(String s) {
        for (int j = 0; j < s.length(); j++) {
            if (!(s.charAt(j) >= 48 && s.charAt(j) <= 57)) {
                return false;
            }
        }
        return true;
    }

    public static void initializeFoodItem(BakerySystem bakerySystem) {
        List<String> foodItems = readFile("foodItem.csv");
        for (String foodItem : foodItems) {
            String[] f = foodItem.split(",");
            if (!f[2].strip().equals("Material")) {
                FoodItem aFoodItem = new FoodItem();
                aFoodItem.setItemNumber(f[0]);
                aFoodItem.setFoodItemName(f[1]);
                aFoodItem.setFoodType(f[2]);
                aFoodItem.setCurrentPrice(Double.parseDouble(f[3]));
                bakerySystem.getFoodList().add(aFoodItem);
            }
        }
    }

    public static boolean login(BakerySystem bakerySystem) {
        Scanner console = new Scanner(System.in);
        System.out.println("--Enter your employee id or email:");
        System.out.println("--Enter your password:");
        String account = console.nextLine();
        if (account.toLowerCase().equals("n")){
            System.exit(1);
        }
        System.out.println("--Enter your employee id or email:" + account);
        System.out.println("--Enter your password:");
        String password = console.nextLine();
        System.out.println("--Enter your employee id or email:" + account);
        System.out.println("--Enter your password:" + password);

        if (validateUser(account, password, bakerySystem))
            return true;
        else
            return false;

    }

    public static void main(String args[]) {
        while (true) {
            BakerySystem bakerySystem = new BakerySystem();
            boolean check;
            UserInterface.displayBakeShop();
            System.out.println("***************************************");
            System.out.println("   Please enter your user credential");
            System.out.println("or enter N in account to end the program");
            System.out.println("***************************************");
            check = login(bakerySystem);
            while (!check) {
                UserInterface.displayLoginError();
                check = login(bakerySystem);
            }
            User currentUser = bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0);
            bakerySystem.mainOption(currentUser, bakerySystem);
        }

    }

    public static List<String> readFile(String fileName) {
        ArrayList<String> strings = new ArrayList<String>();
        try {
            FileReader inputFile = new FileReader(fileName);
            try {
                Scanner parser = new Scanner(inputFile);
                parser.nextLine();
                while (parser.hasNextLine()) {
                    String line = parser.nextLine();
                    if (line.isEmpty())
                        continue;
                    strings.add(line);
                }
            } finally {
                inputFile.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " not found");
        } catch (IOException e) {
            System.out.println("Unexpected I/O exception occur");
        }
        return strings;
    }
    public static void updateNewestInventory(Store currentStore) {
        List<String> inventories = UserInterface.readFile("inventory.csv");
        currentStore.getListOfInventory().clear();
        for (String inventory : inventories) {
            String[] i = inventory.split(",");
            if (i[0].equals(currentStore.getStoreId())) {
                Inventory aInventory = new Inventory(i[1], Integer.parseInt(i[2]), i[3]);
                currentStore.getListOfInventory().add(aInventory);
            }
        }
    }
    public static void displayReportTitle(Report report, Store store) {
        displayBakeShop();
        System.out.println("Report: ");
        System.out.println("dateOfReport: " + report.getDateOfReport());
        System.out.println("nameOfReport: " + report.getNameOfReport());
        System.out.println("typeOfReport: " + report.getTypeOfReport());
        System.out.println("storeId: " + store.getStoreId());
        System.out.println("****************************************");
    }

    public static boolean validateUser(String account, String password, BakerySystem bakerySystem) {
        List<String> users = readFile("user.csv");
        for (String user : users) {
            String[] u = user.split(",");
            if ((u[0].toLowerCase().equals(account.toLowerCase()) || u[2].equals(account))
                    && u[3].equals(password)) {
                int userId = Integer.parseInt(u[0]);
                User aUser = new User(userId, u[1], u[2], u[3], u[4], u[5], u[6], u[7], u[8]);
                List<String> stores = readFile("store.csv");
                ArrayList<Store> storeList = new ArrayList<>();
                bakerySystem.getBakery().setListOfStore(storeList);
                for (String store : stores) {
                    String[] s = store.split(",");
                    String[] storeIds = u[9].split("\\|");
                    for (String storeId : storeIds) {
                        if (s[0].equals(storeId)) {
                            Store aStore = new Store();
                            aStore.setStoreId(s[0]);
                            aStore.setStoreAddress(s[1]);
                            aStore.setStoreContactNumber(s[2]);
                            ArrayList<User> userList = new ArrayList<>();
                            userList.add(aUser);
                            aStore.setListOfUser(userList);
                            List<String> inventory = readFile("inventory.csv");
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
                initializeFoodItem(bakerySystem);
                return true;
            }
        }
        return false;
    }



}
