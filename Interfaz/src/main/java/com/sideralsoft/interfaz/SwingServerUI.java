package com.sideralsoft.interfaz;

import com.sideralsoft.interfaz.comunicadores.ServerListener;
import com.sideralsoft.interfaz.comunicadores.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingServerUI extends JFrame implements ServerListener {
    private JTextArea messageArea;
    private JTextField inputField;
    private JTextField clientAddressField;
    private TCPServer server;

    public SwingServerUI() {
        // Configuración del JFrame (ventana principal)
        setTitle("Servidor TCP con Swing");
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Componentes principales
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        inputField = new JTextField();
        clientAddressField = new JTextField();
        clientAddressField.setToolTipText("Dirección del cliente (ej. /127.0.0.1)");

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToClient();
            }
        });

        // Layout principal
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());
        clientPanel.add(new JLabel("Dirección del cliente:"), BorderLayout.WEST);
        clientPanel.add(clientAddressField, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(clientPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Agregar el panel principal al JFrame
        add(mainPanel);

        // Iniciar el servidor
        server = TCPServer.getInstance();
        server.addServerListener(this);
        server.start();

        setVisible(true);
    }

    private void sendMessageToClient() {
        String message = inputField.getText();
        String clientAddress = clientAddressField.getText();

        if (!message.isEmpty() && !clientAddress.isEmpty()) {
            server.sendMessageToClient(clientAddress, message);  // Enviar mensaje al cliente indicado
            messageArea.append("Servidor (a " + clientAddress + "): " + message + "\n");
            inputField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar un mensaje y una dirección de cliente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append("Cliente: " + message + "\n"));
    }

    @Override
    public void onClientConnected(String clientInfo) {
        SwingUtilities.invokeLater(() -> messageArea.append("Cliente conectado: " + clientInfo + "\n"));
    }

    @Override
    public void onError(String error) {
        SwingUtilities.invokeLater(() -> messageArea.append("Error: " + error + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingServerUI::new);
    }
}