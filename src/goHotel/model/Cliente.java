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
// MODELO CLIENTE 
//============================================================================== 
public class Cliente 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idCliente;
    private int idPlan;
    private String nombre;
    private String correo;
    private String contrasenna;
    private int idPais;
    private int puntosLealtad;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Cliente() 
    {
        this.idCliente = 0;
        this.idPlan = 0;
        this.nombre = "";
        this.correo = "";
        this.contrasenna = "";
        this.idPais = 0;
        this.puntosLealtad = 0;
    }
     
    public Cliente(int idCliente, int idPlan, String nombre, String correo, String contrasenna, int idPais, int puntosLealtad) 
    {
        this.idCliente = idCliente;
        this.idPlan = idPlan;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenna = contrasenna;
        this.idPais = idPais;
        this.puntosLealtad = puntosLealtad;
    }
//==============================================================================
//GETTERS Y SETTERS
//==============================================================================    

    public int getIdCliente()
    {
        return idCliente;
    }

    public void setIdCliente(int idCliente)
    {
        this.idCliente = idCliente;
    }

    public int getIdPlan() 
    {
        return idPlan;
    }

    public void setIdPlan(int idPlan) 
    {
        this.idPlan = idPlan;
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

    public int getIdPais()
    {
        return idPais;
    }

    public void setIdPais(int idPais)
    {
        this.idPais = idPais;
    }

    public int getPuntosLealtad() 
    {
        return puntosLealtad;
    }

    public void setPuntosLealtad(int puntosLealtad) 
    {
        this.puntosLealtad = puntosLealtad;
    }
//==============================================================================
}