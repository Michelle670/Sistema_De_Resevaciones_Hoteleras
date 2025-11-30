
package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michelle
 */
public class RolDAO extends ConexionBD {
    public boolean registrarRol(String nombre, boolean estado) {
        Connection conexion = null;
        PreparedStatement ps = null;
        
        
        try {
            conexion = ConexionBD.getConnection();
            String sql = "INSERT INTO rol_empleado (nombre, estado) VALUES (?, ?)";
            ps = conexion.prepareStatement(sql);

            ps.setString(1, nombre.trim());
            ps.setInt(2, estado ? 1 : 0);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public boolean editarRol(int idRol, String nombre, boolean estado) {
        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionBD.getConnection();
            String sql = "UPDATE rol_empleado SET nombre=?, estado=? WHERE id_rol=?";
            ps = conexion.prepareStatement(sql);

            ps.setString(1, nombre.trim());
            ps.setInt(2, estado ? 1 : 0);
            ps.setInt(3, idRol);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
        }
    }

    public boolean eliminarRol(int idRol) {
        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionBD.getConnection();
            String sql = "DELETE FROM rol_empleado WHERE id_rol = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idRol);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public ArrayList<Object[]> buscarRolesPorNombre(String nombre) {
        ArrayList<Object[]> resultados = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_rol, nombre, estado FROM rol_empleado WHERE nombre LIKE ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                int estadoBD = rs.getInt("estado");
                String estadoTexto;

                if (estadoBD == 1) {
                    estadoTexto = "Activo";
                } else {
                    estadoTexto = "Inactivo";
                }

                Object[] fila = {
                    rs.getInt("id_rol"),
                    rs.getString("nombre"),
                    estadoTexto
                };

                resultados.add(fila);
            }

        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }

        return resultados;
    }

    public void cargarDatosEnTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.getConnection();
            String sql = "SELECT id_rol, nombre, estado FROM rol_empleado ORDER BY id_rol";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            ArrayList<Object[]> lista = new ArrayList<>();

            while (rs.next()) {

                int estadoBD = rs.getInt("estado");
                String estadoTexto;

                if (estadoBD == 1) {
                    estadoTexto = "Activo";
                } else {
                    estadoTexto = "Inactivo";
                }

                Object[] fila = {
                    rs.getInt("id_rol"),
                    rs.getString("nombre"),
                    estadoTexto
                };

                lista.add(fila);
            }

            Iterator<Object[]> it = lista.iterator();
            while (it.hasNext()) {
                modelo.addRow(it.next());
            }
        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public ArrayList<String> cargarRolesActivos() {
        ArrayList<String> roles = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.getConnection();
            String sql = "SELECT nombre FROM rol_empleado WHERE estado = 1 ORDER BY nombre";
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                roles.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            Logger.getLogger(RolDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }

        return roles;
    }

}
