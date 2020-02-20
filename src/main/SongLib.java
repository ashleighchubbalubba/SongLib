package main;//Ashleigh Chung, Jason Cheng

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.SongLibController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SongLib extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/SongLib.fxml"));
        Parent root = loader.load();

        SongLibController songLibController = loader.getController();
        songLibController.start(primaryStage);

        primaryStage.setTitle("Song Library - Ashleigh Chung and Jason Cheng");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            try {
                saveList2();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Implement Save User Data");
            primaryStage.close();
        });
    }
    private void saveList2() throws IOException {
        File file = new File("./SongLibList.txt");
        FileWriter W = new FileWriter(file);
        for(int first_index = 0; first_index < SongLibController.obsList.size(); first_index++){
            W.write(SongLibController.obsList.get(first_index).toString2());
        }
        W.close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
