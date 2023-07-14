package rest;

import com.model.Participant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rest.restclient.ParticipantsRestController;

import java.io.IOException;
import java.util.List;

//@Configuration
//@EnableAutoConfiguration
@SpringBootApplication
public class StartRestServices {

    public static void main(String[] args) throws IOException {
    SpringApplication.run(StartRestServices.class, args);
    findTest();
    //addTest();
   // updateTest(11L);
    //deleteTest(11L);
    }

    private static void deleteTest(Long id) throws IOException {
        ParticipantsRestController participantsRestController = new ParticipantsRestController();
        Participant deletedParticipant = participantsRestController.delete(id);
        System.out.println(deletedParticipant);
    }

    private static void updateTest(Long id) throws IOException {
        ParticipantsRestController participantsRestController = new ParticipantsRestController();

        System.out.println("\nUpdate");
        Participant updatedParticipant = new Participant("Updated RESTER");
        updatedParticipant.setId(id);
        System.out.println(participantsRestController.update(updatedParticipant,3L));
    }

    private static void addTest() throws IOException {
        ParticipantsRestController participantsRestController = new ParticipantsRestController();

        System.out.println("\nSave");
        Participant newParticipant = new Participant("Rester");
        newParticipant = participantsRestController.save(newParticipant);
        System.out.println(newParticipant);
    }

    private static void findTest() throws IOException {
        ParticipantsRestController participantsRestController = new ParticipantsRestController();

        System.out.println("\nFind all");
        List<Participant> participants = participantsRestController.findAll();
        for(Participant participant: participants)
        {
            System.out.println(participant);
        }

        System.out.println("\nFind with id");
        Participant participant = participantsRestController.findOne(1L);
        System.out.println(participant);
    }
}
