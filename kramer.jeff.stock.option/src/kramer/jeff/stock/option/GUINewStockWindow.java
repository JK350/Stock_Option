package kramer.jeff.stock.option;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.HashMap;

/**
 * Creates the window for the user to enter new stocks
 * @author J Kramer
 *
 */
public final class GUINewStockWindow {

	public final static void createWindow(HashMap<String, Stock> stockMap){
		Stage stage = new Stage();
		stage.setTitle("Add New Stock");
		
		BorderPane rootNode = new BorderPane();
		Scene scene = new Scene(rootNode, 450, 250);
		
		GridPane stockGrid = new GridPane();
		stockGrid.setHgap(10);
		stockGrid.setVgap(10);
		stockGrid.setPadding(new Insets(25, 0, 0, 20));
		
		//Symbol in column 1, row 1
		Text symbol = new Text("Symbol");
		symbol.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		stockGrid.add(symbol, 0, 0);
		
		//Text field for symbol user input in column 2, row 1
		TextField symbolText = new TextField();
		stockGrid.add(symbolText, 1, 0);
					
		//Company Name in column 1, row 2
		Text companyName = new Text("Company Name");
		companyName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		stockGrid.add(companyName, 0, 1);
		
		//Text field for Company Name user input in column 2, row 2
		TextField companyNameText = new TextField();
		stockGrid.add(companyNameText, 1, 1);
		
		//Annual Dividend Rate in column 1, row 3
		Text annualDivRate = new Text("Annual Dividend Rate");
		annualDivRate.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		stockGrid.add(annualDivRate, 0, 2);
		
		//Text field for Annual Dividend Rate in column 2, row 3
		TextField annualDivRateText = new TextField();
		stockGrid.add(annualDivRateText, 1, 2);
		
		//Error label
		Label msgLabel = new Label("Error");
		msgLabel.setManaged(false);
		msgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		msgLabel.setWrapText(true);
		msgLabel.setMaxWidth(350);
		stockGrid.add(msgLabel, 0, 3, 2, 1);
										
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
	
		//Placing items in the BorderPane
		//Buttons go in the bottom section of the BorderPane
		//Grid with stock information goes in the center section of the BorderPane
		rootNode.setBottom(tileButtons);
		rootNode.setCenter(stockGrid);
		
		stage.setScene(scene);
		stage.show();
		
		//On action logic for the 'Save' button
		btnSave.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				//Hide error label if it is showing
				msgLabel.setManaged(false);
				msgLabel.setText("");
								
				String symbol = symbolText.getText().toUpperCase();
				String companyName = companyNameText.getText();
				String annualDivRateString = annualDivRateText.getText();
				Double annualDivRate = 0.0;
				
				//Validate a value was provided for the symbol field
				if(symbol.length() == 0){
					msgLabel.setText("Please enter a stock symbol.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					symbolText.requestFocus();
					return;
				} 
				
				//Validate a value was provided for the company name field
				if(companyName.length() == 0){
					msgLabel.setText("Please enter a company name.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					companyNameText.requestFocus();
					return;
				}
				
				//Validate a value was provided for the annual dividend rate field
				if(annualDivRateString.length() == 0){
					msgLabel.setText("Please enter an annual dividend rate.");
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.RED);
					annualDivRateText.requestFocus();
					return;
				}
				
				//Validate that the value put into the annual dividend rate field is a Double
				//This is achieved by attempting to parse the value into a Double variable
				try{
					annualDivRate = Double.parseDouble(annualDivRateText.getText());
				} catch (Exception ex){
					msgLabel.setManaged(true);
					msgLabel.setText("Annual dividend rate must be a number.");
					msgLabel.setTextFill(Color.RED);
					annualDivRateText.setText("");
					annualDivRateText.requestFocus();
					return;
				}
				
				//Create a new Stock object for the new stock and insert the new stock into the database.
				Stock s = new Stock(symbol, companyName, annualDivRate, 1);
				StockService stockService = new StockService();
				if(stockService.insertStock(s)){
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.BLACK);
					symbolText.setText("");
					companyNameText.setText("");
					annualDivRateText.setText("");
					msgLabel.setText("Stock " + symbolText.getText() + " saved.");
				} else {
					msgLabel.setManaged(true);
					msgLabel.setTextFill(Color.BLACK);
					msgLabel.setText("Unable to save Stock");
				}
			}
		});
		
		//On action functionality for the 'Cancel' button
		btnClose.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
				stage.close();
			}
		});
	}
}
