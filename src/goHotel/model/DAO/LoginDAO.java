//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model.DAO;
import java.sql.SQLException;
import goHotel.model.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// LOGIN  DAO
//============================================================================== 
public class LoginDAO extends ConexionBD
{
    // ---------------------------
    // LOGIN EMPLEADO 
    // ---------------------------
    public boolean loginEmpleado(String correo, String pass) 
    {
    String sql = "SELECT password FROM empleado WHERE correo = ?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {

        ps.setString(1, correo);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) 
        {
            // contraseña almacenada en la BD
            String password = rs.getString("password");

            // Comparamos la contraseña ingresada con la de la BD
            return pass.equals(password);
        } else
        {
            // si no encuentra el correo
            return false;
        }

    } catch (Exception e) 
    {
        e.printStackTrace();
        return false;
    }
}

    // ---------------------------
    // LOGIN CLIENTE 
    // ---------------------------
    public boolean loginCliente(String correo, String pass) 
    {
        String sql = "SELECT * FROM cliente WHERE correo = ? AND password = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) 
        {

            ps.setString(1, correo);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            return rs.next(); // Si existe, el login es correcto

        } catch (Exception e)
        {
            System.out.println("Error al validar cliente: " + e.getMessage());
            return false;
        }
    }
    
    //----------------------------
    //CLIENTE NUEVO
    //----------------------------
    public boolean registrarClienteCompleto(String nombre, String correo, String pass, String nombrePais)
{
    Connection con = null;
    PreparedStatement psVerificar = null;
    PreparedStatement psObtenerIdPais = null;
    PreparedStatement psInsertar = null;
    ResultSet rs = null;
    ResultSet rsPais = null;
    
    try
    {
        con = ConexionBD.getConnection();
        
        // 1. Verificar si el correo ya existe
        String queryVerificar = "SELECT COUNT(*) FROM cliente WHERE correo = ?";
        psVerificar = con.prepareStatement(queryVerificar);
        psVerificar.setString(1, correo);
        rs = psVerificar.executeQuery();
        
        if (rs.next() && rs.getInt(1) > 0)
        {
            System.out.println("ERROR: El correo ya existe en la base de datos");
            return false;
        }
        
        // 2. Obtener el id_pais
        if (nombrePais.equals("--- Ninguno ---"))
        {
            System.out.println("ERROR: No se seleccionó un país válido");
            return false;
        }
        
        String queryObtenerIdPais = "SELECT id_pais FROM pais WHERE nombre = ?";
        psObtenerIdPais = con.prepareStatement(queryObtenerIdPais);
        psObtenerIdPais.setString(1, nombrePais);
        rsPais = psObtenerIdPais.executeQuery();
        
        if (!rsPais.next())
        {
            System.err.println("ERROR: País no encontrado en BD: " + nombrePais);
            return false;
        }
        
        int idPais = rsPais.getInt("id_pais");
        System.out.println("ID del país obtenido: " + idPais);
        
        // 3. INSERT con SELECT para generar el id_cliente automáticamente
        String queryInsertar = "INSERT INTO cliente (id_cliente, Nombre, correo, password, id_pais) " +
                               "SELECT COALESCE(MAX(id_cliente), 0) + 1, ?, ?, ?, ? FROM cliente";
        
        psInsertar = con.prepareStatement(queryInsertar);
        psInsertar.setString(1, nombre);
        psInsertar.setString(2, correo);
        psInsertar.setString(3, pass);
        psInsertar.setInt(4, idPais);
        
        System.out.println("Ejecutando INSERT con SELECT: Nombre=" + nombre + ", Correo=" + correo + ", IdPais=" + idPais);
        
        int filasAfectadas = psInsertar.executeUpdate();
        
        if (filasAfectadas > 0)
        {
            System.out.println("Cliente registrado exitosamente");
            return true;
        }
        else
        {
            System.out.println("ERROR: No se insertó ninguna fila");
            return false;
        }
    }
    catch (SQLException ex)
    {
        System.err.println("ERROR SQL al registrar cliente:");
        System.err.println("Mensaje: " + ex.getMessage());
        System.err.println("Código de error: " + ex.getErrorCode());
        System.err.println("SQLState: " + ex.getSQLState());
        ex.printStackTrace();
        return false;
    }
    finally
    {
        try
        {
            if (rs != null) rs.close();
            if (rsPais != null) rsPais.close();
            if (psVerificar != null) psVerificar.close();
            if (psObtenerIdPais != null) psObtenerIdPais.close();
            if (psInsertar != null) psInsertar.close();
            if (con != null) con.close();
        }
        catch (SQLException ex)
        {
            System.err.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }
}
    // ---------------------------
    //  METODO GETUSERINFO
    // --------------------------- 
public static String[] getUserInfo(String correo, String tipo) 
{

    String sql;

    if (tipo.equalsIgnoreCase("empleado")) 
    {
        sql = "SELECT A.nombre, B.nombre AS nombre_rol " +
              "FROM empleado A " +
              "INNER JOIN rol_empleado B ON A.id_rol = B.id_rol " +
              "WHERE A.correo = ?";
    } else 
    {
        sql = "SELECT nombre, null AS nombre_rol FROM cliente WHERE correo = ?";
    }

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {

        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) 
        {
            String nombre = rs.getString("nombre");
            String rol = rs.getString("nombre_rol");
            
            String textoNombre = "¡Bienvenido " + nombre +"!";
            String textoRol = (rol != null ? "Rol: " + rol : "Rol: Cliente");
            
            return new String[]{ textoNombre, textoRol,correo,tipo };   // ← TUPLA
        } else 
        {
            return null;
        }

    } catch (Exception e)
    {
        e.printStackTrace();
        return null;
    }
}

//==============================================================================
public int getIdClienteByCorreo(String correo) 
{
    int idCliente = -1; // -1 indica que no se encontró
    String sql = "SELECT id_Cliente FROM Cliente WHERE correo = ?";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) 
    {
        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) 
        {
            idCliente = rs.getInt("id_Cliente");
        }
    } 
    catch (SQLException e) 
    {
        e.printStackTrace();
    }
    
    return idCliente;
}

//==============================================================================
}