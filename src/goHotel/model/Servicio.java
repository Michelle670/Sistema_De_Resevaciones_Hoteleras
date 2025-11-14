
package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Servicio 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================   
    private String idServicio;
    private String nombreServicio;
    private String descripcion;
//==============================================================================
//CONSTRUCTOR
//==============================================================================
    public Servicio() 
    {
        this.idServicio = "";
        this.nombreServicio = "";
        this.descripcion = "";
    }

    public Servicio(String idServicio, String nombreServicio, String descripcion) 
    {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
       
    } 
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 
    public String getIdServicio() 
    {
        return idServicio;
    }

    public void setIdServicio(String idServicio) 
    {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() 
    {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) 
    {
        this.nombreServicio = nombreServicio;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }
    
    
}
