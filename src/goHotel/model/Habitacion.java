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
// MODELO HABITACION
//============================================================================== 
public class Habitacion 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idHabitacion;
    private int idHotel;
    private int idTipo;
    private int numero;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Habitacion() 
    {
        this.idHabitacion = 0;
        this.idHotel = 0;
        this.idTipo = 0;
        this.numero = 0;
    }

    public Habitacion(int idHabitacion, int idhotel, int idtipo, int numero) 
    {
        this.idHabitacion = idHabitacion;
        this.idHotel = idhotel;
        this.idTipo = idtipo;
        this.numero = numero;
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
    //==========================================================================
}
