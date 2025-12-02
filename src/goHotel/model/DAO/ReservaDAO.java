package goHotel.model.DAO;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import goHotel.model.Reserva;
import goHotel.model.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
                     "A.fecha_entrada, " +
                     "A.fecha_salida " +
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
        String sql = "SELECT nombre FROM servicio ORDER BY nombre";
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
//==============================================================================
//SERVICIOS RESERVA - JFRAME RESERVA BUSCAR HABITACION
//==============================================================================
public Servicio obtenerServicioPorNombre(String nombre) {

    Servicio s = null;

    try (Connection conn = ConexionBD.getConnection()) {
        String sql = "SELECT * FROM servicio WHERE nombre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            s = new Servicio(
                rs.getInt("id_servicio"),
                rs.getString("nombre"),
                rs.getString("descripcion")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return s;
}
//==============================================================================
public void cargarHabitaciones(DefaultTableModel modelo) {
    Connection con = ConexionBD.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    String sql = 
        "SELECT B.nombre AS nombre_hotel, " +
        "       A.numero AS numero_habitacion, " +
        "       C.nombre AS tipo_habitacion, " +
        "       C.capacidad, " +
        "       C.precio_base " +
        "FROM habitacion A " +
        "INNER JOIN hotel B ON A.id_hotel = B.id_hotel " +
        "INNER JOIN tipo_habitacion C ON A.id_tipo = C.id_tipo " +
        "ORDER BY B.nombre, A.numero";

    try {
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        // Limpiar tabla antes de cargar
        modelo.setRowCount(0);

        while (rs.next()) {
            Object fila[] = new Object[5];

            fila[0] = rs.getString("nombre_hotel");
            fila[1] = rs.getInt("numero_habitacion");
            fila[2] = rs.getString("tipo_habitacion");
            fila[3] = rs.getInt("capacidad");
            fila[4] = rs.getBigDecimal("precio_base"); // o double

            modelo.addRow(fila);
        }

    } catch (Exception e) {
        System.out.println("Error cargando habitaciones: " + e.getMessage());
    } finally {
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (rs != null) rs.close(); } catch (Exception e) {}
    }
}

public DefaultTableModel buscarHabitaciones(String hotelNombre, 
                                            Date fechaEntrada,
                                            Date fechaSalida,
                                            int numPersonas,
                                            List<String> serviciosSeleccionados) 
{
    String[] columnas = {"Hotel", "Habitación", "Tipo", "Capacidad", "Precio Base"};
    DefaultTableModel modelo = new DefaultTableModel(null, columnas);

    Connection conn = ConexionBD.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    String sql = "SELECT B.nombre AS nombre_hotel, " +
                 "       A.numero AS numero_habitacion, " +
                 "       C.nombre AS tipo_habitacion, " +
                 "       C.capacidad, " + 
                 "       C.precio_base " +
                 "  FROM habitacion A " +
                 "       INNER JOIN hotel B ON A.id_hotel = B.id_hotel " +
                 "       INNER JOIN tipo_habitacion C ON A.id_tipo = C.id_tipo " +
                 "  WHERE 1=1 ";
    // =============================================================================
    List<Object> params = new ArrayList<>();
        
    // FILTRO POR NOMBRE DE HOTEL
    if (hotelNombre != null && !hotelNombre.equalsIgnoreCase("--- Ninguno ---")) 
    {
        sql += " AND B.nombre = ? ";
        params.add(hotelNombre);
    }
    // FILTRO POR CAPACIDAD (NUMERO PERSONAS)
    if (numPersonas != 0) 
    {
        sql += " AND C.capacidad >= ? ";
        params.add(numPersonas);
    }
    

    // FILTRO POR FECHA DE ENTRADA 
    if (fechaEntrada != null) 
    {
        fechaEntrada.setHours(11);
        fechaEntrada.setMinutes(00);
        fechaEntrada.setSeconds(00);
        sql += "AND A.id_habitacion not in (SELECT id_habitacion " +
	       "                                FROM reserva  " +
               "                               WHERE ? between fecha_entrada and fecha_salida) ";
        params.add(new java.sql.Timestamp(fechaEntrada.getTime()));
    }
    
     // FILTRO POR FECHA DE SALIDA
    if (fechaSalida != null) 
    {
        fechaSalida.setHours(14);
        fechaSalida.setMinutes(00);
        fechaSalida.setSeconds(00);
        sql += "AND A.id_habitacion not in (SELECT id_habitacion " +
	       "                                FROM reserva  " +
               "                               WHERE ? between fecha_entrada and fecha_salida) ";
        params.add(new java.sql.Timestamp(fechaSalida.getTime()));
    }
    
    
    // FILTRO POR SERVICIOS
    if (serviciosSeleccionados != null && !serviciosSeleccionados.isEmpty()) 
    {
        sql += "AND A.id_habitacion in(";  
        for (int i = 0; i < serviciosSeleccionados.size(); i++) 
        {
            sql += "SELECT id_habitacion FROM habitacion_servicio WHERE id_servicio = ? ";
            if (i>=0 && i < serviciosSeleccionados.size()-1) 
            {
               sql += "INTERSECT "; 
            }
            params.add(serviciosSeleccionados.get(i));
        }
        sql += ") ";
    }

    sql += " ORDER BY B.nombre, A.numero";

    try 
    {
        ps = conn.prepareStatement(sql);

        // Insertar parámetros en orden correcto
        for (int i = 0; i < params.size(); i++) 
        {
            ps.setObject(i + 1, params.get(i));
        }
        rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("nombre_hotel"),
                rs.getString("numero_habitacion"),
                rs.getString("tipo_habitacion"),
                rs.getInt("capacidad"),
                rs.getDouble("precio_base")
            });
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return modelo;
}





}

    
    


