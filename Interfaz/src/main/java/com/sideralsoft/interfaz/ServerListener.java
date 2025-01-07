package com.sideralsoft.interfaz;

public interface ServerListener {
    void onMessageReceived(String message);  // Cuando se recibe un mensaje de cliente
    void onClientConnected(String clientInfo);  // Cuando un cliente se conecta
    void onError(String error);  // Cuando ocurre un error
}
