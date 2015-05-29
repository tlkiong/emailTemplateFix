package main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import main.common.util.Resource;
import main.processingFolder.view.ProcessingFolderViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

	private Stage mainStage;
	private static Resource resource;
	
	private static final int COUNTDOWN_TIMER_MINUTE = 1800;
	private int countdownTime;
	private Timer countdownTimer;
	
	public static int getStaticCountdownTimerMinute() {
		return COUNTDOWN_TIMER_MINUTE;
	}

	public int getCountdownTime() {
		return countdownTime;
	}

	public void setCountdownTime(int countdownTime) {
		this.countdownTime = countdownTime;
	}
	
	/**
	 * show the initial settings view
	 */
	public void showInitialSettingsView() {
		try {
			// Load person overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("settings/view/InitialSettingsView.fxml"));
			AnchorPane initialSettingsView = (AnchorPane) loader.load();

			// Set person overview onto center of a scene
			Scene scene = new Scene(initialSettingsView);
			mainStage.setScene(scene);
			mainStage.centerOnScreen();
			mainStage.setResizable(false);

			mainStage.show();
		} catch (Exception e) {
			showExceptionDialog("MainApp initial settings - Exception: ", e);
			// e.printStackTrace();
		}
	}

	/**
	 * show the initial settings view
	 */
	public void showHomepage() {
		try {
			// Load person overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("homepage/view/HomepageView.fxml"));
			AnchorPane homepageView = (AnchorPane) loader.load();

			// Set person overview onto center of a scene
			Scene scene = new Scene(homepageView);
			mainStage.setScene(scene);
			mainStage.centerOnScreen();
			mainStage.setResizable(true);

			mainStage.show();
		} catch (Exception e) {
			showExceptionDialog("MainApp Homepage View - Exception: ", e);
			// e.printStackTrace();
		}
	}

	public void showProcessingFolder() {
		try {
			// Load person overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("processingFolder/view/ProcessingFolderView.fxml"));
			AnchorPane processingFolderView = (AnchorPane) loader.load();

			// Set person overview onto center of a scene
			Scene scene = new Scene(processingFolderView);
			mainStage.setScene(scene);
			mainStage.centerOnScreen();
			mainStage.setResizable(true);
			mainStage.show();
		} catch (Exception e) {
			showExceptionDialog("MainApp Homepage View - Exception: ", e);
			// e.printStackTrace();
		}
	}

	public void showInformationDialog(String title, String headerTxt,
			String contentTxt) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerTxt);
		alert.setContentText(contentTxt);
		alert.showAndWait();
	}

	public void showInformationDialog(String title, String headerTxt,
			String contentTxt, double height) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerTxt);
		alert.setContentText(contentTxt);
		alert.getDialogPane().setPrefHeight(height);
		alert.showAndWait();
	}

	public void showWarningDialog(String title, String headerTxt,
			String contentTxt) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerTxt);
		alert.setContentText(contentTxt);

		alert.showAndWait();
	}

	/**
	 * Error dialog
	 * 
	 * @param title
	 * @param headerTxt
	 *            null will equal no header text
	 * @param contentTxt
	 */
	public void showErrorDialog(String title, String headerTxt,
			String contentTxt) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerTxt);
		alert.setContentText(contentTxt);

		alert.showAndWait();
	}

	public String showExceptionDialog(String contentTxt, Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error Detected!");
		alert.setContentText(contentTxt
				+ "\nPlease e-mail us the following Error :)");

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace is:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);
		alert.setResizable(true);
		alert.showAndWait();
		// Optional<ButtonType> result = alert.showAndWait();
		// if (result.isPresent() && result.get() == ButtonType.OK) {
		// return "ok";
		// }
		return "ok";
	}

	public String showConfirmationDialog(String title, String headerTxt,
			String contentTxt) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerTxt);
		alert.setContentText(contentTxt);
		String userChoice = "cancel";

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			userChoice = "ok";
		}
		// If user click cancel or close dialog, its "cancel"
		return userChoice;
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getMainStage() {
		return this.mainStage;
	}

	public static Resource getResource() {
		return MainApp.resource;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.mainStage = primaryStage;
		this.mainStage.setTitle("Email Template Customizer");
		this.mainStage.initStyle(StageStyle.DECORATED);
		MainApp.resource = new Resource(this);
		startCountdown();
		this.mainStage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public void startCountdown() {
		countdownTimer = new Timer();
		countdownTimer.schedule(new RemindTask(), 1, // initial delay
				getCountdownTime() * 1000); // subsequent rate
	}

	class RemindTask extends TimerTask {
		public void run() {
			// do something
			silentCheckForUpdates();
		}
	}
	
	public void silentCheckForUpdates() {
//		ApplicationLauncher.launchApplicationInProcess("347", null,
//				new ApplicationLauncher.Callback() {
//					public void exited(int exitValue) {
//						// TODO add your code here (not invoked on event
//						// dispatch thread)
//					}
//
//					public void prepareShutdown() {
//						// TODO add your code here (not invoked on event
//						// dispatch thread)
//					}
//				}, ApplicationLauncher.WindowMode.FRAME, null);
	}
}
