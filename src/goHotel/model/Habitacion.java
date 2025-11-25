
package goHotel.model;

import java.sql.*;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class Habitacion 
{
//==============================================================================
//ATRIBUTOS   
//==============================================================================
    private int idHabitacion;
    private int idHotel;
    private int idTipo;
    private int numero;
    private String estado;
//==============================================================================
//CONSTRUCTOR
//==============================================================================   
    public Habitacion() 
    {
        this.idHabitacion = 0;
        this.idHotel = 0;
        this.idTipo = 0;
        this.numero = 0;
        this.estado = "";
    }

    public Habitacion(int idHabitacion, int idhotel, int idtipo, int numero, String estado) 
    {
        this.idHabitacion = idHabitacion;
        this.idHotel = idhotel;
        this.idTipo = idtipo;
        this.numero = numero;
        this.estado = estado;
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

    public String getEstado() 
    {
        return estado;
    }

    public void setEstado(String estado) 
    {
        this.estado = estado;
    }
    
    
    //==============================================================================
    // MÉTODO PARA AGREGAR HOTEL A LA BASE DE DATOS
    //==============================================================================
    
    public boolean agregar() {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try{
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO habitacion (id_habitacion, id_hotel, numero, id_tipo, estado) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.idHabitacion);
            ps.setInt(2, this.idHotel);
            ps.setInt(3, this.numero);
            ps.setInt(4, this.idTipo);
            ps.setString(5, this.estado);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el hotel: " + e.getMessage());
            return false;
        } finally {
            //try { if(ps != null) ps.close(); } catch(Exception e) {}
            ConexionBD.cerrarConexion();
        }
    }
    
    
}
