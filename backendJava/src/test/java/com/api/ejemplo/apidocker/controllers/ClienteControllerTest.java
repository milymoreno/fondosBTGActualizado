/*package com.api.ejemplo.apidocker.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.ejemplo.apidocker.model.Cliente;
import com.api.ejemplo.apidocker.servicio.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    public void testCrearCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId("unique_client_id");
        cliente.setNombres("Nombres del Cliente");
        // Setear otras propiedades del cliente

        Mockito.when(clienteService.crearCliente(Mockito.any(Cliente.class))).thenReturn(cliente);

        ObjectMapper objectMapper = new ObjectMapper();
        String clienteJson = objectMapper.writeValueAsString(cliente);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("unique_client_id")))
                .andExpect(jsonPath("$.nombres", is("Nombres del Cliente")));
        // Añade más validaciones según la respuesta esperada
    }
}*/

package com.api.ejemplo.apidocker.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.api.ejemplo.apidocker.model.Cliente;
import com.api.ejemplo.apidocker.model.FondoSuscrito;
import com.api.ejemplo.apidocker.model.Transaccion;
import com.api.ejemplo.apidocker.servicio.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List; 

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
public void testCrearCliente() throws Exception {
    // Crear un cliente de ejemplo
    Cliente clienteEjemplo = new Cliente();
    clienteEjemplo.setId("unique_client_id");
    clienteEjemplo.setNombres("Nombres del Cliente");
    // Setear otras propiedades del cliente si es necesario

    // Configurar la simulación del servicio clienteService
    Mockito.when(clienteService.crearCliente(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                                            Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                                            Mockito.anyDouble(), Mockito.anyString(), Mockito.anyList(),
                                            Mockito.anyList()))
           .thenAnswer(invocation -> {
               String nombres = invocation.getArgument(0);
               String apellidos = invocation.getArgument(1);
               String tipoIdentificacion = invocation.getArgument(2);
               String numeroIdentificacion = invocation.getArgument(3);
               String email = invocation.getArgument(4);
               String telefono = invocation.getArgument(5);
               double saldoActual = invocation.getArgument(6);
               String preferenciaNotificacion = invocation.getArgument(7);
               List<FondoSuscrito> fondosSuscritos = invocation.getArgument(8);
               List<Transaccion> historialTransacciones = invocation.getArgument(9);
               
               // Configura el cliente con los argumentos recibidos
               clienteEjemplo.setNombres(nombres);
               clienteEjemplo.setApellidos(apellidos);
               clienteEjemplo.setTipoIdentificacion(tipoIdentificacion);
               clienteEjemplo.setNumeroIdentificacion(numeroIdentificacion);
               clienteEjemplo.setEmail(email);
               clienteEjemplo.setTelefono(telefono);
               clienteEjemplo.setSaldoActual(saldoActual);
               clienteEjemplo.setPreferenciaNotificacion(preferenciaNotificacion);
               clienteEjemplo.setFondosSuscritos(fondosSuscritos);
               clienteEjemplo.setHistorialTransacciones(historialTransacciones);
               
               // Devuelve el cliente configurado
               return clienteEjemplo;
           });

    // Convertir el objeto clienteEjemplo a JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String clienteJson = objectMapper.writeValueAsString(clienteEjemplo);

    // Ejecutar la solicitud HTTP POST usando MockMvc
    mockMvc.perform(post("/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clienteJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is("unique_client_id")))
            .andExpect(jsonPath("$.nombres", is("Nombres del Cliente")));
    // Añadir más validaciones según la respuesta esperada del controlador
}

    
}


