package kramer.jeff.stock.option;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;

import java.util.HashMap;
import java.util.TreeMap;

import com.jkramer.model.Account;
import com.jkramer.model.Stock;
import com.jkramer.service.AccountService;
import com.jkramer.service.StockService;
import kramer.jeff.stock.option.common.DatabaseInitializer;
import kramer.jeff.stock.option.gui.GUINewAccount;
import kramer.jeff.stock.option.gui.GUINewStock;
import kramer.jeff.stock.option.gui.GUINewStockPurchase;

public class OptionProgram extends Application{

	private HashMap<String, Stock> stockMap = new HashMap<String, Stock>();
	private TreeMap<String, Account> accountMap = new TreeMap<String, Account>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void init(){
		DatabaseInitializer dbi = new DatabaseInitializer();
		dbi.startUp();
		
		//Load all accounts into a TreeMap for use in the program
		AccountService accountService = new AccountService();
		accountMap = accountService.getAllAccounts();
		
		//Load all stocks into a HashMap for use in the program
		StockService stockService = new StockService();
		stockMap = stockService.getAllStockHashMap(accountMap);
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
		Menu newTransaction = new Menu("Transaction");
		
		//Create sub menu items for 'Transaction' menu
		MenuItem newOptionSaleCall = new MenuItem("Option Sales Call");
		MenuItem newStockPurchase = new MenuItem("Stock Purchase");
		MenuItem newStockSale = new MenuItem("Stock Sale");
		MenuItem newStockDividend = new MenuItem("Stock Dividend");
		
		//Add sub menu items to the 'Transaction' menu
		newTransaction.getItems().addAll(newOptionSaleCall, newStockPurchase, newStockSale, newStockDividend);
		
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
				GUINewAccount.createWindow(accountMap);
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
		
		//On Action functionality for the "Stock Purchase" menu item for the 'Transaction' sub menu
		newStockPurchase.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				GUINewStockPurchase nss = new GUINewStockPurchase(stockMap, accountMap);
			}
		});
		
		//On Action functionality for the "Stock Sale" menu item for the 'Transaction' sub menu
		newStockSale.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				Stage stage = new Stage();
				stage.setTitle("New Stock Sale");
				
				BorderPane rootNode = new BorderPane();
				Scene scene = new Scene(rootNode, 400, 600);
				
				stage.setScene(scene);
				stage.show();
			}
		});
		
		//On Action functionality for the "Option Sale Call" menu item for the 'Transaction' sub menu
		newOptionSaleCall.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				Stage stage = new Stage();
				stage.setTitle("New Option Sale Call");
				
				BorderPane rootNode = new BorderPane();
				Scene scene = new Scene(rootNode, 400, 600);
				
				stage.setScene(scene);
				stage.show();
			}
		});
		
		//On Action functionality for the "Stock Dividend" menu item for the 'Transaction' sub menu
		newStockDividend.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				Stage stage = new Stage();
				stage.setTitle("New Stock Dividend");
				
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
