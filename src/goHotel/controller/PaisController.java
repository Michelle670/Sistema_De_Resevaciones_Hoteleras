package goHotel.controller;

import goHotel.model.DAO.PaisDAO;
import goHotel.model.Pais;
import goHotel.view.GestionPaises;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PaisController implements ActionListener {

    private Pais modelo;
    private GestionPaises view;
    private PaisDAO dao;

    public PaisController(Pais modelo, GestionPaises view) {
        this.modelo = modelo;
        this.view = view;
        this.dao = new PaisDAO();

        this.view.getBtnAgregar().addActionListener(this);
        this.view.getBtnEditar().addActionListener(this);
        this.view.getBtnEliminar().addActionListener(this);
        this.view.getBtnBuscar().addActionListener(this);
        this.view.getBtnSalir().addActionListener(this);

        cargarTabla();
    }

    public void limpiarCampos() {
        view.getTxtId().setText(null);
        view.getTxtCodigoISO().setText(null);
        view.getTxtNombre().setText(null);
    }

    public void cargarTabla() {
        if (view.getJtPais() == null || view.getJtPais().getModel() == null) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getJtPais().getModel();
        model.setRowCount(0);

        try {
            List<Pais> lista = dao.listarPaises(); 

            for (Pais p : lista) {
                Object[] fila = new Object[3];
                fila[0] = p.getIdPais();
                fila[1] = p.getCodigo();
                fila[2] = p.getNombre();
                model.addRow(fila);
            }
            view.getJtPais().setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al cargar datos de países: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // --- 1. AGREGAR PAÍS ---
        if (e.getSource() == view.getBtnAgregar()) {
            if (view.getTxtId().getText().trim().isEmpty()
                    || view.getTxtCodigoISO().getText().trim().isEmpty()
                    || view.getTxtNombre().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios para agregar.");
                return;
            }

            try {
                int id = Integer.parseInt(view.getTxtId().getText().trim());
                modelo.setIdPais(id);
                modelo.setCodigo(view.getTxtCodigoISO().getText().trim());
                modelo.setNombre(view.getTxtNombre().getText().trim());

                if (dao.agregarPais(modelo)) {
                    JOptionPane.showMessageDialog(view, "País agregado con éxito.");
                    limpiarCampos();
                    cargarTabla();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Error: El ID debe ser un número entero válido.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Error al guardar en la BD: " + ex.getMessage());
            }

            // --- 2. EDITAR/MODIFICAR PAÍS ---
        } else if (e.getSource() == view.getBtnEditar()) {
            if (view.getTxtId().getText().trim().isEmpty()
                    || view.getTxtCodigoISO().getText().trim().isEmpty()
                    || view.getTxtNombre().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe ingresar el ID, Código ISO y Nombre para modificar.");
                return;
            }

            try {
                int id = Integer.parseInt(view.getTxtId().getText().trim());
                modelo.setIdPais(id);
                modelo.setCodigo(view.getTxtCodigoISO().getText().trim());
                modelo.setNombre(view.getTxtNombre().getText().trim());

                if (dao.modificarPais(modelo)) {
                    JOptionPane.showMessageDialog(view, "País modificado con éxito.");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(view, "No se encontró el país con ese ID para modificar.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Error: El ID debe ser un número entero válido.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Error al modificar en la BD: " + ex.getMessage());
            }

            // --- 3. ELIMINAR PAÍS ---
        } else if (e.getSource() == view.getBtnEliminar()) {
            if (view.getTxtId().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Debe ingresar el ID del país a eliminar.");
                return;
            }

            Object[] options = {"Sí", "No"};

            int confirmacion = JOptionPane.showOptionDialog(
                    view,
                    "¿Está seguro de eliminar el país con ID: " + view.getTxtId().getText() + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION, // Tipo de diálogo (define la cantidad de botones)
                    JOptionPane.QUESTION_MESSAGE, // Tipo de ícono (el signo de interrogación)
                    null, // No usamos ícono personalizado
                    options, // Nuestro array de strings {"Sí", "No"}
                    options[1] // Opción por defecto ("No")
            );

            if (confirmacion == 0) {
                try {
                    int idAEliminar = Integer.parseInt(view.getTxtId().getText().trim());

                    if (dao.eliminarPais(idAEliminar)) {
                        JOptionPane.showMessageDialog(view, "País eliminado con éxito.");
                        limpiarCampos();
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(view, "No se encontró el país con ese ID para eliminar.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Error: El ID debe ser un número entero válido.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error al eliminar en la BD: " + ex.getMessage());
                }
            }

            // --- 4. BUSCAR PAÍS ---
        } else if (e.getSource() == view.getBtnBuscar()) {
            String criterio = JOptionPane.showInputDialog(view, "Ingrese el ID del país a buscar:");

            if (criterio != null && !criterio.trim().isEmpty()) {
                try {
                    // Solo se busca por ID (número) en este controlador
                    int idBusqueda = Integer.parseInt(criterio.trim());

                    Pais p = dao.buscarPaisPorId(idBusqueda);

                    if (p != null) {
                        view.getTxtId().setText(String.valueOf(p.getIdPais()));
                        view.getTxtCodigoISO().setText(p.getCodigo());
                        view.getTxtNombre().setText(p.getNombre());
                        JOptionPane.showMessageDialog(view, "País encontrado y cargado en los campos.");
                    } else {
                        JOptionPane.showMessageDialog(view, "País no encontrado con el ID: " + idBusqueda);
                        limpiarCampos();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Error: El criterio de búsqueda debe ser el ID (número entero).");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error de base de datos al buscar: " + ex.getMessage());
                }
            }

            // --- 5. SALIR ---
        } else if (e.getSource() == view.getBtnSalir()) {
            view.dispose();
        }
    }
}
