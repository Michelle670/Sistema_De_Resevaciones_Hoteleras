//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.HabitacionDAO;
import goHotel.model.DAO.HabitacionDAO.ComboItemH;
import goHotel.model.DAO.HabitacionServicioDAO;
import goHotel.model.Habitacion;
import goHotel.model.DAO.ServicioDAO;
import goHotel.model.DAO.ServicioDAO.ComboItem;
import goHotel.view.GestionHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// HABITACION CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Gestión de Habitación.
 */
public class HabitacionController implements ActionListener
{
    
    private final Habitacion modelo;
    private final HabitacionDAO consultas;
    private final GestionHabitacion vista;
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final HabitacionServicioDAO habServDAO = new HabitacionServicioDAO();
    
    // ===========================================================
    // CONSTRUCTOR
    // ===========================================================
    public HabitacionController(Habitacion modelo, HabitacionDAO consultas, GestionHabitacion vista) 
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
        this.vista.btnAsignarServicio.addActionListener(this);
        this.vista.btnQuitarServicio.addActionListener(this);
        consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
        consultas.cargarHoteles(vista.cmbIdHotel);
        consultas.cargarTiposHabitacion(vista.cmbIdTipo);
        servicioDAO.cargarServicios(vista.cmbServicios);
    }
    //==========================================================================
    // INICIAR
    //==========================================================================
    public void iniciar()
    {
        vista.setTitle("Gestión de Habitaciones");
        vista.setLocationRelativeTo(null);
//         // Ocultar líneas de la tabla al inicio
//         vista.jtHabitaciones.setShowGrid(false);
//         vista.jtHabitaciones.setIntercellSpacing(new java.awt.Dimension(0, 0));
//         vista.jtHabitaciones.setRowHeight(25);
//         vista.jtHabitaciones.setFillsViewportHeight(false);
//         // Limpiar filas dummy de NetBeans
//         DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtHabitaciones.getModel();
//         modeloTabla.setRowCount(0);
         
          // Ocultar líneas de la tabla al inicio
         vista.jtServiciosAsignados.setShowGrid(false);
         vista.jtServiciosAsignados.setIntercellSpacing(new java.awt.Dimension(0, 0));
         vista.jtServiciosAsignados.setRowHeight(25);
         vista.jtServiciosAsignados.setFillsViewportHeight(false);
         // Limpiar filas dummy de NetBeans
         DefaultTableModel modeloTabla1 = (DefaultTableModel) vista.jtServiciosAsignados.getModel();
         modeloTabla1.setRowCount(0);
    }
    //==========================================================================
    // LIMPIAR
    //==========================================================================
    public void limpiar() 
    {
        vista.txtIdHabitacion.setText("");
        vista.cmbIdHotel.setSelectedIndex(0);
        vista.cmbIdTipo.setSelectedIndex(0);
        vista.txtNumero.setText("");
        vista.cmbIdHotel.setSelectedIndex(0);
    }
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //======================================================================
        // BTN AGREGAR
        //======================================================================
        if(e.getSource() == vista.btnAgregar)
        {
            ComboItemH idHotelPlan = (ComboItemH) vista.cmbIdHotel.getSelectedItem();
            ComboItemH idTipoPlan = (ComboItemH) vista.cmbIdTipo.getSelectedItem();
            
            if(vista.txtIdHabitacion.getText().trim().isEmpty() 
                    || vista.cmbIdHotel.getSelectedIndex() == 0
                    || vista.cmbIdTipo.getSelectedIndex() == 0
                    || vista.txtNumero.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;   
            }
            
            int idHa = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
            modelo.setIdHabitacion(idHa);
            modelo.setHotel(idHotelPlan.getId());
            modelo.setTipo(idTipoPlan.getId());
            int numero = Integer.parseInt(vista.txtNumero.getText().trim());
            modelo.setNumero(numero);
            
            if (consultas.registrarHabitacion(modelo)) 
            {
                JOptionPane.showMessageDialog(null, "Habitación registrada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else 
            {
                JOptionPane.showMessageDialog(null, "Error al registrar habitación.");
            }
        }
        //======================================================================
        // BTN EDITAR
        //======================================================================
        if(e.getSource() == vista.btnEditar)
        {
            ComboItemH idHotelPlan = (ComboItemH) vista.cmbIdHotel.getSelectedItem();
            ComboItemH idTipoPlan = (ComboItemH) vista.cmbIdTipo.getSelectedItem();

            if (vista.txtIdHabitacion.getText().trim().isEmpty()
                    || vista.cmbIdHotel.getSelectedIndex() == 0
                    || vista.cmbIdTipo.getSelectedIndex() == 0
                    || vista.txtNumero.getText().trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }

            int idHa = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
            modelo.setIdHabitacion(idHa);
            modelo.setHotel(idHotelPlan.getId());
            modelo.setTipo(idTipoPlan.getId());
            int numero = Integer.parseInt(vista.txtNumero.getText().trim());
            modelo.setNumero(numero);

            if(consultas.editarHabitacion(modelo)){
                JOptionPane.showMessageDialog(null, "Habitación modificada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else 
            {
                JOptionPane.showMessageDialog(null, "Error al modificar habitación.");
            }
        }
        //======================================================================
        // BTN ELIMINAR
        //======================================================================
        if(e.getSource() == vista.btnEliminar)
        {
            int id = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
            modelo.setIdHabitacion(id);
            
            if(consultas.eliminarHabitacion(modelo))
            {
                JOptionPane.showMessageDialog(null, "Habitación eliminada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else 
            {
                JOptionPane.showMessageDialog(null, "Error al eliminar habitación.");
            }
            cargarServiciosAsignados(id);
            limpiar();
        }
        //======================================================================
        // BTN BUSCAR
        //======================================================================
        
        if(e.getSource() == vista.btnBuscar){
            try{
                int id = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
                modelo.setIdHabitacion(id);
                
                if(consultas.buscarHabitacion(modelo)){
                    vista.txtIdHabitacion.setText(String.valueOf(modelo.getIdHabitacion()));
                    seleccionarPorId(vista.cmbIdHotel, modelo.getHotel());
                    seleccionarPorId(vista.cmbIdTipo, modelo.getTipo());
                    vista.txtNumero.setText(String.valueOf(modelo.getNumero()));
                    
                    consultas.cargarDatosEnTablaPorID((DefaultTableModel) vista.jtHabitaciones.getModel(), id);
                    cargarServiciosAsignados(id);
                    JOptionPane.showMessageDialog(null, "Habitación encontrada.");
                }
                
            }catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.");
            }
        }
        //======================================================================
        // BTN LIMPIAR
        //======================================================================
        if(e.getSource() == vista.btnLimpiar)
        {
            limpiar();
            consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            DefaultTableModel m = (DefaultTableModel) vista.jtServiciosAsignados.getModel();
            m.setRowCount(0);
        }
        //======================================================================
        // BTN SALIR
        //======================================================================
        if (e.getSource() == vista.btnSalir)
        {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION)
            {
                vista.dispose();
            }
        }
        //======================================================================
        // BTN ASIGNAR SERVICIO
        //======================================================================
        if (e.getSource() == vista.btnAsignarServicio)
        {
            asignarServicio();
        }
        //======================================================================
        // BTN QUITAR SERVICIO
        //======================================================================
        if (e.getSource() == vista.btnQuitarServicio) 
        {
            quitarServicio();
        }
    }
    //==========================================================================
    //METODO ASIGNAR SERVICIO
    // Asigna los servicios a la habitacion
    //==========================================================================
    private void asignarServicio() 
    {
        int idHabitacion = Integer.parseInt(vista.txtIdHabitacion.getText().trim());

        ComboItem servItem = (ComboItem) vista.cmbServicios.getSelectedItem();
        if (servItem == null || servItem.getId() == 0) 
        {
            JOptionPane.showMessageDialog(null, "Seleccione un servicio válido.");
            return;
        }

        boolean ok = habServDAO.asignarServicio(idHabitacion, servItem.getId());
        if (ok)
        {
            JOptionPane.showMessageDialog(null, "Servicio asignado.");
            cargarServiciosAsignados(idHabitacion);
        } else 
        {
            JOptionPane.showMessageDialog(null, "Ese servicio ya estaba asignado.");
        }
        
        vista.cmbServicios.setSelectedIndex(0);
    }
    //==========================================================================
    //METODO QUITAR SERVICIO
    // Quita los servicios a la habitacion
    //==========================================================================
    private void quitarServicio() 
    {
        int idHabitacion = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
        int fila = vista.jtServiciosAsignados.getSelectedRow();
        if (fila == -1) 
        {
            JOptionPane.showMessageDialog(null, "Seleccione un servicio asignado.");
            return;
        }
        int idServicio = (int) vista.jtServiciosAsignados.getValueAt(fila, 0);
        if (habServDAO.quitarServicio(idHabitacion, idServicio))
        {
            JOptionPane.showMessageDialog(null, "Servicio removido.");
            cargarServiciosAsignados(idHabitacion);
        }
        vista.cmbServicios.setSelectedIndex(0);
    }
    //==========================================================================
    //METODO CARGAR SERVICIOS ASIGNADOS
    // Carga los servicios asignados
    //==========================================================================
    public void cargarServiciosAsignados(int idHabitacion)
    {
        DefaultTableModel m = (DefaultTableModel) vista.jtServiciosAsignados.getModel();
        m.setRowCount(0);
        List<Integer> ids = habServDAO.obtenerServiciosDeHabitacion(idHabitacion);

        for (Integer idServ : ids) 
        {
            String nombre = servicioDAO.obtenerNombreServicio(idServ); // método simple en ServicioDAO
            m.addRow(new Object[]{idServ, nombre});
        }
    }
    //==========================================================================
    //METODO SELECIONAR POR ID
    // Se usa para los combos del hotel y tipo para seleccionar por id
    //==========================================================================
    private void seleccionarPorId(JComboBox<ComboItemH> combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            ComboItemH it = combo.getItemAt(i);
            if (it != null && it.getId() == idBuscado) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }
    //==========================================================================  
}
