import { Component } from '@angular/core';
import { Fondo } from '../models/fondo.model';
import { FondoService } from '../services/fondo.service';


@Component({
  selector: 'app-crear-fondo',
  templateUrl: './crear-fondos.component.html',
  styleUrls: ['./crear-fondos.component.css']
})
export class CrearFondoComponent {
  mostrarMensaje: boolean = false;
  fondo: Fondo = {
    fondoId: '',
    nombreFondo: '',
    descripcion: '',
    montoMinimo: 0,
    fechaSuscripcion: ''
  };

  constructor(private fondoService: FondoService) {}

  crearFondo() {
    this.fondoService.crearFondo(this.fondo).subscribe(
      response => {
        console.log('Cliente creado:', response);
        this.mostrarMensaje = true; // Mostrar mensaje de confirmación
        this.fondo = {
          fondoId: '',
          nombreFondo: '',
          descripcion: '',
          montoMinimo: 0,
          fechaSuscripcion: ''
        };
      },
      error => {
        console.error('Error al crear el fondo:', error);       
      }
    );
  }
}

