package com.api.ejemplo.apidocker.model;

import java.util.List;
import java.util.UUID;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Clientes") // Nombre de la tabla en DynamoDB
public class Cliente {

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "nombres")
    private String nombres;

    @DynamoDBAttribute(attributeName = "apellidos")
    private String apellidos;

    @DynamoDBAttribute(attributeName = "tipoIdentificacion")
    private String tipoIdentificacion;

    @DynamoDBAttribute(attributeName = "numeroIdentificacion")
    private String numeroIdentificacion;

    @DynamoDBAttribute(attributeName = "email")
    private String email;

    @DynamoDBAttribute(attributeName = "telefono")
    private String telefono;

    @DynamoDBAttribute(attributeName = "saldoActual")
    private double saldoActual;

    @DynamoDBAttribute(attributeName = "fondosSuscritos")
    private List<FondoSuscrito> fondosSuscritos;

    @DynamoDBAttribute(attributeName = "historialTransacciones")
    private List<Transaccion> historialTransacciones;

    @DynamoDBAttribute(attributeName = "preferenciaNotificacion")
    private String preferenciaNotificacion;

    // Getters y Setters
    public Cliente() {
        this.id = UUID.randomUUID().toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public List<FondoSuscrito> getFondosSuscritos() {
        return fondosSuscritos;
    }

    public void setFondosSuscritos(List<FondoSuscrito> fondosSuscritos) {
        this.fondosSuscritos = fondosSuscritos;
    }

    public List<Transaccion> getHistorialTransacciones() {
        return historialTransacciones;
    }

    public void setHistorialTransacciones(List<Transaccion> historialTransacciones) {
        this.historialTransacciones = historialTransacciones;
    }

    public String getPreferenciaNotificacion() {
        return preferenciaNotificacion;
    }

    public void setPreferenciaNotificacion(String preferenciaNotificacion) {
        this.preferenciaNotificacion = preferenciaNotificacion;
    }

    /*@Override
    public String toString() {
        return "Clientes{" +
                "id='" + id + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", saldoActual=" + saldoActual +
                ", fondosSuscritos=" + fondosSuscritos +
                ", historialTransacciones=" + historialTransacciones +
                ", preferenciaNotificacion='" + preferenciaNotificacion + '\'' +
                '}';
    }*/
}
