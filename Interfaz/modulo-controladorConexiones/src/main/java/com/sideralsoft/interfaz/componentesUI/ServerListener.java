package com.sideralsoft.interfaz.componentesUI;

public interface ServerListener {
    void onMessageReceived(String message);
    void onConnectionStatusChanged(String status);
    void onError(String error);
}
