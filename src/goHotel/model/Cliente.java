
package goHotel.model;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Cliente 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private String idCliente;
    private PlanLealtad plan;
    private String nombre;
    private String correo;
    private String contrasenna;
    private String pais;
    private int puntosLealtad;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Cliente() 
    {
        this.idCliente = "";
        this.plan = new PlanLealtad();
        this.nombre = "";
        this.correo = "";
        this.contrasenna = "";
        this.pais = "";
        this.puntosLealtad = 0;
    }
     
    public Cliente(String idCliente, PlanLealtad plan, String nombre, String correo, String contrasenna, String pais, int puntosLealtad) 
    {
        this.idCliente = idCliente;
        this.plan = plan;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenna = contrasenna;
        this.pais = pais;
        this.puntosLealtad = puntosLealtad;
    }
//==============================================================================
//GETTERS Y SETTERS
//==============================================================================    

    public String getIdCliente() 
    {
        return idCliente;
    }

    public void setIdCliente(String idCliente) 
    {
        this.idCliente = idCliente;
    }

    public PlanLealtad getPlan() 
    {
        return plan;
    }

    public void setPlan(PlanLealtad plan) 
    {
        this.plan = plan;
    }

    public String getNombre() 
    {
        return nombre;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
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

    public String getPais() 
    {
        return pais;
    }

    public void setPais(String pais) 
    {  
        this.pais = pais;
    }

    public int getPuntosLealtad() 
    {
        return puntosLealtad;
    }

    public void setPuntosLealtad(int puntosLealtad) 
    {
        this.puntosLealtad = puntosLealtad;
    }
    
}
