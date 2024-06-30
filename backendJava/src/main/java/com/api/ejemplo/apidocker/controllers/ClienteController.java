package com.api.ejemplo.apidocker.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ejemplo.apidocker.model.Cliente;
import com.api.ejemplo.apidocker.model.FondoSuscrito;
import com.api.ejemplo.apidocker.model.Transaccion;
import com.api.ejemplo.apidocker.servicio.ClienteService;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

     //@Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


	@PostMapping
	public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente.getNombres(),cliente.getApellidos(),cliente.getTipoIdentificacion(),cliente.getNumeroIdentificacion(),
                                cliente.getEmail(),cliente.getTelefono(),cliente.getSaldoActual(),cliente.getPreferenciaNotificacion(), cliente.getFondosSuscritos(), cliente.getHistorialTransacciones());
        return ResponseEntity.ok(nuevoCliente);
    }
	
	
    @GetMapping("/{clienteId}")
    public Optional<Cliente> obtenerCliente(@PathVariable String clienteId) {
        return clienteService.obtenerCliente(clienteId);
    }

    @GetMapping
    public ResponseEntity<Iterable<Cliente>> obtenerTodosLosClientes() {
        Iterable<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/{numeroIdentificacion}")
    public ResponseEntity<?> buscarPorNumeroIdentificacion(@PathVariable String numeroIdentificacion) {
        try {
            Cliente cliente = clienteService.buscarPorNumeroIdentificacion(numeroIdentificacion);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            // Captura la excepción y devuelve un mensaje de error apropiado
            Map<String, String> errorResponse = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @PostMapping("/{clienteId}/fondos")
    public ResponseEntity<Object> asociarFondos(@PathVariable String clienteId,
                                                @RequestBody List<FondoSuscrito> fondosSuscritos) {
        try {
            Cliente cliente = clienteService.asociarFondos(clienteId, fondosSuscritos);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            // Captura la excepción y devuelve un mensaje de error apropiado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("/{clienteId}/cancelarFondos")
    public ResponseEntity<Object>cancelarFondos(@PathVariable String clienteId,
                                                @RequestBody List<FondoSuscrito> fondosSuscritos) {
        try {
            Cliente cliente = clienteService.cancelarFondos(clienteId, fondosSuscritos);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            // Captura la excepción y devuelve un mensaje de error apropiado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

   

   @GetMapping("/{clienteId}/transacciones")
    public ResponseEntity<List<Transaccion>> obtenerTransaccionesDeCliente(@PathVariable String clienteId) {
        List<Transaccion> transacciones = clienteService.obtenerTransaccionesDeCliente(clienteId);
        return ResponseEntity.ok(transacciones);
    }
      

    @GetMapping("/transaccionesCli")
    public ResponseEntity<List<Transaccion>> obtenerTransaccionesDeTodosLosClientes() {
        List<Transaccion> transacciones = clienteService.obtenerTransaccionesDeTodosLosClientes();
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<Map<String, Object>>> obtenerTransaccionesConDatosClientes() {
        List<Map<String, Object>> transaccionesConCliente = clienteService.obtenerTransaccionesConDatosClientes();
        return ResponseEntity.ok(transaccionesConCliente);
    }
   
}
