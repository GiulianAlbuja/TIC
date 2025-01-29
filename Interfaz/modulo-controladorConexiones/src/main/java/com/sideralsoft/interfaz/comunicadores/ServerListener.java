package com.sideralsoft.interfaz.comunicadores;

public interface ServerListener {
    void onMessageReceived(String message);
    void onClientConnected(String clientInfo);
    void onError(String error);
}
