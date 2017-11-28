//Brian Abarquez and Richard Lu
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SongLibController{

	//fxml declarations
	@FXML Button add;
	@FXML Button delete;
	@FXML Button edit;
	@FXML Button cancel;
	@FXML TextField songTitle;
	@FXML TextField songArtist;
	@FXML TextField songAlbum;
	@FXML TextField songYear;
	@FXML TextField showTitle;
	@FXML TextField showArtist;
	@FXML TextField showAlbum;
	@FXML TextField showYear;
	@FXML ListView<Song> songList;
	
	//observable list and size declarations
	private ObservableList<Song> obsList;
	int obsSize = 0;
	
	public void start(Stage mainStage) {
		//create observable array list
		obsList = FXCollections.observableArrayList();
		//file read (persistence through instances)
		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		songList.setItems(obsList); 	
		// select and show the first item
		songList.getSelectionModel().select(0);
		showItem();
		// set listener for the items
		songList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> showItem());
		//writes to file at termination of program 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		    	writeFile();
		    }
		}));
	} 
	//reads file and stores into song objects 
	public void readFile() throws IOException{ 
		File file = new File("SongLib.txt");
		if (file.length() == 0) return;
		Scanner s = new Scanner(file);
		s.useDelimiter(", ");
		List<String> myList = new ArrayList<String>();
	    Song newSong = new Song();
	    while (s.hasNext()) {
	       myList.add(s.next());
	    }
	    s.close();
	    for (int index = 0; index < myList.size()-3; index = index + 4) {
	    	obsSize++;
	    	newSong = new Song(myList.get(index),myList.get(index+1), myList.get(index+2), myList.get(index+3));
				obsList.add(newSong);
	    }
	    songList.setItems(obsList);
	}
	//writes each song to file in order title, artist, album, year,
	public void writeFile(){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("SongLib.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (int index = 0; index < obsList.size(); index++){
			if(obsList.get(index).getTitle().equals("")){}
			else writer.print(obsList.get(index).getTitle() + ", " + obsList.get(index).getArtist() + ", " + obsList.get(index).getAlbum() + ", " + obsList.get(index).getYear() + ", ");
		}
		writer.close();
	}
	//refreshes and shows the list dependent on what happened 
	private void showItem(){
		int index = songList.getSelectionModel().getSelectedIndex();
		//if list is empty
		if(obsSize==0){ 
			showTitle.setText("");
			showArtist.setText("");
			showAlbum.setText("");
			showYear.setText("");
		}
		//if an object in the list is added or deleted
		else if(songList.getSelectionModel().getSelectedIndex()==-1){
			showTitle.setText((obsList.get(obsSize-1).getTitle()));
			showArtist.setText((obsList.get(obsSize-1).getArtist()));
			if(obsList.get(obsSize-1).getAlbum().equals(""))
				showAlbum.setText("");
			else 
				showAlbum.setText((obsList.get(obsSize-1).getAlbum()));
			if(obsList.get(obsSize-1).getYear().equals(""))
				showYear.setText("");
			else
				showYear.setText((obsList.get(obsSize-1).getYear()));
		}
		//every other instance 
		else{
			showTitle.setText((obsList.get(index).getTitle()));
			showArtist.setText((obsList.get(index).getArtist()));
			showAlbum.setText((obsList.get(index).getAlbum()));
			showYear.setText((obsList.get(index).getYear()));
		}
		songTitle.setText("");
		songArtist.setText("");
		songAlbum.setText("");
		songYear.setText("");
	}
	//checks if a song with the given title and artist already exists
	public boolean songCheck(ObservableList<Song> List, Song check){
		for(int i=0; i<List.size();i++){
			if(List.get(i).getTitle().toUpperCase().equals(check.getTitle().toUpperCase())
					&& List.get(i).getArtist().toUpperCase().equals(check.getArtist().toUpperCase())) 
				return true;
		}
		return false;
	}
	//button check 
	public void TheyPushedTheButton (ActionEvent e) {
		Button b = (Button)e.getSource();
		Alert error = new Alert(AlertType.ERROR); 
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		int index = songList.getSelectionModel().getSelectedIndex();
		int changedIndex = index;
		Song newSong = new Song();
		Song oldSong = new Song();
		//add button interaction
		if (b == add){
			//checks if song title + artist combination already exists
			if(songTitle.getText().equals("")||songArtist.getText().equals("")){
				error.setTitle("ERROR");
				error.setHeaderText("Please enter AT LEAST a Title and Artist");
				String content = "Title Entered: " + songTitle.getText() + "\nArtist Entered: " + songArtist.getText();
				error.setContentText(content); 
				error.showAndWait();
				return;
			}
			else{
				//increases observable list size counter and creates new song
				String newTitle, newArtist, newAlbum, newYear;
				newTitle = songTitle.getText();
				newArtist = songArtist.getText();
				newAlbum = songAlbum.getText();
				newYear = songYear.getText();
				newSong = new Song(newTitle, newArtist, newAlbum, newYear);
				//checks if song title + artist combination already exists
				if(songCheck(obsList,newSong)){
					error.setTitle("ERROR");
					error.setHeaderText("Please enter a DIFFERENT Title and Artist");
					String content = "There exists a song with this title and artist";
					error.setContentText(content); 
					error.showAndWait();
					return;
				}
				//adds the new song list if it passes the check 
				else{
					//confirmation of add
					confirm.setTitle("Add");
					confirm.setHeaderText("Are you sure you want to add this song?");
					String content = "Title: " + songTitle.getText() + "\nArtist: " + songArtist.getText() + "\nAlbum: " + songAlbum.getText() + "\nYear: " + songYear.getText();
					confirm.setContentText(content); 
					Optional<ButtonType> result = confirm.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.CANCEL) {
						showItem(); 
						return;
					}
					obsList.add(newSong);
					obsSize++;
				}
				songList.setItems(obsList);
			}
		}
		//delete button interaction
		if (b == delete){
			//if list is empty error
			if(obsList.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("Nothing in Songs List");
				String content = "You are trying to delete nothing";
				error.setContentText(content); 
				error.showAndWait(); 
				return;
			}
			//else it deletes from the list
			else{
				//confirmation of deletion
				confirm.setTitle("Delete");
				confirm.setHeaderText("Are you sure you want to delete this song?");
				String content = "Title: " + showTitle.getText() + "\nArtist: " + showArtist.getText() + "\nAlbum: " + showAlbum.getText() + "\nYear: " + showYear.getText();
				confirm.setContentText(content); 
				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					showItem();  
					return;
				}
				obsSize--;
				ObservableList<Song> newList = FXCollections.observableArrayList(); 
				newList.addAll(obsList);
				newList.remove(index);
				songList.setItems(newList);
				obsList = newList;
			}
		}
		//edit button interaction
		if (b == edit){
			String newTitle, newArtist, newAlbum, newYear;
			newTitle = songTitle.getText();
			newArtist = songArtist.getText();
			newAlbum = songAlbum.getText();
			newYear = songYear.getText();
			newSong = new Song(newTitle, newArtist, newAlbum, newYear);
			//if list is empty error
			if(obsList.isEmpty()){
				error.setTitle("ERROR");
				error.setHeaderText("Nothing in Songs List");
				String content = "You are trying to edit nothing";
				error.setContentText(content); 
				error.showAndWait(); 
				return;
			}
			//if the edits they make conflict with an already existing song
			else if(songCheck(obsList,newSong)){
				error.setTitle("ERROR");
				error.setHeaderText("Please enter a DIFFERENT Title and Artist");
				String content = "There exists a song with this title and artist";
				error.setContentText(content); 
				error.showAndWait();
				return;
			}
			//checks if there is actually any edit being made
			else if(songTitle.getText().equals("")&&songArtist.getText().equals("")&&songAlbum.getText().equals("")&&songYear.getText().equals("")){
					error.setTitle("ERROR");
					error.setHeaderText("Please enter something");
					String content = "There are no edits being made here";
					error.setContentText(content); 
					error.showAndWait();	
					return;
			}
			//checks if any of the fields are empty, empty fields are not changed 
			else{
				//confirmation of edit
				confirm.setTitle("Edit");
				confirm.setHeaderText("Are you sure you want to edit this song?");
				String content = "Title: " + showTitle.getText() + "\nArtist: " + showArtist.getText() + "\nAlbum: " + showAlbum.getText() + "\nYear: " + showYear.getText()
				+ "\nNew Title: " + songTitle.getText() + "\nNew Artist: " + songArtist.getText() + "\nNew Album: " + songAlbum.getText() + "\nNew Year: " + songYear.getText();
				confirm.setContentText(content); 
				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					showItem(); 
					return;
				} 
				oldSong = obsList.get(index);
				if(!songTitle.getText().equals("")){
					obsList.get(index).setTitle(songTitle.getText());
					obsList.set(index, obsList.get(index));
				}
				if(!songArtist.getText().equals("")) obsList.get(index).setArtist(songArtist.getText());
				if(!songAlbum.getText().equals("")) obsList.get(index).setAlbum(songAlbum.getText());
				if(!songYear.getText().equals("")) obsList.get(index).setYear(songYear.getText());
			}
		}	
		if(obsList.isEmpty()){}
		else{
			//sorts the list 
			Collections.sort(obsList, Song.YearSort);
			Collections.sort(obsList, Song.AlbumSort);
			Collections.sort(obsList, Song.ArtistSort);
			Collections.sort(obsList, Song.TitleSort);
			//if add  button is used, goes to newly added song
			if(b == add) changedIndex = obsList.indexOf(newSong);
			//if edit button is used, goes to the new index of song
			if(b == edit) changedIndex = obsList.indexOf(oldSong);
			//if delete button is used goes to next song
			if(b == delete && index<obsList.size()) changedIndex = index;
			//if there is no next song, then it goes to the previous song
			else if(b == delete) changedIndex = index-1;
			songList.getSelectionModel().select(changedIndex);
			songList.scrollTo(changedIndex);
		}
	}
}
