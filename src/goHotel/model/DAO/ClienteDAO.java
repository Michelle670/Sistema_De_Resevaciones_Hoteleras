/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model.DAO;

import goHotel.model.Cliente;
import goHotel.model.ConexionBD;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author israelapuy
 */
public class ClienteDAO {
    
    public boolean registrarCliente(Cliente cliente){
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "INSERT INTO cliente (id_cliente, id_plan, Nombre, correo, "
                + "password, id_pais, puntos_lealtad) VALUES (?,?,?,?,?,?,?)";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdCliente());
            ps.setInt(2, cliente.getIdPlan());
            ps.setString(3, cliente.getNombre());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getContrasenna());
            ps.setInt(6, cliente.getIdPais());
            ps.setInt(7, cliente.getPuntosLealtad());
            ps.executeUpdate();
            return true;
        }catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null,
                    "El ID ingresado (" + cliente.getIdCliente() + ") ya está registrado.\n"
                    + "Por favor, use otro ID o edite el cliente existente.",
                    "Error de duplicado", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            System.err.println("Error SQL al registrar cliente: " + e.getMessage());
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
    
    public boolean modificarCliente(Cliente cliente) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "UPDATE cliente "
                + "SET id_plan = ?, "
                + "    Nombre = ?, "
                + "    correo = ?, "
                + "    password = ?, "
                + "    id_pais = ?, "
                + "    puntos_lealtad = ? "
                + "WHERE id_cliente = ?";

        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdPlan());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getContrasenna());
            ps.setInt(5, cliente.getIdPais());
            ps.setInt(6, cliente.getPuntosLealtad());
            ps.setInt(7, cliente.getIdCliente());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al modificar cliente: " + e.getMessage());
            return false;
        }finally {
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
    
    public boolean eliminarCliente(Cliente cliente) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql ="DELETE from cliente WHERE id_cliente=?";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdCliente());
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
    
    public boolean buscarCliente(Cliente cliente) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "SELECT * FROM cliente WHERE id_cliente=?";
        
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdCliente());
            rs = ps.executeQuery();
            
            if(rs.next()){
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setIdPlan(rs.getInt("id_plan"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setContrasenna(rs.getString("password"));
                cliente.setIdPais(rs.getInt("id_pais"));
                cliente.setPuntosLealtad(rs.getInt("puntos_lealtad"));
                return true;
            }
            return false;
        } catch (SQLException e) {
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
            String sql = "SELECT id_cliente, id_plan, Nombre, correo, password, id_pais, puntos_lealtad FROM cliente";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_cliente"),
                    rs.getInt("id_plan"),
                    rs.getString("Nombre"),
                    rs.getString("correo"),
                    rs.getString("password"),
                    rs.getInt("id_pais"),
                    rs.getInt("puntos_lealtad")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar los clientes: " + e.getMessage());
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
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
}
