import { Component } from '@angular/core';
import { NgModule } from '@angular/core';

import { ClienteService } from '../services/cliente.service';
import { FondoService } from '../services/fondo.service';

import { Cliente } from '../models/cliente.model';
import { Fondo } from '../models/fondo.model';


@Component({
  selector: 'app-buscar-asociar-fondos',
  templateUrl: './buscar-asociar-fondos.component.html',
  styleUrls: ['./buscar-asociar-fondos.component.css']
})
export class BuscarAsociarFondosComponent {
numeroIdentificacion: string = ''; 
fondosDisponibles: Fondo[] = [];
fondosDesmarcados: any[] = [];
clienteNoEncontradoMsg: string=''; // Mensaje para mostrar cuando el cliente no se encuentra
cancelarFondosButtonDisabled: boolean = true;
mensaje: string = '';
asociarFondosMsg: string = '';
cancelarFondosMsg: string = '';
cliente: Cliente = {
  id: '',
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

constructor(
  private fondoService: FondoService,
  private clienteService: ClienteService
) { }

  ngOnInit(): void {
    this.obtenerFondosDisponibles(); // Llama al método getFondos() al iniciar el componente
  }

  /*getFondos(): void {
    this.fondoService.getFondos().subscribe(
      fondos => {
        this.fondosDisponibles = fondos; // Asigna los fondos recibidos al array fondosDisponibles
      },
      error => {
        console.error('Error al obtener los fondos:', error);
        // Manejo de errores, si es necesario
      }
    );
  }*/

    obtenerFondosDisponibles() {
      this.fondoService.getFondos().subscribe(
        fondos => {
          this.fondosDisponibles = fondos; // Asigna los fondos recibidos al array fondosDisponibles
        },
        error => {
          console.error('Error al obtener los fondos:', error);
          // Manejo de errores, si es necesario
        }
      );
    }

    marcarFondosSuscritos() {
      if (this.cliente && this.cliente.fondosSuscritos) {
        this.cliente.fondosSuscritos.forEach(fondoSuscrito => {
          const fondo = this.fondosDisponibles.find(f => f.fondoId === fondoSuscrito.fondoId);
          if (fondo) {
            fondo.selected = true;
          }
        });
      }
    }
  
  /*buscarCliente() {
    this.clienteService.buscarClientePorNumeroIdentificacion(this.numeroIdentificacion).subscribe(response => {
      this.cliente = response;
    });
  }  */

   /* buscarCliente() {
      this.clienteService.buscarClientePorNumeroIdentificacion(this.numeroIdentificacion).subscribe(response => {
        this.cliente = response;
        this.marcarFondosSuscritos();
      });
    }*/

      buscarCliente() {
        this.clienteService.buscarClientePorNumeroIdentificacion(this.numeroIdentificacion).subscribe(
          response => {
            this.cliente = response;
            this.marcarFondosSuscritos();
            this.clienteNoEncontradoMsg = ''; 
            this.asociarFondosMsg = '';
            this.cancelarFondosMsg = '';
          },
          error => {
            console.error('Error al buscar cliente:', error);
            if (error.error && error.error.message) {
              this.clienteNoEncontradoMsg = error.error.message; // Establece el mensaje de cliente no encontrado desde el error
            } else {
              this.clienteNoEncontradoMsg = 'No se pudo encontrar al cliente.';
            }
            this.cliente = {
              id: '',
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
            //this.fondosDisponibles = []; // Reinicia los fondos disponibles
            this.fondosDisponibles.forEach(fondo => fondo.selected = false);
          }
        );
      }
    
    

   /*asociarFondos(): void {
      const fondosSeleccionados: any[] = this.fondosDisponibles
        .filter(fondo => fondo.selected)
        .map(fondo => ({
          fondoId: fondo.fondoId, // Accede a fondoId en lugar de id
          //montoSuscrito: fondo.montoSuscrito, // Accede a montoSuscrito en lugar de monto
          fechaSuscripcion: new Date().toISOString()
        }));
    
      this.clienteService.asociarFondos(this.cliente.id, fondosSeleccionados).subscribe(
        response => {
          console.log('Fondos asociados', response);
          this.buscarCliente();  // Recargar los datos del cliente después de asociar fondos
        },
        error => {
          console.error('Error al asociar fondos:', error);
          // Manejo de errores, si es necesario
        }
      );
    }*/

      asociarFondos(): void {
        const fondosSeleccionados: any[] = this.fondosDisponibles
          .filter(fondo => fondo.selected)
          .map(fondo => ({
            fondoId: fondo.fondoId,
            fechaSuscripcion: new Date().toISOString()
          }));
    
        this.clienteService.asociarFondos(this.cliente.id, fondosSeleccionados).subscribe(
          response => {
            console.log('Fondos asociados', response);
            this.asociarFondosMsg = 'Fondos asociados exitosamente.';
            this.buscarCliente();
          },
          error => {
            console.error('Error al asociar fondos:', error);
            this.asociarFondosMsg = 'Error al asociar fondos. Intente nuevamente.';
          }
        );
      }
    
      /*cancelarFondos(clienteId: string, fondosACancelar: any[]) {
        this.clienteService.cancelarFondos(clienteId, fondosACancelar).subscribe(
          response => {
            console.log('Fondos cancelados:', response);
            // Manejo de la respuesta si es necesario
          },
          error => {
            console.error('Error al cancelar fondos:', error);
            // Manejo de errores si es necesario
          }
        );
      }*/

        cancelarFondos(): void {
          if (this.fondosDesmarcados.length > 0) {
            this.clienteService.cancelarFondos(this.cliente.id, this.fondosDesmarcados).subscribe(
              response => {
                console.log('Fondos cancelados', response);
                this.cancelarFondosMsg = 'Fondos cancelados exitosamente.';
                this.buscarCliente();
              },
              error => {
                console.error('Error al cancelar fondos:', error);
                this.cancelarFondosMsg = 'Error al cancelar fondos. Intente nuevamente.';
              }
            );
            this.fondosDesmarcados = []; // Reiniciar la lista de fondos desmarcados
            this.cancelarFondosButtonDisabled = true;
          }
        }
      
        onFondoChange(fondo: any): void {
          if (!fondo.selected) {
            this.fondosDesmarcados.push({
              fondoId: fondo.fondoId,
              fechaSuscripcion: fondo.fechaSuscripcion // Utiliza la fecha específica de suscripción
            });
          } else {
            // Eliminar fondo de la lista de desmarcados si se vuelve a marcar
            this.fondosDesmarcados = this.fondosDesmarcados.filter(f => f.fondoId !== fondo.fondoId);
          }
          this.cancelarFondosButtonDisabled = this.fondosDesmarcados.length === 0;
        }

}
        

