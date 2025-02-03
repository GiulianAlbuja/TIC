<?php

namespace App\Services\HL7;
use App\Services\HL7\TICStrategy;

class AsignadorEstrategia
{
    public function obtenerEstrategia(string $configuracionHL7): EstrategiaProcesamiento
    {
        switch ($configuracionHL7) {
            case 'TIC':
                return new TICStrategy();
            case 'TC-220':
                return new TICStrategy();
            default:
                throw null;
        }
    }
}
