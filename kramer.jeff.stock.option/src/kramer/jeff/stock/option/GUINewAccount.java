package kramer.jeff.stock.option;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.TreeMap;
import java.util.Date;
import java.time.LocalDate;

public final class GUINewAccount {
	
	public final static void createWindow(TreeMap<String, Account> accountMap, TreeMap<String, Integer> accountTypeMap){
		//Create stage
		Stage stage = new Stage();
		stage.setTitle("Create Account");
		
		//Create root node and new scene
		BorderPane rootNode = new BorderPane();
		Scene scene = new Scene(rootNode, 450, 300);
		
		//Create grid pane for center section of root node
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 0, 0, 20));
		
		//Create field labels
		Label numberLabel = new Label();
		numberLabel.setText("Account Number");
		numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label nameLabel = new Label();
		nameLabel.setText("Account Name");
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label typeLabel = new Label();
		typeLabel.setText("Account Type");;
		typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		Label dateLabel = new Label();
		dateLabel.setText("Date Opened");
		dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		//Create text fields
		TextField numberField = new TextField();
		TextField nameField = new TextField();
		
		//Create and populate combo box field for account type
		ComboBox<String> typeField = new ComboBox<String>();
		for(String type : accountTypeMap.keySet()){
			typeField.getItems().add(type);
		}
		
		//Create date picker field
		DatePicker dateField = new DatePicker();
		
		//Create label to communicate with user
		//Hide this field when the window first loads
		Label msgLabel = new Label();
		msgLabel.setManaged(false);
		msgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		msgLabel.setMaxWidth(350);
		msgLabel.setWrapText(true);
		
		//Assign 'Number' fields to row 1 of the grid
		grid.add(numberLabel, 0, 0);
		grid.add(numberField, 1, 0);
		
		//Assign 'Name' fields to row 2 of the grid
		grid.add(nameLabel, 0, 1);
		grid.add(nameField, 1, 1);
		
		//Assign 'Type' fields to row 3 of the grid
		grid.add(typeLabel, 0, 2);
		grid.add(typeField, 1, 2);
		
		//Assign 'Date' fields to row 4 of the grid
		grid.add(dateLabel, 0, 3);
		grid.add(dateField, 1, 3);
		
		//Assign message label to row 5 of the grid
		//Label will span 2 columns
		grid.add(msgLabel, 0, 4, 2, 1);
		
		//Buttons at the bottom of the pane
		//One button is to save the new stock and the other closes the window
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Close");
		
		//Set the maximum size on the buttons
		btnSave.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		btnClose.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		
		//Tile pane to store the buttons
		TilePane tileButtons = new TilePane(Orientation.HORIZONTAL);
		tileButtons.setPadding(new Insets(15, 12, 15, 12));
		tileButtons.setHgap(10.0);
		tileButtons.setVgap(8.0);
		tileButtons.getChildren().addAll(btnSave, btnClose);
		
		//Add elements to the root node
		//Center section gets grid pane with fields that make up the form
		//Bottom section gets tile pane with buttons 'Save' and 'Close'
		rootNode.setCenter(grid);
		rootNode.setBottom(tileButtons);
		
		//Set the scene in the stage and show the stage
		stage.setScene(scene);
		stage.show();
		
		/**
		 * Event handlers
		 */
		//Event handler for the save button
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				//Reset the msgLabel field
				msgLabel.setManaged(false);
				msgLabel.setText("");
				
				//Values from the form
				String number = numberField.getText();
				String name = nameField.getText();
				String type = typeField.getValue();
				LocalDate localDate = dateField.getValue();
				
				//Validation to check if the 'Account Number' text field is empty
				if(number.length() == 0){
					msgLabel.setText("Please enter an Account Number.");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					numberField.requestFocus();
					return;
				}
				
				//Validation to check if the 'Account Name' text field is empty
				if(name.length() == 0){
					msgLabel.setText("Please enter an Account Name.");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					nameField.requestFocus();
					return;
				}
				
				//Validation to check if the 'Account Type' combo box is empty
				if(type == null){
					msgLabel.setText("Please enter an Account Type");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					typeField.requestFocus();
					return;
				}
								
				//Validation to check if the 'Opened Date' date picker is empty
				if(localDate == null){
					msgLabel.setText("Please choose an Opening Date.");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					dateField.requestFocus();
					return;
				}
				
				//Validate account doesn't already exist
				if(accountMap.containsKey(numberField.getText())){
					msgLabel.setText("Account with that number already exists.");
					msgLabel.setTextFill(Color.RED);
					msgLabel.setManaged(true);
					numberField.requestFocus();
					return;
				}
				
				//Cast the localDate into a Date object
				//This happens after the date picker validation
				//If date picker value is null an error is thrown when we try to create the Date object
				Date date = java.sql.Date.valueOf(localDate);
				
				//Create a new Account object
				int typeID = accountTypeMap.get(type);
				Account a = new Account(number, "", 1, date, name, type, typeID);
				AccountService accountService = new AccountService();
				if(accountService.insertAccount(a)){
					accountMap.put(a.getNumber(), a);
					msgLabel.setManaged(true);
					msgLabel.setText("Account "+ numberField.getText() + " saved.");
					msgLabel.setTextFill(Color.BLACK);
					nameField.setText("");
					numberField.setText("");
					dateField.setValue(null);
				} else {
					msgLabel.setManaged(true);
					msgLabel.setText("Unable to save account");
					msgLabel.setTextFill(Color.BLACK);
				}
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
