package main.common.util;

import java.util.prefs.Preferences;

public class UserSettings {
	
	private Preferences preferences;
	public enum USER_SETTINGS { 
		FLICKR_ID,
		FLICKR_CLIENT_API_KEY,
		FLICKR_CLIENT_SHARED_SECRET,
		FLICKR_CLIENT_NSID
	}
	
	public UserSettings() {
		preferences = Preferences.userRoot().node(this.getClass().getName());
	}
	
	public void setSettings(USER_SETTINGS key, String value) {
		if (key != null) {
			preferences.put(key.toString(), value);

//			System.out.println(key + " & " + value + " has been set");
		} else {
			System.out.println("Key or value in user settings is empty or null");
		}
	}

	public String getSettings(USER_SETTINGS key) {
		if (key != null) {
			String value = preferences.get(key.toString(), null);

			if (value != null) {
				return value;
			} else {
//				System.out.println("Value returns is null");
				return "";
			}
		} else {
//			System.out.println("Key is null or empty");
			return "";
		}
	}
	
	public void removeSpecificSettings(USER_SETTINGS key) {
		if (key != null) {
			preferences.remove(key.toString());
		} 
	}
	
	public void clearAllSettings(){
		USER_SETTINGS[] allUserSettings = getAllSettings();
		for(int i = 0; i<allUserSettings.length; i++){
			preferences.remove(allUserSettings[i].toString());
		}
	}
	
	public USER_SETTINGS[] getAllSettings(){
		return USER_SETTINGS.values();
	}
}
