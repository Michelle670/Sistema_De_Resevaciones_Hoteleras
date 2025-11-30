package goHotel.model.DAO;
import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LimpiezaDAO {

    // ============================================================
    // CARGAR REPORTE DE LIMPIEZA
    // ============================================================
    public void cargarReporteLimpieza(DefaultTableModel modelo, 
                                      String nombreHotel,
                                      Date fechaInicio,
                                      Date fechaFin) 
    {
        modelo.setRowCount(0); // limpiar tabla

        Connection con = ConexionBD.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql =
            "SELECT A.id_reserva, "
          + "       A.estado_reserva, "
          + "       A.fecha_entrada, "
          + "       A.fecha_salida, "
          + "       B.nombre AS nombre_hotel, "
          + "       C.numero AS numero_habitacion, "
          + "       D.nombre AS nombre_habitacion, "
          + "       D.capacidad AS capacidad_hotel "
          + "  FROM reserva A "
          + "       INNER JOIN hotel B ON A.id_hotel = B.id_hotel "
          + "       INNER JOIN habitacion C ON A.id_habitacion = C.id_habitacion "
          + "       INNER JOIN tipo_habitacion D ON C.id_tipo = D.id_tipo "
          + " WHERE 1 = 1 ";

        // FILTRO: HOTEL
        if (nombreHotel != null && !nombreHotel.equalsIgnoreCase("--- Ninguno ---")) {
            sql += " AND B.nombre = ? ";
        }

        // FILTRO: RANGO DE FECHAS
        if (fechaInicio != null && fechaFin != null) {
            sql += " AND A.fecha_entrada >= ? "
                 + " AND A.fecha_salida <= ? ";
        }

        sql += " ORDER BY B.nombre, A.fecha_entrada";

        try {
            ps = con.prepareStatement(sql);

            int index = 1;

            if (nombreHotel != null && !nombreHotel.equalsIgnoreCase("--- Ninguno ---")) {
                ps.setString(index++, nombreHotel);
            }

            if (fechaInicio != null && fechaFin != null) 
            {
                fechaInicio.setHours(14);
                fechaInicio.setMinutes(00);
                fechaInicio.setSeconds(00);
                fechaFin.setHours(11);
                fechaFin.setMinutes(00);
                fechaFin.setSeconds(00);
                ps.setTimestamp(index++, new java.sql.Timestamp(fechaInicio.getTime()));
                ps.setTimestamp(index++, new java.sql.Timestamp(fechaFin.getTime()));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_reserva"),
                    rs.getString("estado_reserva"),
                    rs.getTimestamp("fecha_entrada"),
                    rs.getTimestamp("fecha_salida"),
                    rs.getString("nombre_hotel"),
                    rs.getString("numero_habitacion"),
                    rs.getString("nombre_habitacion"),
                    rs.getInt("capacidad_hotel"),
                });
            }

        } catch (SQLException e) {
            System.out.println("ERROR en cargarReporteLimpieza(): " + e);
        }
    }

    // ============================================================
    // CARGAR COMBOBOX DE HOTELES
    // ============================================================
    public void cargarHoteles(javax.swing.JComboBox<String> combo) {
        combo.removeAllItems();
        combo.addItem("--- Ninguno ---");

        Connection con = ConexionBD.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT nombre FROM hotel ORDER BY nombre");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error cargando hoteles: " + e);
        }
    }
}
