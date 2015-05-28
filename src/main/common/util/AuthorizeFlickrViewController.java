package main.common.util;

import org.scribe.model.Token;

import com.flickr4java.flickr.auth.AuthInterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

public class AuthorizeFlickrViewController {
	private static UploadToFlickrUsingFlickr4Java uploadToFlickrUsingFlickr4Java;
	private static AuthInterface authInterface;
	private static Token accessToken;
	private static Hyperlink authFlickrUrl;

	@FXML
	private TextField tokenTxtField;

	public static void setFlickrInformation(String url,
			UploadToFlickrUsingFlickr4Java uploadToFlickrUsingFlickr4Java,
			AuthInterface authInterface, Token accessToken) {
		AuthorizeFlickrViewController.authFlickrUrl = new Hyperlink(url);
		AuthorizeFlickrViewController.uploadToFlickrUsingFlickr4Java = uploadToFlickrUsingFlickr4Java;
		AuthorizeFlickrViewController.authInterface = authInterface;
		AuthorizeFlickrViewController.accessToken = accessToken;
	}

	@FXML
	private void openAuthFlickrLink() {
		Resource.getMainApp().getHostServices()
				.showDocument(authFlickrUrl.getText());
	}

	@FXML
	private void handleAuthorize() {
		uploadToFlickrUsingFlickr4Java.storeFlickrAuth(getTokenTxtField()
				.getText(), AuthorizeFlickrViewController.authInterface,
				AuthorizeFlickrViewController.accessToken);
		closeStage();
	}

	public TextField getTokenTxtField() {
		return tokenTxtField;
	}

	public void setTokenTxtField(String tokenTxtField) {
		this.tokenTxtField.setText(tokenTxtField);
	}
	
	@FXML
	public void handleCancel(){
		String choice = Resource.getMainApp().showConfirmationDialog("AUTHORIZE FLICKR", "FLICKR", "By clicking cancel, you will not be able to upload image\nYou would have to manually add to the html\nAre you sure you want to continue?");
		if(choice.equalsIgnoreCase("ok")){
			closeStage();
		}
	}
	
	private void closeStage(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				UploadToFlickrUsingFlickr4Java.stage.close();
			}
		});
	}
}
