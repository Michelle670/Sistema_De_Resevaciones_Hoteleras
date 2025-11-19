
package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Habitacion 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idHabitacion;
    private int idhotel;
    private int idtipo;
    private int numero;
    private String estado;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Habitacion() 
    {
        this.idHabitacion = 0;
        this.idhotel = 0;
        this.idtipo = 0;
        this.numero = 0;
        this.estado = "";
    }

    public Habitacion(int idHabitacion, int idhotel, int idtipo, int numero, String estado) 
    {
        this.idHabitacion = idHabitacion;
        this.idhotel = idhotel;
        this.idtipo = idtipo;
        this.numero = numero;
        this.estado = estado;
    }
 
//==============================================================================
//GETTERS Y SETTERS
//==============================================================================   

    public int getIdHabitacion() 
    {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) 
    {
        this.idHabitacion = idHabitacion;
    }

    public int getHotel() 
    {
        return idhotel;
    }

    public void setHotel(int idhotel) 
    {
        this.idhotel = idhotel;
    }

    public int getTipo() 
    {
        return idtipo;
    }

    public void setTipo(int tipo) 
    {
        this.idtipo = tipo;
    }

    public int getNumero() 
    {
        return numero;
    }

    public void setNumero(int numero) 
    {
        this.numero = numero;
    }

    public String getEstado() 
    {
        return estado;
    }

    public void setEstado(String estado) 
    {
        this.estado = estado;
    }
    
    
    
    
    
    
}
