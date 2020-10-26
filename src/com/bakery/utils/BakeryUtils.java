package com.bakery.utils;

import com.bakery.model.BakerySystem;
import com.bakery.model.FoodItem;

import java.util.List;
import java.util.Scanner;

public class BakeryUtils {

    private BakeryUtils() {
        super();
    }

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

    public static void displayHomeScreen(String userName, String userType) {
        displayBakeShop();
        System.out.println(" *****Welcome, " + userName + "(" + userType + ")*****");
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
                break;
            default:
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("!Error: User Type is not valid!");
                System.out.println("0. Logout");
        }

    }

    public static void displayLoginError() {
        System.out.println("!Error: User is not valid!");
        System.out.println("****************************************\n"
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
        List<String> foodItems = FileUtils.readFile("foodItem.csv");
        for (String foodItem : foodItems) {
            String[] f = foodItem.split(",");
            FoodItem aFoodItem = new FoodItem();
            aFoodItem.setItemNumber(f[0]);
            aFoodItem.setFoodItemName(f[1]);
            aFoodItem.setFoodType(f[2]);
            aFoodItem.setCurrentPrice(Double.parseDouble(f[3]));
            bakerySystem.getFoodList().add(aFoodItem);
        }
    }

}
