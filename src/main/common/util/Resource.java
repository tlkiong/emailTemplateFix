package main.common.util;

import java.io.File;

import main.MainApp;
import main.common.util.UserSettings.USER_SETTINGS;

public class Resource {
	
	private UserSettings userSettings;
	private static MainApp mainApp;
	private static File folderToProcess;
	
	public Resource(MainApp mainApp){
		this.userSettings = new UserSettings();
		setMainApp(mainApp);
		
		loadDefaultSettings();
		
		if ((getUserSettings().getSettings(
				USER_SETTINGS.FLICKR_CLIENT_API_KEY) != null && !getUserSettings()
				.getSettings(USER_SETTINGS.FLICKR_CLIENT_API_KEY).isEmpty())
				&& (getUserSettings().getSettings(
						USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET) != null && !getUserSettings()
						.getSettings(
								USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET)
						.isEmpty())
				&& (getUserSettings().getSettings(
						USER_SETTINGS.FLICKR_CLIENT_NSID) != null && !getUserSettings()
						.getSettings(USER_SETTINGS.FLICKR_CLIENT_NSID)
						.isEmpty())) {
			getMainApp().showHomepage();
		} else {
			getMainApp().showInitialSettingsView();
		}
	}

	private void loadDefaultSettings() {
		
	}

	public UserSettings getUserSettings() {
		return userSettings;
	}
	
	public String getUserSettingsByKey(USER_SETTINGS key) {
		return getUserSettings().getSettings(key);
	}

	public void setUserSettings(USER_SETTINGS key, String userSettings) {
		this.userSettings.setSettings(key, userSettings);
	}

	public static MainApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(MainApp mainApp) {
		Resource.mainApp = mainApp;
	}

	public void processFolder(File file) {
		setFolderToProcess(file);
		mainApp.showProcessingFolder();
	}
	
	public static File getFolderToProcess(){
		return folderToProcess;
	}
	
	public static void setFolderToProcess(File file){
		folderToProcess = file;
	}
}
