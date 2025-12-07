package goHotel.controller;

import goHotel.model.DAO.PlanLealtadDAO;
import goHotel.model.PlanLealtad;
import goHotel.view.GestionPlanLealtad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 * Controlador para la gestión de Planes de Lealtad (CRUD).
 */
public class PlanLealtadController implements ActionListener {

    private final GestionPlanLealtad vista;
    private final PlanLealtadDAO modeloDAO;
    private final PlanLealtad modelo;

    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public PlanLealtadController(GestionPlanLealtad vista, PlanLealtadDAO modeloDAO, PlanLealtad modelo) {
        this.vista = vista;
        this.modeloDAO = modeloDAO;
        this.modelo = modelo;

        // ESTA LÍNEA DEBE EJECUTARSE SOLO UNA VEZ POR INSTANCIA DE VISTA.
        this.vista.setPlanLealtadController(this);

        // Asignar MouseListener para cargar campos al hacer clic en la tabla
        this.vista.getJtPlanLealtad().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarCamposDesdeTabla();
            }
        });

        cargarTabla();
    }

// -----------------------------------------------------------------------------
// MÉTODOS DE TABLA Y LECTURA
// -----------------------------------------------------------------------------

    /**
     * Carga todos los planes de lealtad en la JTable de la vista.
     */
    public void cargarTabla() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nivel", "Descuento (%)", "Factor Puntos (x)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<PlanLealtad> planes = modeloDAO.obtenerTodos();

        for (PlanLealtad plan : planes) {
            Double descuento = plan.getDescuento();
            Double factorPuntos = plan.getFactorPuntos();

            String descuentoStr = (descuento == null) ? "0.00" : String.format("%.2f", descuento);
            String factorPuntosStr = (factorPuntos == null) ? "0.00" : String.format("%.2f", factorPuntos);

            model.addRow(new Object[]{
                plan.getId(),
                plan.getNivel(),
                descuentoStr,
                factorPuntosStr
            });
        }
        vista.getJtPlanLealtad().setModel(model);
    }

    /**
     * Carga los datos de la fila seleccionada en los campos de texto.
     */
    public void cargarCamposDesdeTabla() {
        JTable tabla = vista.getJtPlanLealtad();
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            try {
                Object id = tabla.getValueAt(fila, 0);
                Object nivel = tabla.getValueAt(fila, 1);
                Object descuento = tabla.getValueAt(fila, 2);
                Object factorPuntos = tabla.getValueAt(fila, 3);

                vista.getTxtId().setText(String.valueOf(id));
                vista.getTxtNivel().setText(String.valueOf(nivel));
                // Usamos replace para manejar el punto decimal en la conversión
                vista.getTxtDescuento().setText(String.valueOf(descuento).replace(",", "."));
                vista.getTxtFactorPuntos().setText(String.valueOf(factorPuntos).replace(",", "."));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al leer datos de la fila seleccionada: " + ex.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lee los datos de los campos de texto y los asigna al objeto modelo,
     * realizando validaciones según la operación.
     * @param incluirId Si es verdadero, intenta parsear el ID y valida que no esté vacío.
     * @param validarCamposAdicionales Si es verdadero, valida que Nivel, Descuento y Factor Puntos no estén vacíos.
     * @return true si la lectura y conversión es exitosa, false en caso contrario.
     */
    private boolean leerCampos(boolean incluirId, boolean validarCamposAdicionales) {

        String idStr = vista.getTxtId().getText().trim();
        String nivel = vista.getTxtNivel().getText().trim();
        String descuentoStr = vista.getTxtDescuento().getText().trim();
        String factorPuntosStr = vista.getTxtFactorPuntos().getText().trim();

        // ** CAMBIO CLAVE: Combinar la validación de ID y campos adicionales. **
        if (validarCamposAdicionales) {
            boolean faltaID = incluirId && idStr.isEmpty();
            boolean faltaNivel = nivel.isEmpty();
            boolean faltaDescuento = descuentoStr.isEmpty();
            boolean faltaFactorPuntos = factorPuntosStr.isEmpty();

            if (faltaID || faltaNivel || faltaDescuento || faltaFactorPuntos) {
                JOptionPane.showMessageDialog(vista, "Es obligatorio llenar todos los campos: ID, Nivel, Descuento y Factor Puntos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else if (incluirId && idStr.isEmpty()) {
            // Esta validación solo se ejecuta si NO es una operación de Agregar/Editar
            // donde solo se necesita el ID (como en Buscar o Eliminar por ID).
            JOptionPane.showMessageDialog(vista, "El campo ID es obligatorio para esta operación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            // Asignación de ID
            if (incluirId) {
                int id = Integer.parseInt(idStr);
                modelo.setId(id);
            }

            // Asignación y Conversión de Formato de campos adicionales
            modelo.setNivel(nivel);

            if (!descuentoStr.isEmpty()) {
                modelo.setDescuento(Double.parseDouble(descuentoStr));
            } else if (!validarCamposAdicionales) {
                // Esto es solo para operaciones que no requieren el campo, no aplica para Agregar/Editar.
                modelo.setDescuento(null); 
            }

            if (!factorPuntosStr.isEmpty()) {
                modelo.setFactorPuntos(Double.parseDouble(factorPuntosStr));
            } else if (!validarCamposAdicionales) {
                // Esto es solo para operaciones que no requieren el campo, no aplica para Agregar/Editar.
                modelo.setFactorPuntos(null); 
            }

            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Error: Verifique que los campos numéricos (ID, Descuento, Factor Puntos) sean válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

// -----------------------------------------------------------------------------
// MANEJO DE EVENTOS (actionPerformed)
// -----------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        // --- 1. Botón AGREGAR ---
        // Al pasar (true, true), el método leerCampos ahora valida que todos los campos estén llenos
        if (comando.equals("Agregar")) {
            if (leerCampos(true, true)) { 
                if (modeloDAO.agregar(modelo)) {
                    JOptionPane.showMessageDialog(vista, "Plan de Lealtad ID " + modelo.getId() + " agregado exitosamente.");
                    vista.limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error: No se pudo agregar el Plan de Lealtad. (Verifique ID duplicado o error de BD)", "Error de BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- 2. Botón EDITAR ---
        // Al pasar (true, true), el método leerCampos ahora valida que todos los campos estén llenos
        else if (comando.equals("Editar")) {
            if (leerCampos(true, true)) {
                if (modeloDAO.editar(modelo)) {
                    JOptionPane.showMessageDialog(vista, "Plan de Lealtad modificado exitosamente.");
                    vista.limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error: No se pudo modificar el Plan de Lealtad. (ID no encontrado)", "Error de BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- 3. Botón BUSCAR (Solo por ID) ---
        else if (comando.equals("Buscar")) {

            String idStr = vista.getTxtId().getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idStr);

                // 1. Cargamos el ID en el objeto modelo
                modelo.setId(id);
                // 2. Limpiamos los otros campos del modelo para que el DAO solo use el ID
                modelo.setNivel(null);
                modelo.setDescuento(null);
                modelo.setFactorPuntos(null);

                // 3. Llamamos al método DAO genérico (modeloDAO.buscar(modelo))
                PlanLealtad encontrado = modeloDAO.buscar(modelo);

                if (encontrado != null) {
                    // Rellenar las casillas con los datos encontrados
                    vista.getTxtId().setText(String.valueOf(encontrado.getId()));
                    vista.getTxtNivel().setText(encontrado.getNivel());
                    vista.getTxtDescuento().setText(String.format("%.2f", encontrado.getDescuento()));
                    vista.getTxtFactorPuntos().setText(String.format("%.2f", encontrado.getFactorPuntos()));
                    // El mensaje se muestra SOLO AQUÍ (una vez por búsqueda exitosa)
                    JOptionPane.showMessageDialog(vista, "Plan de Lealtad ID " + encontrado.getId() + " encontrado.");
                } else {
                    // El mensaje se muestra SOLO AQUÍ (una vez por búsqueda fallida)
                    JOptionPane.showMessageDialog(vista, "Plan de Lealtad con ID " + id + " no encontrado.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
                    // Limpiar solo los campos que no son el ID
                    vista.getTxtNivel().setText("");
                    vista.getTxtDescuento().setText("");
                    vista.getTxtFactorPuntos().setText("");
                }
            } catch (NumberFormatException ex) {
                // El mensaje se muestra SOLO AQUÍ (una vez por error de formato)
                JOptionPane.showMessageDialog(vista, "El ID debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- 4. Botón ELIMINAR (Por ID o por Nivel) ---
        else if (comando.equals("Eliminar")) {
            String idStr = vista.getTxtId().getText().trim();
            String nivel = vista.getTxtNivel().getText().trim();

            if (idStr.isEmpty() && nivel.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar el ID o el Nivel para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = 0;
            boolean eliminarPorId = false;

            // Preparamos el modelo
            modelo.setId(0); // Limpiamos ID por defecto
            modelo.setNivel(null); // Limpiamos Nivel por defecto

            if (!idStr.isEmpty()) {
                try {
                    id = Integer.parseInt(idStr);
                    modelo.setId(id);
                    eliminarPorId = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vista, "Si ingresa el ID, debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (!nivel.isEmpty()) {
                modelo.setNivel(nivel);
                eliminarPorId = false;
            }

            // Confirmación
            String target = eliminarPorId ? ("ID " + id) : ("Nivel: " + nivel);
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro que desea eliminar el plan con " + target + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (modeloDAO.eliminar(modelo)) {
                    JOptionPane.showMessageDialog(vista, "Plan de Lealtad eliminado exitosamente.");
                    vista.limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error: No se pudo eliminar el Plan de Lealtad. (Criterio no encontrado o error de BD)", "Error de BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // --- Botones Auxiliares ---
        else if (comando.equals("Limpiar")) {
            vista.limpiarCampos();
            cargarTabla();
        } else if (comando.equals("Salir")) {
            vista.dispose();
        }
    }
}