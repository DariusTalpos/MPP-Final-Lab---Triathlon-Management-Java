package com.client;

import com.client.gui.LoginController;
import com.client.gui.MainMenuController;
import com.client.gui.ams.LoginWindowAMS;
import com.client.gui.ams.MainMenuWindowAMS;
import com.network.rpcprotocol.CompetitionServicesRpcProxy;
import com.network.rpcprotocol.ams.CompetitionServerAMSRpcProxy;
import com.services.ICompetitionServices;
import com.services.notification.ICompetitionServicesAMS;
import com.services.notification.NotificationReceiver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartAMSRpcClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loaderMain=new FXMLLoader();
        loaderMain.setLocation(getClass().getResource("ams-mainmenu-controller.fxml"));
        AnchorPane rootMain=loaderMain.load();
        MainMenuWindowAMS ctrlMain = loaderMain.getController();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        NotificationReceiver notificationReceiver = context.getBean("notificationReceiver", NotificationReceiver.class);
        ctrlMain.setReceiver(notificationReceiver);

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("ams-login-controller.fxml"));
        AnchorPane root=loader.load();

        ICompetitionServicesAMS server = context.getBean("competitionServices",CompetitionServerAMSRpcProxy.class);
        LoginWindowAMS ctrl = loader.getController();
        ctrl.setup(server,ctrlMain,rootMain);
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
