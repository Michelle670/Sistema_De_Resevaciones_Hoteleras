//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.ServicioDAO;
import goHotel.model.Servicio;
import goHotel.view.GestionServicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// SERVICIOS CONTROLLER 
//==============================================================================  
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * de Gestión de Servicios.
 */
public class ServicioController implements ActionListener
{
    private final Servicio modelo;
    private final ServicioDAO consultas;
    private final GestionServicio vista;
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public ServicioController(Servicio modelo, ServicioDAO consultas, GestionServicio vista)
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
        consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaServicios.getModel());
    }
    // =========================================================================
    // INICIO DE LA VENTANA
    // =========================================================================
    public void iniciar()
    {
        vista.setTitle("Gestión de Servicios");
        vista.setLocationRelativeTo(null);
    }
    // =========================================================================
    // LIMPIAR
    // =========================================================================
    public void limpiar() 
    {
        vista.txtID.setText("");
        vista.txtNombre.setText("");
        vista.txtDescripcion.setText("");
    }
     // ========================================================================
    // MANEJO DE BOTONES
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) 
    {
    // =========================================================================
    // BTN AGREGAR
    // =========================================================================
        if(e.getSource() == vista.btnAgregar)
        {
            
            if (vista.txtID.getText().trim().isEmpty()
                    || vista.txtNombre.getText().trim().isEmpty()
                    || vista.txtDescripcion.getText().trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }

            int id = Integer.parseInt(vista.txtID.getText().trim());
            modelo.setIdServicio(id);
            modelo.setNombreServicio(vista.txtNombre.getText().trim());
            modelo.setDescripcion(vista.txtDescripcion.getText().trim());
            if(consultas.registrarServicio(modelo))
            {
                JOptionPane.showMessageDialog(null, "Servicio registrado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaServicios.getModel());
            }else 
            {
                JOptionPane.showMessageDialog(null, "Error al registrar servicio.");
            }
        }
    // =========================================================================
    // BTN EDITAR
    // =========================================================================
        if(e.getSource() == vista.btnEditar)
        {
            modelo.setIdServicio(Integer.parseInt(vista.txtID.getText().trim()));
            modelo.setNombreServicio(vista.txtNombre.getText().trim());
            modelo.setDescripcion(vista.txtDescripcion.getText().trim());
            if (consultas.modificarServicio(modelo)) 
            {
                JOptionPane.showMessageDialog(null, "Servicio modificado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaServicios.getModel());
            } else 
            {
                JOptionPane.showMessageDialog(null, "Error al modificar servicio.");
            }
        }
        
    // =========================================================================
    // BTN ELIMINAR
    // =========================================================================
        if(e.getSource() == vista.btnEliminar)
        {
            modelo.setIdServicio(Integer.parseInt(vista.txtID.getText().trim()));
            if (consultas.eliminarServicio(modelo))
            {
                JOptionPane.showMessageDialog(null, "Servicio eliminado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaServicios.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar servicio.");
            }
            limpiar();
        }
        
    // =========================================================================
    // BTN BUSCAR
    // =========================================================================
        if (e.getSource() == vista.btnBuscar) 
        {
            try 
            {
                int id = Integer.parseInt(vista.txtID.getText().trim());
                modelo.setIdServicio(id);

                if (consultas.buscarServicio(modelo))
                {
                    vista.txtID.setText(String.valueOf(modelo.getIdServicio()));
                    vista.txtNombre.setText(modelo.getNombreServicio());
                    vista.txtDescripcion.setText(modelo.getDescripcion());
                    JOptionPane.showMessageDialog(null, "Servicio encontrado.");
                    consultas.cargarDatosEnTablaPorID((DefaultTableModel) vista.jtTablaServicios.getModel(), id);
                } else 
                {
                    JOptionPane.showMessageDialog(null, "No se encontró un servicio con ese ID.");
                }
            } catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.");
            }
        }
    // =========================================================================
    // BTN LIMPIAR
    // =========================================================================
        if(e.getSource() == vista.btnLimpiar)
        {
            limpiar();
            consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaServicios.getModel());
        }
        
    // =========================================================================
    // BTN LIMPIAR
    // =========================================================================
        if(e.getSource() == vista.btnSalir)
        {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) 
            {
                vista.dispose();
            }
        }
    }
    //==========================================================================
}
