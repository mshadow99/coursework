package Grava;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    Stage window;
    Button button;
    // Stage and Button object created
    static boolean answer;
    public static boolean display(String title, String message) {
        // display method created with title and message as parameters
        Stage window = new Stage();
        // Stage object window created
        Image image = new Image("Grava.logo.png");
        window.getIcons().add(image);
        // Stage's image icon set
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        // Stage's title, modality and size set

        Label label = new Label();
        label.setText(message);
        // label object created and label's text set as message
        Button yesButton = new Button("yes");
        Button noButton = new Button("no");
        // yes and no buttons created
        yesButton.setOnAction(e -> {// lambda that if yesButton selected then answer = true and window closes
            answer = true;
            window.close();// Closes Stage
        });

        noButton.setOnAction(e -> {// lambda that if noButton selected then answer = false and window closes
            answer = false;
            window.close();// Closes Stage
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        // Vbox object layout created ,items added and position set to center
        Scene scene = new Scene(layout);
        window.setScene(scene);
        // Scene object created with layout added and window scene set
        window.showAndWait();
        // window prompt set to show and wait until an action is complete
        return answer;
    }
}
