
package goHotel.controller;


import goHotel.model.Cliente;
import goHotel.model.ClienteConsultas;
import goHotel.view.GestionCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class ClienteController implements ActionListener {
    private final Cliente modelo;
    private final ClienteConsultas consultas;
    private final GestionCliente vista;

    public ClienteController(Cliente modelo, ClienteConsultas consultas, GestionCliente vista) {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
        PaisController paisCtrl = new PaisController();
        paisCtrl.cargarPaises(vista.cmbPais);
        
        PlanLealtadController planCtrl = new PlanLealtadController();
        planCtrl.cargarPlanes(vista.cmbPlanLealtad);
        
        consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
    }
    
    public void iniciar() {
        vista.setTitle("Gestor de Clientes");
        vista.setLocationRelativeTo(null);
        vista.txtID.setVisible(false);
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
            
            if(vista.txtID.getText().trim().isEmpty()
                    || vista.txtNombre.getText().trim().isEmpty()
                    || vista.txtCorreo.getText().trim().isEmpty()
                    || vista.txtPassword.getText().trim().isEmpty()
                    || vista.cmbPais.getSelectedIndex() == 0){
            
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }
            
            int id = Integer.parseInt(vista.txtID.getText().trim());
            modelo.setIdCliente(id);
            modelo.setIdPlan(vista.cmbPlanLealtad.getSelectedIndex());
            modelo.setNombre(vista.txtNombre.getText().trim());
            modelo.setCorreo(vista.txtCorreo.getText().trim());
            modelo.setContrasenna(vista.txtPassword.getText().trim());
            modelo.setIdPais(Integer.parseInt(vista.cmbPais.getSelectedItem().toString()));
            modelo.setPuntosLealtad(1000);
            
            if(consultas.registrarCliente(modelo)){
                JOptionPane.showMessageDialog(null, "Cliente registrado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar servicio.");
            } 
        }
        
        //btnEditar;
        if (e.getSource() == vista.btnEditar) {
            int id = Integer.parseInt(vista.txtID.getText().trim());
            modelo.setIdCliente(id);
            modelo.setIdPlan(Integer.parseInt(vista.cmbPlanLealtad.getSelectedItem().toString()));
            modelo.setNombre(vista.txtNombre.getText().trim());
            modelo.setCorreo(vista.txtCorreo.getText().trim());
            modelo.setContrasenna(vista.txtPassword.getText().trim());
            modelo.setIdPais(Integer.parseInt(vista.cmbPais.getSelectedItem().toString()));
            modelo.setPuntosLealtad(1000);

            if (consultas.modificarCliente(modelo)) {
                JOptionPane.showMessageDialog(null, "Cliente modificado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar servicio.");
            }
        }
        
        //btnEliminar;
        if (e.getSource() == vista.btnEliminar){
            modelo.setIdCliente(Integer.parseInt(vista.txtID.getText().trim()));
            
            if(consultas.eliminarCliente(modelo)){
                JOptionPane.showMessageDialog(null, "Servicio eliminado.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtTablaCliente.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar servicio.");
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
                    seleccionarPorValor(vista.cmbPlanLealtad, String.valueOf(modelo.getIdPlan()));
                    vista.txtNombre.setText(modelo.getNombre());
                    vista.txtCorreo.setText(modelo.getCorreo());
                    vista.txtPassword.setText(modelo.getContrasenna());
                    vista.cmbPais.setSelectedIndex(modelo.getIdPais());
                    seleccionarPorValor(vista.cmbPais, String.valueOf(modelo.getIdPais()));
                    vista.lblPuntosAcumulados.setText(String.valueOf(modelo.getPuntosLealtad()));
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
    
    private void seleccionarPorValor(javax.swing.JComboBox<String> combo, String value) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (value.equals(combo.getItemAt(i))) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.setSelectedIndex(0);
    }
    
}
