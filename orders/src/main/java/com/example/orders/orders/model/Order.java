package com.example.orders.orders.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import jakarta.validation.constraints.NotNull;
     import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

    @Entity
    @Table(name = "orders")
    public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate fecha;

    @NotNull(message = "Status is required")
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    private String estado;

    @NotNull(message = "Client ID is required")
    @Column(name = "cliente_id")
    private Long clienteId;

    @NotNull(message = "Product is required")
    @Size(max = 100, message = "Product cannot exceed 100 characters")
    private String producto;

    @NotNull(message = "Delivery address is required")
    @Size(max = 200, message = "Delivery address cannot exceed 200 characters")
    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    // Empty constructor
    public Order() {
    }

    // Full constructor
    public Order(Long id, LocalDate fecha, String estado, Long clienteId, String producto, String direccionEntrega) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.clienteId = clienteId;
        this.producto = producto;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", clienteId=" + clienteId +
                ", producto='" + producto + '\'' +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                '}';
    }
    }
