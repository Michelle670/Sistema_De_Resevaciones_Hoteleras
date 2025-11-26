package goHotel.model.DAO;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
public class ReservaDAO extends ConexionBD 
{
    
    //==========================================================================
    // CARGAR RESERVAS EN TABLA
    //==========================================================================
    public void cargarDatosEnTabla(DefaultTableModel modelo,
                                   String IDReserva,
                                   String EstadoReserva,
                                   String NombrePais,
                                   String NombreHotel,
                                   String NumHabitacion,
                                   String NombreCliente) 
    {
        modelo.setRowCount(0); // Limpiar filas previas
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                     "A.id_reserva, " +
                     "A.estado_reserva, " +
                     "F.nombre AS nombre_pais, " +
                     "E.nombre AS nombre_hotel, " +
                     "C.numero AS numero_habitacion, " +
                     "B.nombre AS nombre_cliente, " +
                     "A.fecha_entrada, " +
                     "A.fecha_salida " +
                     "FROM reserva A " +
                     "INNER JOIN cliente B ON A.id_cliente = B.id_cliente " +
                     "INNER JOIN habitacion C ON A.id_habitacion = C.id_habitacion " +
                     "INNER JOIN hotel E ON A.id_hotel = E.id_hotel " +
                     "INNER JOIN pais F ON E.id_pais = F.id_pais ";
        //if (!IDReserva.isEmpty() or !EstadoReserva.isEmpty())
//        {
//            sql + "WHERE = "
//            if (!IDReserva.isEmpty()) 
//            {
//                sql + "A.id_reserva like %?%"
//            }
//            
//        }
        
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_reserva"),
                    rs.getString("estado_reserva"),
                    rs.getString("nombre_pais"),
                    rs.getString("nombre_hotel"),
                    rs.getString("numero_habitacion"),
                    rs.getString("nombre_cliente"),
                    rs.getDate("fecha_entrada"),
                    rs.getDate("fecha_salida")
                };
                modelo.addRow(fila);
            }
            
        } catch (SQLException e) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error al cargar reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
            }
            // NO cerramos conn porque es conexión global
        }
    }
    
//    //==========================================================================
//    // CARGAR RESERVAS CON FILTRO (Opcional - para búsqueda)
//    //==========================================================================
//    public void cargarDatosEnTabla(DefaultTableModel modelo, String criterio) {
//        modelo.setRowCount(0);
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        
//        try {
//            conn = ConexionBD.getConnection();
//            
//            String sql = "SELECT " +
//                         "A.id_reserva, " +
//                         "F.nombre AS nombre_pais, " +
//                         "E.nombre AS nombre_hotel, " +
//                         "C.numero AS numero_habitacion, " +
//                         "B.nombre AS nombre_cliente, " +
//                         "A.estado_reserva, " +
//                         "A.fecha_entrada, " +
//                         "A.fecha_salida " +
//                         "FROM reserva A " +
//                         "INNER JOIN cliente B ON A.id_cliente = B.id_cliente " +
//                         "INNER JOIN habitacion C ON A.id_habitacion = C.id_habitacion " +
//                         "INNER JOIN hotel E ON A.id_hotel = E.id_hotel " +
//                         "INNER JOIN pais F ON E.id_pais = F.id_pais";
//            
//            // Si hay criterio, filtrar
//            if (criterio != null && !criterio.trim().isEmpty()) {
//                sql += " WHERE A.id_reserva = ? OR B.nombre LIKE ? OR E.nombre LIKE ?";
//                ps = conn.prepareStatement(sql);
//                
//                try {
//                    int id = Integer.parseInt(criterio);
//                    ps.setInt(1, id);
//                } catch (NumberFormatException e) {
//                    ps.setInt(1, -1); // Si no es número, búsqueda por ID fallará
//                }
//                ps.setString(2, "%" + criterio + "%");
//                ps.setString(3, "%" + criterio + "%");
//            } else {
//                sql += " ORDER BY A.id_reserva DESC";
//                ps = conn.prepareStatement(sql);
//            }
//            
//            rs = ps.executeQuery();
//            
//            while (rs.next()) {
//                Object[] fila = {
//                    rs.getInt("id_reserva"),
//                    rs.getString("nombre_pais"),
//                    rs.getString("nombre_hotel"),
//                    rs.getString("numero_habitacion"),
//                    rs.getString("nombre_cliente"),
//                    rs.getString("estado_reserva"),
//                    rs.getDate("fecha_entrada"),
//                    rs.getDate("fecha_salida")
//                };
//                modelo.addRow(fila);
//            }
//            
//        } catch (SQLException e) {
//            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
//            JOptionPane.showMessageDialog(null, "Error al buscar reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//            } catch (SQLException e) {
//                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
//            }
//            try {
//                if (ps != null) ps.close();
//            } catch (SQLException e) {
//                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, e);
//            }
//        }
//    }
}
