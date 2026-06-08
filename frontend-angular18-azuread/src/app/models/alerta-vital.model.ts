export interface AlertaVitalRequest {
  pacienteRut: string;
  pacienteNombre: string;
  tipoSigno: string;
  valor: number;
  unidad: string;
  umbralMinimo: number;
  umbralMaximo: number;
  observacion?: string;
}

export interface EstadoAlertaUpdateRequest {
  estado: 'NUEVA' | 'ATENDIDA' | 'DESCARTADA';
  observacion?: string;
}

export interface AlertaVitalResponse {
  id: number;
  pacienteRut: string;
  pacienteNombre: string;
  tipoSigno: string;
  valor: number;
  unidad: string;
  umbralMinimo: number;
  umbralMaximo: number;
  gravedad: string;
  estado: string;
  mensaje: string;
  observacion?: string;
  fechaRegistro: string;
  fechaActualizacion: string;
}
