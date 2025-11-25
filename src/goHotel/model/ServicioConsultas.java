/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model;

import goHotel.controller.ConexionBD;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author israelapuy
 */
public class ServicioConsultas {
    public boolean registrarServicio(Servicio servicio){
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "INSERT INTO servicio (id_servicio, nombre, descripcion) VALUES (?,?,?)";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, servicio.getIdServicio());
            ps.setString(2, servicio.getNombreServicio());
            ps.setString(3, servicio.getDescripcion());
            ps.executeUpdate();
            return true;
        }catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID ingresado (" + servicio.getIdServicio() + ") ya está registrado.\n"
                    + "Por favor, use otro ID o edite el servicio existente.",
                    "Error de duplicado", JOptionPane.ERROR_MESSAGE);
            return false;
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
    
    public boolean modificarServicio(Servicio servicio) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "UPDATE servicio SET nombre=?, descripcion=? WHERE id_servicio=?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setInt(3, servicio.getIdServicio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
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
    
    public boolean eliminarServicio(Servicio servicio) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "DELETE from servicio WHERE id_servicio=?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, servicio.getIdServicio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
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
    
    public boolean buscarServicio(Servicio servicio){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "SELECT * FROM servicio WHERE id_servicio=?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, servicio.getIdServicio());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                servicio.setIdServicio(rs.getInt("id_servicio"));
                servicio.setNombreServicio(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                return true;
            }
            return false;
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
    
    public void cargarDatosEnTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_servicio, nombre, descripcion FROM servicio";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_servicio"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            Logger.getLogger(ServicioConsultas.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar los servicios: " + e.getMessage());
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
                Logger.getLogger(ServicioConsultas.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
    

  

