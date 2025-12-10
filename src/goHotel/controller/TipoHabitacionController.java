//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.TipoHabitacionDAO;
import goHotel.model.TipoHabitacion;
import goHotel.view.GestionTipoHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// TIPO HABITACION CONTROLLER 
//==============================================================================  
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * de Gestión de Tipos de Habitación.
 */
public class TipoHabitacionController implements ActionListener 
{

    // Referencia a la vista (pantalla)
    private final GestionTipoHabitacion vista;
     // Acceso consultas SQL
    private final TipoHabitacionDAO consultas = new TipoHabitacionDAO();
    // Variable para almacenar el ID seleccionado en la tabla
    private int idSeleccionado = 0;
    // ===========================================================
    // CONSTRUCTOR
    // ===========================================================
    public TipoHabitacionController(GestionTipoHabitacion vista) 
    {
        this.vista = vista;
        // Habilita que al hacer clic en la tabla se carguen los datos en los campos
        agregarClickEnTabla();

        // ==========================================
        // BOTONES
        // ==========================================
        this.vista.btAgregar.addActionListener(this);
        this.vista.btEditar.addActionListener(this);
        this.vista.btBuscar.addActionListener(this);
        this.vista.btEliminar.addActionListener(this);
        this.vista.btLimpiar.addActionListener(this);
        this.vista.btSalir.addActionListener(this);
    }

    // =========================================================================
    // INICIO DE LA VENTANA
    // =========================================================================
    public void iniciar() 
    {
        vista.setTitle("Gestión de Tipo Habitación");
        vista.setLocationRelativeTo(null);
        actualizarTabla();
    }
    // =========================================================================
    // OBTENER ID INGRESADO
    // =========================================================================
    private Integer obtenerId() 
    {
        String texto = vista.txtID.getText().trim();

        if (texto.isEmpty()) 
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar un ID.");
            return null;
        }

        try 
        {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.");
            return null;
        }
    }
    // =========================================================================
    // LIMPIAR CAMPOS DE LA PANTALLA
    // =========================================================================
    public void limpiar() 
    {
        vista.txtID.setText("");
        vista.txtNOMBRE.setText("");
        vista.txtPrecioBase.setText("");
        vista.jSpCapacidad.setValue(1);
        vista.txtareaDescrip.setText("");
        idSeleccionado = 0;
    }
    
    // =========================================================================
    // CLICK EN TABLA -> CARGAR DATOS
    // =========================================================================
    private void agregarClickEnTabla() 
    {
        vista.jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = vista.jTable1.getSelectedRow();
                if (fila != -1) 
                {

                    // Guardar ID seleccionado
                    idSeleccionado = Integer.parseInt(vista.jTable1.getValueAt(fila, 0).toString());

                    // Cargar datos de la tabla a los campos
                    vista.txtID.setText(vista.jTable1.getValueAt(fila, 0).toString());
                    vista.txtNOMBRE.setText(vista.jTable1.getValueAt(fila, 1).toString());
                    vista.txtareaDescrip.setText(vista.jTable1.getValueAt(fila, 2).toString());
                    vista.jSpCapacidad.setValue(Integer.parseInt(vista.jTable1.getValueAt(fila, 3).toString()));
                    vista.txtPrecioBase.setText(vista.jTable1.getValueAt(fila, 4).toString());
                }
            }
        });
    }
    // =========================================================================
    // MANEJO DE BOTONES
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e)
    {

        // =====================================================================
        // BOTÓN AGREGAR
        //======================================================================
        if (e.getSource() == vista.btAgregar)
        {

            Integer id = obtenerId();
            if (id == null) 
            {
                return;
            }
            // Crear objeto con datos de la pantalla
            TipoHabitacion tipHA = new TipoHabitacion();
            tipHA.setIdTipo(id);
            tipHA.setNombreTipo(vista.txtNOMBRE.getText());
            tipHA.setDescripcion(vista.txtareaDescrip.getText());
            tipHA.setCapacidad((int) vista.jSpCapacidad.getValue());
            tipHA.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));
            
            // Guardar en BD
            if (consultas.agregar(tipHA)) 
            {
                JOptionPane.showMessageDialog(null, "Agregado correctamente");
                limpiar();
                actualizarTabla();
            }
        }

        //======================================================================
        // BOTÓN EDITAR
        //======================================================================
        if (e.getSource() == vista.btEditar) 
        {

            if (idSeleccionado == 0) 
            {
                JOptionPane.showMessageDialog(null, "Seleccione un tipo de registro en la tabla");
                return;
            }
              // Crear objeto con los nuevos datos
            TipoHabitacion tipHA = new TipoHabitacion();
            tipHA.setIdTipo(idSeleccionado);
            tipHA.setNombreTipo(vista.txtNOMBRE.getText());
            tipHA.setDescripcion(vista.txtareaDescrip.getText());
            tipHA.setCapacidad((int) vista.jSpCapacidad.getValue());
            tipHA.setPrecioBase(Double.parseDouble(vista.txtPrecioBase.getText()));

            if (consultas.editar(tipHA)) 
            {
                JOptionPane.showMessageDialog(null, "Actualizado correctamente");
                limpiar();
                actualizarTabla();
            }
        }

        // =====================================================================
        // BOTÓN BUSCAR
        // =====================================================================
        if (e.getSource() == vista.btBuscar)
        {
            Integer id = obtenerId();
            if (id == null)
            {
                return;
            }
            ArrayList<Object[]> resultados = consultas.buscarPorId(id);
            DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
            modelo.setRowCount(0);// Limpiar tabla
            if (resultados.isEmpty()) 
            {
                JOptionPane.showMessageDialog(null, "No se encontró un tipo con ese ID.");
            } else
            {
                modelo.addRow(resultados.get(0));// Mostrar resultado
            }
        }
        //======================================================================
        // BOTÓN ELIMINAR
        // =====================================================================
        if (e.getSource() == vista.btEliminar)
        {
            if (idSeleccionado == 0)
            {
                JOptionPane.showMessageDialog(null, "Seleccione un tipo para eliminar");
                return;
            }
            if (consultas.eliminar(idSeleccionado)) 
            {
                JOptionPane.showMessageDialog(null, "Rol eliminado");
                limpiar();
                actualizarTabla();
            }
        }
        // =====================================================================
        // LIMPIAR
        // =====================================================================
        if (e.getSource() == vista.btLimpiar) 
        {
            limpiar();
            actualizarTabla();
        }
        // =====================================================================
        // BOTÓN SALIR
        // =====================================================================
        if (e.getSource() == vista.btSalir)
        {
            vista.dispose();// Cerrar ventana
        }
    }
    // =========================================================================
    // ACTUALIZAR TABLA COMPLETA
    // =========================================================================
    public void actualizarTabla() 
    {
        DefaultTableModel modelo = (DefaultTableModel) vista.jTable1.getModel();
        consultas.cargarTabla(modelo);
    }
    //==========================================================================
}
