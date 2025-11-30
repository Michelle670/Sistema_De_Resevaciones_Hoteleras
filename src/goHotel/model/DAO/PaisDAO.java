package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.Pais;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Necesario para listar
import java.util.List; // Necesario para listar

public class PaisDAO {

    // 1. EL DAO RECIBE EL OBJETO PAIS
    public boolean agregarPais(Pais pais) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        String sql = "INSERT INTO pais (id_pais, codigo, nombre) VALUES (?,?,?)";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);

            // El DAO solo usa los datos del objeto Pais (el Modelo)
            ps.setInt(1, pais.getIdPais());
            ps.setString(2, pais.getCodigo());
            ps.setString(3, pais.getNombre());

            ps.execute();
            return true;
        } catch (SQLException e) {
            // El DAO lanza la excepción para que el Controlador la maneje y notifique a la Vista
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos en agregar: " + e.getMessage());
            }
        }
    }

    // 2. EL DAO RECIBE EL OBJETO PAIS PARA MODIFICAR
    public boolean modificarPais(Pais pais) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        String sql = "UPDATE pais SET codigo=?, nombre=? WHERE id_pais=?";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, pais.getCodigo());
            ps.setString(2, pais.getNombre());
            ps.setInt(3, pais.getIdPais());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos en modificar: " + e.getMessage());
            }
        }
    }

    // 3. EL DAO RECIBE SOLO EL ID PARA ELIMINAR
    public boolean eliminarPais(int idPais) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        String sql = "DELETE FROM pais WHERE id_pais=?";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, idPais);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos en eliminar: " + e.getMessage());
            }
        }
    }

    // 4. EL DAO DEVUELVE UN OBJETO PAIS ENCONTRADO
    public Pais buscarPaisPorId(int idPais) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        Pais paisEncontrado = null;
        String sql = "SELECT id_pais, codigo, nombre FROM pais WHERE id_pais = ?";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPais);
            rs = ps.executeQuery();

            if (rs.next()) {
                paisEncontrado = new Pais(
                        rs.getInt("id_pais"),
                        rs.getString("codigo"),
                        rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos en buscar: " + e.getMessage());
            }
        }
        return paisEncontrado;
    }

    // 5. LISTAR TODOS LOS PAÍSES
    public List<Pais> listarPaises() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        List<Pais> listaPaises = new ArrayList<>();
        String sql = "SELECT id_pais, codigo, nombre FROM pais ORDER BY nombre";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pais p = new Pais(
                        rs.getInt("id_pais"),
                        rs.getString("codigo"),
                        rs.getString("nombre")
                );
                listaPaises.add(p);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos en listarPaises: " + e.getMessage());
            }
        }
        return listaPaises;
    }
}
