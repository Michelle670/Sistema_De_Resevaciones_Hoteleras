//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.LoginDAO;
import goHotel.model.DAO.PaisDAO;
import goHotel.view.LoginClienteNuevo;
import goHotel.view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// LOGIN CLIENTE NUEVO CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Login Cliente Nuevo.
 */ 
public class LoginControllerClienteNuevo implements ActionListener
{
    private LoginClienteNuevo vista;
    private LoginDAO loginDAO;
    private PaisDAO paisDAO;
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public LoginControllerClienteNuevo(LoginClienteNuevo vista, LoginDAO loginDAO)
    {
        this.vista = vista;
        this.loginDAO = loginDAO;
        this.paisDAO = new PaisDAO();
        // Agregar listeners a los botones
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
        // Cargar países en el ComboBox usando PaisDAO
        paisDAO.cargarPaises(vista.jcbPaises);
    }
    //==========================================================================
    //INICIAR
    //==========================================================================
    public void iniciar()
    {
        vista.setTitle("Registro de Nuevo Cliente");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == vista.btnRegistrar)
        {
            registrarCliente();
        }
        else if (e.getSource() == vista.btnCancelar)
        {
            volverAlLogin();
        }
    }
    //==========================================================================
    // BTN REGISTRAR
    //==========================================================================
    private void registrarCliente()
    {
        // Obtener datos del formulario
        String nombre = vista.txtNombre.getText().trim();
        String correo = vista.txtCorreo.getText().trim();
        String pass = new String(vista.jPassword.getPassword());
        String pais = (String) vista.jcbPaises.getSelectedItem();
        //======================================================================
        // VALIDACIONES
        //======================================================================
        if (nombre.isEmpty())
        {
            JOptionPane.showMessageDialog(vista, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
   
        if (pais == null || pais.equals("--- Ninguno ---"))
        {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un país.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!validarCorreo(correo))
        {
            JOptionPane.showMessageDialog(vista, "Correo inválido, debe contener '@' y un '.'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!validarPassword(pass))
        {
            JOptionPane.showMessageDialog(vista, "La contraseña debe tener mínimo 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //======================================================================
        // REGISTRAR EN LA BASE DE DATOS
        //======================================================================
        boolean registroExitoso = loginDAO.registrarClienteCompleto(nombre, correo, pass, pais);
        
        if (registroExitoso)
        {
            JOptionPane.showMessageDialog(vista, "¡Registro exitoso! Ahora puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlLogin();
        }
        else
        {
            JOptionPane.showMessageDialog(vista, "Error al registrar. El correo ya existe o hubo un problema.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //==========================================================================
    // VOLVER A AL LOGIN DESPUES DE REGISTRAR AL CLIENTE NUEVO
    //==========================================================================
    private void volverAlLogin()
    {
     // Crear instancias
        LoginDAO dao = new LoginDAO();
        LoginView vista = new LoginView();
        
        // Crear controller y conectar
        LoginController controller = new LoginController(dao, vista);
        vista.setController(controller);
        
        // Iniciar la aplicación
        controller.iniciar();       
    }
    //==========================================================================
    // VALIDAR CORREO
    //==========================================================================
    private boolean validarCorreo(String correo)
    {
        return correo.contains("@") && correo.contains(".");
    }
    //==========================================================================
    // VALIDAR CONTRASEÑA
    //==========================================================================
    private boolean validarPassword(String pass)
    {
        return pass.length() >= 4;
    }
}