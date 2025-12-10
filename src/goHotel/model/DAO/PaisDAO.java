//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model.DAO;
import goHotel.model.ConexionBD;
import goHotel.model.Pais;
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
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// PAIS DAO
//==============================================================================
/**
 * Clase Data Access Object para la entidad Pais.
 */

public class PaisDAO extends ConexionBD 
{
    
    // Si la compañera usa Logger(HotelController) y System.err, eliminamos el logger estático
    // private static final Logger LOGGER = Logger.getLogger(PaisDAO.class.getName());

//==========================================================================
// REGISTRAR PAIS
//==========================================================================
    /**
     * Agrega un nuevo país a la base de datos.
     * @param pais El objeto Pais con los datos a guardar.
     * @return true si se agregó con éxito, false en caso contrario.
     */
    public boolean registrar(Pais pais)
    {
        Connection con = null;
        PreparedStatement ps = null;

        try 
        {
            if (pais.getCodigo().trim().isEmpty() || pais.getNombre().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El Código y el Nombre son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            con = ConexionBD.getConnection();
            String sql;

            if (pais.getIdPais() > 0) {
                sql = "INSERT INTO pais (id_pais, codigo, nombre) VALUES (?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, pais.getIdPais());
                ps.setString(2, pais.getCodigo().trim());
                ps.setString(3, pais.getNombre().trim());
            } else {
                sql = "INSERT INTO pais (codigo, nombre) VALUES (?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, pais.getCodigo().trim());
                ps.setString(2, pais.getNombre().trim());
            }

            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) 
        {
            // Estilo de manejo de error (usando log para consistencia, aunque la compañera a veces usa JOptionPane solo)
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error de base de datos al registrar País: ", e);

            if (e.getSQLState().startsWith("23")) 
            {
                JOptionPane.showMessageDialog(null,
                    "Error: El Código ISO o el ID ingresado ya existen. Verifique los datos.",
                    "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Error de base de datos al registrar País. Consulte el log.",
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } finally 
        {
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e) 
            {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en registrar: ", e);
            }
            // NO cerramos conn porque es conexión global
        }
    }

//==========================================================================
// EDITAR PAIS
//==========================================================================
    /**
     * Modifica un país existente en la base de datos.
     * @param pais El objeto Pais con el ID y los nuevos datos a modificar.
     * @return true si se modificó con éxito, false en caso contrario.
     */
    public boolean editar(Pais pais)
    {
        Connection con = null;
        PreparedStatement ps = null;

        if (pais.getIdPais() <= 0 || pais.getCodigo().trim().isEmpty() || pais.getNombre().trim().isEmpty()) 
        {
              JOptionPane.showMessageDialog(null, "Debe ingresar ID, Código y Nombre válidos para modificar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
              return false;
        }

        try
        {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement("UPDATE pais SET codigo = ?, nombre = ? WHERE id_pais = ?");
            
            ps.setString(1, pais.getCodigo().trim());
            ps.setString(2, pais.getNombre().trim());
            ps.setInt(3, pais.getIdPais());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) 
        {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error de base de datos al editar País: ", e);
            if (e.getSQLState().startsWith("23")) {
                JOptionPane.showMessageDialog(null,
                    "Error: El Código ISO ingresado ya existe en otro país.",
                    "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
            } else 
            {
                JOptionPane.showMessageDialog(null,
                    "Error de base de datos al editar País. Consulte el log.",
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } finally 
        {
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en editar: ", e);
            }
            // NO cerramos conn porque es conexión global
        }
    }

//==========================================================================
// ELIMINAR PAIS
//==========================================================================
    /**
     * Elimina un país de la base de datos por su ID.
     * @param pais El objeto Pais que solo requiere el ID para la eliminación.
     * @return true si se eliminó con éxito, false en caso contrario.
     */
    public boolean eliminar(Pais pais) 
    {
        Connection con = null;
        PreparedStatement ps = null;

        if (pais.getIdPais() <= 0) 
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID válido para eliminar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try 
        {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement("DELETE FROM pais WHERE id_pais = ?");
            
            ps.setInt(1, pais.getIdPais());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) 
        {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error de base de datos al eliminar País: ", e);
            if (e.getSQLState().startsWith("23")) {
                JOptionPane.showMessageDialog(null, "Error: No se puede eliminar el país porque tiene hoteles asociados.", "Violación de Clave Foránea", JOptionPane.ERROR_MESSAGE);
            } else 
            {
                JOptionPane.showMessageDialog(null, "Error al eliminar. Verifique el log de errores.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } finally 
        {
            try
            {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en eliminar: ", e);
            }
            // NO cerramos conn porque es conexión global
        }
    }

//==========================================================================
// BUSCAR PAIS POR ID (UTILIDAD INTERNA/EXTERNA)
//==========================================================================
    /**
     * Busca un país por su ID.
     * @param id El ID del país a buscar.
     * @return El objeto Pais encontrado, o null si no existe.
     */
    public Pais buscarPorId(int id) 
    {
        Pais paisEncontrado = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (id <= 0) return null;

        String sql = "SELECT id_pais, codigo, nombre FROM pais WHERE id_pais = ?";

        try 
        {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) 
            {
                paisEncontrado = new Pais(
                    rs.getInt("id_pais"),
                    rs.getString("codigo"),
                    rs.getString("nombre")
                );
            }
            
        } catch (SQLException e) 
        {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error de base de datos al buscar País por ID", e);
        } finally {
             try {
                if (rs != null) rs.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar rs en buscarPorId: ", e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en buscarPorId: ", e);
            }
            // NO cerramos conn porque es conexión global
        }
        return paisEncontrado;
    }

//==========================================================================
// OBTENER TODOS LOS PAISES (Para tabla y ComboBox)
//==========================================================================
    /**
     * Obtiene todos los países de la base de datos.
     * **CORRECCIÓN:** Ordena por ID ascendente.
     * @return Una lista de objetos Pais.
     */
    public List<Pais> obtenerTodos() 
    {
        List<Pais> paises = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Línea corregida: Ordenar por id_pais (ID de menor a mayor)
        String sql = "SELECT id_pais, codigo, nombre FROM pais";

        try 
        {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) 
            {
                Pais p = new Pais(
                    rs.getInt("id_pais"),
                    rs.getString("codigo"),
                    rs.getString("nombre")
                );
                paises.add(p);
            }
        } catch (SQLException e) 
        {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al obtener todos los países: ", e);
        } finally 
        {
            try 
            {
                if (rs != null) rs.close();
            } catch (SQLException e) 
            {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar rs en obtenerTodos: ", e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) 
            {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en obtenerTodos: ", e);
            }
            // NO cerramos conn porque es conexión global
        }
        return paises;
    }

//==========================================================================
// CARGAR COMBO BOX 
//==========================================================================
    /**
     * Cargar Países en un ComboBox.
     * @param combo El JComboBox a llenar con los nombres de los países.
     */
    public void cargarPaises(JComboBox<String> combo) 
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Para el ComboBox, usualmente se prefiere el orden alfabético por nombre
        String sql = "SELECT id_pais, nombre FROM pais "; 

        try 
        {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            combo.removeAllItems();
            combo.addItem("--- Seleccionar País ---");
            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cargar Países en ComboBox: ", e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar rs en cargarPaises: ", e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                 Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, "Error al cerrar ps en cargarPaises: ", e);
            }
        }
    }
}