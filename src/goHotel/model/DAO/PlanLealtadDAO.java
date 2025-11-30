package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.PlanLealtad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanLealtadDAO {

    public boolean agregarPlanLealtad(PlanLealtad plan) throws SQLException {
        PreparedStatement ps = null;
        Connection con = ConexionBD.getConnection();
        String sql = "INSERT INTO plan_lealtad (id_plan, descuento, nivel) VALUES (?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, plan.getIdLealtad());
            ps.setDouble(2, plan.getDescuento());
            ps.setString(3, plan.getNivel());
            
            return ps.executeUpdate() > 0;
            
        } finally {
            try {
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en agregarPlanLealtad: " + e.getMessage());
            }
        }
    }

    public boolean modificarPlanLealtad(PlanLealtad plan) throws SQLException {
        PreparedStatement ps = null;
        Connection con = ConexionBD.getConnection();
        String sql = "UPDATE plan_lealtad SET descuento=?, nivel=? WHERE id_plan=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setDouble(1, plan.getDescuento());
            ps.setString(2, plan.getNivel());
            ps.setInt(3, plan.getIdLealtad());

            return ps.executeUpdate() > 0;
            
        } finally {
            try {
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en modificarPlanLealtad: " + e.getMessage());
            }
        }
    }
    
    public boolean eliminarPlanLealtad(int idLealtad) throws SQLException {
        PreparedStatement ps = null;
        Connection con = ConexionBD.getConnection();
        String sql = "DELETE FROM plan_lealtad WHERE id_plan=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idLealtad);

            return ps.executeUpdate() > 0;
            
        } finally {
            try {
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en eliminarPlanLealtad: " + e.getMessage());
            }
        }
    }

    public PlanLealtad buscarPlanLealtad(String criterio) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = ConexionBD.getConnection();
        PlanLealtad plan_LealtadEncontrado = null;

        String sql = "SELECT id_plan, nivel, descuento FROM plan_lealtad WHERE id_plan = ? OR (id_plan = 0 AND nivel LIKE ?)"; 
        
        try {
            ps = con.prepareStatement(sql);
            int idBusqueda = 0;
            String nivelBusqueda = null;

            try {
                idBusqueda = Integer.parseInt(criterio);
            } catch (NumberFormatException e) {
                nivelBusqueda = criterio;
            }

            if (nivelBusqueda != null) {
                ps.setInt(1, 0); 
                ps.setString(2, "%" + nivelBusqueda + "%");
            } else {
                ps.setInt(1, idBusqueda);
                ps.setString(2, null); 
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                plan_LealtadEncontrado = new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    rs.getDouble("descuento")
                );
            }
            return plan_LealtadEncontrado;
            
        } finally {
            try {
                if (rs != null) { rs.close(); }
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en buscarPlanLealtad: " + e.getMessage());
            }
        }
    }

    public List<PlanLealtad> listarPlanesLealtad() throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = ConexionBD.getConnection();
        List<PlanLealtad> lista = new ArrayList<>();
        String sql = "SELECT id_plan, descuento, nivel FROM plan_lealtad ORDER BY id_plan";

        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PlanLealtad pl = new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    rs.getDouble("descuento")
                );
                lista.add(pl);
            }
            return lista;
            
        } finally {
            try {
                if (rs != null) { rs.close(); }
                if (ps != null) { ps.close(); }
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en listarPlanesLealtad: " + e.getMessage());
            }
        }
    }
}