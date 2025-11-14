package gohotelapp;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
import goHotel.controller.ConexionBD;
import goHotel.view.Login;
import goHotel.view.Menu;
public class Principal 
{

    public static void main(String[] args) 
    {
        
       ConexionBD.getConnection();
       
       Login lg = new Login();
       lg.setVisible(true);
       lg.setLocationRelativeTo(null);
       
       
        
    }
    
}
