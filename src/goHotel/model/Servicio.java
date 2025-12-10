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
// MODELO SERVICIO
//============================================================================== 
public class Servicio 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================   
    private int idServicio;
    private String nombreServicio;
    private String descripcion;
//==============================================================================
//CONSTRUCTOR
//==============================================================================
    public Servicio() 
    {
        this.idServicio = 0;
        this.nombreServicio = "";
        this.descripcion = "";
    }

    public Servicio(int idServicio, String nombreServicio, String descripcion) 
    {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
       
    } 
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 
    public int getIdServicio() 
    {
        return idServicio;
    }

    public void setIdServicio(int idServicio) 
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
//==============================================================================   
}
