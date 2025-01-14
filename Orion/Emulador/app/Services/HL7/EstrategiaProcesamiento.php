<?php

namespace App\Services\HL7;

interface EstrategiaProcesamiento
{
    public function procesarTramaHL7aJSON(string $hl7Trama): array;
}
