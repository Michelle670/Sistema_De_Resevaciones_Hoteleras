package goHotel.model.DAO;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import goHotel.model.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
//==============================================================================  
// FUNCIONA PARA TODAS LAS CONSULTAS RELACIONADAS CON RESERVA
//==============================================================================  
public class ReservaDAO extends ConexionBD 
{
//==============================================================================
// CARGAR RESERVAS EN TABLA - JFRAME GESTION RESERVAS
//==============================================================================
    public void cargarDatosEnTabla(DefaultTableModel modelo,
                                   String loadMode,
                                   int    idReserva,
                                   String estadoReserva,
                                   String nombrePais,
                                   String nombreHotel,
                                   int    numHabitacion,
                                   String nombreCliente,
                                   String usuarioCorreo,
                                   String usuarioTipo) 
    {
        modelo.setRowCount(0); // Limpiar filas previas
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
        String sql = "SELECT " +
                     "A.id_reserva, " +
                     "A.estado_reserva, " +
                     "A.puntos_lealtad, " +
                     "F.nombre AS nombre_pais, " +
                     "E.nombre AS nombre_hotel, " +
                     "C.numero AS numero_habitacion, " +
                     "B.nombre AS nombre_cliente, " +
                     "B.correo AS correo_cliente, "+
                     "DATE(A.fecha_entrada) AS fecha_entrada, " +
                     "DATE(A.fecha_salida) AS fecha_salida " +
                     "FROM reserva A " +
                     "INNER JOIN cliente B ON A.id_cliente = B.id_cliente " +
                     "INNER JOIN habitacion C ON A.id_habitacion = C.id_habitacion " +
                     "INNER JOIN hotel E ON A.id_hotel = E.id_hotel " +
                     "INNER JOIN pais F ON E.id_pais = F.id_pais " +
                     "WHERE B.correo = case when \"CLIENTE\" = ? then ? else B.correo end";
// =============================================================================
// CONSTRUCCIÓN DINÁMICA DE FILTROS - JFRAME GESTION RESERVAS
// =============================================================================
        List<Object> params = new ArrayList<>();
        params.add(usuarioTipo);
        params.add(usuarioCorreo);
 
        if (loadMode.equalsIgnoreCase("BUSQUEDA")) 
        {

            if ( idReserva > 0) 
            {
                sql += " AND A.id_reserva = ? ";
                params.add(idReserva);
            }

            if (estadoReserva != null && !estadoReserva.isBlank())
            {
                sql += " AND A.estado_reserva LIKE ? ";
                params.add("%" + estadoReserva + "%");
            }

            if (nombrePais != null && !nombrePais.isBlank()) 
            {
                sql += " AND F.nombre LIKE ? ";
                params.add("%" + nombrePais + "%");
            }

            if (nombreHotel != null && !nombreHotel.isBlank())
            {
                sql += " AND E.nombre LIKE ? ";
                params.add("%" + nombreHotel + "%");
            }

            if (numHabitacion > 0) 
            {
                sql += " AND C.numero = ? ";
                params.add(numHabitacion);
            }

            if (nombreCliente != null && !nombreCliente.isBlank())
            {
                sql += " AND B.nombre LIKE ? ";
                params.add("%" + nombreCliente + "%");
            }
        }
        
        try 
        {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(sql);
            // Insertar parámetros en orden correcto
            for (int i = 0; i < params.size(); i++) 
            {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                Object[] fila =
                {
                    rs.getInt("id_reserva"),
                    rs.getString("estado_reserva"),
                    rs.getDouble("puntos_lealtad"),
                    rs.getString("nombre_pais"),
                    rs.getString("nombre_hotel"),
                    rs.getString("numero_habitacion"),
                    rs.getString("nombre_cliente"),
                    rs.getString("fecha_entrada"),
                    rs.getString("fecha_salida")
                };
                modelo.addRow(fila);
            }
            
        } catch (SQLException e)
        {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error al cargar reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally 
        {
            try 
            {
                if (rs != null) rs.close();
            } catch (SQLException e)
            {
                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            }
            try 
            {
                if (ps != null) ps.close();
            } catch (SQLException e)
            {
                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            }
            // NO cerramos conn porque es conexión global
        }
    }
// =============================================================================
// CARGAR HOTELES - JFRAME RESERVA BUSCAR HABITACION
// =============================================================================    
    public void cargarHoteles(JComboBox<String> combo) 
{
    try (Connection conn = ConexionBD.getConnection()) 
    {
        String sql = "SELECT nombre FROM hotel";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        combo.removeAllItems(); // Limpia el combo
        combo.addItem("--- Ninguno ---"); // Opción inicial

        while (rs.next()) 
        {
            combo.addItem(rs.getString("nombre"));
        }

    } catch (Exception e) 
    {
        e.printStackTrace();
    }
}
// =============================================================================
// CARGAR SERVICIOS - JFRAME RESERVA BUSCAR HABITACION
// =============================================================================     
public void cargarServicios(JComboBox<String> combo) 
{
    try (Connection conn = ConexionBD.getConnection()) 
    {
        String sql = "SELECT nombre FROM servicio";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        combo.removeAllItems();
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
//==============================================================================
// ACTUALIZAR ESTADO DE RESERVA - JFRAME GESTION RESERVA
//==============================================================================
public boolean actualizarEstado(int idReserva, String nuevoEstado) {
    Connection conn = null;
    PreparedStatement ps = null;
    
    String sql = "UPDATE reserva SET estado_reserva = ? WHERE id_reserva = ?";
    
    try {
        conn = ConexionBD.getConnection();
        ps = conn.prepareStatement(sql);
        
        ps.setString(1, nuevoEstado);
        ps.setInt(2, idReserva);
        
        int resultado = ps.executeUpdate();
        return resultado > 0;
        
    } catch (SQLException e) {
        Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(null, "Error al actualizar reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

//==============================================================================
// ELIMINAR RESERVA - JFRAME GESTION RESERVA
//==============================================================================
public boolean eliminar(int idReserva) {
    Connection conn = null;
    PreparedStatement ps = null;
    
    String sql = "DELETE FROM reserva WHERE id_reserva = ?";
    
    try {
        conn = ConexionBD.getConnection();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, idReserva);
        
        int resultado = ps.executeUpdate();
        return resultado > 0;
        
    } catch (SQLException e) {
        Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(null, "Error al eliminar reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

}

    
    


