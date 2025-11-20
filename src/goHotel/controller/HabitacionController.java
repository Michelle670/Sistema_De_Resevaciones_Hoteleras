
package goHotel.controller;

import goHotel.model.Habitacion;
import goHotel.view.GestionHabitacion;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class HabitacionController 
{
    public boolean registrarHabitacion(int idHabitacion, int idHotel, int idTipo, int numero, String estado){
        try{
            if (!validarHabitacion(idHabitacion, idHotel, idTipo, numero, estado)) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios y válidos.", "Error", JOptionPane.ERROR_MESSAGE);
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
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    
    public Habitacion buscarHabitacion(int idHabitacion){
        try{
            if(idHabitacion <= 0){
                JOptionPane.showMessageDialog(null, "Código de habitación inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            Habitacion habitacion = buscarHabitacionPorID(idHabitacion);
            
            if(habitacion != null){
                JOptionPane.showMessageDialog(null, "Habitación encontrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return habitacion;
            }else {
                JOptionPane.showMessageDialog(null, "No se encontró la habitación", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public boolean eliminarHabitacion(int idHabitacion, GestionHabitacion vista){
        try{
            Connection conn = ConexionBD.getConnection();

            String sql = "DELETE FROM habitacion WHERE id_habitacion = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, idHabitacion);

            int filas = ps.executeUpdate();

            ps.close();
            
            if (filas > 0) {
                // Mostrar mensaje y actualizar UI desde controller
                JOptionPane.showMessageDialog(vista, "Habitación eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.limpiarCampos();
                vista.actualizarTabla();
                return true;
            } else {
                JOptionPane.showMessageDialog(vista, "No existe una habitación con ese código", "Atención", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }catch (SQLException e) {
            Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void cargarDatosEnTabla(DefaultTableModel modelo, int idHabitacion) {
        modelo.setRowCount(0);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_habitacion, id_hotel, id_tipo, numero, estado FROM habitacion";
            if (idHabitacion > 0) {
                sql += " WHERE id_habitacion = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idHabitacion);
            } else {
                ps = conn.prepareStatement(sql);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_habitacion"),
                    rs.getInt("id_hotel"),
                    rs.getInt("id_tipo"),
                    rs.getInt("numero"),
                    rs.getString("estado")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar las habitaciones: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(HabitacionController.class.getName()).log(Level.SEVERE, null, e);
            }
            // No cerramos conn porque es conexión global
        }
    }
    
    private boolean validarHabitacion(int idHabitacion, int idHotel, int idTipo, int numero, String estado) {
        if (idHabitacion <= 0 || idHotel <= 0 || idTipo <= 0 || numero <= 0) {
            return false;
        }
        if (estado == null) {
            return false;
        }

        for (String e : HabitacionConst.ESTADOS) {
            if (e.equals(estado.trim())) {
                return true;
            }
        }
        return false;
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
    
    public void cargarEstado(JComboBox<String> combo) {
        combo.removeAllItems();
        for (String estado : HabitacionConst.ESTADOS) {
            combo.addItem(estado);
        }
        combo.setSelectedItem(HabitacionConst.ESTADO_PENDIENTE);
    }
    
    public final class HabitacionConst {

        public static final String ESTADO_LIMPIA = "Limpia";
        public static final String ESTADO_PENDIENTE = "Pendiente";
        public static final String[] ESTADOS = {ESTADO_LIMPIA, ESTADO_PENDIENTE};
    }
}
