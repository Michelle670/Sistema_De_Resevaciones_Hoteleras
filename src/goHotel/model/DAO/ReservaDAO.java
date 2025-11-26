package goHotel.model.DAO;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
                                   String loadMode,
                                   int    idReserva,
                                   String estadoReserva,
                                   String nombrePais,
                                   String nombreHotel,
                                   int    numHabitacion,
                                   String nombreCliente
    ) 
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
                     "DATE(A.fecha_entrada) AS fecha_entrada, " +
                     "DATE(A.fecha_salida) AS fecha_salida " +
                     "FROM reserva A " +
                     "INNER JOIN cliente B ON A.id_cliente = B.id_cliente " +
                     "INNER JOIN habitacion C ON A.id_habitacion = C.id_habitacion " +
                     "INNER JOIN hotel E ON A.id_hotel = E.id_hotel " +
                     "INNER JOIN pais F ON E.id_pais = F.id_pais " +
                     "WHERE 1=1";
            
        // ===============================================================
        // CONSTRUCCIÓN DINÁMICA DE FILTROS
        // ===============================================================
        List<Object> params = new ArrayList<>();

        if (loadMode.equalsIgnoreCase("BUSQUEDA")) {

            if ( idReserva > 0) {
                sql += " AND A.id_reserva = ? ";
                params.add(idReserva);
            }

            if (estadoReserva != null && !estadoReserva.isBlank()) {
                sql += " AND A.estado_reserva LIKE ? ";
                params.add("%" + estadoReserva + "%");
            }

            if (nombrePais != null && !nombrePais.isBlank()) {
                sql += " AND F.nombre LIKE ? ";
                params.add("%" + nombrePais + "%");
            }

            if (nombreHotel != null && !nombreHotel.isBlank()) {
                sql += " AND E.nombre LIKE ? ";
                params.add("%" + nombreHotel + "%");
            }

            if (numHabitacion > 0) {
                sql += " AND C.numero = ? ";
                params.add(numHabitacion);
            }

            if (nombreCliente != null && !nombreCliente.isBlank()) {
                sql += " AND B.nombre LIKE ? ";
                params.add("%" + nombreCliente + "%");
            }
        }
        
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(sql);
            // Insertar parámetros en orden correcto
            for (int i = 0; i < params.size(); i++) 
            {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_reserva"),
                    rs.getString("estado_reserva"),
                    rs.getString("nombre_pais"),
                    rs.getString("nombre_hotel"),
                    rs.getString("numero_habitacion"),
                    rs.getString("nombre_cliente"),
                    rs.getString("fecha_entrada"),
                    rs.getString("fecha_salida")
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
}
