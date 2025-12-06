package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.PlanLealtad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//******************************************************************************
/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
//******************************************************************************

public class PlanLealtadDAO extends ConexionBD {
    
    private static final Logger LOGGER = Logger.getLogger(PlanLealtadDAO.class.getName());

    // ==========================================================================
    // MÉTODO 1: OBTENER TODOS (Reemplaza cargarDatosEnTabla para el Controller)
    // Firma requerida por Controller: List<PlanLealtad> planes = modeloDAO.obtenerTodos();
    // ==========================================================================
    public List<PlanLealtad> obtenerTodos() {
        List<PlanLealtad> planes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_plan, nivel, descuento FROM plan_lealtad ORDER BY id_plan";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                PlanLealtad pl = new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    rs.getDouble("descuento")
                );
                planes.add(pl);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar todos los planes de lealtad", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en obtenerTodos", e);
            }
        }
        return planes;
    }

    // ==========================================================================
    // MÉTODO 2: AGREGAR (Reemplaza registrarPlanLealtad)
    // Firma requerida por Controller: if (modeloDAO.agregar(modelo)) {
    // ==========================================================================
    public boolean agregar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO plan_lealtad (id_plan, nivel, descuento) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, modelo.getId()); 
            ps.setString(2, modelo.getNivel());
            ps.setDouble(3, modelo.getDescuento());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al agregar PlanLealtad: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar PreparedStatement en agregar", e);
            }
        }
    }

    // ==========================================================================
    // MÉTODO 3: EDITAR (Reemplaza modificarPlanLealtad)
    // Firma requerida por Controller: if (modeloDAO.editar(modelo)) {
    // ==========================================================================
    public boolean editar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "UPDATE plan_lealtad SET nivel = ?, descuento = ? WHERE id_plan = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, modelo.getNivel());
            ps.setDouble(2, modelo.getDescuento());
            ps.setInt(3, modelo.getId());

            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al editar PlanLealtad: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar PreparedStatement en editar", e);
            }
        }
    }
    
    // ==========================================================================
    // MÉTODO 4: ELIMINAR
    // Firma requerida por Controller: if (modeloDAO.eliminar(modelo)) {
    // ==========================================================================
    public boolean eliminar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConexionBD.getConnection();
            String sql = "DELETE FROM plan_lealtad WHERE id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, modelo.getId()); 

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar PlanLealtad: " + e.getMessage(), e);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar PreparedStatement en eliminar", e);
            }
        }
    }

    // ==========================================================================
    // MÉTODO 5: BUSCAR (Por ID)
    // Firma requerida por Controller: PlanLealtad encontrado = modeloDAO.buscar(modelo);
    // ==========================================================================
    public PlanLealtad buscar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_plan, nivel, descuento FROM plan_lealtad WHERE id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, modelo.getId());
            
            rs = ps.executeQuery();

            if (rs.next()) {
                return new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    rs.getDouble("descuento")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de BD al buscar PlanLealtad por ID", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en buscar", e);
            }
        }
        return null;
    }
}