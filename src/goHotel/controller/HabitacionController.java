
package goHotel.controller;

import goHotel.model.DAO.HabitacionDAO;
import goHotel.model.EstadoHabitacion;
import goHotel.model.Habitacion;
import goHotel.view.GestionHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 */
public class HabitacionController implements ActionListener{
    private final Habitacion modelo;
    private final HabitacionDAO consultas;
    private final GestionHabitacion vista;

    public HabitacionController(Habitacion modelo, HabitacionDAO consultas, GestionHabitacion vista) {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
        
        consultas.cargarEstado(vista.cmbEstado);
        consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
    }
    
    public void iniciar() {
        vista.setTitle("Gestión de Hoteles");
        vista.setLocationRelativeTo(null);
        vista.txtIdHabitacion.setVisible(false);
    }
    
    public void limpiar() {
        vista.txtIdHabitacion.setText("");
        vista.txtIdHotel.setText("");
        vista.txtTipoHabitacion.setText("");
        vista.txtNumero.setText("");
        vista.cmbEstado.setSelectedIndex(0);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        //btnAgregar
        if(e.getSource() == vista.btnAgregar){
            EstadoHabitacion estado = (EstadoHabitacion) vista.cmbEstado.getSelectedItem();
            
            if(vista.txtIdHabitacion.getText().trim().isEmpty() 
                    || vista.txtIdHotel.getText().trim().isEmpty()
                    || vista.txtTipoHabitacion.getText().trim().isEmpty()
                    || vista.txtNumero.getText().trim().isEmpty()
                    || estado == null){
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;   
            }
            
            int idHa = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
            modelo.setIdHabitacion(idHa);
            int idHo = Integer.parseInt(vista.txtIdHotel.getText().trim());
            modelo.setHotel(idHo);
            int tipo = Integer.parseInt(vista.txtTipoHabitacion.getText().trim());
            modelo.setTipo(tipo);
            int numero = Integer.parseInt(vista.txtNumero.getText().trim());
            modelo.setNumero(numero);
            modelo.setEstado(estado.name());
            
            if (consultas.registrarHabitacion(modelo)) {
                JOptionPane.showMessageDialog(null, "Habitación registrada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar cliente.");
            }
        }
        
        //btnEditar
        if(e.getSource() == vista.btnEditar){
            EstadoHabitacion estado = (EstadoHabitacion) vista.cmbEstado.getSelectedItem();

            if (vista.txtIdHabitacion.getText().trim().isEmpty()
                    || vista.txtIdHotel.getText().trim().isEmpty()
                    || vista.txtTipoHabitacion.getText().trim().isEmpty()
                    || vista.txtNumero.getText().trim().isEmpty()
                    || estado == null) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }

            int idHa = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
            modelo.setIdHabitacion(idHa);
            int idHo = Integer.parseInt(vista.txtIdHotel.getText().trim());
            modelo.setHotel(idHo);
            int tipo = Integer.parseInt(vista.txtTipoHabitacion.getText().trim());
            modelo.setTipo(tipo);
            int numero = Integer.parseInt(vista.txtNumero.getText().trim());
            modelo.setNumero(numero);
            modelo.setEstado(estado.name());
            
            if(consultas.editarHabitacion(modelo)){
                JOptionPane.showMessageDialog(null, "Habitación modificada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar habitación.");
            }
        }
        
        //btnEliminar
        if(e.getSource() == vista.btnEliminar){
            modelo.setIdHabitacion(Integer.parseInt(vista.txtIdHabitacion.getText().trim()));
            
            if(consultas.eliminarHabitacion(modelo)){
                JOptionPane.showMessageDialog(null, "Habitación eliminada.");
                consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar habitación.");
            }
            limpiar();
        }
        
        //btnBuscar
        if(e.getSource() == vista.btnBuscar){
            try{
                int id = Integer.parseInt(vista.txtIdHabitacion.getText().trim());
                modelo.setIdHabitacion(id);
                
                if(consultas.buscarHabitacion(modelo)){
                    vista.txtIdHabitacion.setText(String.valueOf(modelo.getIdHabitacion()));
                    vista.txtIdHotel.setText(String.valueOf(modelo.getHotel()));
                    vista.txtTipoHabitacion.setText(String.valueOf(modelo.getTipo()));
                    vista.txtNumero.setText(String.valueOf(modelo.getNumero()));
                    
                    String estadoBD = modelo.getEstado();

                    try {
                        EstadoHabitacion estadoEnum = EstadoHabitacion.valueOf(estadoBD);
                        vista.cmbEstado.setSelectedItem(estadoEnum);
                    } catch (IllegalArgumentException ex) {
                        vista.cmbEstado.setSelectedItem(EstadoHabitacion.DISPONIBLE);
                    }
                    
                    JOptionPane.showMessageDialog(null, "Habitación encontrada.");
                }
                
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.");
            }
        }
        
        //btnLimpiar
        if(e.getSource() == vista.btnLimpiar){
            limpiar();
            consultas.cargarDatosEnTabla((DefaultTableModel) vista.jtHabitaciones.getModel());
        }
        
        if (e.getSource() == vista.btnSalir){
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                vista.dispose();
            }
        }
    }
    
}
