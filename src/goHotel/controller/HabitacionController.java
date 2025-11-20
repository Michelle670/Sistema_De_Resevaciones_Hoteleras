
package goHotel.controller;

import goHotel.model.Habitacion;
import java.sql.*;
import java.util.logging.*;
import javax.swing.JOptionPane;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class HabitacionController 
{
    public boolean registrarHabitacion(int idHabitacion, int idHotel, int idTipo, int numero, String estado){
        try{
            if(idHabitacion <= 0 || idHotel <= 0 || idHotel <= 0 || numero <= 0 || estado.trim().isEmpty() ){
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Verificar si ya existe un hotel con ese código
            if (buscarHabitacionPorID(idHabitacion) != null) {
                JOptionPane.showMessageDialog(null, "Ya existe una habitación con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            Habitacion nuevaHabitacion = new Habitacion(idHabitacion, idHotel, idTipo, numero, estado.trim());
            
            if(nuevaHabitacion.agregar()){
                JOptionPane.showMessageDialog(null, "Habitación agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Error: No se pudo agregar la habitación.", "Fallido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean editarHabitacion(int idHabitacion, int idHotel, int idTipo, int numero, String estado){
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            if(idHabitacion <= 0){
                JOptionPane.showMessageDialog(null, "Ingrese el código del hotel que va a editar", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if(idHotel <= 0 || idTipo <= 0 || numero <= 0 || estado.trim().isEmpty()){
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            conn = ConexionBD.getConnection();
            String sql = "UPDATE habitacion SET id_hotel = ?, id_tipo = ?, numero = ?, estado = ? WHERE id_habitacion = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idHotel);
            ps.setInt(2, idTipo);
            ps.setInt(3, numero);
            ps.setString(4, estado);
            ps.setInt(5, idHabitacion);
            
            int filasActualizadas = ps.executeUpdate();
            
            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(null, "Habitacion actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No existe una habitacion con ese ID", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            }
            // NO cerramos conn porque es conexión global
        }
        
    }
    
    private Habitacion buscarHabitacionPorID(int idHabitacion){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_habitacion, id_hotel, numero, id_tipo, estado FROM habitacion WHERE id_habitacion = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idHabitacion);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Habitacion(
                        rs.getInt("id_habitacion"),
                        rs.getInt("id_hotel"),
                        rs.getInt("id_tipo"),
                        rs.getInt("numero"),
                        rs.getString("estado")
                );
            }
            
        }catch (SQLException e) {
            Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return null;
    }
}
