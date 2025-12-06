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
 * AUTOR: GRUPO 3
 * PROYECTO
 * SEMANA 13 (Estilo PlanLealtadController)
 *****************************************************************************/
public class PaisController implements ActionListener 
{
    // Las variables de instancia se dejan en el orden lógico (Modelo, DAO, Vista)
    private final Pais modelo; // Modelo temporal para operaciones CRUD
    private final PaisDAO modeloDAO;
    private final GestionPaises vista;

    // **********************************************
    // CONSTRUCTOR CORREGIDO
    // **********************************************
    
    // El constructor acepta los argumentos en el ORDEN: (Pais, GestionPaises, PaisDAO)
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
        // Botón Limpiar usa un lambda que llama al método limpiar
        this.vista.getBtnLimpiar().addActionListener(e -> limpiarCampos()); 
    }

    // --- El método iniciar() es llamado desde GestionPaises.java ---
    /**
     * Configura el MouseListener para el autollenado y realiza la carga inicial de datos.
     */
    public void iniciar() {
        
        // =====================================================================
        // CORRECCIÓN PARA ASEGURAR EL MOUSELISTENER
        // Elimina MouseListeners previos para evitar que se ejecute la lógica varias veces
        for (java.awt.event.MouseListener ml : this.vista.getJtPais().getMouseListeners()) {
            // Se asegura de que solo elimina el MouseAdapter que añade este controlador
            if (ml.getClass().getName().contains("MouseAdapter")) { 
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
    
    /** * Implementa la lógica de limpiar campos en el Controller.
     */
    public void limpiarCampos() {
        vista.getTxtId().setText("");
        vista.getTxtCodigoISO().setText("");
        vista.getTxtNombre().setText("");
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
            
            // Los índices de la tabla son: 0: ID, 1: Código, 2: Nombre
            Object id = tableModel.getValueAt(fila, 0);
            Object codigo = tableModel.getValueAt(fila, 1);
            Object nombre = tableModel.getValueAt(fila, 2);
            
            // Rellena los campos de texto
            vista.getTxtId().setText(id != null ? id.toString() : "");
            vista.getTxtCodigoISO().setText(codigo != null ? codigo.toString() : "");
            vista.getTxtNombre().setText(nombre != null ? nombre.toString() : "");
        }
    }
    
    /**
     * Carga todos los países desde el DAO y actualiza la tabla.
     */
    public void actualizarTabla() {
        
        DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        // Asume que PaisDAO tiene el método obtenerTodos()
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
            
            try {
                // El ID se usa para verificar si existe. Si está vacío, se pone 0.
                if (!vista.getTxtId().getText().trim().isEmpty()) {
                    modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText()));
                } else {
                    modelo.setIdPais(0); 
                }
                
                String codigo = vista.getTxtCodigoISO().getText().trim();
                String nombre = vista.getTxtNombre().getText().trim();
                
                if (codigo.isEmpty() || nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "El Código y el Nombre son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                modelo.setCodigo(codigo);
                modelo.setNombre(nombre);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llama al método AGREGAR del DAO, pasando el objeto modelo
            if (modeloDAO.agregar(modelo)) {
                JOptionPane.showMessageDialog(vista, "País Agregado con éxito.");
                limpiarCampos();
                actualizarTabla(); 
            } else {
                JOptionPane.showMessageDialog(vista, "Error al agregar País. Asegúrese de que el ID/Código no existan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // --- 2. Botón EDITAR ---
        else if (e.getSource() == vista.getBtnEditar()) {
            
            if (vista.getTxtId().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe seleccionar un País o ingresar el ID a editar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                modelo.setIdPais(Integer.parseInt(vista.getTxtId().getText())); 
                modelo.setCodigo(vista.getTxtCodigoISO().getText().trim());
                modelo.setNombre(vista.getTxtNombre().getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llama al método EDITAR del DAO, pasando el objeto modelo
            if (modeloDAO.editar(modelo)) {
                JOptionPane.showMessageDialog(vista, "País Editado con éxito.");
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al editar País. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- 3. Botón ELIMINAR ---
        else if (e.getSource() == vista.getBtnEliminar()) {
            
            try {
                int id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setIdPais(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a eliminar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llama al método ELIMINAR del DAO, pasando el objeto modelo
            if (modeloDAO.eliminar(modelo)) {
                JOptionPane.showMessageDialog(vista, "País Eliminado con éxito.");
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar País. Verifique el ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // --- 4. Botón BUSCAR ---
        else if (e.getSource() == vista.getBtnBuscar()) {
            
            try {
                int id = Integer.parseInt(vista.getTxtId().getText());
                modelo.setIdPais(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID del país a buscar.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llama al método BUSCAR del DAO, pasando el objeto modelo
            Pais encontrado = modeloDAO.buscar(modelo);

            if (encontrado != null) {
                // Rellena los campos con el resultado
                vista.getTxtId().setText(String.valueOf(encontrado.getIdPais()));
                vista.getTxtCodigoISO().setText(encontrado.getCodigo());
                vista.getTxtNombre().setText(encontrado.getNombre());
                JOptionPane.showMessageDialog(vista, "País encontrado.");
            } else {
                JOptionPane.showMessageDialog(vista, "País no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                vista.getTxtCodigoISO().setText("");
                vista.getTxtNombre().setText("");
            }
        }
    }
}