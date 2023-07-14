package com.client.gui.ams;

import com.client.gui.MainMenuController;
import com.model.Participant;
import com.model.User;
import com.services.CompetitionException;
import com.services.ICompetitionServices;
import com.services.notification.ICompetitionServicesAMS;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AddScoresWindowAMS {
    @FXML
    private Button returnButton;
    @FXML
    private TableView<Participant> tableViewParticipants;
    @FXML
    private TableColumn<Participant, String> tableColumnNameParticipants;
    @FXML
    private TextField roundNameField;
    @FXML
    private TextField roundPointsField;
    private ObservableList<Participant> modelParticipants;

    private ICompetitionServicesAMS server;
    private User user;
    public void setup(ObservableList<Participant> modelParticipants, User user,ICompetitionServicesAMS server)
    {
        this.server = server;
        this.user = user;
        this.modelParticipants = modelParticipants;
        tableColumnNameParticipants.setCellValueFactory(new PropertyValueFactory<Participant, String>("name"));
        tableViewParticipants.setItems(modelParticipants);

    }

    public void saveDataPressed() throws IOException
    {
        String roundName = roundNameField.getText();
        Participant participant = tableViewParticipants.getSelectionModel().getSelectedItem();
        if(Objects.equals(roundName, "") || participant == null)
        {
            noneSelectedError();
            return;
        }
        try {
            int roundPoints = Integer.parseInt(roundPointsField.getText());
            server.addRoundScore(roundName,participant,roundPoints);
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Success");
            confirmation.setHeaderText("Data saved");
            confirmation.setContentText("The new score has been registered");
            confirmation.show();
        }
        catch(Exception e)
        {
            noneSelectedError();
            return;
        }
    }

    public void returnToMainPressed() throws IOException, CompetitionException {
        Stage thisStage = (Stage) returnButton.getScene().getWindow();
        thisStage.close();
    }

    public void noneSelectedError()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("ERROR");
        alert.setContentText("Please complete the fields accordingly!");
        alert.show();
    }
}
