package goHotel.controller;

/**
 * ***************************************************************************
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 * ***************************************************************************
 */
import goHotel.model.DAO.RolDAO;
import goHotel.view.GestionRol;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador encargado de manejar la lógica de la pantalla Gestión de Roles.
 */
public class RolController implements ActionListener {
    // Referencia a la vista (pantalla)
    private final GestionRol vista;
    // Guarda el ID del rol seleccionado en la tabla
    private int idRolSeleccionado = 0;
     // Acceso a las consultas de base de datos
    private final RolDAO consultas = new RolDAO();

    // ===========================================================
    // CONSTRUCTOR
    // ===========================================================
    public RolController(GestionRol vista) {
        this.vista = vista;
        
        // Activar clic en tabla para cargar datos en los campos
        agregarClickEnTabla();
        // ==========================================
        // BOTONES
        // ==========================================
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }
    
    // ==============================================
    // INICIO DE LA VENTANA
    // ============================================== 
    public void iniciar() {
        vista.setTitle("Gestión de Roles");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
    }

    // ==========================================
    // OBTENER ID VALIDADO
    // ==========================================
    private Integer obtenerId() {
        String texto = vista.txtid_rol.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID.");
            return null;
        }

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero válido.");
            return null;
        }
    }
// ==========================================
// LIMPIAR CAMPOS
// ==========================================

    public void limpiarCampos() {
        vista.txtid_rol.setText("");
        vista.txtNombreRol.setText("");
        vista.cbEstado.setSelectedIndex(0);// Vuelve a "Activo"
        vista.txtNombreRol.requestFocus();// Retoma el foco
        idRolSeleccionado = 0;// Reinicia selección
    }

    // ==========================================
    // CLICK EN TABLA PARA CARGAR DATOS
    // ==========================================
    private void agregarClickEnTabla() {
        vista.jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = vista.jTable2.getSelectedRow();

                if (fila != -1) {
                    
                     // Guardar ID del rol seleccionado
                    idRolSeleccionado = Integer.parseInt(vista.jTable2.getValueAt(fila, 0).toString());
                    
                    // Cargar datos desde la tabla a los campos
                    vista.txtid_rol.setText(vista.jTable2.getValueAt(fila, 0).toString());
                    vista.txtNombreRol.setText(vista.jTable2.getValueAt(fila, 1).toString());
                    vista.cbEstado.setSelectedItem(vista.jTable2.getValueAt(fila, 2).toString());
                }
            }
        });
    }
    
    // =========================================
    // MANEJO DE BOTONES
    // =========================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // ============================
        // BOTÓN AGREGAR
        // ============================

        if (e.getSource() == vista.btAgregar) {

            Integer id_rol = obtenerId();
            if (id_rol == null) {
                return;
            }

            String nombre = vista.txtNombreRol.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre de rol");
                return;
            }
            // Convertir estado del combo en booleano
            boolean estado = vista.cbEstado.getSelectedItem().toString().equals("Activo");
            
             // Registrar rol en BD
            if (consultas.registrarRol(id_rol, nombre, estado)) {
                JOptionPane.showMessageDialog(null, "Rol agregado correctamente");
                limpiarCampos();
                actualizarTabla();
            }
        }
        // ==========================================
        // BOTÓN EDITAR
        // ==========================================
        if (e.getSource() == vista.btEditar) {

            Integer id = obtenerId();
            if (id == null) {
                return;
            }
            String nombre = vista.txtNombreRol.getText().trim();
            boolean estado = vista.cbEstado.getSelectedItem().toString().equals("Activo");

            if (idRolSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un rol de la tabla.");
                return;
            }
            
             // Editar rol en BD
            if (consultas.editarRol(idRolSeleccionado, nombre, estado)) {
                JOptionPane.showMessageDialog(null, "Rol actualizado");
                limpiarCampos();
                actualizarTabla();
            }
        }

        // ============================
        // BOTÓN BUSCAR 
        // ============================
        if (e.getSource() == vista.btBuscar) {

            Integer id_rol = obtenerId();
            if (id_rol == null) {
                return;
            }

            ArrayList<Object[]> resultado = consultas.buscarRolPorId(id_rol);
            DefaultTableModel modelo = (DefaultTableModel) vista.jTable2.getModel();
            modelo.setRowCount(0); // Limpiar tabla

            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No existe un rol con ese ID.");
            } else {
                modelo.addRow(resultado.get(0));// Mostrar resultado en tabla
            }
        }
        // ==========================================
        // BOTÓN ELIMINAR
        // ==========================================
        if (e.getSource() == vista.btEliminar) {

            Integer id = obtenerId();
            if (id == null) {
                return;
            }

            if (idRolSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un rol de la tabla.");
                return;
            }
            
            // Eliminar rol de BD
            if (consultas.eliminarRol(idRolSeleccionado)) {
                JOptionPane.showMessageDialog(null, "Rol eliminado");
                limpiarCampos();
                actualizarTabla();
            }
        }

        // ============================
        // BOTÓN LIMPIAR
        // ============================
        if (e.getSource() == vista.btnLimpiar) {
            limpiarCampos();
            actualizarTabla();
        }

        // ============================
        // BOTÓN SALIR
        // ============================
        if (e.getSource() == vista.btSalir) {
            vista.dispose();// Cierra la ventana
        }
    }

    // ==========================================
    // ACTUALIZAR TABLA
    // ==========================================
    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable2.getModel();
        consultas.cargarDatosEnTabla(modelo);
    }
}
