package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmpleadoTableView extends JFrame {
    private JTable empleadoTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton loadAllButton;
    private JButton clearButton;
    private JLabel statusLabel;
    private JLabel countLabel;
    
    // Colores modernos
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul profesional
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);    // Azul claro
    private final Color ACCENT_COLOR = new Color(46, 204, 113);       // Verde
    private final Color DANGER_COLOR = new Color(231, 76, 60);        // Rojo
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Gris claro
    private final Color HEADER_COLOR = new Color(44, 62, 80);         // Gris oscuro
    private final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);   // Gris tabla
    private final Color TABLE_ROW_EVEN = Color.WHITE;
    private final Color TABLE_ROW_ODD = new Color(245, 247, 250);

    public EmpleadoTableView() {
        setTitle("Sistema de Gestión de Empleados - SupermercadoMVC");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout(0, 0));
        
        // Panel principal con color de fondo
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // ===== HEADER SUPERIOR =====
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ===== PANEL DE BÚSQUEDA =====
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        
        // ===== TABLA =====
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Añadir action listeners a los botones
        searchButton.addActionListener(e -> loadEmpleadoByCargo());
        loadAllButton.addActionListener(e -> loadEmpleados());
        clearButton.addActionListener(e -> clearTable());
        
        // Búsqueda al presionar Enter
        searchField.addActionListener(e -> loadEmpleadoByCargo());
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Título ajustado
        JLabel titleLabel = new JLabel("SupermercadoMVC");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestión de Empleados");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(189, 195, 199));
        
        JPanel titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.Y_AXIS));
        titlesPanel.setBackground(HEADER_COLOR);
        titlesPanel.add(titleLabel);
        titlesPanel.add(Box.createVerticalStrut(3));
        titlesPanel.add(subtitleLabel);
        
        headerPanel.add(titlesPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchContainer = new JPanel();
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.Y_AXIS));
        searchContainer.setBackground(BACKGROUND_COLOR);
        searchContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel searchLabel = new JLabel("🔍 Buscar por Cargo:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(HEADER_COLOR);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        searchButton = createStyledButton("🔎 Buscar", PRIMARY_COLOR);
        loadAllButton = createStyledButton("📋 Cargar Todos", ACCENT_COLOR);
        clearButton = createStyledButton("🗑️ Limpiar", DANGER_COLOR);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(loadAllButton);
        searchPanel.add(clearButton);
        
        searchContainer.add(searchPanel);
        
        return searchContainer;
    }
    
    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.setBackground(BACKGROUND_COLOR);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        
        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer tabla no editable
            }
        };
        tableModel.setColumnIdentifiers(new Object[]{"ID", "Nombre Completo", "Cargo"});
        
        empleadoTable = new JTable(tableModel);
        empleadoTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        empleadoTable.setRowHeight(35);
        empleadoTable.setShowVerticalLines(false);
        empleadoTable.setGridColor(new Color(220, 220, 220));
        empleadoTable.setSelectionBackground(SECONDARY_COLOR);
        empleadoTable.setSelectionForeground(Color.WHITE);
        
        // Estilo del header de la tabla
        JTableHeader tableHeader = empleadoTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setBackground(TABLE_HEADER_COLOR);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer)tableHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Renderizador para alternar colores de filas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                }
                if (column == 0) {
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                return c;
            }
        };
        
        for (int i = 0; i < empleadoTable.getColumnCount(); i++) {
            empleadoTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Ajustar ancho de columnas
        empleadoTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        empleadoTable.getColumnModel().getColumn(0).setMaxWidth(80);
        empleadoTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        empleadoTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(empleadoTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.setPreferredSize(new Dimension(0, 350));
        
        // Panel de estado
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        statusLabel = new JLabel("💡 Haz clic en 'Cargar Todos' para ver los empleados");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(127, 140, 141));
        
        countLabel = new JLabel("Registros: 0");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        countLabel.setForeground(PRIMARY_COLOR);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(countLabel, BorderLayout.EAST);
        
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(statusPanel, BorderLayout.SOUTH);
        
        return tableContainer;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 38));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void updateStatus(String message, int count) {
        statusLabel.setText(message);
        countLabel.setText("Registros: " + count);
    }

    private void loadEmpleados() {
        String url = "jdbc:mysql://localhost:3306/supermercado_db";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM empleados ORDER BY nombre";
            ResultSet resultSet = statement.executeQuery(query);

            // Limpiar el modelo de la tabla
            tableModel.setRowCount(0);
            int count = 0;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String cargo = resultSet.getString("cargo");

                // Añadir los datos a la tabla
                tableModel.addRow(new Object[]{id, nombre, cargo});
                count++;
            }

            updateStatus("✅ Todos los empleados cargados exitosamente", count);
            
            if (count == 0) {
                updateStatus("⚠️ No hay empleados registrados en la base de datos", 0);
            }

        } catch (SQLException e) {
            updateStatus("❌ Error al conectar con la base de datos: " + e.getMessage(), 0);
            JOptionPane.showMessageDialog(this, 
                "No se pudo conectar a la base de datos.\n" +
                "Asegúrate de que MySQL esté ejecutándose.\n\nError: " + e.getMessage(),
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmpleadoByCargo() {
        String url = "jdbc:mysql://localhost:3306/supermercado_db";
        String user = "root";
        String password = "root";
        String cargo = searchField.getText().trim();
        
        if (cargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingresa un cargo para buscar", 
                "Campo Vacío", 
                JOptionPane.WARNING_MESSAGE);
            searchField.requestFocus();
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "SELECT * FROM empleados WHERE cargo LIKE ? ORDER BY nombre")) {

            preparedStatement.setString(1, "%" + cargo + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Limpiar el modelo de la tabla
            tableModel.setRowCount(0);
            int count = 0;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String cargoResult = resultSet.getString("cargo");

                // Añadir los datos a la tabla
                tableModel.addRow(new Object[]{id, nombre, cargoResult});
                count++;
            }

            if (count == 0) {
                updateStatus("⚠️ No se encontraron empleados con el cargo: \"" + cargo + "\"", 0);
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron empleados con el cargo:\n\"" + cargo + "\"\n\n" +
                    "Intenta buscar por palabras parciales (ej: 'Caj' para Cajero)",
                    "Sin Resultados", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateStatus("🔍 Búsqueda completada para: \"" + cargo + "\"", count);
            }

        } catch (SQLException e) {
            updateStatus("❌ Error en la búsqueda: " + e.getMessage(), 0);
            JOptionPane.showMessageDialog(this, 
                "Error al realizar la búsqueda.\n\nError: " + e.getMessage(),
                "Error de Búsqueda", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
        searchField.setText("");
        updateStatus("🗑️ Tabla limpiada. Listo para una nueva búsqueda", 0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmpleadoTableView view = new EmpleadoTableView();
            view.setVisible(true);
        });
    }
}
