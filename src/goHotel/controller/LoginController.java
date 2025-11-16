
package goHotel.controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */

public class LoginController 
{
// ---------------------------
// VALIDACIONES
// ---------------------------
    public boolean validarCorreo(String correo) 
    {
        if (correo == null) return false;

        // Debe tener @ y .
        return correo.contains("@") && correo.contains(".");
    }

    public boolean validarPassword(String pass) {
        if (pass == null) return false;

        return pass.length() >= 4;
    }


    // ---------------------------
    // LOGIN EMPLEADO (FIJO)
    // ---------------------------
    public boolean loginEmpleado(String correo, String pass) {
        // SOLO acepta:
        // correo: gohotel@gmail.com
        // pass: 12345
        return correo.equals("gohotel@gmail.com") && pass.equals("12345");
    }


    // ---------------------------
    // LOGIN CLIENTE 
    // ---------------------------
    public boolean loginCliente(String correo, String pass) {
        String sql = "SELECT * FROM cliente WHERE correo = ? AND password = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            return rs.next(); // Si existe, el login es correcto

        } catch (Exception e) {
            System.out.println("Error al validar cliente: " + e.getMessage());
            return false;
        }
    }

 
}
