
package goHotel.controller;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.view.GestionReserva;
import goHotel.view.ReservaBuscarHabitacion;
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
    private final ReservaBuscarHabitacion vistaReservaBuscar;
    
    public ReservaController(Reserva modelo, ReservaDAO consultas, GestionReserva vista,ReservaBuscarHabitacion vistaReservaBuscar) 
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vistaReservaBuscar = vistaReservaBuscar;
        
        
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
//RESERVA BUSCAR HABITACION
//        this.vista.cbNombresHoteles.addActionListener(this);

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
        consultas.cargarDatosEnTabla(modelo,"CARGA",0,null,null,null,0,null);
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
        if (e.getSource() == vista.btnBuscar)
        {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionReserva.getModel();
        int habitacion = vista.txtHabitacion.getText().isEmpty() ? 0 : Integer.parseInt(vista.txtHabitacion.getText());
        int idReserva = vista.txtID.getText().isEmpty() ? 0 : Integer.parseInt(vista.txtID.getText());
        consultas.cargarDatosEnTabla(
            modelo,
            "BUSQUEDA",
            idReserva,
            vista.txtEstado.getText(),     // 4. estadoReserva
            vista.txtPais.getText(),       // 5. nombrePais
            vista.txtHotel.getText(),      // 6. nombreHotel
            habitacion,                    // 7. numHabitacion
            vista.txtCliente.getText()     // 8. nombreCliente
        );
        //consultas.cargarDatosEnTabla(modelo,"CARGA",0,null,null,null,0,null);
            
            
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
