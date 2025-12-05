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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author israelapuy
 */
public class ClienteDAO {
    
    //Registrar cliente en BD
    public boolean registrarCliente(Cliente cliente){
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();

        String sql = "INSERT INTO cliente (id_cliente, id_plan, Nombre, correo, "
                + "password, id_pais) VALUES (?,?,?,?,?,?)";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdCliente());
            ps.setInt(2, cliente.getIdPlan());
            ps.setString(3, cliente.getNombre());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getContrasenna());
            ps.setInt(6, cliente.getIdPais());
            ps.executeUpdate();
            return true;
        } //Verifica que el ID no esté duplicado
        catch (SQLIntegrityConstraintViolationException e) {
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
    
    //Modifica cliente en BD
    public boolean modificarCliente(Cliente cliente) {
        PreparedStatement ps = null;
        Connection conn = ConexionBD.getConnection();
        
        String sql = "UPDATE cliente "
                + "SET id_plan = ?, "
                + "    Nombre = ?, "
                + "    correo = ?, "
                + "    password = ?, "
                + "    id_pais = ? "
                + "WHERE id_cliente = ?";

        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cliente.getIdPlan());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getContrasenna());
            ps.setInt(5, cliente.getIdPais());
            ps.setInt(6, cliente.getIdCliente());

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
    
    //Eliminar cliente en BD
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
    
    //Busca cliente en BD
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
    
    //Carga los datos en la tabla
    public void cargarDatosEnTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT A.id_cliente, "
                    + "       A.id_plan, "
                    + "       PL.nivel  AS nombre_plan, "
                    + "       A.Nombre  AS nombre_cliente, "
                    + "       A.correo, "
                    + "       A.password, "
                    + "       A.id_pais, "
                    + "       P.nombre  AS nombre_pais, "
                    + "       A.puntos_lealtad "
                    + "FROM cliente A "
                    + "JOIN pais P ON A.id_pais = P.id_pais "
                    + "JOIN plan_lealtad PL ON A.id_plan = PL.id_plan "
                    + "ORDER BY A.id_cliente ASC;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_cliente"),
                    rs.getInt("id_plan"),
                    rs.getString("nombre_plan"),
                    rs.getString("nombre_cliente"),
                    rs.getString("correo"),
                    rs.getString("password"),
                    rs.getInt("id_pais"),
                    rs.getString("nombre_pais"),
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
    
    //Carga el cliente específico en la tabla luego de "Buscar"
    public void cargarDatosEnTablaPorID(DefaultTableModel modelo, int id) {
        modelo.setRowCount(0);

        String sqlBase = "SELECT A.id_cliente, "
                + "       A.id_plan, "
                + "       PL.nivel  AS nombre_plan, "
                + "       A.Nombre  AS nombre_cliente, "
                + "       A.correo, "
                + "       A.password, "
                + "       A.id_pais, "
                + "       P.nombre  AS nombre_pais, "
                + "       A.puntos_lealtad "
                + "FROM cliente A "
                + "JOIN pais P ON A.id_pais = P.id_pais "
                + "JOIN plan_lealtad PL ON A.id_plan = PL.id_plan ";

        String sql = sqlBase;

        if (id > 0) {
            sql += "WHERE A.id_cliente = ?";
        }

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (id > 0) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = {
                        rs.getInt("id_cliente"),
                        rs.getInt("id_plan"),
                        rs.getString("nombre_plan"),
                        rs.getString("nombre_cliente"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getInt("id_pais"),
                        rs.getString("nombre_pais"),
                        rs.getInt("puntos_lealtad")
                    };
                    modelo.addRow(fila);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar los clientes: " + e.getMessage());
        } 
    }
    
    //Helpers para cargar los datos en los ComboBox
    
    public class ComboItem {

        private final int id;
        private final String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        } // lo que se ve en el combo
    }
    
    public void cargarPlanes(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        combo.addItem(new ComboItem(0, "--- Seleccione ---")); // opcional (id 0 NO válido)
        try (Connection c = ConexionBD.getConnection(); PreparedStatement ps = c.prepareStatement(
                "SELECT id_plan, nivel FROM plan_lealtad ORDER BY id_plan"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(new ComboItem(rs.getInt("id_plan"), rs.getString("nivel")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error cargando planes: " + ex.getMessage());
        }
    }
    
    public void cargarPaises(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        combo.addItem(new ComboItem(0, "--- Seleccione ---")); // opcional (id 0 NO válido)
        try (Connection c = ConexionBD.getConnection(); PreparedStatement ps = c.prepareStatement(
                "SELECT id_pais, nombre FROM pais ORDER BY id_pais"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(new ComboItem(rs.getInt("id_pais"), rs.getString("nombre")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error cargando países: " + ex.getMessage());
        }
    }
}
