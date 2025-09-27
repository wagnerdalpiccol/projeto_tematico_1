package com.projetotematico1.notesflow;

import com.projetotematico1.notesflow.model.enums.Screen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        NavigationManager.navigateTo(stage, Screen.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}