package goHotel.controller;

import goHotel.model.PlanLealtad;
import goHotel.model.DAO.PlanLealtadDAO;
import goHotel.view.GestionPlanLealtad;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PlanLealtadController implements ActionListener {

    private final GestionPlanLealtad vista;
    private final PlanLealtadDAO modeloDAO;
    private final PlanLealtad modelo;

    public PlanLealtadController(GestionPlanLealtad vista, PlanLealtadDAO modeloDAO, PlanLealtad modelo) {
        this.vista = vista;
        this.modeloDAO = modeloDAO;
        this.modelo = modelo;
        
        this.vista.setPlanLealtadController(this);

        iniciar(); 
    }

    public void iniciar() {
        
        ActionListener[] actionListeners = this.vista.getBtnAgregar().getActionListeners();
        for (ActionListener al : actionListeners) {
            this.vista.getBtnAgregar().removeActionListener(al);
            this.vista.getBtnEditar().removeActionListener(al);
            this.vista.getBtnEliminar().removeActionListener(al);
            this.vista.getBtnBuscar().removeActionListener(al);
            this.vista.getBtnSalir().removeActionListener(al);
            this.vista.getBtnLimpiar().removeActionListener(al);
        }

        // --- CORRECCIÓN DEL ERROR AQUÍ ---
        java.awt.event.MouseListener[] mouseListeners = this.vista.getJtPlanLealtad().getMouseListeners();
        for (java.awt.event.MouseListener ml : mouseListeners) {
             if (ml instanceof MouseAdapter) {
                this.vista.getJtPlanLealtad().removeMouseListener(ml);
             }
        }
        // ----------------------------------
        
        this.vista.getBtnAgregar().addActionListener(this);
        this.vista.getBtnEditar().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnBuscar().addActionListener(this);
        this.vista.getBtnSalir().addActionListener(this);
        
        this.vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        
        this.vista.getJtPlanLealtad().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaParaEdicion();
            }
        });
        
        actualizarTabla();
        vista.setVisible(true);
    }
    
    private void seleccionarFilaParaEdicion() {
        int fila = vista.getJtPlanLealtad().getSelectedRow();
        
        if (fila >= 0) {
            DefaultTableModel tableModel = (DefaultTableModel) vista.getJtPlanLealtad().getModel();
            
            Object id = tableModel.getValueAt(fila, 0);
            Object nivel = tableModel.getValueAt(fila, 1);
            Object descuento = tableModel.getValueAt(fila, 2);
            
            vista.getTxtId().setText(id != null ? id.toString() : "");
            vista.getTxtNivel().setText(nivel != null ? nivel.toString() : "");
            vista.getTxtDescuento().setText(descuento != null ? descuento.toString() : "");
        }
    }

    public void actualizarTabla() {
        
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nivel", "Descuento (%)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        List<PlanLealtad> planes = modeloDAO.obtenerTodos(); 

        for (PlanLealtad pl : planes) {
            tableModel.addRow(new Object[]{
                pl.getId(),
                pl.getNivel(),
                pl.getDescuento()
            });
        }
        
        this.vista.getJtPlanLealtad().setModel(tableModel);
    }
    
    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea cerrar la ventana de Gestión de Planes de Lealtad?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.getBtnSalir()) {
            salir();
            return;
        }
        
        if (e.getSource() == vista.getBtnAgregar()) {
            
            try {
                if (!vista.getTxtId().getText().trim().isEmpty()) {
                    modelo.setId(Integer.parseInt(vista.getTxtId().getText()));
                } else {
                    modelo.setId(0); 
                }
                
                String nivel = vista.getTxtNivel().getText().trim();
                double descuento = Double.parseDouble(vista.getTxtDescuento().getText());
                
                if (nivel.isEmpty() || descuento < 0) {
                    JOptionPane.showMessageDialog(vista, "El Nivel no puede estar vacío y el Descuento debe ser positivo.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                modelo.setNivel(nivel);
                modelo.setDescuento(descuento);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "ID y Descuento deben ser números válidos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.agregar(modelo)) {
                JOptionPane.showMessageDialog(vista, "Plan de Lealtad Agregado con éxito.");
                vista.limpiarCampos();
                actualizarTabla(); 
            } else {
                JOptionPane.showMessageDialog(vista, "Error al agregar Plan de Lealtad. Asegúrese de que el ID no exista.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else if (e.getSource() == vista.getBtnEditar()) {
            
            if (vista.getTxtId().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un Plan o ingresar el ID a editar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                modelo.setId(Integer.parseInt(vista.getTxtId().getText())); 
                modelo.setNivel(vista.getTxtNivel().getText().trim());
                modelo.setDescuento(Double.parseDouble(vista.getTxtDescuento().getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "ID y Descuento deben ser números válidos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.editar(modelo)) {
                JOptionPane.showMessageDialog(vista, "Plan de Lealtad Editado con éxito.");
                vista.limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al editar Plan de Lealtad. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (e.getSource() == vista.getBtnEliminar()) {
            
            try {
                int id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setId(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del plan a eliminar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.eliminar(modelo)) {
                JOptionPane.showMessageDialog(vista, "Plan de Lealtad Eliminado con éxito.");
                vista.limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar Plan de Lealtad. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        else if (e.getSource() == vista.getBtnBuscar()) {
            
            try {
                int id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setId(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del plan a buscar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PlanLealtad encontrado = modeloDAO.buscar(modelo);

            if (encontrado != null) {
                vista.getTxtNivel().setText(encontrado.getNivel());
                vista.getTxtDescuento().setText(String.valueOf(encontrado.getDescuento()));
                JOptionPane.showMessageDialog(vista, "Plan de Lealtad encontrado.");
            } else {
                JOptionPane.showMessageDialog(vista, "Plan de Lealtad no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                vista.getTxtNivel().setText("");
                vista.getTxtDescuento().setText("");
            }
        }
    }
}