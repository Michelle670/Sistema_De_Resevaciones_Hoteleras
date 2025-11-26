/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author israelapuy
 */
public class HabitacionServicioDAO {
    public boolean asignarServicio(int idHabitacion, int idServicio) {
        String sql = "INSERT INTO habitacion_servicio (id_habitacion, id_servicio) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHabitacion);
            ps.setInt(2, idServicio);

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            System.err.println("Error asignando servicio: " + e.getMessage());
            return false;
        }
    }
    
    public boolean quitarServicio(int idHabitacion, int idServicio){
        String sql = "DELETE FROM habitacion_servicio WHERE id_habitacion=? AND id_servicio=?";
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            ps.setInt(2, idServicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error asignando servicio: " + e.getMessage());
            return false;
        }
    }
    public List<Integer> obtenerServiciosDeHabitacion(int idHabitacion) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id_servicio FROM habitacion_servicio WHERE id_habitacion=?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHabitacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id_servicio"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listando servicios: " + e.getMessage());
        }
        return ids;
    }
}
