package goHotel.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
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

    public Hotel(int idHotel, String nombreHotel, int idPais, String ciudad, String direccion) 
    {
        this.idHotel = idHotel;
        this.nombreHotel = nombreHotel;
        this.idPais = idPais;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }
    
    //==============================================================================
    // MÉTODO PARA AGREGAR HOTEL A LA BASE DE DATOS
    //==============================================================================
    public boolean agregar()
    {
        Connection conn = null;
        PreparedStatement ps = null;

        try
        {
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO hotel (id_hotel, nombre, id_pais, ciudad, direccion) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, this.idHotel);
            ps.setString(2, this.nombreHotel);
            ps.setInt(3, this.idPais);
            ps.setString(4, this.ciudad);
            ps.setString(5, this.direccion);

            ps.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Error al agregar el hotel: " + e.getMessage());
            return false;
        }
        finally
        {
            //try { if(ps != null) ps.close(); } catch(Exception e) {}
            ConexionBD.cerrarConexion();
        }
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
