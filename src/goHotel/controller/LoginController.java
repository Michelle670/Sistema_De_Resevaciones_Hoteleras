
package goHotel.controller;

import goHotel.model.DAO.LoginDAO;
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

    
    public void iniciar()
    {
        vista.setTitle("Login - GoHotel");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
    
    // Método para validar correo
    public boolean validarCorreo(String correo) {
        return correo.contains("@") && correo.contains(".");
    }
    
    // Método para validar contraseña
    public boolean validarPassword(String pass) {
        return pass.length() >= 4;
    }
    
    // Método para login de empleado
    public boolean loginEmpleado(String correo, String pass) {
        return dao.loginEmpleado(correo, pass);
    }
    
    // Método para login de cliente
    public boolean loginCliente(String correo, String pass) {
        return dao.loginCliente(correo, pass);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //======================================================================
        // BOTÓN OK (Login)
        //======================================================================
        if (e.getSource() == vista.btnOk) {
            String correo = vista.txtCorreo.getText().trim();
            String pass = new String(vista.jPassword.getPassword());
            boolean esEmpleado = vista.jrEmpleado.isSelected();
            String tipoUsuario = esEmpleado ? "Empleado" : "Cliente";

            // VALIDAR CORREO
            if (!validarCorreo(correo)) {
                JOptionPane.showMessageDialog(vista, "Correo inválido, debe contener '@' y un '.'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDAR CONTRASEÑA
            if (!validarPassword(pass)) {
                JOptionPane.showMessageDialog(vista, "La contraseña debe tener mínimo 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean loginCorrecto = false;
            
            // SI ES EMPLEADO (login fijo)
            if (esEmpleado) {
                loginCorrecto = loginEmpleado(correo, pass);
            }
            // SI ES CLIENTE (se valida en BD)
            else {
                loginCorrecto = loginCliente(correo, pass);
            }
            
            if (loginCorrecto) {
                JOptionPane.showMessageDialog(vista, "¡Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            //Crear Menu con los parámetros
            Menu vistaMenu = new Menu(correo, tipoUsuario);
            MenuController menuController = new MenuController(vistaMenu, correo, tipoUsuario);
            vistaMenu.setController(menuController);
            
            // Iniciar el menú
            menuController.iniciar();
                
//                Menu m = new Menu(correo, tipoUsuario);
//                m.setVisible(true);
//                m.setLocationRelativeTo(null);
//                
                vista.dispose(); // cerrar ventana de login
            } else {
                JOptionPane.showMessageDialog(vista, "Correo o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        //======================================================================
        // BOTÓN CANCELAR
        //======================================================================
        if (e.getSource() == vista.btnCancelar) {
            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro que desea salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}
