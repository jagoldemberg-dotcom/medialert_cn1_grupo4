import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface QueueResponse { mensaje: string; mensajeId: string; cola: string; publicadoEn: string; }
export interface AlertItem {
  id: number; pacienteRut: string; pacienteNombre: string; tipoSigno: string; valor?: number;
  unidad?: string; severidad: string; estado: string; detalle: string; fechaHora: string;
}
export interface StreamStats {total: number; porTipo: Record<string, number>; porSeveridad: Record<string, number>; actualizadoEn: string;}

@Injectable({providedIn: 'root'})
export class ApiService {
  private readonly base = environment.apiConfig.bffBaseUrl;
  constructor(private readonly http: HttpClient) {}
  health(): Observable<unknown> { return this.http.get(`${this.base}/health`); }
  sendSignal(body: unknown): Observable<QueueResponse> { return this.http.post<QueueResponse>(`${this.base}/colas/senales`, body); }
  sendSummary(body: unknown): Observable<QueueResponse> { return this.http.post<QueueResponse>(`${this.base}/colas/resumenes`, body); }
  alerts(): Observable<AlertItem[]> { return this.http.get<AlertItem[]>(`${this.base}/alertas`); }
  updateAlert(id: number, body: unknown): Observable<AlertItem> { return this.http.put<AlertItem>(`${this.base}/alertas/${id}`, body); }
  events(): Observable<Record<string, unknown>[]> { return this.http.get<Record<string, unknown>[]>(`${this.base}/streaming/eventos?limite=12`); }
  stats(): Observable<StreamStats> { return this.http.get<StreamStats>(`${this.base}/streaming/estadisticas`); }
}
