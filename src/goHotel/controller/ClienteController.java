
package goHotel.controller;


import goHotel.model.Cliente;
import goHotel.model.DAO.ClienteDAO;
import goHotel.model.DAO.ClienteDAO.ComboItem;
import goHotel.view.GestionCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class ClienteController implements ActionListener {
    private final Cliente modelo;
    private final ClienteDAO consultas;
    private final GestionCliente vista;

    public ClienteController(Cliente modelo, ClienteDAO consultas, GestionCliente vista) {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
        
        vista.cmbPlanLealtad.setModel(new javax.swing.DefaultComboBoxModel<>());
        vista.cmbPais.setModel(new javax.swing.DefaultComboBoxModel<>());
        consultas.cargarPlanes(vista.cmbPlanLealtad);
        consultas.cargarPaises(vista.cmbPais);
        consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
    }
    
    public void iniciar() {
        vista.setTitle("Gestión de Clientes");
        vista.setLocationRelativeTo(null);
    }
    
    public void limpiar() {
        vista.txtID.setText("");
        vista.cmbPlanLealtad.setSelectedIndex(0);
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.txtPassword.setText("");
        vista.cmbPais.setSelectedIndex(0);
        vista.lblPuntosAcumulados.setText("");
        vista.txtID.requestFocus();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //btnAgregar;
        if(e.getSource() == vista.btnAgregar){
            ComboItem planItem = (ComboItem) vista.cmbPlanLealtad.getSelectedItem();
            ComboItem paisItem = (ComboItem) vista.cmbPais.getSelectedItem();
            if(vista.txtID.getText().trim().isEmpty()
                    || vista.txtNombre.getText().trim().isEmpty()
                    || vista.txtCorreo.getText().trim().isEmpty()
                    || vista.txtPassword.getText().trim().isEmpty()
                    || vista.cmbPais.getSelectedIndex() == 0){
            
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }
            
            int id = Integer.parseInt(vista.txtID.getText().trim());
            modelo.setIdPlan(planItem.getId());
            modelo.setIdCliente(id);
            modelo.setNombre(vista.txtNombre.getText().trim());
            modelo.setCorreo(vista.txtCorreo.getText().trim());
            modelo.setContrasenna(vista.txtPassword.getText().trim());
            modelo.setIdPais(paisItem.getId());
            modelo.setPuntosLealtad(1000);
            
            if(consultas.registrarCliente(modelo)){
                JOptionPane.showMessageDialog(null, "Cliente registrado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar cliente.");
            } 
        }
        
        //btnEditar;
        if (e.getSource() == vista.btnEditar) {
            ComboItem planItem = (ComboItem) vista.cmbPlanLealtad.getSelectedItem();
            ComboItem paisItem = (ComboItem) vista.cmbPais.getSelectedItem();
            int id = Integer.parseInt(vista.txtID.getText().trim());
            modelo.setIdCliente(id);
            modelo.setIdPlan(planItem.getId());
            modelo.setNombre(vista.txtNombre.getText().trim());
            modelo.setCorreo(vista.txtCorreo.getText().trim());
            modelo.setContrasenna(vista.txtPassword.getText().trim());
            modelo.setIdPais(paisItem.getId());
            modelo.setPuntosLealtad(1000);

            if (consultas.modificarCliente(modelo)) {
                JOptionPane.showMessageDialog(null, "Cliente modificado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar cliente.");
            }
        }
        
        //btnEliminar;
        if (e.getSource() == vista.btnEliminar){
            modelo.setIdCliente(Integer.parseInt(vista.txtID.getText().trim()));
            
            if(consultas.eliminarCliente(modelo)){
                JOptionPane.showMessageDialog(null, "Cliente eliminado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar cliente.");
            }
            limpiar();            
        }
        
        //btnBuscar;
        if (e.getSource() == vista.btnBuscar){
            try{
                int id = Integer.parseInt(vista.txtID.getText().trim());
                modelo.setIdCliente(id);
                
                if(consultas.buscarCliente(modelo)){
                    vista.txtID.setText(String.valueOf(modelo.getIdCliente()));
                    seleccionarPorId(vista.cmbPlanLealtad, modelo.getIdPlan());                    
                    vista.txtNombre.setText(modelo.getNombre());
                    vista.txtCorreo.setText(modelo.getCorreo());
                    vista.txtPassword.setText(modelo.getContrasenna());
                    vista.cmbPais.setSelectedIndex(modelo.getIdPais());
                    seleccionarPorId(vista.cmbPais, modelo.getIdPais());
                    vista.lblPuntosAcumulados.setText(String.valueOf(modelo.getPuntosLealtad()));
                    
                    consultas.cargarDatosEnTablaPorID((DefaultTableModel) vista.jtTablaCliente.getModel(), id);
                    JOptionPane.showMessageDialog(null, "Cliente encontrado.");
                }else {
                    JOptionPane.showMessageDialog(null, "No se encontró un cliente con ese ID.");
                }
                
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.");
            }
        }
        
        //btnLimpiar
        if (e.getSource() == vista.btnLimpiar) {
            limpiar();
            consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
        }
        
        //btnSalir
        if (e.getSource() == vista.btnSalir) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                vista.dispose();
            }
        }
    }
    
    private void seleccionarPorId(JComboBox<ComboItem> combo, int idBuscado) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            ComboItem it = combo.getItemAt(i);
            if (it != null && it.getId() == idBuscado) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }
    
}
