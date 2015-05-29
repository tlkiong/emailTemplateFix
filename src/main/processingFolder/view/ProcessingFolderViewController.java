package main.processingFolder.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import main.MainApp;
import main.common.util.Resource;
import main.common.util.UploadToFlickrUsingFlickr4Java;
import main.common.util.UserSettings.USER_SETTINGS;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.xml.sax.SAXException;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ProcessingFolderViewController {
	private HashSet<File> nonHtmlSet = new HashSet<File>();
	private HashSet<File> htmlSet = new HashSet<File>();
	private BiMap<String, String> imgNameToUrl = HashBiMap.create();
	// Flickr4Java
	UploadMetaData metaData;
	UploadToFlickrUsingFlickr4Java uploadToFlickrUsingFlickr4Java;
	private String authsDirStr = System.getProperty("user.home")
			+ File.separatorChar + ".flickrAuth";
	private String MY_FLICKR_CLIENT_API_KEY = MainApp.getResource().getUserSettings()
			.getSettings(USER_SETTINGS.FLICKR_CLIENT_API_KEY);
	private String MY_FLICKR_CLIENT_SHARED_SECRET = MainApp.getResource().getUserSettings()
			.getSettings(USER_SETTINGS.FLICKR_CLIENT_SHARED_SECRET);
	// https://www.flickr.com/services/api/explore/?method=flickr.people.getInfo
	private String MY_FLICKR_CLIENT_NSID = MainApp.getResource().getUserSettings()
			.getSettings(USER_SETTINGS.FLICKR_CLIENT_NSID);
	// private String FLICKR_UPLOAD_URL = "https://api.imgur.com/3/image";
	private String MY_FLICKR_CLIENT_ACCESS_TOKEN = null; // Optional entry.
	private String MY_FLICKR_CLIENT_TOKEN_SECRET = null; // Optional entry.
	private String MY_FLICKR_CLIENT_USERNAME = null;
	private boolean MY_FLICKR_CLIENT_SET_TAG_NAME = true; // Default to true to
															// add tag while
															// uploading.

	@FXML
	private ListView<String> nonHtmlListView;
	@FXML
	private ListView<String> htmlListView;
	@FXML
	private Label statusLbl;

	@FXML
	private void initialize() {
		System.out.println("hree?");
		setStatusLblTxt("Initalized~");
		metaData = new UploadMetaData();
		System.out.println("\t authsDirStr: " + authsDirStr);
		try {
			uploadToFlickrUsingFlickr4Java = new UploadToFlickrUsingFlickr4Java(
					MY_FLICKR_CLIENT_API_KEY, MY_FLICKR_CLIENT_NSID,
					MY_FLICKR_CLIENT_SHARED_SECRET, new File(authsDirStr),
					MY_FLICKR_CLIENT_USERNAME);
		} catch (FlickrException ex) {
			Resource.getMainApp().showExceptionDialog("Flickr Error: ",
					ex);
			// e.printStackTrace();
		}

		uploadToFlickrUsingFlickr4Java
				.setSetorigfilenametag(MY_FLICKR_CLIENT_SET_TAG_NAME);
		try {
			uploadToFlickrUsingFlickr4Java.setAuth(
					MY_FLICKR_CLIENT_ACCESS_TOKEN, MY_FLICKR_CLIENT_USERNAME,
					MY_FLICKR_CLIENT_TOKEN_SECRET);
		} catch (IOException | SAXException | FlickrException ex) {
			Resource.getMainApp().showExceptionDialog("Flickr Error: ",
					ex);
			// e.printStackTrace();
		}

		startProcessingFolder();
	}

	public Label getStatusLbl() {
		return statusLbl;
	}

	public void setStatusLblTxt(String statusLbl) {
		this.statusLbl.setText(statusLbl);
	}

	private void startProcessingFolder() {
		setStatusLblTxt("Starting to process folder~");

		Iterator<File> result = FileUtils.iterateFilesAndDirs(
				Resource.getFolderToProcess(), FileFileFilter.FILE,
				TrueFileFilter.TRUE);
		while (result.hasNext()) {
			File file = result.next();
			if(!file.isDirectory()){
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(
						"html")) {
					htmlSet.add(file);
					htmlListView.getItems().add(file.getName());
//					System.out.println(file);
				} else {
					nonHtmlSet.add(file);
					nonHtmlListView.getItems().add(file.getName());
				}
			}
		}
		setStatusLblTxt("'"+Resource.getFolderToProcess().getName()+"' folder processed complete");
	}

	public void uploadImageToFlickr() {
		boolean fail = false;
		for(File filePath : nonHtmlSet){
			if (filePath != null) {
				String fileNameWithExtension = filePath.toString().substring(
						filePath.toString().lastIndexOf("/") + 1);
				String fileNameWithoutExtension = fileNameWithExtension.substring(
						0, fileNameWithExtension.lastIndexOf("."));

				metaData.setTitle(fileNameWithoutExtension);
				metaData.setFilename(fileNameWithExtension);

				if (!uploadToFlickrUsingFlickr4Java.canUpload()) {
					Resource.getMainApp().showErrorDialog("ERROR",
							"Image Upload Error", "Unable to upload image to flickr");
				} else {
					try {
						uploadToFlickrUsingFlickr4Java.getPrivacy();
						uploadToFlickrUsingFlickr4Java.getPhotosetsInfo();
						if (metaData.getFilename() != null
								&& !metaData.getFilename().equals("")) {
							uploadToFlickrUsingFlickr4Java.getSetPhotos(metaData
									.getFilename());
						}
						String photoId = uploadToFlickrUsingFlickr4Java.processFileArg(
								filePath, metaData.getFilename());
						System.out.println("photoId: " + photoId);
						if (!photoId.isEmpty() && photoId != null && !photoId.equals("")) {
							Photo photoInfo = uploadToFlickrUsingFlickr4Java.getPhotoURL(
									photoId, "");
							if (photoInfo != null) {
								imgNameToUrl.put(fileNameWithExtension, photoInfo.getOriginalUrl());
								//TODO: update the progress to done
							} else {
								//TODO: update progress to fail
								System.out.println("fail to upload?");
								fail = true;
								break;
							}
						} else {
							System.out.println("Please upload an image file");
						}
					} catch (Exception e) {
						// e.printStackTrace();
						Resource.getMainApp().showExceptionDialog("Upload Image Error", e);
					}
				}
			}
		}
		
		if(!fail){
			addImgUrlToHtml();
		}
	}
	
	private void addImgUrlToHtml(){
		
	}
	
	private int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	
}
