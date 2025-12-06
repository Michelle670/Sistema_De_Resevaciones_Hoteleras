package goHotel.model.DAO;
import goHotel.model.ConexionBD;
import goHotel.model.Pais;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 
import javax.swing.JComboBox;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 13
 *****************************************************************************/
public class PaisDAO extends ConexionBD
{
//============================================================================== 
// CARGAR PAISES EN UN COMBO BOX
//==============================================================================
 public void cargarPaises(JComboBox<String> combo) 
 {
    try (Connection conn = ConexionBD.getConnection()) 
    {
        String sql = "SELECT nombre FROM pais";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        combo.removeAllItems(); // limpiar combo por si acaso
        combo.addItem("--- Ninguno ---");
        while (rs.next()) 
        {
            combo.addItem(rs.getString("nombre"));
        }

    } catch (Exception e) 
    {
        e.printStackTrace();
    }
}   


    public boolean agregarPais(Pais pais) throws SQLException {
        PreparedStatement ps = null;
        Connection con = null;
        String sql = "INSERT INTO pais (id_pais, codigo, nombre) VALUES (?,?,?)";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);

            ps.setInt(1, pais.getIdPais());
            ps.setString(2, pais.getCodigo());
            ps.setString(3, pais.getNombre());

            ps.execute();
            return true;
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
                System.out.println("Error al cerrar recursos en agregar: " + e.getMessage());
            }
        }
    }

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

    public List<Pais> listarPaises() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        List<Pais> listaPaises = new ArrayList<>();
        String sql = "SELECT id_pais, codigo, nombre FROM pais ";

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

