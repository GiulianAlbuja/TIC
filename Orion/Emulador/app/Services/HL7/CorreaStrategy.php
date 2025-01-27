<?php

namespace App\Services\HL7;

class CorreaStrategy implements EstrategiaProcesamiento
{
    public function procesarTramaHL7aJSON(string $hl7Trama): array
    {
    $segments = explode("\\r", $hl7Trama);
    $hl7Data = [
        "MSH" => [],
        "PID" => [],
        "OBR" => [],
        "OBX" => []
    ];
    foreach ($segments as $segment) {
        $fields = explode("|", $segment);
        $segmentType = $fields[0];

        switch ($segmentType) {
            case "MSH":
                $hl7Data["MSH"] = [
                    "sendingApplication" => $fields[2] ?? "",
                    "sendingFacility" => $fields[3] ?? "",
                    "receivingApplication" => $fields[4] ?? "",
                    "receivingFacility" => $fields[5] ?? "",
                    "timestamp" => $fields[6] ?? "",
                    "messageType" => $fields[8] ?? "",
                    "messageControlId" => $fields[9] ?? "",
                    "processingId" => $fields[10] ?? "",
                    "version" => $fields[11] ?? ""
                ];
                break;
            case "PID":
                $hl7Data["PID"] = [
                    "patientId" => $fields[3] ?? "",
                    "patientName" => $fields[5] ?? "",
                    "birthdate" => $fields[7] ?? "",
                    "gender" => $fields[8] ?? "",
                    "address" => $fields[11] ?? "",
                    "phoneNumber" => $fields[13] ?? ""
                ];
                break;
            case "OBR":
                $hl7Data["OBR"] = [
                    "orderId" => $fields[2] ?? "",
                    "fillerOrderId" => $fields[3] ?? "",
                    "testCode" => $fields[4] ?? "",
                    "collectionDateTime" => $fields[7] ?? "",
                    "resultDateTime" => $fields[8] ?? ""
                ];
                break;
            case "OBX":
                $hl7Data["OBX"][] = [
                    "observationId" => $fields[3] ?? "",
                    "observationValue" => $fields[5] ?? "",
                    "units" => $fields[6] ?? "",
                    "referenceRange" => $fields[7] ?? "",
                    "abnormalFlag" => $fields[8] ?? ""
                ];
                break;
            default:
                break;
        }
    }
    return $hl7Data;
    }
}
