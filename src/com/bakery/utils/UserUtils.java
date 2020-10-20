package com.bakery.utils;

import com.bakery.model.BakerySystem;
import com.bakery.model.Inventory;
import com.bakery.model.Store;
import com.bakery.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    private UserUtils() {
    }

    public static boolean validateUser(String account, String password, BakerySystem bakerySystem) {
        List<String> users = FileUtils.readFile("user.csv");
        for (String user : users) {
            String[] u = user.split(",");
            if ((Integer.parseInt(u[0]) == Integer.parseInt(account) || u[2].equals(account))
                    && u[3].equals(password)) {
                int userId = Integer.parseInt(u[0]);
                User aUser = new User(userId, u[1], u[2], u[3], u[4], u[5], u[6], u[7], u[8]);
                List<String> stores = FileUtils.readFile("store.csv");
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
                            List<String> inventory = FileUtils.readFile("inventory.csv");
                            ArrayList<Inventory> inventoryList = new ArrayList<>();
                            for (String item : inventory) {
                                String[] i = item.split(",");
                                Inventory anItem = new Inventory();
                                anItem.setItemNumber(i[0]);
                                anItem.setQuantity(Integer.parseInt(i[1]));
                                anItem.setDateAdded(i[2]);
                                inventoryList.add(anItem);
                            }
                            aStore.setListOfInventory(inventoryList);
                            storeList.add(aStore);
                        }
                    }
                }
                BakeryUtils.initializeFoodItem(bakerySystem);
                return true;
            }
        }
        return false;
    }
}
