package rest.restclient;

import com.model.Participant;
import com.persistence.repository.ParticipantDBRepo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@RestController
@CrossOrigin(origins = "*")
public class ParticipantsRestController {

    private ParticipantDBRepo participantDBRepo;

    public ParticipantsRestController() throws IOException {
        Properties serverProps=new Properties();
        serverProps.load(ParticipantsRestController.class.getResourceAsStream("/competitionserver.properties"));
        participantDBRepo = new ParticipantDBRepo(serverProps);
    }

    @PostMapping("/api/participants")
    public Participant save(@RequestBody Participant participant)
    {
        return participantDBRepo.save(participant);
    }

    @GetMapping("/api/participants")
    public List<Participant> findAll()
    {
        return (List<Participant>) participantDBRepo.findAll();
    }

    @GetMapping("/api/participants/{id}")
    public Participant findOne(@PathVariable Long id)
    {
        return participantDBRepo.findOne(id);
    }

    @PutMapping("/api/participants/{id}")
    public Participant update(@RequestBody Participant participant, @PathVariable Long id)
    {
        participant.setId(id);
        participantDBRepo.update(participant);
        return participant;
    }

    @DeleteMapping("/api/participants/{id}")
    public Participant delete(@PathVariable Long id)
    {
        return participantDBRepo.delete(id);
    }

}
