package cl.duoc.medialert;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Service
public class SenalProducer {
 private final KafkaTemplate<String,SenalVital> kafka; private final Random random=new Random(); private final AtomicLong sent=new AtomicLong();
 public SenalProducer(KafkaTemplate<String,SenalVital> kafka){this.kafka=kafka;}
 @Scheduled(fixedRateString="${medialert.generation-ms:1000}")
 public void generar(){ long paciente=1+random.nextInt(5); int fc=55+random.nextInt(90); int ps=95+random.nextInt(95); int pd=60+random.nextInt(55); double t=Math.round((35.5+random.nextDouble()*4.2)*10.0)/10.0; publicar(new SenalVital(paciente,fc,ps,pd,t,Instant.now())); }
 public void publicar(SenalVital s){ SenalVital value=s.conFecha(); kafka.send("senales_vitales",String.valueOf(value.pacienteId()),value); sent.incrementAndGet(); }
 public Map<String,Object> estado(){return Map.of("servicio","productor-senales","estado","UP","senalesPublicadas",sent.get());}
}
