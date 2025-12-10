//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.PaisDAO;
import goHotel.model.Pais;
import goHotel.view.GestionPaises;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// PAIS CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Gestión Pais.
 */ 
public class PaisController implements ActionListener 
{

    private final Pais modelo;
    private final GestionPaises vista;
    private final PaisDAO modeloDAO;
    /**
     * Constructor del controlador.
     * @param modelo El objeto País temporal para las operaciones.
     * @param vista La ventana de Gestión de Países.
     * @param modeloDAO El objeto DAO para el acceso a datos.
     */
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public PaisController(Pais modelo, GestionPaises vista, PaisDAO modeloDAO)
    {
        this.modelo = modelo;
        this.vista = vista;
        this.modeloDAO = modeloDAO;
        // 1. Asociar eventos de la Vista al Controller
        this.vista.getBtnAgregar().addActionListener(this);
        this.vista.getBtnEditar().addActionListener(this);
        this.vista.getBtnEliminar().addActionListener(this);
        this.vista.getBtnBuscar().addActionListener(this);
        this.vista.getBtnSalir().addActionListener(this);
        // Usar expresión lambda para el botón Limpiar
        this.vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());
    }
    /**
     * Configura el MouseListener para el autollenado y realiza la carga inicial de datos.
     */
    //==========================================================================
    //INICIAR
    //==========================================================================
    public void iniciar() 
    {
        
        // =====================================================================
        // ASEGURA que el MouseListener se configura SOLO UNA VEZ
        // (Lógica heredada y mantenida para evitar duplicados de listeners)
        //======================================================================
        for (java.awt.event.MouseListener ml : this.vista.getJtPais().getMouseListeners()) 
        {
            if (ml instanceof MouseAdapter) 
            { 
                 this.vista.getJtPais().removeMouseListener(ml);
            }
        }
        // AGREGAR MouseListener a la tabla para autocompletar campos
        this.vista.getJtPais().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                seleccionarFilaParaEdicion();
            }
        });
        // =====================================================================
        this.vista.setTitle("Gestión de Países");
        this.vista.setLocationRelativeTo(null);
        actualizarTabla(); 
    }
    // =========================================================================
    // MÉTODOS AUXILIARES
    // =========================================================================
    
        /** * Implementa la lógica de limpiar campos en el Controller
         * y refresca la tabla.
         */
    //==========================================================================
    // LIMPIAR CAMPOS
    //==========================================================================
    public void limpiarCampos() 
    {
        vista.getTxtId().setText("");
        vista.getTxtCodigoISO().setText("");
        vista.getTxtNombre().setText("");
        actualizarTabla(); // Refresca la tabla
    }
    //==========================================================================
    // SALIR
    //==========================================================================
    private void salir() 
    {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea cerrar la ventana de Gestión de Países?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) 
        {
            vista.dispose();
        }
    }
    //==========================================================================
    // SELECCIONAR FILAS PARA EDICION
    //==========================================================================
    /**
     * Obtiene los datos de la fila seleccionada y rellena los campos.
     */
    private void seleccionarFilaParaEdicion() 
    {
        int fila = vista.getJtPais().getSelectedRow();
        
        if (fila >= 0) 
        {
            DefaultTableModel tableModel = (DefaultTableModel) vista.getJtPais().getModel();
            // Se asume que los datos están en el orden: ID, Código, Nombre
            String id = tableModel.getValueAt(fila, 0) != null ? tableModel.getValueAt(fila, 0).toString() : "";
            String codigo = tableModel.getValueAt(fila, 1) != null ? tableModel.getValueAt(fila, 1).toString() : "";
            String nombre = tableModel.getValueAt(fila, 2) != null ? tableModel.getValueAt(fila, 2).toString() : "";
            vista.getTxtId().setText(id);
            vista.getTxtCodigoISO().setText(codigo);
            vista.getTxtNombre().setText(nombre);
        }
    }
    //==========================================================================
    // ACTUALIZAR TABLA
    //==========================================================================
    /**
     * Carga todos los países desde el DAO y actualiza la tabla de la vista.
     */
    public void actualizarTabla() 
    {
        
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre"}, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;  
            }
        };
        List<Pais> paises = modeloDAO.obtenerTodos();
        for (Pais p : paises)
        {
            tableModel.addRow(new Object[]
            {
                p.getIdPais(),
                p.getCodigo(),
                p.getNombre()
            });
        }
        this.vista.getJtPais().setModel(tableModel);
    }
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
        //======================================================================
        //BTN SALIR
        //======================================================================
        if (e.getSource() == vista.getBtnSalir()) 
        {
            salir();
            return;
        }   
        //======================================================================
        //BTN AGREGAR
        //======================================================================
        if (e.getSource() == vista.getBtnAgregar()) 
        {
            String codigo = vista.getTxtCodigoISO().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            if (codigo.isEmpty() || nombre.isEmpty()) 
            {
                JOptionPane.showMessageDialog(vista, "El Código y el Nombre son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try 
            {
                // Si el campo ID está vacío, se asume que el DAO autogenerará el ID.
                if (!vista.getTxtId().getText().trim().isEmpty()) {
                    modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText()));
                } else
                {
                    modelo.setIdPais(0); // O un valor que el DAO reconozca como "nuevo registro"
                }
                modelo.setCodigo(codigo);
                modelo.setNombre(nombre);
            } catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido (o déjelo vacío para autogenerar).", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (modeloDAO.registrar(modelo)) 
            { 
                JOptionPane.showMessageDialog(vista, "País Agregado con éxito.");
                limpiarCampos(); 
            }
        }
        //======================================================================
        //BTN EDITAR
        //======================================================================
        else if (e.getSource() == vista.getBtnEditar()) 
        {
            
            if (vista.getTxtId().getText().isEmpty()) 
            {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un País o ingresar el ID a editar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try 
            {
                String codigo = vista.getTxtCodigoISO().getText().trim();
                String nombre = vista.getTxtNombre().getText().trim();
                
                if (codigo.isEmpty() || nombre.isEmpty())
                {
                    JOptionPane.showMessageDialog(vista, "El Código y el Nombre son obligatorios para editar.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText())); 
                modelo.setCodigo(codigo);
                modelo.setNombre(nombre);
                
            } catch (NumberFormatException ex) 
            
            {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.editar(modelo)) 
            {
                JOptionPane.showMessageDialog(vista, "País Editado con éxito.");
                limpiarCampos(); 
            }
        }
            
        //======================================================================
        //BTN ELIMINAR
        //======================================================================
        else if (e.getSource() == vista.getBtnEliminar()) 
        {
            
            int id;
            try 
            {
                id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setIdPais(id);
            } catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a eliminar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                                     "ADVERTENCIA: ¿Está seguro que desea eliminar el País con ID: " + id + "?", 
                                     "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION)
            {
                if (modeloDAO.eliminar(modelo))
                {
                    JOptionPane.showMessageDialog(vista, "País Eliminado con éxito.");
                    limpiarCampos(); 
                }
            }
        }
            
        //======================================================================
        //BTN BUSCAR
        //======================================================================
       else if (e.getSource() == vista.getBtnBuscar())
{
    int id;
    try {
        id = Integer.parseInt(vista.getTxtId().getText());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a buscar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    Pais encontrado = modeloDAO.buscarPorId(id);
    
    if (encontrado != null) 
    {
        // Llenar los campos de texto
        vista.getTxtId().setText(String.valueOf(encontrado.getIdPais()));
        vista.getTxtCodigoISO().setText(encontrado.getCodigo());
        vista.getTxtNombre().setText(encontrado.getNombre());
        
        // Filtrar la tabla para mostrar solo el resultado
        actualizarTablaFiltrada(encontrado);
        
        JOptionPane.showMessageDialog(vista, "País encontrado: " + encontrado.getNombre());
    } 
    else 
    {
        JOptionPane.showMessageDialog(vista, "País no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        vista.getTxtCodigoISO().setText("");
        vista.getTxtNombre().setText("");
    }
               }
    }
    
 //==========================================================================
// ACTUALIZAR TABLA FILTRADA (mostrar solo un país)
//==========================================================================
/**
 * Muestra solo el país encontrado en la tabla.
 * @param pais El país a mostrar en la tabla.
 */
private void actualizarTablaFiltrada(Pais pais) 
{
    DefaultTableModel tableModel = new DefaultTableModel(
        new Object[]{"ID", "Código", "Nombre"}, 0) 
    {
        @Override
        public boolean isCellEditable(int row, int column) 
        {
            return false;  
        }
    };
    
    // Agregar solo el país encontrado
    tableModel.addRow(new Object[]
    {
        pais.getIdPais(),
        pais.getCodigo(),
        pais.getNombre()
    });
    
    this.vista.getJtPais().setModel(tableModel);
}
    //==========================================================================
}