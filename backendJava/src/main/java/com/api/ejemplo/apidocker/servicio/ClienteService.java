package com.api.ejemplo.apidocker.servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.ejemplo.apidocker.model.Cliente;
import com.api.ejemplo.apidocker.model.Fondo;
import com.api.ejemplo.apidocker.model.FondoSuscrito;
import com.api.ejemplo.apidocker.model.Transaccion;
import com.api.ejemplo.apidocker.repository.ClienteRepository;
import com.api.ejemplo.apidocker.repository.FondoRepository;


@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final FondoRepository fondoRepository;
    private final NotificationService notificationService;

   // @Autowired
    public ClienteService(ClienteRepository clienteRepository, FondoRepository fondoRepository, NotificationService notificationService) {
        this.clienteRepository = clienteRepository;
        this.fondoRepository = fondoRepository;
        this.notificationService = notificationService;
    }


   /* public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }*/

    public Cliente crearCliente(String nombres,String apellidos,String tipoIdentificacion,String numeroIdentificacion,String email, 
                                String telefono,double saldoActual,String preferenciaNotificacion, List<FondoSuscrito> fondosSuscritos, List<Transaccion> historialTransacciones ){
        Cliente cliente = new Cliente();
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setTipoIdentificacion(tipoIdentificacion);
        cliente.setNumeroIdentificacion(numeroIdentificacion);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);
        cliente.setSaldoActual(saldoActual);
        cliente.setPreferenciaNotificacion(preferenciaNotificacion);
        cliente.setFondosSuscritos(fondosSuscritos);
        cliente.setHistorialTransacciones(historialTransacciones);

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerCliente(String id) {
        return clienteRepository.findById(id);
    }

    public Iterable<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }  

    private void enviarNotificacion(Cliente cliente, String nombreFondo) {
        String preferenciaNotificacion = cliente.getPreferenciaNotificacion();
        String mensaje = "Se ha suscrito exitosamente al fondo " + nombreFondo + ".";

        if ("email".equalsIgnoreCase(preferenciaNotificacion)) {
            notificationService.sendEmail(cliente.getEmail(), "Suscripción a fondo: " + nombreFondo, mensaje);
        } else if ("sms".equalsIgnoreCase(preferenciaNotificacion)) {
            notificationService.sendSms(cliente.getTelefono(), mensaje);
        }
    }

    //Refactorizada
    public Cliente asociarFondos(String clienteId, List<FondoSuscrito> fondosSuscritos) {
        Cliente cliente = clienteRepository.findById(clienteId)
                                           .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<FondoSuscrito> fondosCliente = cliente.getFondosSuscritos();
        if (fondosCliente == null) {
            fondosCliente = new ArrayList<>();
            cliente.setFondosSuscritos(fondosCliente);
        }

        List<Transaccion> historialTransacciones = cliente.getHistorialTransacciones();
        if (historialTransacciones == null) {
            historialTransacciones = new ArrayList<>();
        }

        for (FondoSuscrito fondoSuscrito : fondosSuscritos) {
            Fondo fondo = fondoRepository.findById(fondoSuscrito.getFondoId())
                                         .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));

            double montoMinimoSuscripcion = fondo.getMontoMinimo();
            

            if (montoMinimoSuscripcion > cliente.getSaldoActual()) {
                throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombreFondo());
            }

            // Asignar el monto mínimo de suscripción al fondo suscrito
            fondoSuscrito.setMontoSuscrito(montoMinimoSuscripcion);

            try {

                  // Verificar si el fondo ya está suscrito por el cliente
                  boolean yaSuscrito = false;
                  for (FondoSuscrito fs : fondosCliente) {
                      if (fs.getFondoId().equals(fondo.getFondoId())) {
                          yaSuscrito = true;
                          break;
                      }
                  }
          
                  if (yaSuscrito) {
                      throw new RuntimeException("El fondo " + fondo.getNombreFondo() + " ya está suscrito por el cliente.");
                  }
  

                Transaccion transaccionSuscripcion = crearTransaccion("apertura", montoMinimoSuscripcion, fondo.getFondoId());

                // Validar que la transacción se haya creado correctamente
                if (transaccionSuscripcion == null) {
                    throw new RuntimeException("No se pudo crear la transacción para el fondo " + fondo.getNombreFondo());
                }

                enviarNotificacion(cliente, fondo.getNombreFondo());
                // Añadir el fondo suscrito a la lista de fondos del cliente
                fondosCliente.add(fondoSuscrito);

                // Ajustar el saldo del cliente
                cliente.setSaldoActual(cliente.getSaldoActual() - montoMinimoSuscripcion);

                // Agregar la transacción al historial de transacciones del cliente
                historialTransacciones.add(transaccionSuscripcion);

                // Enviar notificación al cliente por cada suscripción
                //enviarNotificacion(cliente, fondo);
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar la suscripción para el fondo " + fondo.getNombreFondo() + ": " + e.getMessage());
            }
        }

        cliente.setHistorialTransacciones(historialTransacciones);

        return clienteRepository.save(cliente);
    }

    private Transaccion crearTransaccion(String tipo, double monto, String fondoId) {
        Transaccion transaccion = new Transaccion();
        //transaccion.setTransactionId(UUID.randomUUID().toString());
        transaccion.setTipo(tipo);
        transaccion.setMonto(monto);
        transaccion.setFondoId(fondoId);
        transaccion.setFecha(new Date());       
        // Enviar notificación al cliente por cada suscripción
        return transaccion;
    }


    //Refactorizada
    public Cliente cancelarFondos(String clienteId, List<FondoSuscrito> fondos) {
        Cliente cliente = clienteRepository.findById(clienteId)
                                           .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    
        List<FondoSuscrito> fondosCliente = cliente.getFondosSuscritos();
        if (fondosCliente == null || fondosCliente.isEmpty()) {
            throw new RuntimeException("El cliente no tiene fondos suscritos para cancelar.");
        }
    
        List<Transaccion> historialTransacciones = cliente.getHistorialTransacciones();
        if (historialTransacciones == null) {
            historialTransacciones = new ArrayList<>();
        }
    
        //double totalMontoCancelado = 0.0;
    
        for (FondoSuscrito fondoSuscrito : fondos) {
            // Obtener el ID del fondo suscrito
            String fondoId = fondoSuscrito.getFondoId();

            Fondo fondo = fondoRepository.findById(fondoSuscrito.getFondoId())
                        .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));
                        
            String nombreFondo = fondo.getNombreFondo();
    
            // Buscar el fondo suscrito por su ID
            FondoSuscrito fondoSuscritoEncontrado = null;
            for (FondoSuscrito fs : fondosCliente) {
                if (fs.getFondoId().equals(fondoId)) {
                    fondoSuscritoEncontrado = fs;
                    break;
                }
            }
    
            // Verificar si se encontró el fondo suscrito
            if (fondoSuscritoEncontrado == null) {
                throw new RuntimeException("El fondo con ID " + nombreFondo + " no está suscrito por el cliente.");
            }
    
            // Obtener el monto suscrito que se va a cancelar
            double montoSuscrito = fondoSuscritoEncontrado.getMontoSuscrito();
    
            // Sumar el monto de suscripción cancelado al saldo actual del cliente
            cliente.setSaldoActual(cliente.getSaldoActual() + montoSuscrito);
    
            // Crear la transacción de cancelación
            try {
                Transaccion transaccionCancelacion = crearTransaccion("cancelacion", montoSuscrito, fondoId);
    
                // Validar que la transacción se haya creado correctamente
                if (transaccionCancelacion == null) {
                    throw new RuntimeException("No se pudo crear la transacción de cancelación para el fondo con ID " + fondoId);
                }
                
                enviarNotificacion(cliente, nombreFondo);
                // Agregar la transacción al historial de transacciones del cliente
                historialTransacciones.add(transaccionCancelacion);
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar la cancelación para el fondo con ID " + fondoId + ": " + e.getMessage());
            }
    
            // Eliminar el fondo suscrito de la lista de fondos del cliente
            fondosCliente.remove(fondoSuscritoEncontrado);
    
            // Sumar el monto cancelado saldo actual
           cliente.setSaldoActual(cliente.getSaldoActual() + montoSuscrito);
        }
    
        cliente.setFondosSuscritos(fondosCliente);
        cliente.setHistorialTransacciones(historialTransacciones);
    
        // Guardar los cambios en el cliente
        return clienteRepository.save(cliente);
    }
    

   
    public Cliente buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        return clienteRepository.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }    

    public List<Transaccion> obtenerTransaccionesDeCliente(String clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                                           .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return cliente.getHistorialTransacciones();
    }

   public List<Transaccion> obtenerTransaccionesDeTodosLosClientes() {
        List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();
        List<Transaccion> todasLasTransacciones = new ArrayList<>();

        for (Cliente cliente : clientes) {
            List<Transaccion> transaccionesCliente = cliente.getHistorialTransacciones();
            if (transaccionesCliente != null) {
                todasLasTransacciones.addAll(transaccionesCliente);
            }
        }

        return todasLasTransacciones;
    }

    public List<Map<String, Object>> obtenerTransaccionesConDatosClientes() {
        Iterable<Cliente> clientesIterable = clienteRepository.findAll();
        List<Cliente> clientes = StreamSupport.stream(clientesIterable.spliterator(), false)
                                              .collect(Collectors.toList());
    
        // Imprimir los clientes obtenidos por findAll()
        /*System.out.println("Clientes obtenidos por findAll():");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }*/
    
        List<Map<String, Object>> transaccionesConCliente = new ArrayList<>();
    
        for (Cliente cliente : clientes) {
            List<Transaccion> transaccionesCliente = cliente.getHistorialTransacciones();
    
            for (Transaccion transaccion : transaccionesCliente) {
                Map<String, Object> transaccionConCliente = new HashMap<>();
    
                transaccionConCliente.put("nombres", cliente.getNombres());
                transaccionConCliente.put("apellidos", cliente.getApellidos());
                transaccionConCliente.put("tipoIdentificacion", cliente.getTipoIdentificacion());
                transaccionConCliente.put("numeroIdentificacion", cliente.getNumeroIdentificacion());
    
                transaccionConCliente.put("transactionId", transaccion.getTransactionId());
                transaccionConCliente.put("fondoId", transaccion.getFondoId());
                transaccionConCliente.put("tipo", transaccion.getTipo());
                transaccionConCliente.put("monto", transaccion.getMonto());
                transaccionConCliente.put("fecha", transaccion.getFecha());
    
                transaccionesConCliente.add(transaccionConCliente);
            }
        }
    
        return transaccionesConCliente;
    }
    

 
  
}
    


    
   

    

