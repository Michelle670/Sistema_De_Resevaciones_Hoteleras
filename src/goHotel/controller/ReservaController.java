
package goHotel.controller;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.view.GestionReserva;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
public class ReservaController implements ActionListener 
{
    
    private final Reserva modelo;
    private final ReservaDAO consultas;
    private final GestionReserva vista;
    
    public ReservaController(Reserva modelo, ReservaDAO consultas, GestionReserva vista) 
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        
//GESTION RESERVA
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
        this.vista.txtID.addActionListener(this);
        this.vista.txtHotel.addActionListener(this);
        this.vista.txtHabitacion.addActionListener(this);
        this.vista.txtEstado.addActionListener(this);
        this.vista.txtCliente.addActionListener(this);
        this.vista.txtPais.addActionListener(this);
    }
    
    public void iniciar() 
    {
        vista.setTitle("Gestión de Reservas");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        actualizarTabla();
    }
    
    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionReserva.getModel();
        consultas.cargarDatosEnTabla(modelo,null,null,null,null,null,null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //======================================================================
        // BOTÓN AGREGAR
        //======================================================================
        if (e.getSource() == vista.btnAgregar) {
           
            
            
            
        }
        
        //======================================================================
        // BOTÓN EDITAR
        //======================================================================
        if (e.getSource() == vista.btnEditar) {
        
            
            
            
        }
        
        //======================================================================
        // BOTÓN BUSCAR
        //======================================================================
        if (e.getSource() == vista.btnBuscar) {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionReserva.getModel();
        consultas.cargarDatosEnTabla(modelo,vista.txtID.getText(),vista.txtHotel.getText(),vista.txtHabitacion.getText(),vista.txtCliente.getText(),vista.txtEstado.getText(),vista.txtPais.getText());
        
            
            
        }
        
        //======================================================================
        // BOTÓN ELIMINAR
        //======================================================================
        if (e.getSource() == vista.btnEliminar) {
       
            
            
            
        }
        
        //======================================================================
        // BOTÓN LIMPIAR
        //======================================================================
        if (e.getSource() == vista.btnLimpiar) {
            actualizarTabla();
            vista.jtGestionReserva.clearSelection();
        }
        
        //======================================================================
        // BOTÓN SALIR
        //======================================================================
        if (e.getSource() == vista.btnSalir) {
            int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                vista.dispose();
            }
        }
    }
}
