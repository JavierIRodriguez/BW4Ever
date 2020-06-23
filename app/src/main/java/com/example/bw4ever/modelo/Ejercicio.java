package com.example.bw4ever.modelo;

import androidx.annotation.Nullable;

public class Ejercicio {
    private String id;
    private String nombre;
    private String descripcion;

    public Ejercicio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return id.equals(((Ejercicio)obj).id);
    }
}
