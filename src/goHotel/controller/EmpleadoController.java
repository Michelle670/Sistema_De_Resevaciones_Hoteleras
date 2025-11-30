package goHotel.controller;

import goHotel.model.DAO.EmpleadoDAO;
import goHotel.view.GestionEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * ***************************************************************************
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 * ***************************************************************************
 */
public class EmpleadoController implements ActionListener {

    private final GestionEmpleado vista;
    private final EmpleadoDAO consultas = new EmpleadoDAO();
    private int idSeleccionado = 0;

    public EmpleadoController(GestionEmpleado vista) {
        this.vista = vista;
        cargarRolesEnCombo();
        
        vista.jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = vista.jTable3.getSelectedRow();

                if (fila != -1) {

                    idSeleccionado = Integer.parseInt(vista.jTable3.getValueAt(fila, 0).toString());

                    vista.txtId.setText(vista.jTable3.getValueAt(fila, 0).toString());
                    vista.txtNombre.setText(vista.jTable3.getValueAt(fila, 1).toString());
                    vista.txtCorreo.setText(vista.jTable3.getValueAt(fila, 2).toString());

                    String rol = vista.jTable3.getValueAt(fila, 3).toString();
                    vista.cbRol.setSelectedItem(rol);

                    vista.jPasswordField1.setText(vista.jTable3.getValueAt(fila, 4).toString());
                }
            }
        });

        // BOTONES
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Gestión de Empleados");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
        
    }

    private void cargarRolesEnCombo() {
        vista.cbRol.removeAllItems();
        vista.cbRol.addItem("Admin");
        vista.cbRol.addItem("Recepcion");
        vista.cbRol.addItem("Limpieza");
    }

    public void limpiar() {
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.jPasswordField1.setText("");
        vista.cbRol.setSelectedIndex(0);
        idSeleccionado = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // BOTÓN AGREGAR
        if (e.getSource() == vista.btAgregar) {
            String nombre = vista.txtNombre.getText();
            String correo = vista.txtCorreo.getText();
            String password = new String(vista.jPasswordField1.getPassword());
            int idRol = obtenerIdRol(vista.cbRol.getSelectedItem().toString());

            if (consultas.agregar(nombre, idRol, correo, password)) {
                JOptionPane.showMessageDialog(null, "Empleado agregado");
                limpiar();
                actualizarTabla();
            }
        }

        // BOTÓN EDITAR
        if (e.getSource() == vista.btEditar) {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado");
                return;
            }

            String nombre = vista.txtNombre.getText();
            String correo = vista.txtCorreo.getText();
            String password = new String(vista.jPasswordField1.getPassword());
            int idRol = obtenerIdRol(vista.cbRol.getSelectedItem().toString());

            if (consultas.editar(idSeleccionado, nombre, idRol, correo, password)) {
                JOptionPane.showMessageDialog(null, "Empleado actualizado");
                limpiar();
                actualizarTabla();
            }
        }

        // BOTÓN BUSCAR
        if (e.getSource() == vista.btBuscar) {

            String nombre = vista.txtNombre.getText().trim();

            DefaultTableModel modelo = (DefaultTableModel) vista.jTable3.getModel();
            modelo.setRowCount(0);

            ArrayList<Object[]> lista = consultas.buscar(nombre);
            Iterator<Object[]> it = lista.iterator();

            while (it.hasNext()) {
                Object[] fila = it.next();
                modelo.addRow(fila);
            }
        }

        // BOTÓN ELIMINAR
        if (e.getSource() == vista.btEliminar) {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un empleado");
                return;
            }

            int conf = JOptionPane.showConfirmDialog(
                    null, "¿Eliminar?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (conf == JOptionPane.YES_OPTION) {
                if (consultas.eliminar(idSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Empleado eliminado");
                    limpiar();
                    actualizarTabla();
                }
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

    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable3.getModel();
        consultas.cargarTabla(modelo);
    }

    public void setIdSeleccionado(int id) {
        this.idSeleccionado = id;
    }

    private int obtenerIdRol(String rol) {
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
}
