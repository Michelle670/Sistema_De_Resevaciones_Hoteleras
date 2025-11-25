
package goHotel.model.DAO;
import goHotel.controller.HotelController;
import goHotel.model.ConexionBD;
import goHotel.model.Hotel;
import goHotel.view.GestionHoteles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

//******************************************************************************
/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
//******************************************************************************
public class HotelDAO extends ConexionBD 
{
    //==========================================================================
    // REGISTRAR HOTEL
    //==========================================================================
public boolean registrarHotel(String codigoTexto, String nombre, int idPais, String ciudad, String direccion) {
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        // Validaciones
        if (codigoTexto.trim().isEmpty() || nombre.trim().isEmpty() || ciudad.trim().isEmpty() || direccion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int codigo = Integer.parseInt(codigoTexto.trim());

        // Verificar si ya existe un hotel con ese código
        if (buscarHotelPorCodigo(codigo) != null) {
            JOptionPane.showMessageDialog(null, "Ya existe un hotel con ese código", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // INSERTAR DIRECTAMENTE EN LA BASE DE DATOS
        conn = ConexionBD.getConnection();
        String sql = "INSERT INTO hotel (id_hotel, nombre, id_pais, ciudad, direccion) VALUES (?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        
        ps.setInt(1, codigo);
        ps.setString(2, nombre.trim());
        ps.setInt(3, idPais);
        ps.setString(4, ciudad.trim());
        ps.setString(5, direccion.trim());
        
        int filasAfectadas = ps.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Hotel agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo agregar el hotel.", "Fallido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error de base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
        }
        // NO cerramos conn porque es conexión global
    }
}

    //==========================================================================
    // EDITAR HOTEL 
    //==========================================================================
    public boolean editarHotel(String codigoTexto, String nombre, int idPais, String ciudad, String direccion) 
    {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            if (codigoTexto.trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "Ingrese el código del hotel que va a editar", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int idHotel = Integer.parseInt(codigoTexto.trim());

            if (nombre.trim().isEmpty() || ciudad.trim().isEmpty() || direccion.trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            conn = ConexionBD.getConnection();
            String sql = "UPDATE hotel SET nombre = ?, id_pais = ?, ciudad = ?, direccion = ? WHERE id_hotel = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombre.trim());
            ps.setInt(2, idPais);
            ps.setString(3, ciudad.trim());
            ps.setString(4, direccion.trim());
            ps.setInt(5, idHotel);

            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas > 0) 
            {
                JOptionPane.showMessageDialog(null, "Hotel actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else 
            {
                JOptionPane.showMessageDialog(null, "No existe un hotel con ese ID", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) 
        {
            Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally 
        {
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            }
            // NO cerramos conn porque es conexión global
        }
    }
    //==========================================================================
    // BUSCAR HOTEL
    //==========================================================================
    public Hotel buscarHotel(String codigoTexto) 
    {
        try 
        {
            if (codigoTexto.trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "Ingrese el código del hotel que va a buscar", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            int codigo = Integer.parseInt(codigoTexto.trim());
            Hotel hotel = buscarHotelPorCodigo(codigo);

            if (hotel != null) 
            {
                JOptionPane.showMessageDialog(null, "Hotel encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return hotel;
            } else 
            {
                JOptionPane.showMessageDialog(null, "No se encontró el hotel", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(null, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    //==========================================================================
    // ELIMINAR HOTEL
    //==========================================================================
    public boolean eliminarHotel(int codigo, GestionHoteles vista) {
    try {
        Connection conn = ConexionBD.getConnection();

        String sql = "DELETE FROM hotel WHERE id_hotel = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, codigo);

        int filas = ps.executeUpdate();

        ps.close();

        if (filas > 0) 
        {
            // Mostrar mensaje y actualizar UI desde controller
            JOptionPane.showMessageDialog(vista, "Hotel eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vista.limpiarCampos();
            vista.actualizarTabla();
            return true;
        } else 
        {
            JOptionPane.showMessageDialog(vista, "No existe un hotel con ese código", "Atención", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    } catch (SQLException e) 
    {
        Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
} 
    //==========================================================================
    // CARGAR DATOS EN LA TABLA
    //==========================================================================
    public void cargarDatosEnTabla(DefaultTableModel modelo,String codigo) 
    {
        modelo.setRowCount(0);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try 
        {
            conn = ConexionBD.getConnection();
            String sql = "SELECT A.id_hotel, A.nombre as nombre_hotel, A.id_pais, B.nombre as nombre_pais, A.ciudad, A.direccion FROM hotel A INNER JOIN pais B ON A.id_pais = B.id_pais";
            if (codigo!=null) 
            {
                sql += " WHERE A.id_hotel = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, codigo);
            }
            else
            {   
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                Object[] fila = 
                {
                    rs.getInt("id_hotel"),
                    rs.getString("nombre_hotel"),
                    rs.getInt("id_pais"),
                    rs.getString("nombre_pais"),
                    rs.getString("ciudad"),
                    rs.getString("direccion")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e)
        {
            Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al cargar los hoteles: " + e.getMessage());
        } finally 
        {
            try 
            {
                if (rs != null) rs.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            }
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e)
            {
                Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            }
            // NO cerramos conn porque es conexión global
        }
    }
    //==========================================================================
    // BUSCAR HOTEL POR CODIGO
    //==========================================================================
    private Hotel buscarHotelPorCodigo(int codigo) 
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try 
        {
            conn = ConexionBD.getConnection();
            String sql = "SELECT id_hotel, nombre, id_pais, ciudad, direccion FROM hotel WHERE id_hotel = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) 
            {
                return new Hotel(
                    rs.getInt("id_hotel"),
                    rs.getString("nombre"),
                    rs.getInt("id_pais"),
                    rs.getString("ciudad"),
                    rs.getString("direccion")
                );
            }

        } catch (SQLException e)
        {
            Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally 
        {
            try 
            {
                if (rs != null) rs.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            }
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e) 
            {
                Logger.getLogger(HotelController.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return null;
    }
    
}
