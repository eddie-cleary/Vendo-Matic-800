package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "Sales Report";
	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] HIDDEN_MENU_OPTIONS = { MAIN_MENU_OPTION_SALES_REPORT };
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};


	private Menu menu;
	private VendingMachine vendingMachine;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {

		VendingLog.initialize();

		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS, HIDDEN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				System.out.println(vendingMachine.displayVendingMachineItems());
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				while (true) {
					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS, "Current money provided: " + VendingMachine.formatMoney(vendingMachine.getCurrentBalance()));
					System.out.println();
					if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
						vendingMachine.feedMoney();
					} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
						//print out options and get user selection
						System.out.println(vendingMachine.displayVendingMachineItems());
						System.out.println("Please make your selection: ");
						Scanner inputScanner = new Scanner(System.in);
						String input = inputScanner.nextLine();
						vendingMachine.selectProduct(input);
					} else if (choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						vendingMachine.finishTransaction();
						break;
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				//exit program
				break;
			}
			else if (choice.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
				//hidden option - generate the sales report file
				SalesReport.generateReport(vendingMachine.getItems(), vendingMachine.getTotalSales());
			}
		}
	}


	public void readVendingMachineData(String dataPath) {
		try (Scanner fileScanner = new Scanner(new File(dataPath))) {
			vendingMachine = new VendingMachine();
			while (fileScanner.hasNextLine()) {
				VendingMachineItem currentItem;
				String currentLine = fileScanner.nextLine();
				String[] lineInfo = currentLine.split("\\|");
				Consumable currentConsumable = null;
				switch(lineInfo[3]) {
					case "Chip":
						currentConsumable = new Chips();
						break;
					case "Candy":
						currentConsumable = new Candy();
						break;
					case "Drink":
						currentConsumable = new Drink();
						break;
					case "Gum":
						currentConsumable = new Gum();
						break;
				}
				currentItem = new VendingMachineItem(lineInfo[0], lineInfo[1], new BigDecimal(lineInfo[2]), currentConsumable);
				vendingMachine.addItem(currentItem);
			}

		} catch (FileNotFoundException ex) {
			System.err.println("Error loading machine data.");
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.readVendingMachineData("capstone/vendingmachine.csv");
		cli.run();
	}
}
