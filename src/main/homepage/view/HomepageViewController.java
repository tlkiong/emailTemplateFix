package main.homepage.view;

import java.io.File;

import main.MainApp;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

public class HomepageViewController {
	
	@FXML
	private StackPane contentPane;
	@FXML
	private Label folderName;
	@FXML
	private WebView oldWebView;
	@FXML
	private WebView newWebView;
	
	@FXML
	private void initialize() {
		resetLabel();
		contentPane.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				mouseDragOver(event);
			}
		});

		contentPane.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				mouseDragDropped(event);
			}
		});
		
		contentPane.setOnDragExited(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				contentPane.setStyle("-fx-border-color: none;"
	                    + "-fx-background-color: none;");
				resetLabel();
			}
		});
		
		
//		WebEngine webEngine = oldWebView.getEngine();
//		webEngine.load("https://www.google.com");
	}
	
	private void resetLabel(){
		folderName.setAlignment(Pos.CENTER);
		folderName.setText("Please\nDRAG 'n' DROP\nYour\nFolder\nHere");
	}
	
	private  void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        
        File file = new File(db.getFiles().get(0).getPath());
        final boolean isAccepted = file.isDirectory();
 
        if (isAccepted) {
        	folderName.setAlignment(Pos.CENTER);
        	folderName.setText("DROP\n\""+file.getName()+"\"\nfolder");
        	contentPane.setStyle("-fx-border-color: blue;"
                    + "-fx-border-width: 5;"
                    + "-fx-background-color: rgba(0,199,255,0.85);"
                    + "-fx-border-style: solid;");
                      e.acceptTransferModes(TransferMode.COPY);
                      
        } else {
            e.consume();
        }
    }
	
	private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        File file = new File(db.getFiles().get(0).getPath());
        final boolean isAccepted = file.isDirectory();
 
        if (isAccepted) {
            success = true;
            MainApp.getResource().processFolder(file);
        }
        e.setDropCompleted(success);
        e.consume();
    }
}
