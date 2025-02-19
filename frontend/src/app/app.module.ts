import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BuscarAsociarFondosComponent } from './buscar-asociar-fondos/buscar-asociar-fondos.component';


import { ClienteService } from './services/cliente.service';
import { FondoService } from './services/fondo.service';


import { CrearClienteComponent } from './crear-cliente/crear-cliente.component';
import { CrearFondoComponent } from './crear-fondos/crear-fondos.component';
import { HistorialTransaccionesComponent } from './historial-transacciones/historial-transacciones.component';

@NgModule({
  declarations: [
    AppComponent,
        BuscarAsociarFondosComponent,
        CrearClienteComponent,
        CrearFondoComponent,
        HistorialTransaccionesComponent   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [ClienteService, FondoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
