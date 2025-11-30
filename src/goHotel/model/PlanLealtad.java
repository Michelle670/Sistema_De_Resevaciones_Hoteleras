package goHotel.model;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
public class PlanLealtad {
//==============================================================================
// ATRIBUTOS    
//==============================================================================

    private int idLealtad;
    private String nivel;
    private double descuento;

//==============================================================================
// CONSTRUCTOR
//==============================================================================    
    public PlanLealtad() {
        this.idLealtad = 0;
        this.nivel = "";
        this.descuento = 0.0;
    }

    public PlanLealtad(int idLealtad, String nivel, double descuento) {
        this.idLealtad = idLealtad;
        this.nivel = nivel;
        this.descuento = descuento;
    }

//==============================================================================
// GETTERS Y SETTERS
//==============================================================================  
    public int getIdLealtad() {
        return idLealtad;
    }

    public void setIdLealtad(int idLealtad) {
        this.idLealtad = idLealtad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return nivel + " (" + descuento + "%)";
    }
}