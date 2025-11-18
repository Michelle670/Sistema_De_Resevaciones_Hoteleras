
package goHotel.controller;
import goHotel.model.Pais;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author soloa
 */
public class PaisController 
{
public void cargarPaises(JComboBox<String> combo) {
    try (Connection conn = ConexionBD.getConnection()) {
        String sql = "SELECT nombre FROM pais";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        combo.removeAllItems(); // limpiar combo por si acaso
        combo.addItem("--- Ninguno ---");
        while (rs.next()) {
            combo.addItem(rs.getString("nombre"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
