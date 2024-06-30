import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Cliente } from '../models/cliente.model';
import { Fondo } from '../models/fondo.model';



@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://localhost:8082/clientes';

  constructor(private http: HttpClient) { }

  buscarClientePorNumeroIdentificacion(numeroIdentificacion: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/buscar/${numeroIdentificacion}`);
  }

  agregarCliente(cliente: Cliente): Observable<any> {    
    console.log('JSON a enviar para clientes:',cliente);
    return this.http.post<any>(this.apiUrl, cliente);
  }


  asociarFondos(clienteId: string, fondosSuscritos: Fondo[]): Observable<any> {
    // Imprimir el JSON que se va a 
    console.log('Cliente Id:', clienteId);
    console.log('JSON a enviar a la transacción:', fondosSuscritos);
    return this.http.post<any>(`${this.apiUrl}/${clienteId}/fondos`, fondosSuscritos);
    
  }

  cancelarFondos(clienteId: string, fondosSuscritos: Fondo[]) {
    console.log('Cliente Id:', clienteId);
    console.log('JSON a enviar a la transacción:', fondosSuscritos);
    const url = `${this.apiUrl}/${clienteId}/cancelarFondos`;
    return this.http.post(url, fondosSuscritos);
  }

 

  getCliente(clienteId: string): Observable<Cliente> {
    const url = `${this.apiUrl}/${clienteId}`;
    return this.http.get<Cliente>(url);
  }
  
  
 
}
