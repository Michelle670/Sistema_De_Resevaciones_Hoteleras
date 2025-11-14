
package goHotel.model;
/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class PlanLealtad 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private String idPlan;
    private String nivel;
    private double descuento;
//==============================================================================
//CONSTRUCTOR
//==============================================================================
    public PlanLealtad() 
    {
        this.idPlan = "";
        this.nivel = "";
        this.descuento = 0.0;
    }

    public PlanLealtad(String idPlan, String nivel, double descuento) 
    {
        this.idPlan = idPlan;
        this.nivel = nivel;
        this.descuento = descuento;
    }   
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 

    public String getIdPlan() 
    {
        return idPlan;
    }

    public void setIdPlan(String idPlan) 
    {
        this.idPlan = idPlan;
    }

    public String getNivel() 
    {
        return nivel;
    }

    public void setNivel(String nivel) 
    {
        this.nivel = nivel;
    }

    public double getDescuento() 
    {
        return descuento;
    }

    public void setDescuento(double descuento) 
    {
        this.descuento = descuento;
    }
    
    
    
    
}
