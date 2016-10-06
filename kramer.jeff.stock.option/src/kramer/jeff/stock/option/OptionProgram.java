package kramer.jeff.stock.option;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.paint.*;

import java.util.HashMap;
import java.util.TreeMap;

public class OptionProgram extends Application{

	private HashMap<String, Stock> stockMap = new HashMap<String, Stock>();
	private TreeMap<String, Account> accountMap = new TreeMap<String, Account>();
	private TreeMap<String, Integer> accountTypeMap = new TreeMap<String, Integer>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void init(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		StockService stockService = new StockService();
		stockMap = stockService.getAllStockHashMap();
		
		AccountService accountService = new AccountService();
		accountMap = accountService.getAllAccounts();
		accountTypeMap = accountService.getAccountTypes();
	}
	
	public void start(Stage stage){
		stage.setTitle("Stock Options");
		stage.setResizable(false);
		BorderPane rootNode = new BorderPane();
		
		//Create new scene
		Scene scene = new Scene(rootNode, 1250, 800);
		
		stage.setScene(scene);
		
		//Main menu bar
		MenuBar menuBar = new MenuBar();
		
		//Create 'File' menu
		Menu file = new Menu("File");
		
		//Create 'Exit' menu item for 'File' menu
		MenuItem exit = new MenuItem("Exit");
		
		//Create 'New' sub menu for File menu
		Menu newItem = new Menu("New");
		
		//Create sub menu items for 'New' menu
		MenuItem newAccount = new MenuItem("Account");
		MenuItem newStock = new MenuItem("Stock");
		MenuItem newPrice = new MenuItem("Price");
		MenuItem newTransaction = new MenuItem("Transaction");
		
		//Add sub menu items to the 'New' menu
		newItem.getItems().addAll(newAccount, newStock, newPrice, newTransaction);
		
		//Add menu items to the main menu bar
		file.getItems().addAll(newItem, new SeparatorMenuItem(), exit);
		menuBar.getMenus().addAll(file);
		
		//Add main menu to the top section of the layout
		rootNode.setTop(menuBar);
		
		//Show the stage
		stage.show();
		
		//On Action functionality for the 'New Account' sub menu item
		newAccount.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				GUINewAccount.createWindow(accountMap, accountTypeMap);
			}
		});
		
		//On Action functionality for the 'New Stock' sub menu item
		newStock.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				GUINewStock.createWindow(stockMap, accountMap);
			}
		});
		
		//On Action functionality for the "New Price" sub menu item
		newPrice.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				Stage stage = new Stage();
				stage.setTitle("Add New Stock Price");
				
				BorderPane rootNode = new BorderPane();
				Scene scene = new Scene(rootNode, 400, 600);
				
				stage.setScene(scene);
				stage.show();				
			}
		});
		
		//On Action functionality for the "New Transaction" sub menu item
		newTransaction.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				Stage stage = new Stage();
				stage.setTitle("Add New Stock Transaction");
				
				BorderPane rootNode = new BorderPane();
				Scene scene = new Scene(rootNode, 400, 600);
				
				stage.setScene(scene);
				stage.show();
			}
		});
		
		exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae){
				System.exit(0);
			}
		});
	}
}
