
package goHotel.model;

import java.time.LocalDate;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Reserva 
{
//==============================================================================
//ATRIBUTOS 
//==============================================================================    
    private int idReserva;
    private int idPais;
    private String nombreHotel;
    private String nombrePais;
    private String nombreCliente;
    private int    numHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;


//==============================================================================
//CONSTRUCTOR
//==============================================================================   
      public Reserva() 
      {
        this.idReserva = 0;
        this.idPais = 0;
        this.nombreHotel = "";
        this.nombrePais = "";
        this.nombreCliente = "";
        this.numHabitacion = 0;
        this.fechaEntrada = null;
        this.fechaSalida = null;
    } 

       public Reserva(int idReserva, int idPais, String nombreHotel, String nombrePais, String nombreCliente, int numHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida)
       {
        this.idReserva = idReserva;
        this.idPais = idPais;
        this.nombreHotel = nombreHotel;
        this.nombrePais = nombrePais;
        this.nombreCliente = nombreCliente;
        this.numHabitacion = numHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 

    public int getIdReserva() 
    {
        return idReserva;
    }

    public void setIdReserva(int idReserva) 
    {
        this.idReserva = idReserva;
    }

    public int getIdPais() 
    {
        return idPais;
    }

    public void setIdPais(int idPais) 
    {
        this.idPais = idPais;
    }

    public String getNombreHotel() 
    {
        return nombreHotel;
    }

    public void setNombreHotel(String nombreHotel) 
    {
        this.nombreHotel = nombreHotel;
    }

    public String getNombrePais() 
    {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) 
    {
        this.nombrePais = nombrePais;
    }

    public String getNombreCliente() 
    {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) 
    {
        this.nombreCliente = nombreCliente;
    }

    public int getNumHabitacion() 
    {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion)
    {
        this.numHabitacion = numHabitacion;
    }

    public LocalDate getFechaEntrada()
    {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada)
    {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() 
    {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) 
    {
        this.fechaSalida = fechaSalida;
    }

}
