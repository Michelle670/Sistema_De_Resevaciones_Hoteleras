package gohotelapp;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
import goHotel.controller.HotelController;
import goHotel.controller.LoginController;
import goHotel.model.ConexionBD;
import goHotel.model.DAO.HotelDAO;
import goHotel.model.DAO.LoginDAO;
import goHotel.model.Hotel;
import goHotel.view.GestionHoteles;
import goHotel.view.LoginView;
import goHotel.view.Menu;
public class Principal 
{

    public static void main(String[] args) 
    {
        
       ConexionBD.getConnection();
       
        // Crear instancias
        LoginDAO dao = new LoginDAO();
        LoginView vista = new LoginView();
        
        // Crear controller y conectar
        LoginController controller = new LoginController(dao, vista);
        vista.setController(controller);
        
        // Iniciar la aplicación
        controller.iniciar();
       
        
    }
    
}
