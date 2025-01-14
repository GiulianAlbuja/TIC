<?php

namespace App\Services\HL7;
use App\Services\HL7\TICStrategy;

class AsignadorEstrategia
{
    public function obtenerEstrategia(string $codigoEquipo): EstrategiaProcesamiento
    {
        switch ($codigoEquipo) {
            case 'TIC':
                return new TICStrategy();
            case 'TC-220':
                return new TICStrategy();
            default:
                throw null;
        }
    }
}
