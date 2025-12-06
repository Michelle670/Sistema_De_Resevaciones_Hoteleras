
package goHotel.controller;
import goHotel.model.DAO.LoginDAO;
import goHotel.view.LoginClienteNuevo;
import goHotel.view.LoginView;
import goHotel.view.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
//==============================================================================
//LOGIN CONTROLLER
// Tiene toda la funcionalidad de los btn
//==============================================================================
public class LoginController implements ActionListener 
{
    
    private final LoginDAO dao;
    private final LoginView vista;
    
    public LoginController(LoginDAO dao, LoginView vista) 
    {
        this.dao = dao;
        this.vista = vista;
        
        // Registrar los listeners
        this.vista.btnOk.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
    }

    //==========================================================================
    // INICIAR
    // Inicia el frame
    //==========================================================================
    public void iniciar()
    {
        vista.setTitle("Login - GoHotel");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
    //==========================================================================
    // METODO PARA VALIDAR CORREO
    // Valida que todos los correos tengan @ y punto para que sigan ese formato
    //==========================================================================
    public boolean validarCorreo(String correo)
    {
        return correo.contains("@") && correo.contains(".");
    }
    //==========================================================================
    // METODO PARA VALIDAR CONTRASEÑA
    // Valida que todas contraseñas tengan al menos 4 digitos para seguir formato
    //==========================================================================
    public boolean validarPassword(String pass) 
    {
        return pass.length() >= 4;
    }
    
    //==========================================================================
    // METODO PARA LOGIN EMPLEADO
    // Valida que si es empleado con el correo y password
    //==========================================================================
    public boolean loginEmpleado(String correo, String pass)
    {
        return dao.loginEmpleado(correo, pass);
    }
    
    //==========================================================================
    // METODO PARA LOGIN CLIENTE
    // Valida que si es cliente con el correo y password
    //==========================================================================
    public boolean loginCliente(String correo, String pass)
    {
        return dao.loginCliente(correo, pass);
    }
    //==========================================================================
    // ACTION PERFORMED  
    // Contiene toda la funcionalidad de los btn
    //==========================================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {
        
    //==========================================================================
    // BOTON OK (Login)
    // Si es  cliente nuevo va a la panatalla de registar nuevo cliente,
    // si es empleado lo valida y reviso su rol y si es cliente ya registrado 
    // lo lleva al menu
    //==========================================================================
    if (e.getSource() == vista.btnOk)
{
    String correo = vista.txtCorreo.getText().trim();
    String pass = new String(vista.jPassword.getPassword());
    boolean esEmpleado = vista.jrEmpleado.isSelected();
    boolean esClienteNuevo = vista.jrClienteNuevo.isSelected();
    String tipoUsuario = esEmpleado ? "Empleado" : "Cliente";
    
    // SI ES CLIENTE NUEVO - ABRIR VENTANA DE REGISTRO
    if (esClienteNuevo)
    {
        LoginClienteNuevo vistaRegistro = new LoginClienteNuevo();
        LoginControllerClienteNuevo controllerRegistro = new LoginControllerClienteNuevo(vistaRegistro, dao);
        controllerRegistro.iniciar();
        vista.dispose(); // Cerrar ventana de login actual
        return;
    }
    
    // VALIDAR CORREO
    if (!validarCorreo(correo)) 
    {
        JOptionPane.showMessageDialog(vista, "Correo inválido, debe contener '@' y un '.'.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // VALIDAR CONTRASEÑA
    if (!validarPassword(pass)) 
    {
        JOptionPane.showMessageDialog(vista, "La contraseña debe tener mínimo 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    boolean loginCorrecto = false;
    
    // SI ES EMPLEADO (login fijo)
    if (esEmpleado) 
    {
        loginCorrecto = loginEmpleado(correo, pass);
    }
    // SI ES CLIENTE (se valida en BD)
    else 
    {
        loginCorrecto = loginCliente(correo, pass);
    }
    
    if (loginCorrecto)
    { 
        // Crear Menu con los parámetros
        Menu vistaMenu = new Menu(correo, tipoUsuario);
        MenuController menuController = new MenuController(vistaMenu, correo, tipoUsuario);
        vistaMenu.setController(menuController);
        
        // Iniciar el menú
        menuController.iniciar();
        
        vista.dispose(); // cerrar ventana de login
    } 
    else
    {
        JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
        //======================================================================
        // BOTÓN CANCELAR
        // sale de la pantalla
        //======================================================================
        if (e.getSource() == vista.btnCancelar)
        {
            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro que desea salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        }
    }
}
