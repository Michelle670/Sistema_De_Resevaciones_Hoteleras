package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Pais {
//==============================================================================
// ATRIBUTOS
//==============================================================================
    private int idPais;
    private String codigo;
    private String nombre;
//==============================================================================
// CONSTRUCTOR
//==============================================================================
    public Pais() {
        this.idPais = 0;
        this.codigo = "";
        this.nombre = "";
    }

    public Pais(int idPais, String codigo, String nombre) {
        this.idPais = idPais;
        this.codigo = codigo;
        this.nombre = nombre;
    }
//==============================================================================
// GETTERS Y SETTERS
//==============================================================================

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}