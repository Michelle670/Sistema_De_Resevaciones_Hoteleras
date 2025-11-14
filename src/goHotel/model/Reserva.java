
package goHotel.model;
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
    private String idReserva;
    private Cliente cliente;
    private Habitacion habitacion;
    private String fechaEntrada;
    private String fechaSalida;
    private String fechaCreacion;
    private String fechaModificacion;
    private Empleado creadoPor;
    private Empleado modificadoPor;

//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Reserva() {
        this.idReserva = "";
        this.cliente = new Cliente();
        this.habitacion = new Habitacion();
        this.fechaEntrada = "";
        this.fechaSalida = "";
        this.fechaCreacion = "";
        this.fechaModificacion = "";
        this.creadoPor = new Empleado();
        this.modificadoPor = new Empleado();
    }

    
    public Reserva(String idReserva, Cliente cliente, Habitacion habitacion, 
                   String fechaEntrada, String fechaSalida, 
                   String fechaCreacion, String fechaModificacion, 
                   Empleado creadoPor, Empleado modificadoPor) {
        
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.creadoPor = creadoPor;
        this.modificadoPor = modificadoPor;
    }


//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 

    public String getIdReserva() 
    {
        return idReserva;
    }

    public void setIdReserva(String idReserva) 
    {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() 
    {
        return cliente;
    }

    public void setCliente(Cliente cliente)
    {
        this.cliente = cliente;
    }

    public Habitacion getHabitacion() 
    {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion)
    {
        this.habitacion = habitacion;
    }

    public String getFechaEntrada() 
    {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) 
    {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() 
    {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) 
    {
        this.fechaSalida = fechaSalida;
    }

    public String getFechaCreacion() 
    {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) 
    {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() 
    {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) 
    {
        this.fechaModificacion = fechaModificacion;
    }

    public Empleado getCreadoPor() 
    {
        return creadoPor;
    }

    public void setCreadoPor(Empleado creadoPor) 
    {
        this.creadoPor = creadoPor;
    }

    public Empleado getModificadoPor()
    {
        return modificadoPor;
    }

    public void setModificadoPor(Empleado modificadoPor)
    {
        this.modificadoPor = modificadoPor;
    }

    
}
