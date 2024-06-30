import { NgModule } from '@angular/core';
import { Component } from '@angular/core';
import { Cliente } from '../models/cliente.model';
import { ClienteService } from '../services/cliente.service';

@Component({
  selector: 'app-crear-cliente',
  templateUrl: './crear-cliente.component.html',
  styleUrls: ['./crear-cliente.component.css']
})
export class CrearClienteComponent {
  mostrarMensaje: boolean = false;
  cliente: Cliente = {
    id: 'vacio',
    nombres: '',
    apellidos: '',
    tipoIdentificacion: '',
    numeroIdentificacion: '',
    email: '',
    telefono: '',
    saldoActual: 0,
    preferenciaNotificacion: '',
    fondosSuscritos: [],
    historialTransacciones: []
  };
  
  constructor(private clienteService: ClienteService) {}

  crearCliente() {
    this.clienteService.agregarCliente(this.cliente).subscribe(
      response => {
        console.log('Cliente creado:', response);
        this.mostrarMensaje = true; // Mostrar mensaje de confirmación
        this.cliente = {
          id: 'vacio',
          nombres: '',
          apellidos: '',
          tipoIdentificacion: '',
          numeroIdentificacion: '',
          email: '',
          telefono: '',
          saldoActual: 0,
          preferenciaNotificacion: '',
          fondosSuscritos: [],
          historialTransacciones: []
        };
      },
      error => {
        console.error('Error al crear cliente:', error);
        // Manejo de errores, si es necesario
      }
    );
  }
}
