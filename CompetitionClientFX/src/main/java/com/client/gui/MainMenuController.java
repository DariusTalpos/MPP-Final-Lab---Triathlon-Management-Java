package com.client.gui;

import com.model.Participant;
import com.model.Round;
import com.model.Score;
import com.model.User;
import com.services.CompetitionException;
import com.services.ICompetitionObserver;
import com.services.ICompetitionServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

public class MainMenuController implements ICompetitionObserver {

    private ICompetitionServices server;
    private User user;


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

    public void setup(User user, ICompetitionServices server) throws CompetitionException {
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
        server.logout(user,this);
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
        loader.setLocation(getClass().getResource("/com/client/addscores-controller.fxml"));
        AnchorPane root = loader.load();

        AddScoresController ctrl = loader.getController();
        ctrl.setup(modelParticipants, user, server);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Log in");
        stage.show();
        Stage thisStage = (Stage) logOutButton.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void newRound() throws CompetitionException {
        Platform.runLater(()->{
            try {
                refreshRoundTable();
            } catch (CompetitionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void newScore(Score score) throws CompetitionException {
        Platform.runLater(()->{
            try {
                refreshParticipantTable();
            } catch (CompetitionException e) {
                throw new RuntimeException(e);
            }
            Round round = tableViewRounds.getSelectionModel().getSelectedItem();
            if(round != null && Objects.equals(round.getName(), score.getRound().getName())) {
                try {
                    refreshScoreTable(round.getName());
                } catch (CompetitionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
