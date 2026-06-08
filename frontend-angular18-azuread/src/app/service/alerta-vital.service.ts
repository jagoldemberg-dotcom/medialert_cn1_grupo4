import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AlertaVitalRequest, AlertaVitalResponse, EstadoAlertaUpdateRequest } from '../models/alerta-vital.model';

@Injectable({
  providedIn: 'root',
})
export class AlertaVitalService {
  private readonly baseUrl = `${environment.apiConfig.bffBaseUrl}/alertas`;
  private readonly jsonHeaders = new HttpHeaders({ 'Content-Type': 'application/json; charset=UTF-8' });

  constructor(private readonly http: HttpClient) {}

  listar(estado?: string, pacienteRut?: string): Observable<AlertaVitalResponse[]> {
    let params = new HttpParams();
    if (estado) {
      params = params.set('estado', estado);
    }
    if (pacienteRut) {
      params = params.set('pacienteRut', pacienteRut);
    }
    return this.http.get<AlertaVitalResponse[]>(this.baseUrl, { params });
  }

  buscarPorId(id: number): Observable<AlertaVitalResponse> {
    return this.http.get<AlertaVitalResponse>(`${this.baseUrl}/${id}`);
  }

  crear(request: AlertaVitalRequest): Observable<AlertaVitalResponse> {
    return this.http.post<AlertaVitalResponse>(this.baseUrl, request, { headers: this.jsonHeaders });
  }

  actualizar(id: number, request: AlertaVitalRequest): Observable<AlertaVitalResponse> {
    return this.http.put<AlertaVitalResponse>(`${this.baseUrl}/${id}`, request, { headers: this.jsonHeaders });
  }

  cambiarEstado(id: number, request: EstadoAlertaUpdateRequest): Observable<AlertaVitalResponse> {
    return this.http.put<AlertaVitalResponse>(`${this.baseUrl}/${id}/estado`, request, { headers: this.jsonHeaders });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
