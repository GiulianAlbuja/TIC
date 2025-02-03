<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Services\HL7\AsignadorEstrategia;
use Illuminate\Support\Facades\Validator;
use App\Services\JWT\JWTService;

class ResultadosController extends Controller
{
    protected $jwtService;

    public function __construct(JWTService $jwtService)
    {
        $this->jwtService = $jwtService;
    }

    public function procesarResultadosClinicos(Request $request)
    {
        $validacion = $this->validarSolicitud($request);
        if ($validacion !== true) {
            return $validacion;  // Si no es true, se devuelve el response JSON con el error
        }

        try {
            $asignadorEstrategia = new AsignadorEstrategia();
            $strategy = $asignadorEstrategia->obtenerEstrategia($request->get('configuracionHL7'));
            $resultado = $strategy->procesarTramaHL7aJSON($request->get('hl7Trama'));
            

            return response()->json(['success' => true, 'data' => $resultado], 200);
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
            'configuracionHL7' => 'required',
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
