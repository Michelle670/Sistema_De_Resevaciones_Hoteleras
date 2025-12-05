package goHotel.controller;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
import goHotel.model.DAO.TipoHabitacionDAO;
import goHotel.model.TipoHabitacion;
import goHotel.view.GestionTipoHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
public class TipoHabitacionController implements ActionListener {

    private final GestionTipoHabitacion vista;
    private final TipoHabitacionDAO consultas = new TipoHabitacionDAO();
    private int idSeleccionado = 0;

    public TipoHabitacionController(GestionTipoHabitacion vista) {
        this.vista = vista;

        agregarClickEnTabla();

        // BOTONES
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btLimpiar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Gestión de Tipo Habitación");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
    }

    private Integer obtenerId() {
        String texto = vista.txtID.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID.");
            return null;
        }

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.");
            return null;
        }
    }

    public void limpiar() {
        vista.txtID.setText("");
        vista.txtNOMBRE.setText("");
        vista.txtPrecioBase.setText("");
        vista.jSpCapacidad.setValue(1);
        vista.txtareaDescrip.setText("");
        idSeleccionado = 0;
    }

    private void agregarClickEnTabla() {
        vista.jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = vista.jTable1.getSelectedRow();
                if (fila != -1) {

                    idSeleccionado = Integer.parseInt(vista.jTable1.getValueAt(fila, 0).toString());

                    vista.txtID.setText(vista.jTable1.getValueAt(fila, 0).toString());
                    vista.txtNOMBRE.setText(vista.jTable1.getValueAt(fila, 1).toString());
                    vista.txtareaDescrip.setText(vista.jTable1.getValueAt(fila, 2).toString());
                    vista.jSpCapacidad.setValue(Integer.parseInt(vista.jTable1.getValueAt(fila, 3).toString()));
                    vista.txtPrecioBase.setText(vista.jTable1.getValueAt(fila, 4).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // -------------------------
        // BOTÓN AGREGAR
        // -------------------------
        if (e.getSource() == vista.btAgregar) {

            Integer id = obtenerId();
            if (id == null) {
                return;
            }

            TipoHabitacion tipHA = new TipoHabitacion();
            tipHA.setIdTipo(id);
            tipHA.setNombreTipo(vista.txtNOMBRE.getText());
            tipHA.setDescripcion(vista.txtareaDescrip.getText());
            tipHA.setCapacidad((int) vista.jSpCapacidad.getValue());
            tipHA.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));

            if (consultas.agregar(tipHA)) {
                JOptionPane.showMessageDialog(null, "Agregado correctamente");
                limpiar();
                actualizarTabla();
            }
        }

        // -------------------------
        // BOTÓN EDITAR
        // -------------------------
        if (e.getSource() == vista.btEditar) {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un tipo de registro en la tabla");
                return;
            }

            TipoHabitacion tipHA = new TipoHabitacion();
            tipHA.setIdTipo(idSeleccionado);
            tipHA.setNombreTipo(vista.txtNOMBRE.getText());
            tipHA.setDescripcion(vista.txtareaDescrip.getText());
            tipHA.setCapacidad((int) vista.jSpCapacidad.getValue());
            tipHA.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));

            if (consultas.editar(tipHA)) {
                JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                limpiar();
                actualizarTabla();
            }
        }

        // -------------------------
        // BOTÓN BUSCAR
        // -------------------------
        if (e.getSource() == vista.btBuscar) {

            Integer id = obtenerId();
            if (id == null) {
                return;
            }
            ArrayList<Object[]> resultados = consultas.buscarPorId(id);

            DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
            modelo.setRowCount(0);

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontró un tipo con ese ID.");
            } else {
                modelo.addRow(resultados.get(0));
            }
        }

        // -------------------------
        // BOTÓN ELIMINAR
        // -------------------------
        if (e.getSource() == vista.btEliminar) {

            if (idSeleccionado == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione un tipo para eliminar");
                return;
            }

            if (consultas.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(null, "Rol eliminado");
                limpiar();
                actualizarTabla();
            }
        }

        // LIMPIAR
        if (e.getSource() == vista.btLimpiar) {
            limpiar();
        }

        // -------------------------
        // BOTÓN SALIR
        // -------------------------
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
    }

    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        consultas.cargarTabla(modelo);
    }
}
