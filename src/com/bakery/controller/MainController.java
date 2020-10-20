package com.bakery.controller;

import com.bakery.model.BakerySystem;
import com.bakery.model.User;
import com.bakery.service.OptionService;
import com.bakery.utils.BakeryUtils;

public class MainController {

	private static OptionService optionService = new OptionService();

	public static void main(String args[]) {
		while (true) {
			BakerySystem bakerySystem = new BakerySystem();
			boolean check;
			BakeryUtils.displayBakeShop();
			System.out.println("***Please enter your user credential***");
			check = optionService.login(bakerySystem);
			while (!check) {
				BakeryUtils.displayLoginError();
				check = optionService.login(bakerySystem);
			}
			User currentUser = bakerySystem.getBakery().getListOfStore().get(0).getListOfUser().get(0);
			optionService.mainOption(currentUser, bakerySystem);
		}

	}

}
