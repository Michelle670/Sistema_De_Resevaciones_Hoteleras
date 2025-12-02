package goHotel.controller;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
import goHotel.model.DAO.TipoHabitaciónDAO;
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
    private final TipoHabitaciónDAO consultas = new TipoHabitaciónDAO();
    private int idSeleccionado = 0;

    public TipoHabitacionController(GestionTipoHabitacion vista) {
        this.vista = vista;

        // BOTONES
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Gestión de Tipo Habitación");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
    }

    public void limpiar() {
        vista.txtID.setText("");
        vista.txtNOMBRE.setText("");
        vista.txtPrecioBase.setText("");
        vista.jSpCapacidad.setValue(1);
        vista.txtareaDescrip.setText("");
        idSeleccionado = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // -------------------------
        // BOTÓN AGREGAR
        // -------------------------
        if (e.getSource() == vista.btAgregar) {
            TipoHabitacion t = new TipoHabitacion();
            t.setNombreTipo(vista.txtNOMBRE.getText());
            t.setDescripcion(vista.txtareaDescrip.getText());
            t.setCapacidad((int) vista.jSpCapacidad.getValue());
            t.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));

            if (consultas.agregar(t)) {
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
                JOptionPane.showMessageDialog(null, "Seleccione un registro en la tabla");
                return;
            }

            TipoHabitacion t = new TipoHabitacion();
            t.setIdTipo(idSeleccionado);
            t.setNombreTipo(vista.txtNOMBRE.getText());
            t.setDescripcion(vista.txtareaDescrip.getText());
            t.setCapacidad((int) vista.jSpCapacidad.getValue());
            t.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));

            if (consultas.editar(t)) {
                JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                limpiar();
                actualizarTabla();
            }
        }

        // -------------------------
        // BOTÓN BUSCAR
        // -------------------------
        if (e.getSource() == vista.btBuscar) {
            String nombre = vista.txtNOMBRE.getText();
          
            DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
            modelo.setRowCount(0);
            
            ArrayList<Object[]> resultados = consultas.buscar(nombre);
            Iterator<Object[]> it = resultados.iterator();
            while (it.hasNext()) {
                modelo.addRow(it.next());
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

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "¿Eliminar?", "Confirmación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (consultas.eliminar(idSeleccionado)) {
                    JOptionPane.showMessageDialog(null, "Eliminado");
                    limpiar();
                    actualizarTabla();
                }
            }
        }

        // -------------------------
        // BOTÓN SALIR
        // -------------------------
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
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        consultas.cargarTabla(modelo);
    }

    public void setIdSeleccionado(int id) {
        this.idSeleccionado = id;
    }
}
