package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Empleado 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================   
   private String idEmpleado;
   private String nombreEmpleado;
   private String rol;
   private String correo;
   private String contrasenna;   
//==============================================================================
//CONSTRUCTOR
//==============================================================================    
     public Empleado() 
    {
        this.idEmpleado = "";
        this.nombreEmpleado = "";
        this.rol = "";
        this.correo = "";
        this.contrasenna = "";
    }
    
    public Empleado(String idEmpleado, String nombreEmpleado, String rol, String correo, String contrasenna) 
    {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.rol = rol;
        this.correo = correo;
        this.contrasenna = contrasenna;
    }
//==============================================================================
//GETTERS Y SETTERS
//============================================================================== 

    public String getIdEmpleado()
    {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) 
    {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() 
    {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) 
    {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getRol() 
    {
        return rol;
    }

    public void setRol(String rol) 
    {
        this.rol = rol;
    }

    public String getCorreo() 
    {
        return correo;
    }

    public void setCorreo(String correo) 
    {
        this.correo = correo;
    }

    public String getContrasenna() 
    {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) 
    {
        this.contrasenna = contrasenna;
    }
    
    
}
