<?php

namespace App\Services\HL7;
use App\Services\HL7\TICStrategy;

class AsignadorEstrategia
{
    public function obtenerEstrategia(string $configuracionHL7): EstrategiaProcesamiento
    {
        switch ($configuracionHL7) {
            case 'AnalyzerStrategy':
                return new TICStrategy();
            case 'HematologyStrategy':
                return new TICStrategy();
            case 'BioAnalyzerStrategy':
                return new TICStrategy();
            case 'DefaultStrategy':
                return new TICStrategy();
            default:
                throw null;
        }
    }
}