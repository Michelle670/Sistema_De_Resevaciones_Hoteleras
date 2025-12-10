//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model.DAO;
import goHotel.model.ConexionBD;
import goHotel.model.Servicio;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// SERVICIO DAO
//==============================================================================
public class ServicioDAO {
    
    //Registrar servicio en BD
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
    
    //Modificar servicio en BD
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
    
    //Eliminar servicio en BD
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
    
    //Buscar servicio en BD
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
    
    //Cargar todos los servicios en la tabla
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
            Logger.getLogger(ServicioDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(ServicioDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    //Carga solo el servicio buscado en la tabla
    public void cargarDatosEnTablaPorID(DefaultTableModel modelo, int id) {
        modelo.setRowCount(0);

        String sqlBase = "SELECT id_servicio, nombre, descripcion FROM servicio";

        String sql = sqlBase;

        if (id > 0) {
            sql += " WHERE id_servicio = ?";
        }

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (id > 0) {
                ps.setInt(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = {
                        rs.getInt("id_servicio"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    };
                    modelo.addRow(fila);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar los servicios: " + e.getMessage());
        } 
    }
    
    //Helpers para cargar los ComboBox
    public static class ComboItem {

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
        }
    }

    public void cargarServicios(JComboBox<ComboItem> combo) {
        combo.removeAllItems();
        combo.addItem(new ComboItem(0, "--- Seleccione ---"));
        String sql = "SELECT id_servicio, nombre FROM servicio ORDER BY nombre";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                combo.addItem(new ComboItem(
                        rs.getInt("id_servicio"),
                        rs.getString("nombre")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error cargando servicios: " + e.getMessage());
        }
    }
    
    //Obtener el nombre del servicio basado en el ID
    public String obtenerNombreServicio(int idServicio) {
        String sql = "SELECT nombre FROM servicio WHERE id_servicio=?";
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }
}
    

  

