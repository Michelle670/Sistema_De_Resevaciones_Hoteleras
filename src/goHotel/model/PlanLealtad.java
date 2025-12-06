package goHotel.model;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
//******************************************************************************
public class PlanLealtad {
//==============================================================================
//ATRIBUTOS 
//==============================================================================

    private int id; 
    private String nivel;
    private double descuento;

//==============================================================================
//CONSTRUCTOR
//==============================================================================
    public PlanLealtad() {
        this.id = 0;
        this.nivel = "";
        this.descuento = 0.0;
    }

    public PlanLealtad(int id, String nivel, double descuento) {
        this.id = id;
        this.nivel = nivel;
        this.descuento = descuento;
    }

//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 
    public int getId() { 
        return id;
    }

    public void setId(int id) { 
        this.id = id;
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
}