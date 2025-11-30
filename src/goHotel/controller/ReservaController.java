
package goHotel.controller;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.view.GestionReserva;
import goHotel.view.ReservaBuscarHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/*****************************************************************************
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 9
 *****************************************************************************/
//==============================================================================  
//CONTROLLER PARA LA PANTALLA DE GESTION RESERVA
//==============================================================================  
public class ReservaController implements ActionListener 
{
  
    
    private final Reserva modelo;
    private final ReservaDAO consultas;
    private final GestionReserva vista;
    private final String correoUsuario;
    private final String tipoUsuario;

    
    public ReservaController(Reserva modelo, ReservaDAO consultas, GestionReserva vista,String correo, String tipo) 
    {
       
        this.correoUsuario = correo;
        this.tipoUsuario = tipo;
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
    
        
//==============================================================================      
//GESTION RESERVA
//==============================================================================  
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
//==============================================================================  
//INICIAR
//==============================================================================      
    public void iniciar() 
    {
        vista.setTitle("Gestión de Reservas");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        actualizarTabla();
        ajustarColumnas();
    }
    
    
    
//==============================================================================  
//ACTUALIZAR TABLA
//==============================================================================      
    public void actualizarTabla()
    {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionReserva.getModel();
        consultas.cargarDatosEnTabla(modelo,"CARGA",0,null,null,null,0,null,correoUsuario,tipoUsuario);
     
    }
//==============================================================================  
// CARGAR DATOS DE LA TABLA A LOS CAMPOS DE FILTRO
//==============================================================================
    private void cargarDatosDeTabla() 
    {
        int filaSeleccionada = vista.jtGestionReserva.getSelectedRow();
        
        if (filaSeleccionada != -1) 
        {
            vista.txtID.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 0).toString());
            vista.txtEstado.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 1).toString());
            vista.txtPais.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 2).toString());
            vista.txtHotel.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 3).toString());
            vista.txtHabitacion.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 4).toString());
            vista.txtCliente.setText(vista.jtGestionReserva.getValueAt(filaSeleccionada, 5).toString());
        }
    }
//==============================================================================  
// LIMPIAR CAMPOS DE FILTRO
//==============================================================================
    private void limpiarCampos() 
    {
        vista.txtID.setText("");
        vista.txtEstado.setText("");
        vista.txtPais.setText("");
        vista.txtHotel.setText("");
        vista.txtHabitacion.setText("");
        vista.txtCliente.setText("");
        vista.jtGestionReserva.clearSelection();
    }
//==============================================================================  
// SE LLAMAN LAS FUNCIONES DE LOS BTN's 
//==============================================================================  
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
        //======================================================================
        // BOTÓN AGREGAR
        //======================================================================
        if (e.getSource() == vista.btnAgregar) 
        {
            Reserva modelo = new Reserva();
            ReservaDAO dao = new ReservaDAO();
            ReservaBuscarHabitacion vista = new ReservaBuscarHabitacion();
        
            ReservaBusquedaController controller = new ReservaBusquedaController(modelo, dao, vista);
            controller.iniciar(); // ← AQUÍ SE ABRE EL FRAME Y SE CARGAN LOS HOTELES
               

        }
        
        //======================================================================
        // BOTÓN EDITAR
        //======================================================================
        if (e.getSource() == vista.btnEditar) 
        {
            
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
            vista.txtCliente.getText(),    // 8. nombreCliente
            correoUsuario,
            tipoUsuario
        );  
        }
        //======================================================================
        // BOTÓN ELIMINAR
        //======================================================================
        if (e.getSource() == vista.btnEliminar) 
        {
            int filaSeleccionada = vista.jtGestionReserva.getSelectedRow();
            
            // Validar que haya una fila seleccionada
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, 
                    "Debe seleccionar una reserva de la tabla", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener datos de la fila seleccionada
            int idReserva = Integer.parseInt(vista.jtGestionReserva.getValueAt(filaSeleccionada, 0).toString());
            String cliente = vista.jtGestionReserva.getValueAt(filaSeleccionada, 5).toString();
            String hotel = vista.jtGestionReserva.getValueAt(filaSeleccionada, 3).toString();
            
            // Confirmar la eliminación
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Está seguro de eliminar esta reserva?\n\n" +
                "ID: " + idReserva + "\n" +
                "Cliente: " + cliente + "\n" +
                "Hotel: " + hotel + "\n\n" +
                "Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) 
            {
                if (consultas.eliminar(idReserva)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Reserva eliminada exitosamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                    limpiarCampos();
                } else 
                {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al eliminar la reserva", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
            
            
        }
    
        //======================================================================
        // BOTÓN LIMPIAR
        //======================================================================
        if (e.getSource() == vista.btnLimpiar) 
        {
            actualizarTabla();
            vista.jtGestionReserva.clearSelection();
        }
        
        //======================================================================
        // BOTÓN SALIR
        //======================================================================
        if (e.getSource() == vista.btnSalir) 
        {
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
    
    private void ajustarColumnas() 
    {
    TableColumnModel columnModel = vista.jtGestionReserva.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(40);
    columnModel.getColumn(2).setPreferredWidth(40); 
    columnModel.getColumn(5).setPreferredWidth(40);
    columnModel.getColumn(6).setPreferredWidth(100);
    columnModel.getColumn(7).setPreferredWidth(90);
    columnModel.getColumn(8).setPreferredWidth(90);
    }

    }
