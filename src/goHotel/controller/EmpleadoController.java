//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.EmpleadoDAO;
import goHotel.view.GestionEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// EMPLEADO CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Gestión de Empleados.
 */
public class EmpleadoController implements ActionListener 
{
    // Referencia a la vista (pantalla)
    private final GestionEmpleado vista;
    
     // Acceso a las consultas de base de datos
    private final EmpleadoDAO consultas = new EmpleadoDAO();
    
    // Guarda el ID del rol seleccionado en la tabla
    private int idSeleccionado = 0;

    // ===========================================================
    // CONSTRUCTOR
    // ===========================================================
    public EmpleadoController(GestionEmpleado vista)
    {
        this.vista = vista;
        
        // Carga los roles disponibles en el combo box
        cargarRolesEnCombo();
        
        // Activar clic en tabla para cargar datos en los campos
        agregarClickEnTabla();

        // ==========================================
        // BOTONES
        // ==========================================
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        vista.btLimpiar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    // ==============================================
    // INICIO DE LA VENTANA
    // ============================================== 
    public void iniciar() 
    {
        vista.setTitle("Gestión de Empleados");
        vista.setLocationRelativeTo(null);
        actualizarTabla();

    }
    // ==========================
    // VALIDAR ID MANUAL
    // ==========================

    private Integer obtenerId() 
    {
        String tid = vista.txtId.getText().trim();

        if (tid.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID.");
            return null;
        }

        try
        {
            return Integer.parseInt(tid);
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.");
            return null;
        }
    }

    // ==========================================
    // CLICK EN TABLA PARA CARGAR DATOS
    // ==========================================
    private void agregarClickEnTabla()
    {
        vista.jTable3.addMouseListener(new java.awt.event.MouseAdapter() 
        
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {

                int fila = vista.jTable3.getSelectedRow();
                if (fila != -1) 
                {
                    
                    // Guardar ID del empleado seleccionado
                    idSeleccionado = Integer.parseInt(vista.jTable3.getValueAt(fila, 0).toString());
                    // Cargar datos desde la tabla a los campos
                    vista.txtId.setText(vista.jTable3.getValueAt(fila, 0).toString());
                    vista.txtNombre.setText(vista.jTable3.getValueAt(fila, 1).toString());
                    vista.txtCorreo.setText(vista.jTable3.getValueAt(fila, 2).toString());
                    vista.cbRol.setSelectedItem(vista.jTable3.getValueAt(fila, 3).toString());
                    vista.jPasswordField1.setText(vista.jTable3.getValueAt(fila, 4).toString());
                }
            }
        });
    }
    // ==========================
    // LIMPIAR
    // ==========================
    public void limpiar() 
    {
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.jPasswordField1.setText("");
        vista.cbRol.setSelectedIndex(0);
        idSeleccionado = 0;
    }
    
    // ==========================
    // CARGAR ROLES EN COMBO
    // ==========================
    private void cargarRolesEnCombo() 
    {
        vista.cbRol.removeAllItems();
        vista.cbRol.addItem("Admin");
        vista.cbRol.addItem("Recepcion");
        vista.cbRol.addItem("Limpieza");
    }

    // =========================================
    // MANEJO DE BOTONES
    // =========================================
    @Override
    public void actionPerformed(ActionEvent e)
    {

        // ============================
        // BOTÓN AGREGAR
        // ============================
        if (e.getSource() == vista.btAgregar) 
        {

            Integer id = obtenerId();
            if (id == null)
                return;
            
            String nombre = vista.txtNombre.getText().trim();
            String correo = vista.txtCorreo.getText().trim();
            String password = new String(vista.jPasswordField1.getPassword());
            int idRol = obtenerIdRol(vista.cbRol.getSelectedItem().toString());
            
            // Registrar nuevo empleado
            if (consultas.agregar(id,nombre, idRol, correo, password)) {
                JOptionPane.showMessageDialog(null, "Empleado agregado");
                limpiar();
                actualizarTabla();
            }
        }

        // ==========================================
        // BOTÓN EDITAR
        // ==========================================
        if (e.getSource() == vista.btEditar) 
        {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado");
                return;
            }

            String nombre = vista.txtNombre.getText();
            String correo = vista.txtCorreo.getText();
            String password = new String(vista.jPasswordField1.getPassword());
            int idRol = obtenerIdRol(vista.cbRol.getSelectedItem().toString());
            
            // Actualizar empleado
            if (consultas.editar(idSeleccionado, nombre, idRol, correo, password)) 
            {
                JOptionPane.showMessageDialog(null, "Empleado actualizado");
                limpiar();
                actualizarTabla();
            }
        }

        // ============================
        // BOTÓN BUSCAR 
        // ============================
        if (e.getSource() == vista.btBuscar) 
        {

             Integer id = obtenerId();
            if (id == null) 
            {
                return;
            }

            DefaultTableModel modelo = (DefaultTableModel) vista.jTable3.getModel();
            modelo.setRowCount(0);// Limpiar tabla

            ArrayList<Object[]> lista = consultas.buscarPorId(id);
             if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No existe un empleado con ese ID");
            } else 
             {
                modelo.addRow(lista.get(0));
            }
        }

        // ==========================================
        // BOTÓN ELIMINAR
        // ==========================================
        if (e.getSource() == vista.btEliminar) 
        {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado");
                return;
            }
            // Eliminar empleado
            if (consultas.eliminar(idSeleccionado))
            {
                JOptionPane.showMessageDialog(null, "Empleado eliminado");
                limpiar();
                actualizarTabla();
            }
        }

        // ============================
        // BOTÓN LIMPIAR
        // ============================
        if (e.getSource() == vista.btLimpiar) 
        {
            limpiar();
            actualizarTabla();
        }

        // ============================
        // BOTÓN SALIR
        // ============================
        if (e.getSource() == vista.btSalir) 
        {
            vista.dispose();// Cerrar ventana
        }
    }

    // ==========================================
    // ACTUALIZAR TABLA
    // ==========================================
    public void actualizarTabla()
    {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable3.getModel();
        consultas.cargarTabla(modelo);
    }
    // ==========================================
    // OBTENER ID DEL ROL SEGÚN TEXTO
    // ==========================================
    private int obtenerIdRol(String rol)
    {
        switch (rol) {
            case "Admin":
                return 1;
            case "Recepcion":
                return 2;
            case "Limpieza":
                return 3;
            default:
                return -1;
        }
    }
    //============================================
}
