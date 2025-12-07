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

/**
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 13 (Estilo PlanLealtadController)
 *
 * Controlador para la gestión de la entidad Pais.
 */
//*****************************************************************************
public class PaisController implements ActionListener {

    private final Pais modelo;
    private final GestionPaises vista;
    private final PaisDAO modeloDAO;

    // **********************************************
    // CONSTRUCTOR
    // **********************************************

    /**
     * Constructor del controlador.
     * @param modelo El objeto País temporal para las operaciones.
     * @param vista La ventana de Gestión de Países.
     * @param modeloDAO El objeto DAO para el acceso a datos.
     */
    public PaisController(Pais modelo, GestionPaises vista, PaisDAO modeloDAO) {
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

    // **********************************************
    // INICIO DEL CONTROLADOR
    // **********************************************

    /**
     * Configura el MouseListener para el autollenado y realiza la carga inicial de datos.
     */
    public void iniciar() {
        
        // =====================================================================
        // ASEGURA que el MouseListener se configura SOLO UNA VEZ
        // (Lógica heredada y mantenida para evitar duplicados de listeners)
        for (java.awt.event.MouseListener ml : this.vista.getJtPais().getMouseListeners()) {
            if (ml instanceof MouseAdapter) { 
                 this.vista.getJtPais().removeMouseListener(ml);
            }
        }
        
        // AGREGAR MouseListener a la tabla para autocompletar campos
        this.vista.getJtPais().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaParaEdicion();
            }
        });
        // =====================================================================
        
        this.vista.setTitle("Gestión de Países");
        this.vista.setLocationRelativeTo(null);
        actualizarTabla(); 
    }
    
    // **********************************************
    // MÉTODOS AUXILIARES
    // **********************************************
    
    /** * Implementa la lógica de limpiar campos en el Controller
     * y refresca la tabla.
     */
    public void limpiarCampos() {
        vista.getTxtId().setText("");
        vista.getTxtCodigoISO().setText("");
        vista.getTxtNombre().setText("");
        
        actualizarTabla(); // Refresca la tabla
    }
    
    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea cerrar la ventana de Gestión de Países?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.dispose();
        }
    }

    /**
     * Obtiene los datos de la fila seleccionada y rellena los campos.
     */
    private void seleccionarFilaParaEdicion() {
        int fila = vista.getJtPais().getSelectedRow();
        
        if (fila >= 0) {
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
    
    /**
     * Carga todos los países desde el DAO y actualiza la tabla de la vista.
     */
    public void actualizarTabla() {
        
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  
            }
        };
        
        List<Pais> paises = modeloDAO.obtenerTodos(); 

        for (Pais p : paises) {
            tableModel.addRow(new Object[]{
                p.getIdPais(),
                p.getCodigo(),
                p.getNombre()
            });
        }
        
        this.vista.getJtPais().setModel(tableModel);
    }
    
    // **********************************************
    // MANEJADOR DE EVENTOS (ActionListener)
    // **********************************************

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // --- Botón SALIR ---
        if (e.getSource() == vista.getBtnSalir()) {
            salir();
            return;
        }

        // --- 1. Botón AGREGAR ---
        if (e.getSource() == vista.getBtnAgregar()) {
            
            String codigo = vista.getTxtCodigoISO().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            
            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El Código y el Nombre son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Si el campo ID está vacío, se asume que el DAO autogenerará el ID.
                if (!vista.getTxtId().getText().trim().isEmpty()) {
                    modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText()));
                } else {
                    modelo.setIdPais(0); // O un valor que el DAO reconozca como "nuevo registro"
                }
                
                modelo.setCodigo(codigo);
                modelo.setNombre(nombre);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido (o déjelo vacío para autogenerar).", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.registrar(modelo)) { 
                JOptionPane.showMessageDialog(vista, "País Agregado con éxito.");
                limpiarCampos(); 
            }
        }
        
        // --- 2. Botón EDITAR ---
        else if (e.getSource() == vista.getBtnEditar()) {
            
            if (vista.getTxtId().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un País o ingresar el ID a editar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                String codigo = vista.getTxtCodigoISO().getText().trim();
                String nombre = vista.getTxtNombre().getText().trim();
                
                if (codigo.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "El Código y el Nombre son obligatorios para editar.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText())); 
                modelo.setCodigo(codigo);
                modelo.setNombre(nombre);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modeloDAO.editar(modelo)) {
                JOptionPane.showMessageDialog(vista, "País Editado con éxito.");
                limpiarCampos(); 
            }
        }

        // --- 3. Botón ELIMINAR ---
        else if (e.getSource() == vista.getBtnEliminar()) {
            
            int id;
            try {
                id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setIdPais(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a eliminar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                                     "ADVERTENCIA: ¿Está seguro que desea eliminar el País con ID: " + id + "?", 
                                     "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modeloDAO.eliminar(modelo)) {
                    JOptionPane.showMessageDialog(vista, "País Eliminado con éxito.");
                    limpiarCampos(); 
                }
            }
        }
        
        // --- 4. Botón BUSCAR ---
        else if (e.getSource() == vista.getBtnBuscar()) {
            
            int id;
            try {
                id = Integer.parseInt(vista.getTxtId().getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a buscar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pais encontrado = modeloDAO.buscarPorId(id);

            if (encontrado != null) {
                vista.getTxtId().setText(String.valueOf(encontrado.getIdPais()));
                vista.getTxtCodigoISO().setText(encontrado.getCodigo());
                vista.getTxtNombre().setText(encontrado.getNombre());
                JOptionPane.showMessageDialog(vista, "País encontrado: " + encontrado.getNombre());
                
                // OPCIONAL: Podrías querer filtrar la tabla para mostrar solo el resultado
                // Para esto necesitarías un método de búsqueda en el DAO que devuelva una lista de 1 elemento,
                // o actualizar el método actualizarTabla() para aceptar un filtro de ID.
                
            } else {
                JOptionPane.showMessageDialog(vista, "País no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                vista.getTxtCodigoISO().setText("");
                vista.getTxtNombre().setText("");
            }
        }
    }
}