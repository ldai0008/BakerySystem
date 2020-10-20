package service;

import model.*;
import utils.BakeryUtils;
import utils.FileUtils;
import utils.ReportUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportService2 {


    private OptionService optionService;

    public ReportService2(OptionService optionService) {
        this.optionService = optionService;
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
            } else if (!option.equals("1") && !option.equals("2")){
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

    public void generateReportOfLowInventory(Store store, List<FoodItem> foodList, int lowInventory){
        System.out.println("Food items low in inventory: ");
        for (int i = 0; i < store.getListOfInventory().size(); i++) {
            if (store.getListOfInventory().get(i).getQuantity() < lowInventory) {
                String itemNumber = store.getListOfInventory().get(i).getItemNumber();
                int itemQuantity = store.getListOfInventory().get(i).getQuantity();
                for (FoodItem item : foodList) {
                    if (itemNumber.equals(item.getItemNumber())) {
                        String itemName = item.getFoodItemName();
                        System.out.println("itemName:" + itemName + "   " + "quantity:" + itemQuantity);
                    }
                }
            }
        }
    }

    /*
    This method can be used to generate a report for most sale made in dollars in last month.
    @author Muhammad Umer Chawla
    @return void
    @params store store id whose report is required.
    */
    public void generateReportForMostSaleMadeInDollars(Store store){
        System.out.println("Most Sale Made In Dollars");
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int previousMonth = 0;
        if (currentMonth == 1)
        {
            previousMonth = 12;
        }
        else
        {
            previousMonth = currentMonth - 1;
        }

        List<String> orders = FileUtils.readFile("order.csv");
        String storeId = "0";
        double totalSale = 0;
        String orderDate;
        int orderMonth = 0;
        for (String order: orders)
        {
            String[] orderDetails = order.split(",");
            storeId = orderDetails[0];

            if (store.getStoreId() == storeId)
            {
                orderDate = orderDetails[7];
                orderMonth = Integer.parseInt(orderDate.split("-")[1]);

                if (previousMonth == orderMonth)
                totalSale = totalSale + Double.parseDouble(orderDetails[6]);

            }
        }
        System.out.println("Total Sale made in last month is: " + totalSale);
    }

    /*
    This method can be used to generate a report of days of week that made the most sale
    @author Muhammad Umer Chawla
    @return void
    @params store store id whose report is required.
    */
    public void generateReportForDaysOfWeekWithMostMostSaleLastMonth(String store){
        System.out.println("Days Of the Week that made the most Sale In the last month");
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int previousMonth = 0;
        if (currentMonth == 1)
        {
            previousMonth = 12;
        }
        else
        {
            previousMonth = currentMonth - 1;
        }
        List<String> orders = FileUtils.readFile("order.csv");
        String storeId = "0";
        int totalSale = 0;
        String orderDate;
        int orderMonth = 0;
        int dayWithMostSale = 0;
        double maximumSaleThisMonthInOneDay = 0.0;
        double[] saleEachDayOfMonth = new double[32];
        for (String order: orders)
        {
            String[] orderDetails = order.split(",");
            storeId = orderDetails[0];

            if (store == storeId)
            {
                orderDate = orderDetails[7];
                orderMonth = Integer.parseInt(orderDate.split("-")[1]);

                if (previousMonth == orderMonth){
                    for (int day = 1 ; day <= 31; day++){
                        if (Integer.parseInt(orderDate.split("-")[2]) == day)
                        saleEachDayOfMonth[day] = saleEachDayOfMonth[day] + Double.parseDouble(orderDetails[6]);
                    }
                }


            }
        }
        maximumSaleThisMonthInOneDay = saleEachDayOfMonth[1];
        dayWithMostSale = 1;
        for (int day = 2; day <= 31 ; day++){
            if (maximumSaleThisMonthInOneDay < saleEachDayOfMonth[day]) {
                maximumSaleThisMonthInOneDay = saleEachDayOfMonth[day];
                dayWithMostSale = day;
            }
        }

        System.out.println(dayWithMostSale + " Day of Previous month made the most sale");

    }












    public void generateReportOfSoldFoodItem(){
        List<String> orders = FileUtils.readFile("order.csv");
        int totalNum = 0;
        for (String order: orders){
            String[] quantities = order.split(",");
            String[] quantity = quantities[4].split("\\|");
            for (String q : quantity){
                totalNum += Integer.parseInt(q);
            }
        }
        System.out.println("Total number of sold items in last month is: " + totalNum);
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
            Report reportOfSoldFoodItem = new Report(LocalDate.now(), "Number of sold items in last month",
                    "business report", store);
            ReportUtils.displayReportTitle(reportOfSoldFoodItem, store);
            generateReportOfSoldFoodItem();
        }
        optionService.previousOption(currentUser, bakerySystem, u -> generateReport(currentUser, bakerySystem, store));
    }
}
