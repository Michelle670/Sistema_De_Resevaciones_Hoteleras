/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goHotel.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;

/**
 *
 * @author israelapuy
 */
public class PlanLealtadController {
    public void cargarPlanes(JComboBox<String> combo) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT nivel FROM plan_lealtad";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            combo.removeAllItems();
            combo.addItem("--- Ninguno ---");
            while (rs.next()) {
                combo.addItem(rs.getString("nivel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
