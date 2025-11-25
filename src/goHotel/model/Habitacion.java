
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
    private int idHotel;
    private int idTipo;
    private int numero;
    private String estado;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Habitacion() 
    {
        this.idHabitacion = 0;
        this.idHotel = 0;
        this.idTipo = 0;
        this.numero = 0;
        this.estado = "";
    }

    public Habitacion(int idHabitacion, int idhotel, int idtipo, int numero, String estado) 
    {
        this.idHabitacion = idHabitacion;
        this.idHotel = idhotel;
        this.idTipo = idtipo;
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
        return idHotel;
    }

    public void setHotel(int idhotel) 
    {
        this.idHotel = idhotel;
    }

    public int getTipo() 
    {
        return idTipo;
    }

    public void setTipo(int tipo) 
    {
        this.idTipo = tipo;
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
