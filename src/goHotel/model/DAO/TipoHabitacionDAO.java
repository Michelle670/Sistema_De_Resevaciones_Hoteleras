package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.TipoHabitacion;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
public class TipoHabitacionDAO {

    // ---------------------------
    // AGREGAR TIPO DE HABITACIÓN 
    // ---------------------------
    public boolean agregar(TipoHabitacion th) {
        PreparedStatement stmt = null;
        Connection conexion = ConexionBD.getConnection();
        String sql = "INSERT INTO tipo_habitacion (id_tipo, nombre, descripcion, capacidad, precio_base) VALUES (?, ?, ?, ?, ?)";

        try {

            stmt = conexion.prepareStatement(sql);
             stmt.setInt(1, th.getIdTipo());           
            stmt.setString(2, th.getNombreTipo());
            stmt.setString(3, th.getDescripcion());
            stmt.setInt(4, th.getCapacidad());
            stmt.setDouble(5, th.getPrecioBase());


            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            return false;
        }
    }
    // ---------------------------
    // EDITAR TIPO DE HABITACIÓN 
    // ---------------------------

    public boolean editar(TipoHabitacion t) {
        PreparedStatement stmt = null;
        Connection conexion = ConexionBD.getConnection();

        String sql = "UPDATE tipo_habitacion SET nombre=?, descripcion=?, capacidad=?, precio_base=? WHERE id_tipo=?";

        try {
            stmt = conexion.prepareStatement(sql);
            
            stmt.setString(1, t.getNombreTipo());
            stmt.setString(2, t.getDescripcion());
            stmt.setInt(3, t.getCapacidad());
            stmt.setDouble(4, t.getPrecioBase());
            stmt.setInt(5, t.getIdTipo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al editar: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------
    // ELIMINAR TIPO DE HABITACIÓN 
    // ---------------------------
    public boolean eliminar(int id) {
        Connection conexion = ConexionBD.getConnection();
        PreparedStatement stmt = null;
        String sql = "DELETE FROM tipo_habitacion WHERE id_tipo=?";

        try {

            stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
    // ---------------------------------------
    // BUSCAR POR NOMBRE EN TIPO DE HABITACIÓN 
    // ---------------------------------------

    public ArrayList<Object[]> buscarPorId(int id) {
        ArrayList<Object[]> lista = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conexion = ConexionBD.getConnection();
        String sql = "SELECT * FROM tipo_habitacion WHERE id_tipo=?";

        try {

            stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_tipo"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("capacidad"),
                    rs.getDouble("precio_base")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }

        return lista;
    }
    // ---------------------------
    // CARGAR TABLA
    // ---------------------------

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String sql = "SELECT * FROM tipo_habitacion";

        try (Connection conexion = ConexionBD.getConnection();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) 
        {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_tipo"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("capacidad"),
                    rs.getDouble("precio_base")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar tabla: " + e.getMessage());
        }
    }
}
