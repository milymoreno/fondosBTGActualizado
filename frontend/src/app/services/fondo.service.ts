import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';
import { Fondo } from '../models/fondo.model';



@Injectable({
  providedIn: 'root'
})
export class FondoService {

  private apiUrl = 'http://localhost:8082/fondos'; // Asegúrate de que la URL es correcta

  constructor(private http: HttpClient) { }

  crearFondo(fondo: Fondo): Observable<any> {    
    console.log('JSON a enviar para fondos:', fondo);
    return this.http.post<any>(this.apiUrl, fondo);
  }

    getFondos(): Observable<any[]> {
      return this.http.get<any[]>(`${this.apiUrl}`).pipe(
        map(fondos => fondos.map(fondo => ({ ...fondo, selected: false })))
      );
    }

  validarSaldo(clienteId: string, fondoId: string): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/validar-saldo?clienteId=${clienteId}&fondoId=${fondoId}`);
  }
}
