import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subscription, forkJoin, interval, startWith, switchMap } from 'rxjs';
import { ApiService, AlertItem, StreamStats } from './api.service';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root', standalone: true, imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html', styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  public readonly auth = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ApiService);

  readonly loading = signal(false);
  readonly message = signal('');
  readonly error = signal('');
  readonly alerts = signal<AlertItem[]>([]);
  readonly events = signal<Record<string, unknown>[]>([]);
  readonly stats = signal<StreamStats | null>(null);
  private refreshSub?: Subscription;

  readonly signalForm = this.fb.nonNullable.group({
    pacienteRut: ['12.345.678-9', [Validators.required, Validators.pattern(/^\d{1,2}\.\d{3}\.\d{3}-[0-9Kk]$/)]],
    pacienteNombre: ['Paciente Demo', [Validators.required, Validators.maxLength(120)]],
    tipoSigno: ['FRECUENCIA_CARDIACA', Validators.required],
    valor: [135, [Validators.required, Validators.min(0.1)]],
    unidad: ['lpm', Validators.required],
    umbralMinimo: [60, Validators.required],
    umbralMaximo: [100, Validators.required],
    observacion: ['Demostración de señal crítica', Validators.maxLength(500)]
  });
  readonly summaryForm = this.fb.nonNullable.group({
    pacienteRut: ['12.345.678-9', [Validators.required, Validators.pattern(/^\d{1,2}\.\d{3}\.\d{3}-[0-9Kk]$/)]],
    pacienteNombre: ['Paciente Demo', Validators.required],
    periodoMinutos: [15, [Validators.required, Validators.min(1), Validators.max(1440)]],
    cantidadMediciones: [15, [Validators.required, Validators.min(1)]],
    promedioFrecuenciaCardiaca: [104, [Validators.required, Validators.min(20), Validators.max(250)]],
    promedioSaturacionOxigeno: [92, [Validators.required, Validators.min(50), Validators.max(100)]],
    observacion: ['Resumen para demostración', Validators.maxLength(500)]
  });


  async ngOnInit(): Promise<void> {
    await this.auth.initialize();
    if (this.auth.account()) this.startRefresh();
  }
  ngOnDestroy(): void { this.refreshSub?.unsubscribe(); }

  async login(): Promise<void> { await this.auth.login(); this.startRefresh(); }
  async logout(): Promise<void> { this.refreshSub?.unsubscribe(); await this.auth.logout(); }

  sendSignal(): void {
    this.clearStatus();
    if (this.signalForm.invalid) { this.signalForm.markAllAsTouched(); this.error.set('Corrige los campos marcados.'); return; }
    const v = this.signalForm.getRawValue();
    if (v.umbralMinimo >= v.umbralMaximo) { this.error.set('El umbral mínimo debe ser menor que el máximo.'); return; }
    this.loading.set(true);
    this.api.sendSignal(v).subscribe({next:r=>{this.message.set(`${r.mensaje}. ID: ${r.mensajeId}`);this.loading.set(false);setTimeout(()=>this.refresh(),1500);},error:e=>this.fail(e)});
  }
  sendSummary(): void {
    this.clearStatus();
    if (this.summaryForm.invalid) { this.summaryForm.markAllAsTouched(); this.error.set('Corrige los campos marcados.'); return; }
    this.loading.set(true);
    this.api.sendSummary(this.summaryForm.getRawValue()).subscribe({next:r=>{this.message.set(`${r.mensaje}. ID: ${r.mensajeId}`);this.loading.set(false);setTimeout(()=>this.refresh(),1500);},error:e=>this.fail(e)});
  }
  attend(a: AlertItem): void {
    this.api.updateAlert(a.id,{estado:'ATENDIDA',detalle:a.detalle,severidad:a.severidad}).subscribe({next:()=>this.refresh(),error:e=>this.fail(e)});
  }
  refresh(): void {
    forkJoin({alerts:this.api.alerts(),events:this.api.events(),stats:this.api.stats()}).subscribe({
      next:r=>{this.alerts.set(r.alerts);this.events.set(r.events);this.stats.set(r.stats);}, error:e=>this.fail(e)
    });
  }
  fieldInvalid(form: 'signal' | 'summary', name: string): boolean {
    if (form === 'signal') {
      const control = this.signalForm.get(name);
      return !!control && control.invalid && (control.dirty || control.touched);
    }

    const control = this.summaryForm.get(name);
    return !!control && control.invalid && (control.dirty || control.touched);
  }
  objectEntries(value: Record<string, number>|undefined): [string,number][] { return Object.entries(value ?? {}); }
  severityCount(name: string): number { return this.stats()?.porSeveridad?.[name] ?? 0; }
  private startRefresh(): void { this.refreshSub?.unsubscribe(); this.refreshSub=interval(5000).pipe(startWith(0),switchMap(()=>forkJoin({alerts:this.api.alerts(),events:this.api.events(),stats:this.api.stats()}))).subscribe({next:r=>{this.alerts.set(r.alerts);this.events.set(r.events);this.stats.set(r.stats);},error:e=>this.fail(e)}); }
  private clearStatus(): void {this.message.set('');this.error.set('');}
  private fail(e: any): void {this.loading.set(false);this.error.set(e?.error?.error ?? e?.message ?? 'No fue posible completar la solicitud.');}
}
