package goHotel.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD 
{
    private static final String URL = "jdbc:mysql://localhost:3306/gohotel";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    // Conexión única para toda la app
    private static Connection conn = null;

    // Obtener conexión
    public static Connection getConnection() 
    {
        try 
        {
            if (conn == null || conn.isClosed()) 
            {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión iniciada.");
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conn;
    }
    
    // Cerrar conexión
    public static void cerrarConexion() 
    {
        try 
        {
            if (conn != null && !conn.isClosed()) 
            {
                conn.close();
                System.out.println("Conexión cerrada.");
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }
}