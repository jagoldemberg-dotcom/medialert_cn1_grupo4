import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MsalBroadcastService, MsalService } from '@azure/msal-angular';
import { AuthenticationResult, EventMessage, EventType, InteractionStatus } from '@azure/msal-browser';
import { Subject, filter, takeUntil } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AlertaVitalRequest, AlertaVitalResponse } from '../models/alerta-vital.model';
import { AlertaVitalService } from '../service/alerta-vital.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class HomeComponent implements OnInit, OnDestroy {
  loginDisplay = false;
  cargando = false;
  mensaje = '';
  error = '';
  filtroEstado = '';
  filtroPacienteRut = '';
  alertas: AlertaVitalResponse[] = [];
  readonly apiGatewayPendiente = this.urlApiGatewayPendiente(environment.apiConfig.bffBaseUrl);
  private readonly destroying$ = new Subject<void>();

  readonly form = this.fb.group({
    pacienteRut: ['', [Validators.required, Validators.pattern(/^\d{1,2}\.?\d{3}\.?\d{3}-[0-9kK]$/)]],
    pacienteNombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(120)]],
    tipoSigno: ['FRECUENCIA_CARDIACA', [Validators.required]],
    valor: [120, [Validators.required, Validators.min(0.1)]],
    unidad: ['lpm', [Validators.required]],
    umbralMinimo: [60, [Validators.required]],
    umbralMaximo: [100, [Validators.required]],
    observacion: [''],
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: MsalService,
    private readonly msalBroadcastService: MsalBroadcastService,
    private readonly alertaVitalService: AlertaVitalService,
  ) {}

  ngOnInit(): void {
    this.msalBroadcastService.msalSubject$
      .pipe(
        filter((msg: EventMessage) => msg.eventType === EventType.LOGIN_SUCCESS),
        takeUntil(this.destroying$),
      )
      .subscribe((result: EventMessage) => {
        const payload = result.payload as AuthenticationResult;
        this.authService.instance.setActiveAccount(payload.account);
      });

    this.msalBroadcastService.inProgress$
      .pipe(
        filter((status: InteractionStatus) => status === InteractionStatus.None),
        takeUntil(this.destroying$),
      )
      .subscribe(() => {
        this.setLoginDisplay();
        if (this.loginDisplay && !this.apiGatewayPendiente) {
          this.cargarAlertas();
        }
      });
  }

  cargarAlertas(): void {
    this.mensaje = '';
    this.error = '';

    if (this.apiGatewayPendiente) {
      this.alertas = [];
      return;
    }

    this.cargando = true;
    this.alertaVitalService.listar(this.filtroEstado || undefined, this.filtroPacienteRut || undefined).subscribe({
      next: (alertas) => {
        this.alertas = alertas;
        this.cargando = false;
      },
      error: (err) => {
        this.error = this.obtenerMensajeError(err);
        this.cargando = false;
      },
    });
  }

  crearAlerta(): void {
    this.mensaje = '';
    this.error = '';

    if (this.apiGatewayPendiente) {
      this.error = 'Antes de guardar, configura la URL real de AWS API Gateway en environment.ts.';
      return;
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error = 'Revisa los campos obligatorios antes de guardar.';
      return;
    }

    const request = this.form.getRawValue() as AlertaVitalRequest;
    if (request.umbralMinimo >= request.umbralMaximo) {
      this.error = 'El umbral mínimo debe ser menor que el umbral máximo.';
      return;
    }

    this.cargando = true;
    this.alertaVitalService.crear(request).subscribe({
      next: (alerta) => {
        this.mensaje = `Alerta creada para ${alerta.pacienteNombre} con gravedad ${alerta.gravedad}.`;
        this.form.patchValue({ valor: 120, observacion: '' });
        this.cargarAlertas();
      },
      error: (err) => {
        this.error = this.obtenerMensajeError(err);
        this.cargando = false;
      },
    });
  }

  marcarAtendida(alerta: AlertaVitalResponse): void {
    this.alertaVitalService.cambiarEstado(alerta.id, { estado: 'ATENDIDA', observacion: 'Atendida desde frontend Angular' }).subscribe({
      next: () => {
        this.mensaje = `Alerta ${alerta.id} marcada como atendida.`;
        this.cargarAlertas();
      },
      error: (err) => (this.error = this.obtenerMensajeError(err)),
    });
  }

  descartar(alerta: AlertaVitalResponse): void {
    this.alertaVitalService.cambiarEstado(alerta.id, { estado: 'DESCARTADA', observacion: 'Descartada desde frontend Angular' }).subscribe({
      next: () => {
        this.mensaje = `Alerta ${alerta.id} descartada.`;
        this.cargarAlertas();
      },
      error: (err) => (this.error = this.obtenerMensajeError(err)),
    });
  }

  eliminar(alerta: AlertaVitalResponse): void {
    this.alertaVitalService.eliminar(alerta.id).subscribe({
      next: () => {
        this.mensaje = `Alerta ${alerta.id} eliminada.`;
        this.cargarAlertas();
      },
      error: (err) => (this.error = this.obtenerMensajeError(err)),
    });
  }

  actualizarFiltroEstado(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.filtroEstado = select.value;
  }

  actualizarFiltroRut(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.filtroPacienteRut = input.value;
  }

  ngOnDestroy(): void {
    this.destroying$.next();
    this.destroying$.complete();
  }

  private setLoginDisplay(): void {
    this.loginDisplay = this.authService.instance.getAllAccounts().length > 0;
  }

  private urlApiGatewayPendiente(url: string): boolean {
    return !url || url.includes('REEMPLAZAR_API_ID') || url.includes('TU_API_ID') || url.includes('execute-api.us-east-1.amazonaws.com/prod/api/bff') && url.includes('REEMPLAZAR');
  }

  private obtenerMensajeError(err: unknown): string {
    if (err instanceof HttpErrorResponse) {
      if (err.status === 0) {
        return 'No se pudo llegar a AWS API Gateway. Revisa que environment.ts tenga la URL real, que CORS permita http://localhost:4200 y que el BFF esté levantado en la VM.';
      }
      if (err.error?.errores?.length) {
        return err.error.errores.join(' | ');
      }
      if (err.error?.message) {
        return err.error.message;
      }
      if (err.error?.detalle) {
        return err.error.detalle;
      }
    }

    if (typeof err === 'object' && err !== null && 'error' in err) {
      const errorBody = (err as { error?: { message?: string; errores?: string[]; detalle?: string } }).error;
      if (errorBody?.errores?.length) {
        return errorBody.errores.join(' | ');
      }
      if (errorBody?.message) {
        return errorBody.message;
      }
      if (errorBody?.detalle) {
        return errorBody.detalle;
      }
    }
    return 'No se pudo completar la operación. Revisa API Gateway, CORS y backend.';
  }
}
