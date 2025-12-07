package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */

//******************************************************************************
public class PlanLealtad {
//==============================================================================
//ATRIBUTOS 
//==============================================================================

    private int id;
    private String nivel;
    private Double descuento; 
    private Double factorPuntos; 

//==============================================================================
//CONSTRUCTOR
//==============================================================================
    
    public PlanLealtad() {
        this.id = 0;
        this.nivel = "";
        this.descuento = 0.0; // Inicializar Double a 0.0
        this.factorPuntos = 0.0; // Inicializar Double a 0.0
    }

    public PlanLealtad(int id, String nivel, Double descuento, Double factorPuntos) {
        this.id = id;
        this.nivel = nivel;
        this.descuento = descuento;
        this.factorPuntos = factorPuntos;
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

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getFactorPuntos() {
        return factorPuntos;
    }

    public void setFactorPuntos(Double factorPuntos) {
        this.factorPuntos = factorPuntos;
    }
}