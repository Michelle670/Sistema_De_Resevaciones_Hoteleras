//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.Cliente;
import goHotel.model.DAO.ClienteDAO;
import goHotel.model.DAO.HabitacionDAO;
import goHotel.model.DAO.HotelDAO;
import goHotel.model.DAO.LimpiezaDAO;
import goHotel.model.DAO.LoginDAO;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.DAO.ServicioDAO;
import goHotel.model.DAO.PlanLealtadDAO; 
import goHotel.model.Habitacion;
import goHotel.model.Hotel;
import goHotel.model.Reserva;
import goHotel.model.Servicio;
import goHotel.view.GestionCliente;
import goHotel.view.GestionHabitacion;
import goHotel.view.GestionHoteles;
import goHotel.view.GestionPaises;
import goHotel.view.GestionReporteLimpieza;
import goHotel.view.GestionReserva;
import goHotel.view.GestionServicio;
import goHotel.view.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import goHotel.view.GestionPlanLealtad; 
import goHotel.model.PlanLealtad; 

/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// MENU CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Menu.
 */
public class MenuController implements ActionListener 
{
    private final Menu vista;
    private final String correoUsuario;
    private final String tipoUsuario;
    private final int idCliente;

    public MenuController(Menu vista, String correo, String tipo, int idCliente)
    {
        this.vista = vista;
        this.correoUsuario = correo;
        this.tipoUsuario = tipo;
        this.idCliente = idCliente;

        // Registrar listeners
        this.vista.chmGestionHoteles.addActionListener(this);
        this.vista.chmGestionPaises.addActionListener(this);
        this.vista.chmGestionPlanLealtad.addActionListener(this); 
        this.vista.chmGestionReservas.addActionListener(this);
        this.vista.chmGestionClientes.addActionListener(this);
        this.vista.chmGestionEmpleados.addActionListener(this);
        this.vista.chmGestionHabitaciones.addActionListener(this);
        this.vista.chmReporteOcupacion.addActionListener(this);
        this.vista.chmTipoServicio.addActionListener(this);
        this.vista.chmSalir.addActionListener(this);
    }

    public void iniciar() 
    {
        vista.setTitle("Menu Principal - GoHotel");
        vista.setLocationRelativeTo(null);
        cargarInformacionUsuario();
        configurarPermisosPorRol();
        vista.setVisible(true);
    }

    // =========================================================================
    // MÉTODO CARGARINFORMACIONUSUARIO
    // Carga la informacion del usuario para ver que rol tiene
    // =========================================================================
    private void cargarInformacionUsuario() 
    {
    String[] infoUsuario = LoginDAO.getUserInfo(correoUsuario, tipoUsuario);
    if (infoUsuario != null) 
    {
        vista.jlCorreo.setText(infoUsuario[0]);
        vista.jlTipo.setText(infoUsuario[1]);
        
        // Mostrar el ID del cliente si es Cliente
        if (tipoUsuario.equalsIgnoreCase("Cliente") && idCliente > 0) 
        {
            vista.jlIdCliente.setText("ID: " + idCliente);
        }
    }
    }
    // =========================================================================
    // MÉTODO CONFIGURACIONPERMISOSPORROL
    //  Configura los permisos por rol y los muestra en pantalla su rol y correo
    // =========================================================================
    private void configurarPermisosPorRol() 
    {
        // Si es Cliente, ocultar menús de empleado
          if (tipoUsuario.equalsIgnoreCase("Cliente"))
          {
            vista.mnuMantenimientos.setVisible(false);
            vista.mnuLimpieza.setVisible(false);
            return;
          }

        // Si es Empleado, configurar según rol
        if (tipoUsuario.equalsIgnoreCase("Empleado"))
        {
            String[] infoUsuario = LoginDAO.getUserInfo(correoUsuario, tipoUsuario);
            if (infoUsuario != null) {
                String rol = infoUsuario[1].replace("Rol:", "").trim();
                aplicarPermisosSegunRol(rol);
            }
        }
    }
    // =========================================================================
    // MÉTODO APLICARPERMISOSSEGUNROL
    // Oculta las pantallas  dependiendo de su rol
    // =========================================================================
    private void aplicarPermisosSegunRol(String rol) 
    {
        switch (rol) 
        {
            // Si es admin puede ver todo
            case "Admin":
                vista.mnuReservas.setVisible(true);
                vista.mnuMantenimientos.setVisible(true);
                vista.mnuLimpieza.setVisible(true);
                break;
            // Si es recepcionista solo puede ver reservas
            case "Recepcion":
                vista.mnuReservas.setVisible(true);
                vista.mnuMantenimientos.setVisible(false);
                vista.mnuLimpieza.setVisible(false);
                break;
            // Si es micelaneo solo puede ver limpieza
            case "Limpieza":
                vista.mnuReservas.setVisible(false);
                vista.mnuMantenimientos.setVisible(false);
                vista.mnuLimpieza.setVisible(true);
                break;
            // Si es cliente solo puede ver reservas
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
    private void abrirGestionHoteles()
    {
        Hotel modelo = new Hotel();
        HotelDAO dao = new HotelDAO();
        GestionHoteles vistaHoteles = new GestionHoteles();
        
        HotelController controller = new HotelController(modelo, dao, vistaHoteles);
        vistaHoteles.setController(controller);
        
        controller.iniciar();
    }
    // =========================================================================
    private void abrirGestionPaises() 
    {
        // Mantenemos el código original que funciona:
        GestionPaises vistaPaises = new GestionPaises();
        vistaPaises.setVisible(true);
        vistaPaises.setLocationRelativeTo(null);
    }
    // =========================================================================
    private void abrirGestionPlanLealtad() 
    {
        try 
        {
            // 1. Instanciar el MODELO
            PlanLealtad modelo = new PlanLealtad();
            // 2. Instanciar el DAO
            PlanLealtadDAO modeloDAO = new PlanLealtadDAO(); 
            // 3. Crear la VISTA sin usar Singleton
            GestionPlanLealtad vistaPlan = new GestionPlanLealtad();
            // 4. Creamos e inicializamos el controlador con los 3 argumentos
            PlanLealtadController controller = new PlanLealtadController(vistaPlan, modeloDAO, modelo); 
            // 5. Asignar el controlador a la vista si existe el setter (Ya lo hace el controlador, pero se deja para ser explícito)
            vistaPlan.setPlanLealtadController(controller);
            // 6. Mostrar la Vista (El controlador ya llama a iniciar(), que debería hacer esto)
            vistaPlan.setVisible(true); 
            vistaPlan.setLocationRelativeTo(null);
            
        } catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(vista, "Error al abrir Gestión Plan Lealtad: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    // =========================================================================
    private void abrirGestionReservas() 
    {
        Reserva modelo = new Reserva();
        ReservaDAO consultas = new ReservaDAO();
        GestionReserva vista = new GestionReserva(correoUsuario,tipoUsuario);
        ReservaController controller = new ReservaController(modelo, consultas, vista,correoUsuario,tipoUsuario.toUpperCase());
        vista.setController(controller);
        controller.iniciar();
    }
    // =========================================================================
    private void abrirGestionClientes() 
    {
        Cliente m = new Cliente();
        ClienteDAO q = new ClienteDAO();
        GestionCliente v = new GestionCliente();
        ClienteController ctrl = new ClienteController(m, q, v);
        v.setVisible(true);
        v.setLocationRelativeTo(null);
        ctrl.iniciar();
    }
    // =========================================================================
    private void abrirGestionEmpleados() 
    {
        
    }
    // =========================================================================
    private void abrirGestionHabitaciones()
    {
        Habitacion m = new Habitacion();
        HabitacionDAO q = new HabitacionDAO();
        GestionHabitacion v = new GestionHabitacion();
        HabitacionController ctrl = new HabitacionController(m, q, v);
        v.setVisible(true);
        v.setLocationRelativeTo(null);
        ctrl.iniciar();
    }
    // =========================================================================
    private void abrirGestionServicio()
    {
        Servicio m = new Servicio();
        ServicioDAO q = new ServicioDAO();
        GestionServicio v = new GestionServicio();
        ServicioController ctrl = new ServicioController(m, q, v);
        v.setVisible(true);
        v.setLocationRelativeTo(null);
        ctrl.iniciar();
    }
    // =========================================================================
    private void abrirReporteLimpieza()
    {
        LimpiezaDAO dao = new LimpiezaDAO();
        GestionReporteLimpieza vista = new GestionReporteLimpieza();
        LimpiezaController controller = new LimpiezaController(dao, vista);
        controller.iniciar();
    }
    // =========================================================================
    private void salirDelSistema() 
    {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea salir del sistema?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION)
        {
            System.exit(0);
        }
    }
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.chmGestionHoteles)
        {
            abrirGestionHoteles();
        }
        
        if (e.getSource() == vista.chmGestionPaises)
        {
            abrirGestionPaises();
        }
        
        if (e.getSource() == vista.chmGestionPlanLealtad) 
        {
            abrirGestionPlanLealtad(); 
        }
        
        if (e.getSource() == vista.chmGestionReservas) 
        {
            abrirGestionReservas();
        }
        
        if (e.getSource() == vista.chmGestionClientes)
        {
            abrirGestionClientes();
        }
        
        if (e.getSource() == vista.chmGestionEmpleados)
        {
            abrirGestionEmpleados();
        }
        
        if (e.getSource() == vista.chmGestionHabitaciones) 
        {
            abrirGestionHabitaciones();
        }
        
        if (e.getSource() == vista.chmTipoServicio)
        {
            abrirGestionServicio();
        }
        
        if (e.getSource() == vista.chmReporteOcupacion)
        {
            abrirReporteLimpieza(); 
        }
        
        if (e.getSource() == vista.chmSalir) 
        {
            salirDelSistema();
        }
    }
    // =========================================================================
}