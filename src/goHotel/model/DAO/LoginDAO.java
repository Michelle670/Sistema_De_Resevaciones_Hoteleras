/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
public class LoginDAO 
{
    // ---------------------------
    // LOGIN EMPLEADO 
    // ---------------------------
    public boolean loginEmpleado(String correo, String pass) {
    String sql = "SELECT password FROM empleado WHERE correo = ?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, correo);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // contraseña almacenada en la BD
            String password = rs.getString("password");

            // Comparamos la contraseña ingresada con la de la BD
            return pass.equals(password);
        } else {
            // si no encuentra el correo
            return false;
        }

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
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
    
    // ---------------------------
    //   Metodo GetUserInfo
    // --------------------------- 
public static String[] getUserInfo(String correo, String tipo) {

    String sql;

    if (tipo.equalsIgnoreCase("empleado")) {
        sql = "SELECT A.nombre, B.nombre AS nombre_rol " +
              "FROM empleado A " +
              "INNER JOIN rol_empleado B ON A.id_rol = B.id_rol " +
              "WHERE A.correo = ?";
    } else {
        sql = "SELECT nombre, null AS nombre_rol FROM cliente WHERE correo = ?";
    }

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String nombre = rs.getString("nombre");
            String rol = rs.getString("nombre_rol");
            
            String textoNombre = "¡Bienvenido " + nombre +"!";
            String textoRol = (rol != null ? "Rol: " + rol : "Rol: Cliente");
            
            return new String[]{ textoNombre, textoRol };   // ← TUPLA
        } else {
            return null;
        }

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
