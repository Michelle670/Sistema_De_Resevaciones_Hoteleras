//==============================================================================
// IMPORTES
//==============================================================================
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
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// PLAN LEALTAD DAO
//==============================================================================
public class PlanLealtadDAO extends ConexionBD 
{

    private static final Logger LOGGER = Logger.getLogger(PlanLealtadDAO.class.getName());

    //==========================================================================
    // MÉTODO 1: OBTENER TODOS (READ ALL)
    //==========================================================================
    public List<PlanLealtad> obtenerTodos() 
    {
        List<PlanLealtad> planes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_plan, nivel, descuento, factor_puntos FROM plan_lealtad ORDER BY id_plan";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) 
            {
                PlanLealtad pl = new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    // Se usa getObject(column, Double.class) para manejar NULL, 
                    // si la BD retorna null y el modelo usa Double (clase envoltorio)
                    // Si el modelo usa double (primitivo), getDouble() retorna 0.0 si es NULL.
                    rs.getDouble("descuento"), 
                    rs.getDouble("factor_puntos")
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

    //==========================================================================
    // MÉTODO 2: AGREGAR (CREATE)
    //==========================================================================
    public boolean agregar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "INSERT INTO plan_lealtad (id_plan, nivel, descuento, factor_puntos) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, modelo.getId());
            ps.setString(2, modelo.getNivel());
            // Usar setDouble() que maneja Double (la clase) y también double (el primitivo)
            ps.setObject(3, modelo.getDescuento()); 
            ps.setObject(4, modelo.getFactorPuntos()); 

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

    //==========================================================================
    // MÉTODO 3: EDITAR (UPDATE)
    //==========================================================================
    public boolean editar(PlanLealtad modelo) 
    {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "UPDATE plan_lealtad SET nivel = ?, descuento = ?, factor_puntos = ? WHERE id_plan = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, modelo.getNivel());
            ps.setObject(2, modelo.getDescuento());
            ps.setObject(3, modelo.getFactorPuntos()); 
            ps.setInt(4, modelo.getId());

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

    //==========================================================================
    // MÉTODO 4: ELIMINAR (DELETE - Por ID o Nivel)
    //==========================================================================
    public boolean eliminar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql;

        try {
            conn = ConexionBD.getConnection();

            if (modelo.getId() > 0) {
                sql = "DELETE FROM plan_lealtad WHERE id_plan = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, modelo.getId());
            } else if (modelo.getNivel() != null && !modelo.getNivel().trim().isEmpty()) {
                sql = "DELETE FROM plan_lealtad WHERE nivel = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, modelo.getNivel());
            } else {
                LOGGER.log(Level.WARNING, "Intento de eliminar PlanLealtad sin ID ni Nivel válidos.");
                return false;
            }

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

    //==========================================================================
    // MÉTODO 5: BUSCAR (READ ONE - Por ID)
    //==========================================================================
    public PlanLealtad buscar(PlanLealtad modelo) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PlanLealtad encontrado = null;

        if (modelo.getId() <= 0) {
             return null; 
        }

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_plan, nivel, descuento, factor_puntos FROM plan_lealtad WHERE id_plan = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, modelo.getId());

            rs = ps.executeQuery();

            if (rs.next()) {
                encontrado = new PlanLealtad(
                    rs.getInt("id_plan"),
                    rs.getString("nivel"),
                    rs.getDouble("descuento"),
                    rs.getDouble("factor_puntos") 
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
        return encontrado;
    }

    public PlanLealtad buscarPorId(int id) {
        // Este método está duplicado/no usado. Es mejor eliminarlo o refactorizar 
        // para que use el método 'buscar(PlanLealtad modelo)'. 
        // Lo dejamos para cumplir con el código original, pero es redundante.
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}