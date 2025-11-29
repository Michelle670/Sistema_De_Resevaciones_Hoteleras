package goHotel.controller;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
import goHotel.model.DAO.ConsultasRol;
import goHotel.view.GestionRol;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
public class RolController implements ActionListener {

    private final GestionRol vista;
    private int idRolSeleccionado = 0;
    private final ConsultasRol consultas = new ConsultasRol();

    public RolController(GestionRol vista) {
        this.vista = vista;
      // BOTONES
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Gestión de Roles");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
    }

    public void limpiarCampos() {
        vista.txtNombreRol.setText("");
        vista.cbEstado.setSelectedIndex(0);
        vista.txtNombreRol.requestFocus();
        idRolSeleccionado = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // BOTÓN AGREGAR
        if (e.getSource() == vista.btAgregar) {
            String nombre = vista.txtNombreRol.getText();
            boolean estado = vista.cbEstado.getSelectedItem().toString().equals("Activo");

            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre es obligatorio");
                return;
            }

            if (consultas.registrarRol(nombre, estado)) {
                JOptionPane.showMessageDialog(null, "Rol agregado correctamente");
                limpiarCampos();
                actualizarTabla();
            }
        }

        // BOTÓN EDITAR
        if (e.getSource() == vista.btEditar) {
            String nombre = vista.txtNombreRol.getText();
            boolean estado = vista.cbEstado.getSelectedItem().toString().equals("Activo");

            if (idRolSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un rol de la tabla");
                return;
            }

            if (consultas.editarRol(idRolSeleccionado, nombre, estado)) {
                JOptionPane.showMessageDialog(null, "Rol actualizado");
                limpiarCampos();
                actualizarTabla();
            }
        }

        // BOTÓN BUSCAR
        if (e.getSource() == vista.btBuscar) {
            buscarRol();
        }

        // BOTÓN ELIMINAR
        if (e.getSource() == vista.btEliminar) {

            if (idRolSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un rol");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Eliminar?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (consultas.eliminarRol(idRolSeleccionado)) {
                JOptionPane.showMessageDialog(null, "Rol eliminado");
                limpiarCampos();
                actualizarTabla();
            }
        }

        // BOTÓN SALIR
        if (e.getSource() == vista.btSalir) {
            int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    "¿Está seguro que desea salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    private void buscarRol() {
        String nombre = vista.txtNombreRol.getText().trim();

        ArrayList<Object[]> resultados = consultas.buscarRolesPorNombre(nombre);

        DefaultTableModel modelo = (DefaultTableModel) vista.jTable2.getModel();
        modelo.setRowCount(0);

        Iterator<Object[]> it = resultados.iterator();
        while (it.hasNext()) {
            modelo.addRow(it.next());
        }
    }

    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable2.getModel();
        consultas.cargarDatosEnTabla(modelo);
    }

    public void setIdRolSeleccionado(int id) {
        this.idRolSeleccionado = id;
    }
}
