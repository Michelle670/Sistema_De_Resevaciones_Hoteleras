package gohotelapp;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
import goHotel.controller.LoginController;
import goHotel.model.ConexionBD;
import goHotel.model.DAO.LoginDAO;
import goHotel.view.LoginView;
import goHotel.view.ReservaBuscarHabitacion;
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
       
     
        
        
//       ReservaBuscarHabitacion rh = new ReservaBuscarHabitacion();
//       rh.setVisible(true);
//       rh.setLocationRelativeTo(null);
    }
    
}
