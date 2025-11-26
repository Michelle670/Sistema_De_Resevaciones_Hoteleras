/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.model.DAO;

import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/

public class PaisDAO 
{
 public void cargarPaises(JComboBox<String> combo) 
 {
    try (Connection conn = ConexionBD.getConnection()) 
    {
        String sql = "SELECT nombre FROM pais";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        combo.removeAllItems(); // limpiar combo por si acaso
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
}
