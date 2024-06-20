package com.ijala.view.stock;

import com.ijala.controller.MovementController;
import com.ijala.model.movement.MovementDAO;
import com.ijala.model.product.Product;
import com.ijala.model.product.ProductDAO;
import com.ijala.model.stock.Stock;
import com.ijala.model.stock.StockDAO;
import com.ijala.util.ButtonUtil;
import com.ijala.util.panel.TablePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class StockManageFrame extends JFrame {
    private MovementController movementController;
    private TablePanel tablePanel;
    private StockDAO stockDAO;
    private MovementDAO movementDAO;
    private JTable table;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;

    public StockManageFrame() {
        super("Estoque");
        this.productDAO = new ProductDAO(); // Pass this instance to ProductDAO
        this.stockDAO = new StockDAO(productDAO); // Pass productDAO to StockDAO
        this.movementDAO = new MovementDAO(productDAO);
        initComponents();
        loadStock();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(43, 43, 43));
        topPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 50));

        JLabel titleLabel = new JLabel("Gestão de Estoque");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setBackground(new Color(43, 43, 43));

        ButtonUtil movementButton = new ButtonUtil("Histórico de Movimentação", e -> {
            new StockMovementFrame().setVisible(true);
            this.dispose();
        });

        ButtonUtil levelStockButton = new ButtonUtil("Níveis de Estoque", e -> {
            new StockLevelsFrame().setVisible(true);
            this.dispose();
        });

        customButton(movementButton);
        customButton(levelStockButton);

        buttonPanel.add(movementButton);
        buttonPanel.add(levelStockButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columnNames = {"COD.", "Produto ID", "Produto", "Estoque inicial", "Entrada", "Saída", "Estoque final"};
        int[] columnWidths = {25, 50, 300, 150, 150, 150, 150};

        tablePanel = new TablePanel(columnNames, columnWidths);
        JScrollPane scrollPane = tablePanel.getScrollPane();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(topPanel, gbc);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void addProductToTable(Product product) {
        DefaultTableModel model = tablePanel.getModel();
        model.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice(),
                product.getCategoryId(),
                product.getSupplierId()
        });
    }

    public void loadStock() {
        DefaultTableModel model = tablePanel.getModel();
        model.setRowCount(0);
        List<Stock> stocks = stockDAO.listProductInStock();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Stock stock : stocks) {
            String entryFormated = stock.getEntry() != null ? dateFormat.format(stock.getEntry()) : "Sem entrada";
            String exitFormated = stock.getExit() != null ? dateFormat.format(stock.getExit()) : "Sem saída";
            model.addRow(new Object[]{
                    stock.getId(), // ID do estoque
                    stock.getProduct().getId(), // ID do produto
                    stock.getProduct().getName(), // Nome do produto
                    stock.getInitialStock(), // Estoque inicial
                    entryFormated, // Data de entrada formatada
                    exitFormated, // Data de saída formatada
                    stock.getFinalStock() // Estoque final
            });
        }
    }

    private void customButton(ButtonUtil button) {
        button.setPreferredSize(new Dimension(300, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StockManageFrame stockManageFrame = new StockManageFrame();
            stockManageFrame.setVisible(true);
        });
    }
}
