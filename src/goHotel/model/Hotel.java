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
// MODELO HOTEL
//============================================================================== 
public class Hotel 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idHotel;
    private String nombreHotel;
    private int idPais;
    private String ciudad;
    private String direccion;
//==============================================================================
//CONSTRUCTOR
//==============================================================================

     public Hotel() 
     {
        this.idHotel = 0;
        this.nombreHotel = "";
        this.idPais = 0;
        this.ciudad = "";
        this.direccion = "";
    }

    public Hotel(int idHotel, String nombreHotel, int idPais, String ciudad, String direccion) {
        this.idHotel = idHotel;
        this.nombreHotel = nombreHotel;
        this.idPais = idPais;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }
//==============================================================================
//GETTERS Y SETTERS
//==============================================================================   
    public String getDireccion() 
    {
        return direccion;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public int getIdHotel() 
    {
        return idHotel;
    }

    public void setIdHotel(int idHotel)
    {
        this.idHotel = idHotel;
    }

    public String getNombreHotel() 
    {
        return nombreHotel;
    }

    public void setNombreHotel(String nombreHotel) 
    {
        this.nombreHotel = nombreHotel;
    }

    public int getIdPais() 
    {
        return idPais;
    }

    public void setIdPais(int idPais) 
    {
        this.idPais = idPais;
    }

    public String getCiudad()
    {
        return ciudad;
    }

    public void setCiudad(String ciudad) 
    {
        this.ciudad = ciudad;
    }

}
