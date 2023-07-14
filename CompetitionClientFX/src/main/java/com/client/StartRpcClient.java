package com.client;

import com.client.gui.LoginController;
import com.client.gui.MainMenuController;
import com.network.rpcprotocol.CompetitionServicesRpcProxy;
import com.network.protobuffprotocol.ProtoCompetitionProxy;
import com.services.ICompetitionServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {
    private Stage primaryStage;
    private static int defaultCompetitionPort = 55555;
    private static String defaultServer = "localhost";
    @Override
    public void start(Stage stage) throws Exception {

        Properties clientProps=new Properties();
        try{
            clientProps.load(StartRpcClient.class.getResourceAsStream("/competitionclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        }
        catch (IOException e)
        {
            System.out.println("Cannot find competitionclient.properties "+e);
            return;
        }

        String serverIP = clientProps.getProperty("competition.server.host", defaultServer);
        int serverPort = defaultCompetitionPort;

        try{
            serverPort = Integer.parseInt(clientProps.getProperty("competition.server.port"));
        } catch (NumberFormatException e)
        {
            System.out.println("Wrong port number "+e.getMessage());
            System.out.println("Using default port: " + defaultCompetitionPort);
        }
        System.out.println("Using server IP "+ serverIP);
        System.out.println("Using server port "+ serverPort);

        ICompetitionServices server = new CompetitionServicesRpcProxy(serverIP, serverPort);
        //ICompetitionServices server = new ProtoCompetitionProxy(serverIP, serverPort);

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("login-controller.fxml"));
        AnchorPane root=loader.load();

        LoginController ctrl = loader.getController();

        FXMLLoader loaderMain=new FXMLLoader();
        loaderMain.setLocation(getClass().getResource("mainmenu-controller.fxml"));
        AnchorPane rootMain=loaderMain.load();
        MainMenuController ctrlMain = loaderMain.getController();

        ctrl.setup(server,ctrlMain,rootMain);
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}