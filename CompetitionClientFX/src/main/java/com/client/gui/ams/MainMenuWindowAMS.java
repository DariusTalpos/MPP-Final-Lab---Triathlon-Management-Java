package com.client.gui.ams;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.notification.Notification;
import com.services.CompetitionException;
import com.services.notification.ICompetitionServicesAMS;
import com.services.notification.NotificationReceiver;
import com.services.notification.NotificationSubscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class MainMenuWindowAMS implements NotificationSubscriber {

    private ICompetitionServicesAMS server;
    private User user;
    private NotificationReceiver receiver;

    @FXML
    private Label greetLabel;
    @FXML
    private Button logOutButton;
    @FXML
    private TableView<Participant> tableViewParticipants;
    @FXML
    private TableColumn<Participant, String> tableColumnNameParticipants;
    @FXML
    private TableColumn<Participant,Integer> tableColumnPointsParticipants;
    ObservableList<Participant> modelParticipants = FXCollections.observableArrayList();

    @FXML
    private TableView<Round> tableViewRounds;
    @FXML
    private TableColumn<Round, String> tableColumnNameRounds;
    ObservableList<Round> modelRounds = FXCollections.observableArrayList();

    @FXML
    private TableView<Score> tableViewScores;
    @FXML
    private TableColumn<Score, String> tableColumnRoundParticipantName;
    @FXML
    private TableColumn<Score, Integer> tableColumnRoundParticipantPoints;
    ObservableList<Score> modelScores = FXCollections.observableArrayList();

    public void setup(User user, ICompetitionServicesAMS server) throws CompetitionException {
        receiver.start(this);
        this.server = server;
        this.user = user;
        greetLabel.setText("Hello, "+user.getUsername()+"!");

        tableColumnNameParticipants.setCellValueFactory(new PropertyValueFactory<Participant, String>("name"));
        tableColumnPointsParticipants.setCellValueFactory(new PropertyValueFactory<Participant, Integer>("fullPoints"));
        refreshParticipantTable();

        tableColumnNameRounds.setCellValueFactory(new PropertyValueFactory<Round, String>("name"));
        refreshRoundTable();

        tableColumnRoundParticipantName.setCellValueFactory(new PropertyValueFactory<Score, String>("participantName"));
        tableColumnRoundParticipantPoints.setCellValueFactory(new PropertyValueFactory<Score, Integer>("points"));
        //refresh(modelParticipants,participantService.getParticipantList(),tableViewParticipants);
    }

    public void refreshParticipantTable() throws CompetitionException {
        modelParticipants.setAll(server.getParticipantList());
        tableViewParticipants.setItems(modelParticipants);
    }

    public void refreshRoundTable() throws CompetitionException {
        modelRounds.setAll(server.getRoundList());
        tableViewRounds.setItems(modelRounds);
    }

    public void refreshScoreTable(String roundName) throws CompetitionException {
        modelScores.setAll(server.getScoreListFromRound(roundName));
        tableViewScores.setItems(modelScores);
    }

    @FXML
    private void logoutPressed() throws IOException, CompetitionException {
        logout();
    }

    public void logout() throws IOException, CompetitionException {

        Stage thisStage = (Stage) logOutButton.getScene().getWindow();
        server.logout(user);
        receiver.stop();
        thisStage.close();
    }

    @FXML
    private void loadRoundDataPressed() throws IOException, CompetitionException {
        Round round = tableViewRounds.getSelectionModel().getSelectedItem();
        if(round ==null)
            noneSelectedError();
        else
            refreshScoreTable(round.getName());
    }

    public void noneSelectedError()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No round selected");
        alert.setContentText("Please select a round!");
        alert.show();
    }

    @FXML
    private void addDataPressed() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/client/ams-addscores-controller.fxml"));
        AnchorPane root = loader.load();

        AddScoresWindowAMS ctrl = loader.getController();
        ctrl.setup(modelParticipants, user, server);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add data");
        stage.show();
    }

    @Override
    public void notificationReceived(Notification notif) {
            try
            {
                System.out.println("Ctrl notificationReceived ... " + notif.getType());
                SwingUtilities.invokeLater(()->{
                    switch (notif.getType()) {
                        case NEW_ROUND -> {
                            try {
                                refreshRoundTable();
                            } catch (CompetitionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case NEW_SCORE -> {
                            try {
                                refreshRoundTable();
                            } catch (CompetitionException e) {
                                throw new RuntimeException(e);
                            }
                            Round round = tableViewRounds.getSelectionModel().getSelectedItem();
                            if(round != null) {
                                try {
                                    refreshScoreTable(round.getName());
                                } catch (CompetitionException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
    }

    public void setReceiver(NotificationReceiver receiver) {
        this.receiver = receiver;
    }
}
