
package goHotel.controller;
import goHotel.model.DAO.HotelDAO;
import goHotel.model.DAO.LoginDAO;
import goHotel.model.Hotel;
import goHotel.view.GestionHoteles;
import goHotel.view.GestionPaises;
import goHotel.view.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
public class MenuController implements ActionListener {
    
    private final Menu vista;
    private final String correoUsuario;
    private final String tipoUsuario;
    
    public MenuController(Menu vista, String correo, String tipo) {
        this.vista = vista;
        this.correoUsuario = correo;
        this.tipoUsuario = tipo;
        
        // Registrar listeners
        this.vista.chmGestionHoteles.addActionListener(this);
        this.vista.chmGestionPaises.addActionListener(this);
        this.vista.chmGestionReservas.addActionListener(this);
        this.vista.chmGestionClientes.addActionListener(this);
        this.vista.chmGestionEmpleados.addActionListener(this);
        this.vista.chmGestionHabitaciones.addActionListener(this);
        this.vista.chmSalir.addActionListener(this);
    }
    
    public void iniciar() {
        vista.setTitle("Menu Principal - GoHotel");
        vista.setLocationRelativeTo(null);
        cargarInformacionUsuario();
        configurarPermisosPorRol();
        vista.setVisible(true);
    }
    
    // =========================================================================
    // MÉTODOS DE CONFIGURACIÓN
    // =========================================================================
    
    private void cargarInformacionUsuario() {
        String[] infoUsuario = LoginDAO.getUserInfo(correoUsuario, tipoUsuario);
        if (infoUsuario != null) {
            vista.jlCorreo.setText(infoUsuario[0]);
            vista.jlTipo.setText(infoUsuario[1]);
        }
    }
    
    private void configurarPermisosPorRol() {
        // Si es Cliente, ocultar menús de empleado
         if (tipoUsuario.equalsIgnoreCase("Cliente")) 
        {
            vista.mnuMantenimientos.setVisible(false);
            vista.mnuLimpieza.setVisible(false);
            return;
        }
        
        // Si es Empleado, configurar según rol
        if (tipoUsuario.equalsIgnoreCase("Empleado")) {
            String[] infoUsuario = LoginDAO.getUserInfo(correoUsuario, tipoUsuario);
            if (infoUsuario != null) {
                String rol = infoUsuario[1].replace("Rol:", "").trim();
                aplicarPermisosSegunRol(rol);
            }
        }
    }
    
    private void aplicarPermisosSegunRol(String rol) {
        switch (rol) {
            case "Admin":
                vista.mnuReservas.setVisible(true);
                vista.mnuMantenimientos.setVisible(true);
                vista.mnuLimpieza.setVisible(true);
                break;
                
            case "Recepcion":
                vista.mnuReservas.setVisible(true);
                vista.mnuMantenimientos.setVisible(false);
                vista.mnuLimpieza.setVisible(false);
                break;
                
            case "Limpieza":
                vista.mnuReservas.setVisible(false);
                vista.mnuMantenimientos.setVisible(false);
                vista.mnuLimpieza.setVisible(true);
                break;
                
            default:
                vista.mnuReservas.setVisible(true);
                vista.mnuMantenimientos.setVisible(false);
                vista.mnuLimpieza.setVisible(false);
                break;
        }
    }
    
    // =========================================================================
    // MÉTODOS PARA ABRIR VENTANAS
    // =========================================================================
    
    private void abrirGestionHoteles() {
        Hotel modelo = new Hotel();
        HotelDAO dao = new HotelDAO();
        GestionHoteles vistaHoteles = new GestionHoteles();
        
        HotelController controller = new HotelController(modelo, dao, vistaHoteles);
        vistaHoteles.setController(controller);
        
        controller.iniciar();
    }
    
    private void abrirGestionPaises() {
        GestionPaises vistaPaises = new GestionPaises();
        vistaPaises.setVisible(true);
        vistaPaises.setLocationRelativeTo(null);
    }
    
    private void abrirGestionReservas() {
        JOptionPane.showMessageDialog(vista, "Módulo en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
        // TODO: Implementar cuando esté listo
    }
    
    private void abrirGestionClientes() {
        JOptionPane.showMessageDialog(vista, "Módulo en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
        // TODO: Implementar cuando esté listo
    }
    
    private void abrirGestionEmpleados() {
        JOptionPane.showMessageDialog(vista, "Módulo en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
        // TODO: Implementar cuando esté listo
    }
    
    private void abrirGestionHabitaciones() {
        JOptionPane.showMessageDialog(vista, "Módulo en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
        // TODO: Implementar cuando esté listo
    }
    
    private void salirDelSistema() {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea salir del sistema?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.chmGestionHoteles) {
            abrirGestionHoteles();
        }
        
        if (e.getSource() == vista.chmGestionPaises) {
            abrirGestionPaises();
        }
        
        if (e.getSource() == vista.chmGestionReservas) {
            abrirGestionReservas();
        }
        
        if (e.getSource() == vista.chmGestionClientes) {
            abrirGestionClientes();
        }
        
        if (e.getSource() == vista.chmGestionEmpleados) {
            abrirGestionEmpleados();
        }
        
        if (e.getSource() == vista.chmGestionHabitaciones) {
            abrirGestionHabitaciones();
        }
        
        if (e.getSource() == vista.chmSalir) {
            salirDelSistema();
        }
    }
} 

