package com.bakery.service;

import com.bakery.model.*;
import com.bakery.utils.BakeryUtils;
import com.bakery.utils.FileUtils;
import com.bakery.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OptionService {

    private BakeryService bakeryService = new BakeryService(this);
    private ReportService reportService = new ReportService(this);

    public boolean login(BakerySystem bakerySystem) {
        Scanner console = new Scanner(System.in);
        System.out.println("--Enter your employee id or email:");
        System.out.println("--Enter your password:");
        String account = console.nextLine();
        System.out.println("--Enter your employee id or email:" + account);
        System.out.println("--Enter your password:");
        String password = console.nextLine();
        System.out.println("--Enter your employee id or email:" + account);
        System.out.println("--Enter your password:" + password);

        if (UserUtils.validateUser(account, password, bakerySystem))
            return true;
        else
            return false;

    }


    public void mainOption(User currentUser, BakerySystem bakerySystem) {
        List<FoodItem> foodList = bakerySystem.getFoodList();
        Bakery bakery = bakerySystem.getBakery();
        boolean isContinue = true;
        Scanner console = new Scanner(System.in);
        String currentUserName = currentUser.getUserName();
        String currentUserType = currentUser.getUserType();
        while (isContinue) {
//            BakeryUtils.displayHomeScreen(currentUserName, currentUserType);
            String selection = "";
            if (currentUserType.equals("Staff") || currentUserType.equals("Manager")) {
                BakeryUtils.displayHomeScreen(currentUserName, currentUserType);
                selection = console.nextLine();
                switch (selection) {
                    case "1":
                        bakeryService.createNewOrder(bakerySystem);
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
                Store store = bakeryService.chooseStore(bakery);
                BakeryUtils.displayHomeScreen(currentUserName, currentUserType);
                selection = console.nextLine();
                switch (selection) {
                    case "1":
                        bakeryService.createNewOrder(bakerySystem);
                        break;
                    case "8":
                        reportService.generateReport(currentUser, bakerySystem, store);
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


    public void previousOption(User currentUser, BakerySystem bakerySystem, Consumer<User> callback) {
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

}
