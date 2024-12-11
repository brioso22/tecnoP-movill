package com.example.tecnof.models;

public class Producto {
    private String nombre;
    private double precio;
    private String descripcion;
    private String ram;
    private String almacenamiento;
    private String procesador;
    private String imagenUrl;

    // Constructor
    public Producto(String nombre, double precio, String descripcion,
                    String ram, String almacenamiento, String procesador, String imagenUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.ram = ram;
        this.almacenamiento = almacenamiento;
        this.procesador = procesador;
        this.imagenUrl = imagenUrl;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(String almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    // Método toString
    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                ", ram='" + ram + '\'' +
                ", almacenamiento='" + almacenamiento + '\'' +
                ", procesador='" + procesador + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                '}';
    }
}
