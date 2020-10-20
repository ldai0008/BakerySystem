package com.bakery.utils;

import com.bakery.model.Report;
import com.bakery.model.Store;

public class ReportUtils {

	public static void displayReportTitle(Report report, Store store) {
		BakeryUtils.displayBakeShop();
		System.out.println("Report: ");
		System.out.println("dateOfReport: " + report.getDateOfReport());
		System.out.println("nameOfReport: " + report.getNameOfReport());
		System.out.println("typeOfReport: " + report.getTypeOfReport());
		System.out.println("storeId: " + store.getStoreId());
		System.out.println("****************************************");
	}

}
