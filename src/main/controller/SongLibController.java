//Ashleigh Chung, Jason Cheng
package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;


public class SongLibController implements Initializable{

    @FXML
    private TextField addYear;

    @FXML
    private TextField addName;

    @FXML
    private TextField addAlbum;

    @FXML
    private TextField addArtist;

    @FXML
    private TextField editYear;

    @FXML
    private TextField editName;

    @FXML
    private TextField editAlbum;

    @FXML
    private TextField editArtist;

    @FXML
    private ListView<String> songList;

    public static ObservableList<Song> obsList;

    @FXML
    private TextArea selectedSongInfo;

    private int first_index = 0;

    private int curr_index = 0;

    private String textFilePath = "./SongLibList.txt";

    public void initialize(URL location, ResourceBundle resources){
        //Load in Data
        obsList = FXCollections.observableArrayList();
        songList.getSelectionModel().select(curr_index);
    }

    public void start(Stage mainStage) {
        //set listener for the items
        try {
            Scanner fileIn = new Scanner(new File(this.textFilePath));

            while(fileIn.hasNextLine() == true){
                //Reading the information of a song from the text file
                String name = fileIn.nextLine();

                String artist = fileIn.nextLine();

                String year = fileIn.nextLine();
                int int_year;
                try {
                    int_year = Integer.parseInt(year);
                }catch(NumberFormatException e){
                    int_year = -1;
                }
                String album = fileIn.nextLine();

                //Creating new Song object and assigning values to song object
                Song song = new Song();

                song.setName(name);
                song.setArtist(artist);
                song.setYear(int_year);
                song.setAlbum(album);

                obsList.add(song);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        update();
        songList.getSelectionModel().select(0);
        showItem();

        songList
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldVal, newVal) -> showItem());
    }

    private void showItem(){//when user selects song in the listview
        curr_index = songList.getSelectionModel().getSelectedIndex();
        if(curr_index == -1){
            selectedSongInfo.setText("");
            editName.clear();
            editArtist.clear();
            editAlbum.clear();
            editYear.clear();
            return;
        }
        Song song = obsList.get(curr_index);
        String context = song.toString();
        selectedSongInfo.setText(context);
        editName.setText(song.getName());
        editArtist.setText(song.getArtist());
        editAlbum.setText(song.getAlbum());
        String string_year;
        if (song.getYear() >= 0){
            string_year = Integer.toString(song.getYear());
        }else{
            string_year = "";
        }
        editYear.setText(string_year);
    }

    private void update(){
        FXCollections.sort(obsList);
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Song song : obsList) {
            list.add(song.getName() + " - " + song.getArtist());
        }
        songList.setItems(list);
    }

//    Creating Alerts
    private void alert(String title, String message) {
        Alert alert =
                new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);


        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private boolean isDuplicate(Song other){
        for(Song song: obsList){
            if(song.compareTo(other) == 0){
                return true;
            }
        }
        return false;
    }
    private boolean isDuplicate(Song other, int index){
        int counter = 0;
        for(Song song: obsList){
            if(song.compareTo(other) == 0 && counter != index){
                return true;
            }
            counter++;
        }
        return false;
    }

    @FXML
    private void addSong(ActionEvent event) {

        //Input for name
        String name = addName.getText().strip();
        if (name.length() == 0) {
            alert("Name Error", "Name cannot be left empty!");
            return;
        }

        //Input for Artist
        String artist = addArtist.getText().strip();
        if (artist.length() == 0) {
            alert("Artist Error", "Artist cannot be left empty!");
            addArtist.clear();
            return;
        }
        //Input for Album
        String album = addAlbum.getText().strip();// Optional Input

        //Input Check for Year
        String year = addYear.getText().strip();
        int int_year;
        if(year.equals("")){
            int_year = -1;
        }
        else if(year.matches("\\d+")){
            int_year = Integer.parseInt(year);
        }else{
            alert("Year Error", "Year is invalid!");
            addYear.clear();
            return;
        }
//        if (int_year > 2020){
//            alert("Year Error", "Year cannot be in the future!");
//            addYear.clear();
//            return;
//        }

        //Creating new Song object and assigning values to song object
        Song song = new Song();
        song.setName(name);
        song.setArtist(artist);
        //Check if song is within the observable list already
        //Have to check both name and album if one of them match is not enough
        if (isDuplicate(song) == true){
            alert("Song Error", "This Song already exist!");
            addName.clear();
            addArtist.clear();
            return;
        }
        song.setYear(int_year);
        song.setAlbum(album);
        if (!confirm("Add Confirmation", "Are you sure you want to add?")){
            return;
        }
        obsList.add(song);
        update();
        int index_of_song = obsList.indexOf(song);
        songList.getSelectionModel().select(index_of_song);
        showItem();

        addName.clear();
        addArtist.clear();
        addAlbum.clear();
        addYear.clear();
    }

    @FXML
    private void editSong(ActionEvent event) {
        //onAction="#editSong" in fxml
        //Input for name
        String name = editName.getText().strip();
        if(name.length() == 0){
            alert("Name Error", "Cannot have a song with no name!");
            showItem();
            return;
        }
        //Input for Artist
        String artist = editArtist.getText().strip();
        if(artist.length() == 0){
            alert("Artist Error", "Cannot have a song with no artist!");
            showItem();
            return;
        }
        //Input for Album
        String album = editAlbum.getText().strip();// Optional Input

        //Input Check for Year
        String year = editYear.getText().strip();
        int int_year;
        if(year.equals("")){
            int_year = -1;
        }
        else if(year.matches("\\d+")){
            int_year = Integer.parseInt(year);
        }else{
            alert("Year Error", "Year is invalid!");
            //editYear.clear();
            return;
        }
//        if (int_year > 2020){
//            alert("Year Error", "Year cannot be in the future!");
//            //editYear.clear();
//            return;
//        }
        int index = songList.getSelectionModel().getSelectedIndex();//assumming that the index of songList == obsList
        if(index == -1){
            alert("Selection Error", "No song was selected for editing");
            return;
        }
        Song song = obsList.get(index);

        //Setting
//        String new_name = "";
//        String new_artist = "";
//        String new_album = "";
//        int new_year;
//        boolean changed = false;
//        if (name.length() > 0) {
//            new_name = name;
//            changed = true;
//        }else{
//            new_name = song.getName();
//        }
//        if (artist.length() > 0) {
//            new_artist = artist;
//            changed = true;
//        }else{
//            new_artist = song.getArtist();
//        }
//        if(album.length() > 0) {
//            new_album = album;
//        }else {
//            new_album = song.getAlbum();
//        }
//        if(int_year != -1) {
//            new_year = int_year;
//        }else {
//            new_year = song.getYear();
//        }
        //if(changed) {
            if (isDuplicate(new Song(name, artist), index) == true) {
                alert("Song Error", "This Song already exist!");
//                editName.clear();
//                editArtist.clear();
                return;
            }
        //}
        if (!confirm("Edit Confirmation", "Are you sure you want to edit?")){
            return;
        }
        //Creating new Song object and assigning values to song object
        song.setName(name);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setYear(int_year);


        editName.clear();
        editArtist.clear();
        editAlbum.clear();
        editYear.clear();

        obsList.set(index, song);
        update();
        int index_of_song = obsList.indexOf(song);
        songList.getSelectionModel().select(index_of_song);
        showItem();

    }

    @FXML
    private void deleteSong(ActionEvent event) { //onAction="#deleteSong" in fxml
        int index = songList.getSelectionModel().getSelectedIndex();//what happens when their is nothing to delete?
        if(index == -1){//Delete when there is no Elements
            return;
        }
        if(!confirm("Delete Confirmation", "Are you sure you want to delete?")){
            return;
        }
        int new_index = -1;
        if (index != obsList.size() - 1) {
            new_index = index;
        } else {
            if (index != 0 && obsList.size() != 1) {
                new_index = index - 1;
            }else{//Deletion on the last element
                curr_index = -1;
                obsList.remove(index);
                update();
                return;
            }
        }


        obsList.remove(index);
        update();
        songList.getSelectionModel().select(new_index);
        showItem();


    }

    @FXML
    private void saveList(ActionEvent event) throws IOException { //onAction="#saveList" in fxml
        File file = new File(this.textFilePath);
        FileWriter W = new FileWriter(file);
        for(first_index = 0; first_index < obsList.size(); first_index++){
            W.write(obsList.get(first_index).toString2());
        }
        W.close();
    }
}


