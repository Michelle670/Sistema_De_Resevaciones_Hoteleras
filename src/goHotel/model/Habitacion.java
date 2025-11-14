
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
    private Hotel hotel;
    private TipoHabitacion tipo;
    private int numero;
    private String estado;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Habitacion() 
    {
        this.idHabitacion = 0;
        this.hotel = new Hotel();
        this.tipo = new TipoHabitacion();
        this.numero = 0;
        this.estado = "";
    }

    public Habitacion(int idHabitacion, Hotel hotel, TipoHabitacion tipo, int numero, String estado) 
    {
        this.idHabitacion = idHabitacion;
        this.hotel = hotel;
        this.tipo = tipo;
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

    public Hotel getHotel() 
    {
        return hotel;
    }

    public void setHotel(Hotel hotel) 
    {
        this.hotel = hotel;
    }

    public TipoHabitacion getTipo() 
    {
        return tipo;
    }

    public void setTipo(TipoHabitacion tipo) 
    {
        this.tipo = tipo;
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
