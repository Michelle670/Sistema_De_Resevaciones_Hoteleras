
package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Habitacion_Servicio 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
  private Habitacion habitacion;
  private Servicio servicio;
//==============================================================================
//CONSTRUCTOR
//==============================================================================  
     public Habitacion_Servicio() 
     {
        this.habitacion = new Habitacion();
        this.servicio = new Servicio();
    }
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 
    public Habitacion_Servicio(Habitacion habitacion, Servicio servicio) 
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
