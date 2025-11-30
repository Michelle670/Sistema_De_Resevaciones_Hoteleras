package gohotelapp;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
import goHotel.controller.LoginController;
import goHotel.controller.ReservaController;
import goHotel.model.ConexionBD;
import goHotel.model.DAO.LoginDAO;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.view.GestionReserva;
import goHotel.view.LoginView;
import goHotel.view.ReservaBuscarHabitacion;
import goHotel.view.GestionPlanLealtad;
import goHotel.model.PlanLealtad;
import goHotel.view.GestionPaises;
import goHotel.model.Pais;

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
