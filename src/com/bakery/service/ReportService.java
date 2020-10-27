package com.bakery.service;

import com.bakery.model.*;
import com.bakery.utils.BakeryUtils;
import com.bakery.utils.FileUtils;
import com.bakery.utils.ReportUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ReportService {

    private OptionService optionService;

    public ReportService(OptionService optionService) {
        this.optionService = optionService;
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

    public int chooseReport(){
        BakeryUtils.displayTrackBusinessOption();
        Scanner sc = new Scanner(System.in);
        String option = sc.nextLine();
        boolean isNumeric;
        do {
            isNumeric = true;
            if (option.length() == 0){
                System.out.println("The option cannot be blank");
                isNumeric = false;
            } else if (!BakeryUtils.isNumeric(option)){
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

    public Map<String, Double> originDays(){
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

    public int setLowInventoryBar(){
        System.out.println("Please enter the number to set the bar for low inventory items: ");
        Scanner sc = new Scanner(System.in);
        String barForLowInventory = sc.nextLine();
        boolean isNumeric;
        do {
            isNumeric = true;
            if (barForLowInventory.length() == 0){
                System.out.println("The standard of low inventory cannot be blank");
                isNumeric = false;
            } else if (!BakeryUtils.isNumeric(barForLowInventory)){
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

    public void generateReportOfDaysMadeTheMostSold(){
        List<String> orders = FileUtils.readFile("order.csv");
        Map<String, Double> daysSold = originDays();
        for (String order: orders){
            String[] quantities = order.split(",");
            String amount = quantities[6];
            String date = quantities[7];
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


    public void generateReportOfLowInventory(Store store, List<FoodItem> foodList, int lowInventory){
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


    public void generateReportOfSoldCoffee(List<FoodItem> foodList){
        List<String> orders = FileUtils.readFile("order.csv");
        int totalNum = 0;
        for (String order: orders){
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
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
        System.out.println("Total number of coffee sold in last month is: " + totalNum);
    }

    public void generateReportOfSoldCoffeeBean(){
        List<String> orders = FileUtils.readFile("order.csv");
        int totalNum = 0;
        for (String order: orders){
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
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
        System.out.println("Total number of coffee bean sold in last month is: " + totalNum);
    }

    public void generateReportOfSoldFoodItem(){
        List<String> orders = FileUtils.readFile("order.csv");
        int totalNum = 0;
        for (String order: orders){
            String[] quantities = order.split(",");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
            if (checkIfInLastMonth(date)) {
                for (String q : quantity) {
                    totalNum += Integer.parseInt(q);
                }
            }
        }
        System.out.println("Total number of sold items in last month is: " + totalNum);
    }

    public void generateReportOfTotalSold(String storeId){
        List<String> orders = FileUtils.readFile("order.csv");
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

    public void generateReportOfTypeOfCoffeeSoldMost(List<FoodItem> foodList){
        List<String> orders = FileUtils.readFile("order.csv");
        Map<String, Integer> coffeeSold = new HashMap<>();
        for (String order: orders){
            String[] quantities = order.split(",");
            String[] items = quantities[3].split("\\|");
            String[] quantity = quantities[4].split("\\|");
            String date = quantities[7];
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

    public void generateReport(User currentUser, BakerySystem bakerySystem, Store store){
        int choice = chooseReport();
        if (choice == 1){
            Report reportOfLowInventory = new Report(LocalDate.now(), "items low in inventory",
                    "inventory report", store);
            int lowInventory = setLowInventoryBar();
            ReportUtils.displayReportTitle(reportOfLowInventory, store);
            generateReportOfLowInventory(store, bakerySystem.getFoodList(), lowInventory);
        } else if (choice == 2){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of item sold in last month",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldFoodItem();
        } else if (choice == 3){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of coffee sold in last month",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldCoffee(bakerySystem.getFoodList());
        } else if (choice == 4){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Number of coffee bean sold in last month",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldCoffeeBean();
        } else if (choice == 5){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Type of coffee sold the most per store in the last month",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfTypeOfCoffeeSoldMost(bakerySystem.getFoodList());
        } else if (choice == 6){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Days of the week that made the most sale in the last month per store",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfDaysMadeTheMostSold();
        } else if (choice == 7){
            Report reportOfSoldFoodItem = new Report(LocalDate.now(),
                    "Total sale made in dollars in the last month per store",
                    "business report", store);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Store id for which you want to see the report");
            String storeId = sc.nextLine();
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfTotalSold(storeId);
        }
        optionService.previousOption(currentUser, bakerySystem, u -> generateReport(currentUser, bakerySystem, store));
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
