package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.Pais;
import goHotel.view.GestionPaises; // Mantener solo si se necesita para JOptionPane en algunos métodos
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 13 (Ajustado al patrón DAO/Modelo)
 *****************************************************************************/
public class PaisDAO extends ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(PaisDAO.class.getName());

    // ==========================================================================
    // MÉTODO 1: AGREGAR (Recibe OBJETO Pais)
    // ==========================================================================
    /**
     * Agrega un nuevo país a la base de datos.
     * @param pais El objeto Pais con los datos a guardar.
     * @return true si se agregó con éxito, false en caso contrario.
     */
    public boolean agregar(Pais pais) {
        Connection con = null;
        PreparedStatement ps = null;

        // Validaciones internas antes de DB
        if (pais.getCodigo().trim().isEmpty() || pais.getNombre().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El Código y el Nombre son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // ====================================================================
            // INICIO CORRECCIÓN para evitar 'new Pais(int)'
            // ====================================================================
            if (pais.getIdPais() != 0) {
                // 1. Crea un objeto Pais temporal (Asume que existe public Pais())
                Pais tempPais = new Pais(); 
                // 2. Asigna solo el ID al objeto temporal para la búsqueda
                tempPais.setIdPais(pais.getIdPais()); 
                
                // 3. Usa el objeto temporal para buscar si el ID ya existe
                if (buscar(tempPais) != null) {
                    JOptionPane.showMessageDialog(null, "Ya existe un país con el ID: " + pais.getIdPais() + ".", "Error de ID", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            // ====================================================================
            // FIN CORRECCIÓN
            // ====================================================================

            con = ConexionBD.getConnection();
            String sql = "INSERT INTO pais (codigo, nombre) VALUES (?,?)";
            
            // Si el ID es > 0, lo incluimos en el INSERT (si el campo no es AUTO_INCREMENT)
            if (pais.getIdPais() > 0) {
                 sql = "INSERT INTO pais (id_pais, codigo, nombre) VALUES (?,?,?)";
                 ps = con.prepareStatement(sql);
                 ps.setInt(1, pais.getIdPais());
                 ps.setString(2, pais.getCodigo().trim());
                 ps.setString(3, pais.getNombre().trim());
            } else {
                 // Si el ID es 0, asumimos que es AUTO_INCREMENT y dejamos que la DB lo genere.
                 ps = con.prepareStatement(sql);
                 ps.setString(1, pais.getCodigo().trim());
                 ps.setString(2, pais.getNombre().trim());
            }

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // El Controller manejará el mensaje de éxito

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al agregar País: ", e);
            // Mensaje de error detallado, pero el Controller es quien debe mostrar el mensaje de error general.
            return false; 
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos: ", e);
            }
        }
    }

    // ==========================================================================
    // MÉTODO 2: EDITAR (Recibe OBJETO Pais)
    // ==========================================================================
    /**
     * Modifica un país existente en la base de datos.
     * @param pais El objeto Pais con el ID y los nuevos datos a modificar.
     * @return true si se modificó con éxito, false en caso contrario.
     */
    public boolean editar(Pais pais) {
        Connection con = null;
        PreparedStatement ps = null;
        
        // Validación obligatoria de ID y campos
        if (pais.getIdPais() <= 0 || pais.getCodigo().trim().isEmpty() || pais.getNombre().trim().isEmpty()) {
             JOptionPane.showMessageDialog(null, "Debe ingresar ID, Código y Nombre válidos para modificar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return false;
        }

        try {
            con = ConexionBD.getConnection();
            String sql = "UPDATE pais SET codigo=?, nombre=? WHERE id_pais=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, pais.getCodigo().trim());
            ps.setString(2, pais.getNombre().trim());
            ps.setInt(3, pais.getIdPais());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // El Controller maneja el mensaje de éxito

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al modificar País: ", e);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos: ", e);
            }
        }
    }

    // ==========================================================================
    // MÉTODO 3: ELIMINAR (Recibe OBJETO Pais)
    // ==========================================================================
    /**
     * Elimina un país de la base de datos por su ID.
     * @param pais El objeto Pais que solo requiere el ID para la eliminación.
     * @return true si se eliminó con éxito, false en caso contrario.
     */
    public boolean eliminar(Pais pais) {
        Connection con = null;
        PreparedStatement ps = null;
        
        if (pais.getIdPais() <= 0) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido para eliminar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            con = ConexionBD.getConnection();
            String sql = "DELETE FROM pais WHERE id_pais=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, pais.getIdPais());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // El Controller maneja el mensaje de éxito
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al eliminar País: ", e);
            // Mensaje más genérico para el usuario final:
            JOptionPane.showMessageDialog(null, "Error al eliminar. Verifique si existen registros dependientes (llaves foráneas).", "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos: ", e);
            }
        }
    }
    
    // ==========================================================================
    // MÉTODO 4: BUSCAR (Recibe OBJETO Pais y retorna el objeto encontrado)
    // ==========================================================================
    /**
     * Busca un país por su ID.
     * @param pais El objeto Pais que contiene el ID de búsqueda.
     * @return El objeto Pais encontrado, o null si no existe.
     */
    public Pais buscar(Pais pais) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        Pais paisEncontrado = null;
        
        if (pais.getIdPais() <= 0) {
            // El Controller debe validar si el campo está vacío antes de llamar al DAO
            // Aquí solo evitamos una excepción SQL/lógica.
            return null;
        }
        
        String sql = "SELECT id_pais, codigo, nombre FROM pais WHERE id_pais = ?";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, pais.getIdPais());
            rs = ps.executeQuery();

            if (rs.next()) {
                paisEncontrado = new Pais(
                    rs.getInt("id_pais"),
                    rs.getString("codigo"),
                    rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de base de datos al buscar País", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en buscar: ", e);
            }
        }
        return paisEncontrado;
    }
    
    // ==========================================================================
    // MÉTODO 5: OBTENER TODOS (Necesario para actualizarTabla del Controller)
    // ==========================================================================
    /**
     * Obtiene todos los países de la base de datos.
     * @return Una lista de objetos Pais.
     */
    public List<Pais> obtenerTodos() {
        List<Pais> paises = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_pais, codigo, nombre FROM pais ORDER BY id_pais";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Pais p = new Pais(
                    rs.getInt("id_pais"),
                    rs.getString("codigo"),
                    rs.getString("nombre")
                );
                paises.add(p);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los países: ", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar recursos en obtenerTodos: ", e);
            }
        }
        return paises;
    }
    
    // ==========================================================================
    // MÉTODOS ANTERIORES NO UTILIZADOS POR EL NUEVO CONTROLLER (Mantenidos por si acaso)
    // ==========================================================================
    
    /**
     * NOTA: Este método queda obsoleto con el nuevo Controller, pero se mantiene si es usado en otro lado.
     */
     public void cargarDatosEnTabla(DefaultTableModel modeloTabla, String idCriterio) {
         modeloTabla.setRowCount(0);
         Connection conn = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         
         try {
             conn = ConexionBD.getConnection();
             String sql = "SELECT id_pais, codigo, nombre FROM pais";
             
             if (idCriterio != null && !idCriterio.trim().isEmpty()) {
                 int id = Integer.parseInt(idCriterio.trim());
                 sql += " WHERE id_pais = ?";
                 ps = conn.prepareStatement(sql);
                 ps.setInt(1, id);
             } else {
                 sql += " ORDER BY id_pais";
                 ps = conn.prepareStatement(sql);
             }
             
             rs = ps.executeQuery();
             
             while (rs.next()) {
                 Object[] fila = {
                     rs.getInt("id_pais"),
                     rs.getString("codigo"),
                     rs.getString("nombre")
                 };
                 modeloTabla.addRow(fila);
             }
         } catch (NumberFormatException e) {
              JOptionPane.showMessageDialog(null, "El ID de búsqueda debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         catch (SQLException e) {
             LOGGER.log(Level.SEVERE, "Error al cargar los países: ", e);
             JOptionPane.showMessageDialog(null, "Error de base de datos al cargar la tabla: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
         } finally {
             try {
                 if (rs != null) rs.close();
                 if (ps != null) ps.close();
             } catch (SQLException e) {
                 LOGGER.log(Level.SEVERE, "Error al cerrar recursos en cargarDatosEnTabla: ", e);
             }
         }
     }
    
    /**
     * Cargar Países en un ComboBox.
     */
    public void cargarPaises(JComboBox<String> combo) {
         try (Connection conn = ConexionBD.getConnection()) {
             String sql = "SELECT nombre FROM pais ORDER BY nombre"; // Se añade ORDER BY para consistencia
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

             combo.removeAllItems();
             combo.addItem("--- Ninguno ---");
             while (rs.next()) {
                 combo.addItem(rs.getString("nombre"));
             }

         } catch (Exception e) {
             LOGGER.log(Level.SEVERE, "Error al cargar Países en ComboBox: ", e);
         }
    }
}