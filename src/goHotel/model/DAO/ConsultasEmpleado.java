package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import goHotel.model.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
public class ConsultasEmpleado {

    // ---------------------------
    // AGREGAR EMPLEADO
    // ---------------------------
    public boolean agregar(String nombre, int idRol, String correo, String password) {
    PreparedStatement ps = null;
    Connection conexion = ConexionBD.getConnection();
    
        String sql = "INSERT INTO empleado (nombre, id_rol, correo, password) VALUES (?, ?, ?, ?)";

        try   {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, idRol);
            ps.setString(3, correo);
            ps.setString(4, password);

            return  ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    // ---------------------------
    // EDITAR EMPLEADO
    // ---------------------------
    public boolean editar(int id, String nombre, int idRol, String correo, String password) {
        PreparedStatement ps = null;
        Connection conexion = ConexionBD.getConnection();
        String sql = "UPDATE empleado SET nombre=?, id_rol=?, correo=?, password=? WHERE id_empleado=?";

        try    {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setInt(2, idRol);
            ps.setString(3, correo);
            ps.setString(4, password);
            ps.setInt(5, id);

             return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    // ---------------------------
    // ELIMINAR EMPLEADO
    // ---------------------------
    public boolean eliminar(int id) {
        PreparedStatement ps = null;
        Connection conexion = ConexionBD.getConnection();

        String sql = "DELETE FROM empleado WHERE id_empleado = ?";

        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

             return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    // ---------------------------
    //  ROL
    // ---------------------------
    private String obtenerRolTexto(int idRol) {
        switch (idRol) {
            case 1:
                return "Admin";
            case 2:
                return "Recepcion";
            case 3:
                return "Limpieza";
            default:
                return "Desconocido";
        }
    }

    // ---------------------------
    // BUSCAR EMPLEADO
    // ---------------------------
    public ArrayList<Object[]> buscar(String nombre) {
        ArrayList<Object[]> lista = new ArrayList<>();
        PreparedStatement ps = null;
        Connection conexion = ConexionBD.getConnection();
        
        String sql = "SELECT * FROM empleado WHERE nombre LIKE ?";

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idRol = rs.getInt("id_rol");
                String rolTexto = obtenerRolTexto(idRol);

                lista.add(new Object[]{
                    rs.getInt("id_empleado"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rolTexto,
                    rs.getString("password")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error buscando empleados: " + e.getMessage());
        }

        return lista;
    }
    // ---------------------------
    // CARGAR TABLA
    // ---------------------------

    public void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);

        String sql = "SELECT * FROM empleado";

        try (
                Connection conexion = ConexionBD.getConnection();
                Statement st = conexion.createStatement(); 
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                int idRol = rs.getInt("id_rol");
                String rolTexto = obtenerRolTexto(idRol);

                modelo.addRow(new Object[]{
                    rs.getInt("id_empleado"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rolTexto,
                    rs.getString("password")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar la tabla empleados: " + e.getMessage());
        }
    }
}
