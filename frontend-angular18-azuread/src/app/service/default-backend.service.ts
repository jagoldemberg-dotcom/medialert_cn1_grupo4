import { Injectable } from '@angular/core';
import { AlertaVitalService } from './alerta-vital.service';

@Injectable({
  providedIn: 'root',
})
export class DefaultBackendService {
  constructor(private readonly alertaVitalService: AlertaVitalService) {}

  public consumirBackend() {
    return this.alertaVitalService.listar();
  }
}
