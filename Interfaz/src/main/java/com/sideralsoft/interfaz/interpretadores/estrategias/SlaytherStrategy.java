package com.sideralsoft.interfaz.interpretadores.estrategias;

public class SlaytherStrategy implements  EstrategiaProcesamiento{
    @Override
    public String procesarMensaje(String mensaje) {
        return "ACK-SLAYTHER";
    }

    @Override
    public String analizarTipoMensaje(String mensaje) {
        return "ACK";
    }

    @Override
    public void validarMensaje(String clientAddress, String mensaje) {
        System.out.println("VALIDAR-SLAYTHER");
    }

    @Override
    public void generarRespuestaConfirmacion(String clientAddress, String mensaje) {
        System.out.println("Confirmacion-SLAYTHER");
    }
}
