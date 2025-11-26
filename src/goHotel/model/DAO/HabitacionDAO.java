/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model.DAO;

import goHotel.controller.HabitacionController;
import goHotel.model.ConexionBD;
import goHotel.model.EstadoHabitacion;
import goHotel.model.Habitacion;
import goHotel.view.GestionHabitacion;
import java.sql.*;
import java.util.logging.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author israelapuy
 */
public class HabitacionDAO extends ConexionBD{
    public boolean registrarHabitacion(Habitacion habitacion){
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "INSERT INTO habitacion (id_habitacion, id_hotel, id_tipo, numero, estado) VALUES (?,?,?,?,?)";
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, habitacion.getIdHabitacion());
            ps.setInt(2, habitacion.getHotel());
            ps.setInt(3, habitacion.getTipo());
            ps.setInt(4, habitacion.getNumero());
            ps.setString(5, habitacion.getEstado());
            
            ps.executeUpdate();
            return true;
            
        }catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID ingresado (" + habitacion.getIdHabitacion() + ") ya está registrado.\n"
                    + "Por favor, use otro ID o edite la habitación existente.",
                    "Error de duplicado", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            System.err.println("Error SQL al registrar habitación: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conn.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public boolean editarHabitacion(Habitacion habitacion) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "UPDATE habitacion "
                + "SET id_hotel = ?, "
                + "id_tipo = ?, "
                + "numero = ?, "
                + "estado = ? "
                + "WHERE id_habitacion = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, habitacion.getHotel());
            ps.setInt(2, habitacion.getTipo());
            ps.setInt(3, habitacion.getNumero());
            ps.setString(4, habitacion.getEstado());
            ps.setInt(5, habitacion.getIdHabitacion());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID ingresado (" + habitacion.getIdHabitacion() + ") ya está registrado.\n"
                    + "Por favor, use otro ID o edite la habitació existente.",
                    "Error de duplicado", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            System.err.println("Error SQL al modificar habitación: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conn.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public boolean eliminarHabitacion(Habitacion habitacion) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "DELETE from habitacion WHERE id_habitacion=?";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, habitacion.getIdHabitacion());
            
            int rows = ps.executeUpdate();
            return rows > 0;

        }catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                conn.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
            
    }

    public boolean buscarHabitacion(Habitacion habitacion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "SELECT * FROM habitacion WHERE id_habitacion=?";
        
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, habitacion.getIdHabitacion());
            rs = ps.executeQuery();
            
            if(rs.next()){
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setHotel(rs.getInt("id_hotel"));
                habitacion.setTipo(rs.getInt("id_tipo"));
                habitacion.setNumero(rs.getInt("numero"));
                habitacion.setEstado(rs.getString("estado"));
                return true;
            }
            return false;
        }catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    public void cargarDatosEnTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_habitacion, "
                    + "id_hotel, "
                    + "id_tipo, "
                    + "numero, "
                    + "estado "
                    + "FROM habitacion";
            
            ps = conn.prepareStatement(sql);
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
    
    public void cargarEstado(JComboBox<EstadoHabitacion> combo) {
        combo.removeAllItems();
        for (EstadoHabitacion estado : EstadoHabitacion.values()) {
            combo.addItem(estado);
        }
        combo.setSelectedItem(EstadoHabitacion.DISPONIBLE);
    }
    
    //Helpers
//    public class ComboItem {
//
//        private final int id;
//        private final String label;
//
//        public ComboItem(int id, String label) {
//            this.id = id;
//            this.label = label;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        @Override
//        public String toString() {
//            return label;
//        }
//    }
//    
//    public void cargarPlanes(JComboBox<ComboItem> combo) {
//        combo.removeAllItems();
//        combo.addItem(new ComboItem(0, "--- Seleccione ---")); // opcional (id 0 NO válido)
//        try (Connection c = ConexionBD.getConnection(); PreparedStatement ps = c.prepareStatement(
//                "SELECT id_plan, nivel FROM plan_lealtad ORDER BY id_plan"); ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                combo.addItem(new ComboItem(rs.getInt("id_plan"), rs.getString("nivel")));
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error cargando planes: " + ex.getMessage());
//        }
//    }
}
