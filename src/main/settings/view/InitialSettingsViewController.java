package main.settings.view;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import main.MainApp;
import main.common.util.Resource;
import main.common.util.UploadToFlickrUsingFlickr4Java;
import main.common.util.UserSettings.USER_SETTINGS;

public class InitialSettingsViewController {
	private Hyperlink flickrNsid = new Hyperlink(
			"https://www.flickr.com/services/api/explore/?method=flickr.people.getInfo");
	private Hyperlink flickrApiKey = new Hyperlink(
			"https://www.flickr.com/services/apps/create/apply/");
	
	@FXML
	private TextField flickrClientApiKeyTxtField;
	@FXML
	private TextField flickrClientSharedSecretTxtField;
	@FXML
	private TextField flickrClientNsidTxtField;
	@FXML
	private Label getApiLbl;
	@FXML
	private Label getNsidLbl;
	
	
	@FXML
	private void initialize() {
		flickrClientApiKeyTxtField.setText(Resource.getUserSettingsByKey(
				USER_SETTINGS.FLICKR_CLIENT_API_KEY));
		flickrClientSharedSecretTxtField.setText(Resource.getUserSettingsByKey(
				USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET));
		flickrClientNsidTxtField.setText(Resource.getUserSettingsByKey(
				USER_SETTINGS.FLICKR_CLIENT_NSID));
	}
	
	@FXML
	public void handleDone() {
		if ((flickrClientApiKeyTxtField.getText() != null && !flickrClientApiKeyTxtField
				.getText().isEmpty())
				&& (flickrClientSharedSecretTxtField.getText() != null && !flickrClientSharedSecretTxtField
						.getText().isEmpty())
				&& (flickrClientNsidTxtField.getText() != null && !flickrClientNsidTxtField
						.getText().isEmpty())) {
			MainApp.getResource().setUserSettings(
					USER_SETTINGS.FLICKR_CLIENT_API_KEY,
					flickrClientApiKeyTxtField.getText());
			MainApp.getResource().setUserSettings(
					USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET,
					flickrClientSharedSecretTxtField.getText());
			MainApp.getResource().setUserSettings(
					USER_SETTINGS.FLICKR_CLIENT_NSID,
					flickrClientNsidTxtField.getText());

			String authsDirStr = System.getProperty("user.home")
					+ File.separatorChar + ".flickrAuth";

			try {
				UploadToFlickrUsingFlickr4Java uploadToFlickrUsingFlickr4Java = new UploadToFlickrUsingFlickr4Java(
						MainApp.getResource().getUserSettingsByKey(
								USER_SETTINGS.FLICKR_CLIENT_API_KEY), MainApp.getResource().getUserSettingsByKey(
										USER_SETTINGS.FLICKR_CLIENT_NSID),
										MainApp.getResource().getUserSettingsByKey(
								USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET),
						new File(authsDirStr), null);

				uploadToFlickrUsingFlickr4Java.setAuth(null, null, null);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Resource.getMainApp().showHomepage();
					}
				});
			} catch (FlickrException ex) {
				Resource.getMainApp().showExceptionDialog("FlickrException: ", ex);
//				ex.printStackTrace();
			} catch (IOException | SAXException ex) {
				Resource.getMainApp().showExceptionDialog("IOException || SAXException: ", ex);
//				ex.printStackTrace();
			}

		} else {
			Resource.getMainApp().showErrorDialog("ERROR", "Incomplete Data",
					"All fields cannot be empty");
		}
	}
	
	@FXML
	private void getFlickrApiKey() {
		Resource.getMainApp().getHostServices().showDocument(flickrApiKey.getText());
		changeApiLblColour();
	}
	
	@FXML
	private void getFlickrNsid() {
		Resource.getMainApp().getHostServices().showDocument(flickrNsid.getText());
		changeNsidLblColour();
	}
	
	@FXML
	private void changeApiLblColour() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getApiLbl.setTextFill(Paint.valueOf("#ff0000"));
			}
		});
	}
	
	@FXML
	private void changeNsidLblColour() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getNsidLbl.setTextFill(Paint.valueOf("#ff0000"));
			}
		});
	}
	
	@FXML
	private void originalApiLblColour() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getApiLbl.setTextFill(Paint.valueOf("#0000ff"));
			}
		});
	}

	@FXML
	private void originalNsidLblColour() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				getNsidLbl.setTextFill(Paint.valueOf("#0000ff"));
			}
		});
	}
	
	/*
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
