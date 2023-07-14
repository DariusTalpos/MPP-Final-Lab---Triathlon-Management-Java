package com.client.gui;


import com.model.User;
import com.services.CompetitionException;
import com.services.ICompetitionServices;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class LoginController {

    User appUser;
    ICompetitionServices server;

    MainMenuController mainCtrl;

    AnchorPane mainPane;


    @FXML
    private Button logInButton;
    @FXML
    private Button createAccountButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void loginPressed() throws IOException
    {
        String username = usernameField.getText();
        String password = passwordField.getText();
        appUser = new User(username,password);
        try {
            usernameField.clear();
            passwordField.clear();
            server.login(appUser,mainCtrl);
            mainCtrl.setup(appUser,server);
            Stage stage = new Stage();
            stage.setScene(new Scene(mainPane));
            stage.setTitle("Main Page");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        mainCtrl.logout();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (CompetitionException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                }
            });
            stage.show();
            Stage thisStage = (Stage) logInButton.getScene().getWindow();
            thisStage.close();
        }
        catch (CompetitionException e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
                alert.setHeaderText("There is no account with this username or password");
            alert.setContentText("Please try again!");
            alert.show();
            return;
        }
    }

    public void setup(ICompetitionServices server, MainMenuController mainCtrl, AnchorPane mainPane)
    {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.mainPane = mainPane;
    }
}
