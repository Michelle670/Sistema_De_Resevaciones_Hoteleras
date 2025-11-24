
package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class HabitacionServicio 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
  private Habitacion habitacion;
  private Servicio servicio;
//==============================================================================
//CONSTRUCTOR
//==============================================================================  
     public HabitacionServicio() 
     {
        this.habitacion = new Habitacion();
        this.servicio = new Servicio();
    }
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 
    public HabitacionServicio(Habitacion habitacion, Servicio servicio) 
    {
        this.habitacion = habitacion;
        this.servicio = servicio;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
   

   
   
   
   
   
  
   
}
