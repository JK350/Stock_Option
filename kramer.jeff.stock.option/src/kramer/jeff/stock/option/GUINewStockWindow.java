package kramer.jeff.stock.option;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.LinkedHashMap;

/**
 * Creates the window for the user to enter new stocks
 * @author J Kramer
 *
 */
public final class GUINewStockWindow {

	public final static void createWindow(LinkedHashMap<String, Stock> stockMap){
		Stage stage = new Stage();
		stage.setTitle("Add New Stock");
		
		BorderPane rootNode = new BorderPane();
		Scene scene = new Scene(rootNode, 400, 250);
		
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
		Label errorLabel = new Label("Error");
		errorLabel.setManaged(false);
		errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		errorLabel.setTextFill(Color.RED);
		errorLabel.setWrapText(true);
		errorLabel.setMaxWidth(350);
		stockGrid.add(errorLabel, 0, 3, 2, 1);
										
		//Buttons at the bottom of the pane
		//One button is to save the new stock and the other closes the window
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Close");
		
		//Set the maximum size on the buttons
		btnSave.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnClose.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
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
				if(errorLabel.isManaged()){
					errorLabel.setManaged(false);
				}						
				
				String symbol = symbolText.getText().toUpperCase();
				String companyName = companyNameText.getText();
				String annualDivRateString = annualDivRateText.getText();
				Double annualDivRate = 0.0;
				
				//Validate a value was provided for the symbol field
				if(symbol.length() == 0){
					errorLabel.setText("Please enter a stock symbol.");
					errorLabel.setManaged(true);
					symbolText.requestFocus();
					return;
				} 
				
				//Validate a value was provided for the company name field
				if(companyName.length() == 0){
					errorLabel.setText("Please enter a company name.");
					errorLabel.setManaged(true);
					companyNameText.requestFocus();
					return;
				}
				
				//Validate a value was provided for the annual dividend rate field
				if(annualDivRateString.length() == 0){
					errorLabel.setText("Please enter an annual dividend rate.");
					errorLabel.setManaged(true);
					annualDivRateText.requestFocus();
					return;
				}
				
				//Validate that the value put into the annual dividend rate field is a Double
				//This is achieved by attempting to parse the value into a Double variable
				try{
					annualDivRate = Double.parseDouble(annualDivRateText.getText());
				} catch (Exception ex){
					errorLabel.setManaged(true);
					errorLabel.setText("Annual dividend rate must be a number.");
					annualDivRateText.setText("");
					annualDivRateText.requestFocus();
					return;
				}
				
				//Create a new Stock object for the new stock and insert the new stock into the database.
				Stock s = new Stock(symbol, companyName, annualDivRate, 1);
				StockDAOImpl sImpl = new StockDAOImpl();
				sImpl.insertStock(s);
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
