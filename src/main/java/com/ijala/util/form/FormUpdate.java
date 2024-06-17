package com.ijala.util.form;

import com.ijala.model.product.Product;
import com.ijala.model.product.ProductDAO;
import com.ijala.util.ButtonPanel;

import javax.swing.*;
import java.awt.*;

public class FormUpdate extends FormBasePanel {
    private Product produto;
    private Window parentWindow; // Adicionamos um campo para armazenar a janela pai

    public FormUpdate(Product produto, Window parentWindow) {
        this.produto = produto;
        this.parentWindow = parentWindow;
    }

    public FormUpdate(Product produto) {
        this.produto = produto;
    }

    public JPanel getFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#2B2B2B"));

        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.decode("#2B2B2B"));
        formContent.add(Box.createVerticalStrut(30));
        formContent.add(createCustomContainer("Nome do Produto", true));
        formContent.add(Box.createVerticalStrut(20));
        formContent.add(createCustomContainer("Fornecedor", true));
        formContent.add(Box.createVerticalStrut(20));
        formContent.add(createCustomContainer("Categoria", true));
        formContent.add(Box.createVerticalStrut(20));
        formContent.add(createCustomContainer("Descrição", true));
        formContent.add(Box.createVerticalStrut(20));

        JPanel formSmallContent = new JPanel();
        formSmallContent.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
        formSmallContent.setBackground(Color.decode("#2B2B2B"));
        formSmallContent.add(createSmallContainer("Preço", true));
        formSmallContent.add(createSmallContainer("Quantidade", true));
        formContent.add(Box.createVerticalStrut(0));
        formContent.add(formSmallContent);

        textFieldName.setText(produto.getName());
        textFieldSupplier.setText(String.valueOf(produto.getSupplierId()));
        textFieldCategory.setText(String.valueOf(produto.getCategoryId()));
        textFieldDescription.setText(produto.getDescription());
        textFieldPrice.setText(String.valueOf(produto.getPrice()));
        textFieldQuantity.setText(String.valueOf(produto.getQuantity()));

        ButtonPanel buttonPanel = new ButtonPanel("Atualizar", e -> updateProduct());
        formContent.add(Box.createVerticalStrut(10));
        formContent.add(buttonPanel);
        formContent.add(Box.createVerticalStrut(40));
        panel.add(formContent);

        return panel;
    }

    private void updateProduct(){
        try {
            produto.setName(textFieldName.getText());
            produto.setSupplierId(Integer.parseInt(textFieldSupplier.getText()));
            produto.setCategoryId(Integer.parseInt(textFieldCategory.getText()));
            produto.setDescription(textFieldDescription.getText());
            produto.setPrice(Double.parseDouble(textFieldPrice.getText()));
            produto.setQuantity(Integer.parseInt(textFieldQuantity.getText()));

            ProductDAO produtoDAO = new ProductDAO();
            produtoDAO.updateProduct(produto);

            // Exibir mensagem de sucesso
            JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Fechar a janela após a atualização bem-sucedida
            parentWindow.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Erro: Preço e Quantidade devem ser números válidos.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + ex.getMessage());
        }
    }
}