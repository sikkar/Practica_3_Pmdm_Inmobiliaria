package com.izv.angel.inmobiliaria;


import java.io.Serializable;

public class Inmueble implements Serializable, Comparable <Inmueble> {

    private long id;
    private String localidad;
    private String direccion;
    private String tipo;
    private int precio;

    public Inmueble() {
        this(0,"","","",0);
    }

    public Inmueble(long id, String localidad, String direccion, String tipo, int precio) {
        this.id = id;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Inmueble(String localidad, String direccion, String tipo, int precio) {
        this.id = 0;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Inmueble.class != o.getClass()) return false;

        Inmueble inmueble = (Inmueble) o;

        if (id != inmueble.id) return false;
        if (precio != inmueble.precio) return false;
        if (direccion != null ? !direccion.equals(inmueble.direccion) : inmueble.direccion != null)
            return false;
        if (localidad != null ? !localidad.equals(inmueble.localidad) : inmueble.localidad != null)
            return false;
        if (tipo != null ? !tipo.equals(inmueble.tipo) : inmueble.tipo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + precio;
        return result;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + id +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                '}';
    }


    @Override
    public int compareTo(Inmueble inmueble) {
        if (this.localidad.compareTo(inmueble.localidad)!=0){
            return this.localidad.compareTo(inmueble.localidad);
        }else{
            return this.tipo.compareTo(inmueble.tipo);
        }
    }
}
