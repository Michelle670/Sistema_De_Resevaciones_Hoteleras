
package goHotel.model;

import goHotel.controller.ConexionBD;
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

        try {
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO habitacion (id_habitacion, id_hotel, id_tipo, numero, estado) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, this.idHabitacion);
            ps.setInt(2, this.idHotel);
            ps.setInt(3, this.idTipo);
            ps.setInt(4, this.numero);

            // Validamos y normalizamos el estado antes de insertarlo
            String estadoValido = (this.estado != null && this.estado.trim().equalsIgnoreCase("Limpia"))
                    ? "Limpia"
                    : "Pendiente"; // si es nulo o cualquier otra cosa, queda como "Pendiente"
            ps.setString(5, estadoValido);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar la habitación: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                System.out.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
            ConexionBD.cerrarConexion();
        }
    }
    
    
}
