package cl.duoc.medialert;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/senales")
public class SenalController {
 private final SenalProducer service; public SenalController(SenalProducer service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.ACCEPTED) public Map<String,Object> publicar(@Valid @RequestBody SenalVital s){service.publicar(s);return Map.of("mensaje","Señal publicada en Kafka");}
 @GetMapping("/estado") public Map<String,Object> estado(){return service.estado();}
}
