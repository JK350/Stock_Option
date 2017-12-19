package kramer.jeff.stock.option.gui;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import kramer.jeff.stock.option.model.Account;
import kramer.jeff.stock.option.model.Stock;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kramer.jeff.stock.option.common.Constants;


public final class GUINewStockPurchase implements GUINewTransaction{
	
	GUINewStockPurchase(HashMap<String, Stock> stockMap, TreeMap<String, Account> accountMap){
		createWindow(stockMap, accountMap);
	}
	
	public final void createWindow(HashMap<String, Stock> stockMap, TreeMap<String, Account> accountMap){		
		Stage stage = new Stage();
		stage.setTitle("New Stock Purchase");
		
		BorderPane rootNode = new BorderPane();
		Scene scene = new Scene(rootNode, 400, 600);
		
		/**
		 * Set up for Center section of BorderPane
		 * Contains fields and labels for the form the user uses 
		 * 		to enter the information for a new stock sale
		 */
		//Grid set up for the form
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 0, 0, 20));		
		
		//*******Field Labels*********
		//Account label
		Label accountLabel = new Label();
		accountLabel.setText("Account");
		accountLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//Stock label
		Label stockLabel = new Label();
		stockLabel.setText("Stock");
		stockLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//Shares Purchased label
		Label sharesLabel = new Label();
		sharesLabel.setText("Shares Purchased");
		sharesLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//Price Per Share label
		Label priceLabel = new Label();
		priceLabel.setText("Price Per Share");
		priceLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//Purchase Date label
		Label purchaseDateLabel = new Label();
		purchaseDateLabel.setText("Purchase Date");
		purchaseDateLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//Commission label
		Label commissionLabel = new Label();
		commissionLabel.setText("Commission");
		commissionLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		
		//*******Text Fields*******
		TextField sharesField = new TextField();
		TextField priceField = new TextField();
		TextField commissionField = new TextField();
		
		//*******Combo Box Fields*******
		ComboBox<String> accountComboBox = new ComboBox<String>();
		for(String a : accountMap.keySet()){
			accountComboBox.getItems().add(a);
		}
		
		ComboBox<String> stockComboBox = new ComboBox<String>();
		for(String s : stockMap.keySet()){
			stockComboBox.getItems().add(s);
		}
		
		//*******Date Picker Fields*******
		DatePicker purchaseDatePicker = new DatePicker();
		
		//Create label to communicate with user
		//Hide this field when the window first loads
		Label msgLabel = new Label();
		msgLabel.setManaged(false);
		msgLabel.setFont(Constants.FONT_ARIAL_BOLD_16);
		msgLabel.setMaxWidth(350);
		msgLabel.setWrapText(true);
		
		//******Add fields to grid******
		//Assign 'Account' fields to row 1
		grid.add(accountLabel, 0, 0);
		grid.add(accountComboBox, 1, 0);
		
		//Assign 'Stock' fields to row 2
		grid.add(stockLabel, 0, 1);
		grid.add(stockComboBox, 1, 1);
		
		//Assign 'Shares Purchased' fields to row 3
		grid.add(sharesLabel, 0, 2);
		grid.add(sharesField, 1, 2);
		
		//Assign 'Price per Share' fields to row 4
		grid.add(priceLabel, 0, 3);
		grid.add(priceField, 1, 3);
		
		//Assign 'Purchase Date' fields to row 5
		grid.add(purchaseDateLabel, 0, 4);
		grid.add(purchaseDatePicker, 1, 4);

		//Assign 'Commission' fields to row 6
		grid.add(commissionLabel, 0, 5);
		grid.add(commissionField, 1, 5);
		
		//Assign message label to row 7 of the grid
		//Label will span 2 columns
		grid.add(msgLabel, 0, 6, 2, 1);
		
		/**
		 * Set up for Bottom section of BorderPane
		 * Contains Save button and Close button
		 */
		
		//Create 'Save' and 'Close' buttons
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Close");
		
		//Set button max size
		btnSave.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		btnClose.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		
		//Create Tile pane to store the buttons in
		//Padding 15, 12, 15, 12
		//Hgap 10
		//Vgap 8
		TilePane buttonTile = new TilePane(Orientation.HORIZONTAL);
		buttonTile.setPadding(new Insets(15, 12, 15, 12));
		buttonTile.setHgap(10.0);
		buttonTile.setVgap(8.0);
		buttonTile.getChildren().addAll(btnSave, btnClose);
		
		//******Place items in the BorderPane******
		//Grid for form goes in center section of the BorderPane
		//Tile Pane for buttons goes in the bottom section of the BorderPane
		rootNode.setCenter(grid);
		rootNode.setBottom(buttonTile);
		
		stage.setScene(scene);
		stage.show();
		
		/**
		 * Event Handlers
		 */
		//Event handler for the save button
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				//Reset the msgLabel field
				msgLabel.setManaged(false);
				msgLabel.setText("");
				
				//Values from the form
				String account = accountComboBox.getValue();
				String stock = stockComboBox.getValue();
				String sharesPurchasedText = sharesField.getText();
				String pricePerShareText = priceField.getText();
				LocalDate purchaseDate = purchaseDatePicker.getValue();
				String commissionText = commissionField.getText();
				double commission = 0.0;
				
				/**
				 * Validation for account field
				 */
				if(account == null){
					msgLabel.setText("Please select an account.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					accountComboBox.requestFocus();
					return;
				}
				
				/**
				 * Validation for stock field
				 */
				if(stock == null){
					msgLabel.setText("Please select a stock.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					stockComboBox.requestFocus();
					return;
				}
				
				/**
				 * Validation for shared purchased field
				 */
				//Validate a value was provided for the shares purchased field
				if(sharesPurchasedText.length() == 0){
					msgLabel.setText("Please enter the number of shares purchased.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					sharesField.requestFocus();
					return;
				}
				
				//Value to store number of shares purchased
				int sharesPurchased = 0;
				
				//Validate number of shares purchased is an integer
				try{
					sharesPurchased = Integer.parseInt(sharesPurchasedText);
				} catch (Exception ex){
					msgLabel.setManaged(true);
					msgLabel.setText("Number of shares purchased must be an integer.");
					msgLabel.setTextFill(Color.RED);
					sharesField.setText("");
					sharesField.requestFocus();
					return;
				}
				
				/**
				 * Validation for price per share field
				 */
				//Validate a value was provided for the price per share field
				if(pricePerShareText.length() == 0){
					msgLabel.setText("Please enter the price per share.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					priceField.requestFocus();
					return;
				}
				
				//Remove dollar sign if present
				pricePerShareText = pricePerShareText.replace("$", "");
				
				//Value to store price of each share
				double pricePerShare = 0.0;
				
				//Validate price per share is a double
				try{
					pricePerShare = Double.parseDouble(pricePerShareText);
				} catch (Exception ex){
					msgLabel.setManaged(true);
					msgLabel.setText("Price per share must be a number.");
					msgLabel.setTextFill(Color.RED);
					priceField.setText("");
					priceField.requestFocus();
					return;
				}
				
				/**
				 * Validate purchased date
				 */
				if(purchaseDate == null){
					msgLabel.setText("Please choose a purchase date.");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					purchaseDatePicker.requestFocus();
					return;
				}
				
				//Cast localDate into a Date object
				Date date = java.sql.Date.valueOf(purchaseDate);
			}
		});
		
		//Event handler for the close button
		btnClose.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				stage.close();
			}
		});
	}
}
