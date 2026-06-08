package cl.duoc.alertas.bff.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cl.duoc.alertas.bff.dto.AlertaVitalRequest;
import cl.duoc.alertas.bff.dto.AlertaVitalResponse;
import cl.duoc.alertas.bff.dto.EstadoAlertaUpdateRequest;

@FeignClient(name = "alertaVitalClient", url = "${alertas.microservice.url}")
public interface AlertaVitalClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<AlertaVitalResponse> listar(@RequestParam(name = "estado", required = false) String estado,
                                      @RequestParam(name = "pacienteRut", required = false) String pacienteRut);

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    AlertaVitalResponse buscarPorId(@PathVariable("id") Long id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AlertaVitalResponse crear(@RequestBody AlertaVitalRequest request);

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AlertaVitalResponse actualizar(@PathVariable("id") Long id, @RequestBody AlertaVitalRequest request);

    @PutMapping(value = "/{id}/estado", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AlertaVitalResponse cambiarEstado(@PathVariable("id") Long id, @RequestBody EstadoAlertaUpdateRequest request);

    @DeleteMapping("/{id}")
    void eliminar(@PathVariable("id") Long id);
}
