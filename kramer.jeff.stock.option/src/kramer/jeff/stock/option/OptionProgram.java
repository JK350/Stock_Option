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

public class OptionProgram extends Application{

	public static void main(String[] args) {
		//DatabaseInitializer db = new DatabaseInitializer();
		
		launch(args);
		
		//db.closeConnection();
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
		MenuItem newStock = new MenuItem("Stock");
		MenuItem newPrice = new MenuItem("Price");
		MenuItem newTransaction = new MenuItem("Transaction");
		
		//Add sub menu items to the 'New' menu
		newItem.getItems().addAll(newStock, newPrice, newTransaction);
		
		//Add menu items to the main menu bar
		file.getItems().addAll(newItem, new SeparatorMenuItem(), exit);
		menuBar.getMenus().addAll(file);
		
		//Add main menu to the top section of the layout
		rootNode.setTop(menuBar);
		
		//Show the stage
		stage.show();
		
		//On Action functionality for the 'New Stock' sub menu item
		//Creates a new stage with fields the user fills out.
		//Provides a 'Save' and 'Cancel' button to the user.
		newStock.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent ae){
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
						if(errorLabel.isManaged()){
							errorLabel.setManaged(false);
						}						
						
						String symbol = symbolText.getText().toUpperCase();
						String companyName = companyNameText.getText();
						String annualDivRateString = annualDivRateText.getText();
						Double annualDivRate = 0.0;
						
						if(symbol.length() == 0){
							errorLabel.setText("Please enter a stock symbol.");
							errorLabel.setManaged(true);
							symbolText.requestFocus();
							return;
						} 
						
						if(companyName.length() == 0){
							errorLabel.setText("Please enter a company name.");
							errorLabel.setManaged(true);
							companyNameText.requestFocus();
							return;
						}
						
						if(annualDivRateString.length() == 0){
							errorLabel.setText("Please enter an annual dividend rate.");
							errorLabel.setManaged(true);
							annualDivRateText.requestFocus();
							return;
						}
						
						try{
							annualDivRate = Double.parseDouble(annualDivRateText.getText());
						} catch (Exception ex){
							errorLabel.setManaged(true);
							errorLabel.setText("Annual dividend rate must be a number.");
							annualDivRateText.setText("");
							annualDivRateText.requestFocus();
							return;
						}
						
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
