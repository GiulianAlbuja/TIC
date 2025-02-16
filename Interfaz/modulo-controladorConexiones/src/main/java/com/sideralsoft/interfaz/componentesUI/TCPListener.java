package com.sideralsoft.interfaz.componentesUI;

public interface TCPListener {
    void updateReceivedMessage(String message);
    void updateSentMessage(String message);
}