//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// MODELO TIPO HABITACION
//============================================================================== 
public class TipoHabitacion 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idTipo;
    private String nombreTipo;
    private String descripcion;
    private int capacidad;
    private double precioBase;
//==============================================================================
//CONSTRUCTOR
//==============================================================================
    public TipoHabitacion()
    {
        this.idTipo = 0;
        this.nombreTipo = "";
        this.descripcion = "";
        this.capacidad = 0;
        this.precioBase = 0.0;
    }

    public TipoHabitacion(int idTipo, String nombreTipo, String descripcion, int capacidad, double precioBase) 
    {
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
    } 
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 

    public int getIdTipo() 
    {
        return idTipo;
    }

    public void setIdTipo(int idTipo) 
    {
        this.idTipo = idTipo;
    }

    public String getNombreTipo() 
    {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) 
    {
        this.nombreTipo = nombreTipo;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }

    public int getCapacidad() 
    {
        return capacidad;
    }

    public void setCapacidad(int capacidad)
    {
        this.capacidad = capacidad;
    }

    public double getPrecioBase() 
    {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) 
    {
        this.precioBase = precioBase;
    }
//==============================================================================   
}
