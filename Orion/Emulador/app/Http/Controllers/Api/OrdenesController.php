<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Services\HL7\AsignadorEstrategia;
use Illuminate\Support\Facades\Validator;
use App\Services\JWT\JWTService;

class OrdenesController extends Controller
{
    protected $jwtService;

    public function __construct(JWTService $jwtService)
    {
        $this->jwtService = $jwtService;
    }

    public function consultarOrdenes(Request $request)
    {
        $validacion = $this->validarSolicitud($request);
        if ($validacion !== true) {
            return $validacion;
        }

        try {
            $resultado = 'MSH|^~\&|LIS|Hospital|EquipoNoRegistrado|LabFacility|202402061202||DSR|654321|P|2.3|\rPID|1|12345^^^Hospital^MR||Doe^John||19800101|M|||456 Elm St^^Metropolis^NY^12345||555-555-5555|\rOBR|1|54321|12345|CMP^Comprehensive Metabolic Panel^L|||202402061100|202402061201\r';
            return response()->json(['success' => true, 'orden' => $resultado], 200);
        } catch (\Exception $e) {
            return response()->json(['success' => false, 'error' => $e->getMessage()], 400);
        }
    }

    private function validarSolicitud(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'ip' => 'required',
            'id' => 'required',
            'token' => 'required',
            'estrategiaHL7' => 'required',
            'hl7Trama' => 'required'
        ]);

        if ($validator->fails()) {
            $data = [
                'message' => 'Error en la validación de datos',
                'errors' => $validator->errors(),
                'status' => 400
            ];
            return response()->json($data, 400);
        }

        if (!$this->jwtService->validateToken($request->get('token'), $request->get('id'))) {
            return response()->json(['error' => 'Token inválido o expirado'], 401);
        }
        return true;
    }


}
