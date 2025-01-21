package com.sideralsoft.interfaz.comunicadores;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        // Dirección del servidor y puerto
        String servidor = "localhost"; // Cambiar por la IP o hostname del servidor si es necesario
        int puerto = 3002;  // Puerto del servidor al que se quiere conectar

        try {
            // Crear el socket y conectarse al servidor
            Socket socket = new Socket(servidor, puerto);
            System.out.println("Conectado al servidor " + servidor + " en el puerto " + puerto);

            // Crear flujos de entrada y salida
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            // Leer mensajes desde la consola para enviarlos al servidor
            BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));

            String mensajeCliente;
            while (true) {
                // Leer mensaje del usuario
                System.out.print("Escribe un mensaje para el servidor (o 'salir' para cerrar conexión): ");
                mensajeCliente = consola.readLine();

                // Enviar mensaje al servidor
                salida.println(mensajeCliente);

                if (mensajeCliente.equalsIgnoreCase("salir")) {
                    System.out.println("Cerrando conexión con el servidor...");
                    break;
                }

                // Leer respuesta del servidor
                String respuestaServidor = entrada.readLine();
                System.out.println("Respuesta del servidor: " + respuestaServidor);
            }

            // Cerrar flujos y socket
            entrada.close();
            salida.close();
            socket.close();
            System.out.println("Conexión cerrada.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
