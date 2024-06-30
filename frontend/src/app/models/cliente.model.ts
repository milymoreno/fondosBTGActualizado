import { Fondo } from './fondo.model';
import { Transaccion } from './transaccion.model';

export interface Cliente {
  id: string;
  nombres: string;
  apellidos: string;
  tipoIdentificacion: string;
  numeroIdentificacion: string;
  email: string;
  telefono: string;
  saldoActual: number;
  fondosSuscritos: Fondo[];
  historialTransacciones: Transaccion[];
  preferenciaNotificacion: string;
}
