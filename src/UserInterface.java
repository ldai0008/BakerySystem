import java.time.LocalDate;
import java.util.Scanner;

/**
 * @version: V1.0
 * @author: Team_04
 * @className: UserInterface
 * @description: interface of the application
 * @data: 2020-11-1 9:00
 **/
public class UserInterface {
    /**
     * This method is used to display title of the Bake shop.
     */
    public static void displayBakeShop() {
        System.out.println("=======================================");
        System.out.println("|                                     |");
        System.out.println("|               Bake Shop             |");
        System.out.println("|                                     |");
        System.out.println("=======================================");
    }

    /**
     * This method is used to display the option of create advanced order.
     */
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

    /**
     * This method is used to display the option of create new order.
     */
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

    /**
     * This method is used to display the option on the home screen.
     */
    public static void displayHomeScreen(String userName, String userType, String storeID) {
        displayBakeShop();
        System.out.println("    *****Welcome, " + userName + "(" + userType + ")*****");
        System.out.println("        You are in store *" + storeID + "*");
        switch (userType) {
            case "Staff" -> {
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("1. Create new order");
                System.out.println("2. Create new special order for roast coffee bean");
                System.out.println("3. Manage order");
                System.out.println("4. Manage inventory");
                System.out.println("5. View my profile");
                System.out.println("0. Logout");
            }
            case "Manager" -> {
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("1. Create new order");
                System.out.println("2. Create new special order for roast coffee bean");
                System.out.println("3. Manage order");
                System.out.println("4. Manage inventory");
                System.out.println("5. Manage all advance order");
                System.out.println("6. View my profile");
                System.out.println("0. Logout");
            }
            case "Owner" -> {
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
            }
            default -> {
                System.out.println("-- Please select one option by entering the number:");
                System.out.println("!Error: User Type is not valid!");
                System.out.println("0. Logout");
            }
        }

    }

    /**
     * This method is used to display the error message of the Login.
     */
    public static void displayLoginError() {
        System.out.println("!Error: User is not valid!");
        System.out.println("***************************************\n"
                + "Please try login again.or contact the owner to reset the password.");
    }

    /**
     * This method is used to display the title of the report.
     */
    public static void displayReportTitle(LocalDate reportDate, String reportName, String reportType, String storeID) {
        UserInterface.displayBakeShop();
        System.out.println("Report: ");
        System.out.println("dateOfReport: " + reportDate);
        System.out.println("nameOfReport: " + reportName);
        System.out.println("typeOfReport: " + reportType);
        System.out.println("storeId: " + storeID);
        System.out.println("****************************************");
    }

    /**
     * This method is used to display the option of all reports.
     */
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

    /**
     * This method is used to display login option.
     */
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

        return bakerySystem.validateUser(account, password);

    }

    /**
     * This method is the entry point of this java program.
     */
    public static void main(String[] args) {
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
            bakerySystem.mainOption(currentUser);
        }

    }

}
