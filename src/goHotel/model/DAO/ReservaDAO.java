//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model.DAO;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import goHotel.model.Reserva;
import goHotel.model.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// RESERVA DAO
//==============================================================================
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
public void cargarHabitaciones(DefaultTableModel modelo) 
{
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
//==============================================================================
// FRAME REGISTRO RESERVA
//==============================================================================

public boolean buscarClientePorCodigo(int codigo, Reserva modelo) 
{
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = ConexionBD.getConnection();

    String sql = "SELECT nombre FROM cliente WHERE id_cliente = ?";

    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, codigo);
        rs = ps.executeQuery();

        if (rs.next()) {
            modelo.setNombreCliente(rs.getString("nombre"));
            return true;
        }

        return false;

    } catch (SQLException e) {
        System.err.println("ERROR: " + e.getMessage());
        return false;
    } finally {
        try { con.close(); } catch (Exception ex) { }
    }
}

public boolean buscarHotelPorCodigo(int codigo, Reserva modelo) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = ConexionBD.getConnection();

    String sql = "SELECT nombre FROM hotel WHERE id_hotel = ?";

    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, codigo);
        rs = ps.executeQuery();

        if (rs.next()) {
            modelo.setNombreHotel(rs.getString("nombre"));
            return true;
        }

        return false;

    } catch (SQLException e) {
        System.err.println("ERROR (Hotel): " + e.getMessage());
        return false;

    } finally {
        try { con.close(); } catch (Exception ex) {}
    }
}

public boolean buscarDescripcionHabitacion(int idHotel, int numeroHabitacion, Reserva modelo) 
{
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = ConexionBD.getConnection();

    String sql = 
        "SELECT CONCAT(B.nombre, ' - Cap.Max: ', CAST(B.capacidad AS CHAR)) AS DescripcionHabitacion " +
        "FROM habitacion A " +
        "INNER JOIN tipo_habitacion B ON A.id_tipo = B.id_tipo " +
        "WHERE A.id_hotel = ? AND A.numero = ?";

    try
    {
        ps = con.prepareStatement(sql);
        ps.setInt(1, idHotel);
        ps.setInt(2, numeroHabitacion);
        rs = ps.executeQuery();

        if (rs.next()) 
        {
            modelo.setDescripcionHabitacion(rs.getString("DescripcionHabitacion"));
            return true;
        }

        return false;

    } catch (SQLException e) 
    {
        System.err.println("ERROR HABITACIÓN: " + e.getMessage());
        return false;

    } finally 
    {
        try { con.close(); } catch (Exception e) {}
    }
}


//==============================================================================
// CARGAR SERVICIOS DE UNA HABITACIÓN EN TABLA
//==============================================================================
public void cargarServiciosHabitacion(DefaultTableModel modelo, int idHotel, int numeroHabitacion) 
{
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection con = ConexionBD.getConnection();
    
    String sql = 
        "SELECT C.nombre " +
        "FROM habitacion A " +
        "INNER JOIN habitacion_servicio B ON A.id_habitacion = B.id_habitacion " +
        "INNER JOIN servicio C ON B.id_servicio = C.id_servicio " +
        "WHERE A.id_hotel = ? AND A.numero = ?";
    
    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, idHotel);
        ps.setInt(2, numeroHabitacion);
        rs = ps.executeQuery();
        
        // Limpiar tabla
        modelo.setRowCount(0);
        
        while (rs.next()) {
            Object[] fila = {rs.getString("nombre")};
            modelo.addRow(fila);
        }
        
    } catch (SQLException e) {
        System.err.println("ERROR al cargar servicios: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ex) {}
        try { if (ps != null) ps.close(); } catch (Exception ex) {}
        try { con.close(); } catch (Exception ex) {}
    }
}
public boolean guardarReserva(int idCliente, int idHotel, int numHabitacion,
                               LocalDateTime fechaEntrada, LocalDateTime fechaSalida,
                               String estadoReserva, String correoUsuario) 
{
    String sql = """
        INSERT INTO reserva(id_cliente, id_hotel, id_habitacion, fecha_entrada, fecha_salida,
                           monto, estado_reserva, creado_por, fecha_creacion,
                           modificado_por, fecha_modificacion, puntos_lealtad)
        SELECT C.id_cliente,
               A.id_hotel,
               A.id_habitacion,
               ? AS fecha_entrada,
               ? AS fecha_salida,
               (DATEDIFF(?, ?) * B.precio_base) - 
               ((DATEDIFF(?, ?) * B.precio_base) * COALESCE(D.descuento/100, 0)) AS monto,
               ? AS estado_reserva,
               ? AS creado_por,
               NOW() AS fecha_creacion,
               ? AS modificado_por,
               NOW() AS fecha_modificacion,
               (DATEDIFF(?, ?) * B.precio_base) * COALESCE(D.factor_puntos, 0) AS puntos_lealtad
        FROM habitacion A
            INNER JOIN tipo_habitacion B ON A.id_tipo = B.id_tipo
            INNER JOIN cliente C ON C.id_cliente = ?
            LEFT JOIN plan_lealtad D ON C.id_plan = D.id_plan
        WHERE A.numero = ?
          AND A.id_hotel = ?
        """;

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        // Usar java.sql.Timestamp explícitamente
        java.sql.Timestamp tsEntrada = java.sql.Timestamp.valueOf(fechaEntrada);
        java.sql.Timestamp tsSalida = java.sql.Timestamp.valueOf(fechaSalida);

        ps.setTimestamp(1, tsEntrada);
        ps.setTimestamp(2, tsSalida);
        ps.setTimestamp(3, tsSalida);
        ps.setTimestamp(4, tsEntrada);
        ps.setTimestamp(5, tsSalida);
        ps.setTimestamp(6, tsEntrada);
        ps.setString(7, estadoReserva);
        ps.setString(8, correoUsuario);
        ps.setString(9, correoUsuario);
        ps.setTimestamp(10, tsSalida);
        ps.setTimestamp(11, tsEntrada);
        ps.setInt(12, idCliente);
        ps.setInt(13, numHabitacion);
        ps.setInt(14, idHotel);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al guardar reserva: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

public List<String> obtenerConflictosReserva(int idHotel, int numHabitacion,
                                              LocalDateTime fechaEntrada, 
                                              LocalDateTime fechaSalida) 
{
    List<String> conflictos = new ArrayList<>();
    
    String sql = """
        SELECT R.id_reserva,
               R.fecha_entrada,
               R.fecha_salida,
               R.estado_reserva,
               CONCAT(C.nombre, ' ', C.apellido) AS cliente
        FROM reserva R
            INNER JOIN habitacion H ON R.id_habitacion = H.id_habitacion
            INNER JOIN cliente C ON R.id_cliente = C.id_cliente
        WHERE H.id_hotel = ?
          AND H.numero = ?
          AND R.estado_reserva NOT IN ('CANCELADA', 'FINALIZADA')
          AND (
                (? >= R.fecha_entrada AND ? < R.fecha_salida)
                OR (? > R.fecha_entrada AND ? <= R.fecha_salida)
                OR (? <= R.fecha_entrada AND ? >= R.fecha_salida)
              )
        ORDER BY R.fecha_entrada
        """;

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        java.sql.Timestamp tsEntrada = java.sql.Timestamp.valueOf(fechaEntrada);
        java.sql.Timestamp tsSalida = java.sql.Timestamp.valueOf(fechaSalida);

        ps.setInt(1, idHotel);
        ps.setInt(2, numHabitacion);
        ps.setTimestamp(3, tsEntrada);
        ps.setTimestamp(4, tsEntrada);
        ps.setTimestamp(5, tsSalida);
        ps.setTimestamp(6, tsSalida);
        ps.setTimestamp(7, tsEntrada);
        ps.setTimestamp(8, tsSalida);

        try (ResultSet rs = ps.executeQuery()) 
        {
            while (rs.next()) {
                String info = String.format("Reserva #%d: %s a %s (%s) - %s",
                    rs.getInt("id_reserva"),
                    rs.getTimestamp("fecha_entrada").toLocalDateTime().toLocalDate(),
                    rs.getTimestamp("fecha_salida").toLocalDateTime().toLocalDate(),
                    rs.getString("estado_reserva"),
                    rs.getString("cliente")
                );
                conflictos.add(info);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener conflictos: " + e.getMessage());
        e.printStackTrace();
    }

    return conflictos;
}

public int obtenerIdHotelPorNombre(String nombreHotel) 
{
    String sql = "SELECT id_hotel FROM hotel WHERE nombre = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        ps.setString(1, nombreHotel);
        
        try (ResultSet rs = ps.executeQuery()) 
        {
            if (rs.next()) {
                return rs.getInt("id_hotel");
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener ID hotel: " + e.getMessage());
    }
    
    return 0;
}

public int obtenerIdClientePorReserva(int idReserva) 
{
    String sql = "SELECT id_cliente FROM reserva WHERE id_reserva = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        ps.setInt(1, idReserva);
        
        try (ResultSet rs = ps.executeQuery()) 
        {
            if (rs.next()) {
                return rs.getInt("id_cliente");
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener ID cliente: " + e.getMessage());
    }
    
    return 0;
}

public boolean actualizarReserva(int idReserva, int idCliente, int idHotel, int numHabitacion,
                                  LocalDateTime fechaEntrada, LocalDateTime fechaSalida,
                                  String estadoReserva, String correoUsuario) 
{
    String sql = """
        UPDATE reserva R
        INNER JOIN habitacion H ON H.id_hotel = ? AND H.numero = ?
        INNER JOIN cliente C ON C.id_cliente = ?
        LEFT JOIN plan_lealtad D ON C.id_plan = D.id_plan
        INNER JOIN tipo_habitacion B ON H.id_tipo = B.id_tipo
        SET R.id_cliente = C.id_cliente,
            R.id_hotel = H.id_hotel,
            R.id_habitacion = H.id_habitacion,
            R.fecha_entrada = ?,
            R.fecha_salida = ?,
            R.monto = (DATEDIFF(?, ?) * B.precio_base) - 
                      ((DATEDIFF(?, ?) * B.precio_base) * COALESCE(D.descuento/100, 0)),
            R.estado_reserva = ?,
            R.modificado_por = ?,
            R.fecha_modificacion = NOW(),
            R.puntos_lealtad = (DATEDIFF(?, ?) * B.precio_base) * COALESCE(D.factor_puntos, 0)
        WHERE R.id_reserva = ?
        """;

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        java.sql.Timestamp tsEntrada = java.sql.Timestamp.valueOf(fechaEntrada);
        java.sql.Timestamp tsSalida = java.sql.Timestamp.valueOf(fechaSalida);

        ps.setInt(1, idHotel);              // H.id_hotel
        ps.setInt(2, numHabitacion);        // H.numero
        ps.setInt(3, idCliente);            // C.id_cliente
        ps.setTimestamp(4, tsEntrada);      // fecha_entrada
        ps.setTimestamp(5, tsSalida);       // fecha_salida
        ps.setTimestamp(6, tsSalida);       // DATEDIFF monto
        ps.setTimestamp(7, tsEntrada);      // DATEDIFF monto
        ps.setTimestamp(8, tsSalida);       // DATEDIFF descuento
        ps.setTimestamp(9, tsEntrada);      // DATEDIFF descuento
        ps.setString(10, estadoReserva);    // estado_reserva
        ps.setString(11, correoUsuario);    // modificado_por
        ps.setTimestamp(12, tsSalida);      // DATEDIFF puntos
        ps.setTimestamp(13, tsEntrada);     // DATEDIFF puntos
        ps.setInt(14, idReserva);           // WHERE id_reserva

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al actualizar reserva: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

// Sobrecarga CON exclusión de reserva (para edición)
public boolean verificarDisponibilidadHabitacion(int idHotel, int numHabitacion,
                                                  LocalDateTime fechaEntrada, 
                                                  LocalDateTime fechaSalida,
                                                  int idReservaExcluir) 
{
    String sql = """
        SELECT COUNT(*) AS conflictos
        FROM reserva R
            INNER JOIN habitacion H ON R.id_habitacion = H.id_habitacion
        WHERE H.id_hotel = ?
          AND H.numero = ?
          AND R.estado_reserva NOT IN ('CANCELADA', 'FINALIZADA')
          AND R.id_reserva != ?
          AND (
                (? >= R.fecha_entrada AND ? < R.fecha_salida)
                OR (? > R.fecha_entrada AND ? <= R.fecha_salida)
                OR (? <= R.fecha_entrada AND ? >= R.fecha_salida)
              )
        """;

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        java.sql.Timestamp tsEntrada = java.sql.Timestamp.valueOf(fechaEntrada);
        java.sql.Timestamp tsSalida = java.sql.Timestamp.valueOf(fechaSalida);

        ps.setInt(1, idHotel);
        ps.setInt(2, numHabitacion);
        ps.setInt(3, idReservaExcluir);
        ps.setTimestamp(4, tsEntrada);
        ps.setTimestamp(5, tsEntrada);
        ps.setTimestamp(6, tsSalida);
        ps.setTimestamp(7, tsSalida);
        ps.setTimestamp(8, tsEntrada);
        ps.setTimestamp(9, tsSalida);

        try (ResultSet rs = ps.executeQuery()) 
        {
            if (rs.next()) {
                return rs.getInt("conflictos") == 0;
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al verificar disponibilidad: " + e.getMessage());
        e.printStackTrace();
    }

    return false;
}
//==============================================================================
public boolean actualizarPuntosLealtad(int idCliente) 
{
    String sql = """
        UPDATE cliente
        SET puntos_lealtad = (
            SELECT COALESCE(SUM(A.puntos_lealtad), 0) 
            FROM reserva A 
            WHERE A.id_cliente = ? 
              AND A.estado_reserva NOT IN ('CANCELADA')
        )
        WHERE id_cliente = ?
        """;

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        ps.setInt(1, idCliente);
        ps.setInt(2, idCliente);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al actualizar puntos de lealtad: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
}







    
    


