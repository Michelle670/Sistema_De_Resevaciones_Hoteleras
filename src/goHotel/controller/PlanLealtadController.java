package goHotel.controller;

import goHotel.model.PlanLealtad;
import goHotel.model.DAO.PlanLealtadDAO; 
import goHotel.view.GestionPlanLealtad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException; 
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.lang.IllegalArgumentException;

public class PlanLealtadController implements ActionListener, MouseListener {

    private final PlanLealtad modelo;
    private final GestionPlanLealtad view;
    private final PlanLealtadDAO dao; 
    private final DecimalFormat df = new DecimalFormat("#.##");

    public PlanLealtadController(PlanLealtad modelo, GestionPlanLealtad vista) {
        this.modelo = modelo;
        this.view = vista;
        this.dao = new PlanLealtadDAO(); 

        this.view.setVisible(true);
        this.view.setLocationRelativeTo(null);
        
        // 1. Registro de Listeners
        this.view.getBtnAgregar().addActionListener(this);
        this.view.getBtnEditar().addActionListener(this);
        this.view.getBtnBuscar().addActionListener(this);
        this.view.getBtnEliminar().addActionListener(this);
        this.view.getBtnSalir().addActionListener(this);

        this.view.getTxtDescuento().addFocusListener(new DescuentoFocusHandler());

        this.view.getJtPlanLealtad().addMouseListener(this);

        cargarTabla(); 

    }

    private class DescuentoFocusHandler implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            String text = view.getTxtDescuento().getText().trim();
            if (text.startsWith("%")) {
                view.getTxtDescuento().setText(text.substring(1));
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            String text = view.getTxtDescuento().getText().trim();
            if (!text.isEmpty() && !text.startsWith("%")) {
                try {
                    double desc = Double.parseDouble(text);
                    view.getTxtDescuento().setText("%" + df.format(desc));
                } catch (NumberFormatException ex) {
                }
            }
        }
    }
    
    private PlanLealtad obtenerPlanDesdeVista() throws NumberFormatException, IllegalArgumentException {
        
        String idText = view.getTxtId().getText().trim();
        String descuentoText = view.getTxtDescuento().getText().trim().replace("%", "");
        String nivelText = view.getTxtNivel().getText().trim();

        if (idText.isEmpty() || descuentoText.isEmpty() || nivelText.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar ID, Descuento y Nivel.");
        }
        
        int idIngresado = Integer.parseInt(idText);
        double descuentoIngresado = Double.parseDouble(descuentoText);
        
        return new PlanLealtad(idIngresado, nivelText, descuentoIngresado);
    }

    public void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) view.getJtPlanLealtad().getModel();
        model.setRowCount(0);

        try {
            List<PlanLealtad> lista = dao.listarPlanesLealtad(); 
            
            for (PlanLealtad pl : lista) {
                Object[] fila = new Object[3];
                fila[0] = pl.getIdLealtad();
                fila[1] = "%" + df.format(pl.getDescuento()); 
                fila[2] = pl.getNivel();
                model.addRow(fila);
            }
            view.getJtPlanLealtad().setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al cargar datos de planes de lealtad: " + e.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == view.getBtnAgregar()) {
            try {
                PlanLealtad nuevoPlan = obtenerPlanDesdeVista();
                if (dao.agregarPlanLealtad(nuevoPlan)) { 
                    JOptionPane.showMessageDialog(view, "Plan de Lealtad agregado con éxito.");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(view, "El Plan de Lealtad NO pudo ser agregado.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage());
                JOptionPane.showMessageDialog(view, "Error: El ID y/o el Descuento deben ser valores numéricos válidos.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Error al guardar el Plan de Lealtad en la BD: " + ex.getMessage());
            }

        } else if (e.getSource() == view.getBtnEditar()) {
            try {
                PlanLealtad planModificar = obtenerPlanDesdeVista();
                if (dao.modificarPlanLealtad(planModificar)) { 
                    JOptionPane.showMessageDialog(view, "Plan de Lealtad modificado con éxito.");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(view, "El Plan de Lealtad NO pudo ser modificado. Verifique el ID.");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(view, ex.getMessage());
                JOptionPane.showMessageDialog(view, "Error: El ID y/o el Descuento deben ser números válidos.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Error al modificar: " + ex.getMessage());
            }

        } else if (e.getSource() == view.getBtnEliminar()) {
            String idText = view.getTxtId().getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe ingresar el ID del Plan de Lealtad a eliminar.");
                return;
            }
            
            Object[] options = {"Sí", "No"};
            int confirmacion = JOptionPane.showOptionDialog(
                view,
                "¿Está seguro de eliminar el Plan de Lealtad con ID: " + idText + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
            );

            if (confirmacion == 0) {
                try {
                    int idAEliminar = Integer.parseInt(idText); 
                    if (dao.eliminarPlanLealtad(idAEliminar)) { 
                        JOptionPane.showMessageDialog(view, "Plan de Lealtad eliminado con éxito.");
                        limpiarCampos();
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(view, "No se encontró el Plan de Lealtad con ese ID para eliminar.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Error: El ID debe ser un número entero válido.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error al eliminar: " + ex.getMessage());
                }
            }

        } else if (e.getSource() == view.getBtnBuscar()) {
            String criterio = JOptionPane.showInputDialog(view, "Ingrese ID o Nivel del Plan de Lealtad:");
            if (criterio != null && !criterio.trim().isEmpty()) {
                try {
                    PlanLealtad pl = dao.buscarPlanLealtad(criterio.trim()); 
                    if (pl != null) {
                        view.getTxtId().setText(String.valueOf(pl.getIdLealtad()));
                        view.getTxtNivel().setText(pl.getNivel());
                        view.getTxtDescuento().setText(String.valueOf(pl.getDescuento())); 

                    } else {
                        JOptionPane.showMessageDialog(view, "Plan de Lealtad no encontrado.");
                        limpiarCampos();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error de base de datos al buscar: " + ex.getMessage());
                }
            }
        } else if (e.getSource() == view.getBtnSalir()) {
            view.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == view.getJtPlanLealtad()) {
            int fila = view.getJtPlanLealtad().getSelectedRow();
            if (fila >= 0) {
                DefaultTableModel modeloTabla = (DefaultTableModel) view.getJtPlanLealtad().getModel();

                String id = modeloTabla.getValueAt(fila, 0).toString();
                String descuentoConSimbolo = modeloTabla.getValueAt(fila, 1).toString();
                String nivel = modeloTabla.getValueAt(fila, 2).toString();

                String descuentoSinSimbolo = descuentoConSimbolo.replace("%", "");

                view.getTxtId().setText(id);
                view.getTxtNivel().setText(nivel);
                view.getTxtDescuento().setText(descuentoSinSimbolo);
            }
        }
    }

    
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    
    public void limpiarCampos() {
        view.getTxtId().setText("");
        view.getTxtNivel().setText("");
        view.getTxtDescuento().setText("");
    }
}