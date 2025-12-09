
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
    private String descripcionHabitacion;
    private int    numHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String horaEntrada;
    private String horaSalida;


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
        this.descripcionHabitacion = "";
        this.numHabitacion = 0;
        this.fechaEntrada = null;
        this.fechaSalida = null;
        this.horaEntrada = "";
        this.horaSalida = "";
    } 

       public Reserva(int idReserva, int idPais, String nombreHotel, String nombrePais, String nombreCliente,String descripcionHabitacion, int numHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida,String horaEntrada,String horaSalida)
       {
        this.idReserva = idReserva;
        this.idPais = idPais;
        this.nombreHotel = nombreHotel;
        this.nombrePais = nombrePais;
        this.nombreCliente = nombreCliente;
        this.descripcionHabitacion = descripcionHabitacion;
        this.numHabitacion = numHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
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

    public String getDescripcionHabitacion() 
    {
        return descripcionHabitacion;
    }

    public void setDescripcionHabitacion(String descripcionHabitacion) 
    {
        this.descripcionHabitacion = descripcionHabitacion;
    }

    public String getHoraEntrada() 
    {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) 
    {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() 
    {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) 
    {
        this.horaSalida = horaSalida;
    }
    
    
    
    

}
