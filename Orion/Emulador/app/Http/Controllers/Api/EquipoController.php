<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Equipo;
use Illuminate\Support\Facades\Validator;

class EquipoController extends Controller
{
    public function index()
    {
        $equipos = Equipo::all();

        $data = [
            'equipos' => $equipos,
            'status' => 200
        ];
        return response() -> json($data, 200);
    }

    
    public function store(Request $request){

        $validator = Validator::make($request->all(), [
            'nombreEquipo' => 'required',
            'codigoEquipo' => 'required',
            'laboratorio' => 'required'
        ]);
        
        if($validator->fails()){
            $data = [
                'message' => 'Error en la validacion de datos',
                'errors' => $validator->errors(),
                'status' => 400
            ];
            return response() -> json($data, 400);
        }

        $equipo = Equipo::create([
            'nombreEquipo' => $request->nombreEquipo,
            'codigoEquipo' => $request->codigoEquipo,
            'laboratorio' => $request->laboratorio
        ]);
        if (!$equipo){
            $data = [
                'message' => 'Error al crear el equipo',
                'status' => 500
            ];
            return response() -> json($data, 500);
        }
        
        $data = [
            'equipo' => $equipo,
            'status' => 201
        ];
        return response() -> json($data, 201);
    }

    public function recibirResultados(Request $request){
        $confirmacion = $request;
        $data = [
            'confirmacion' => $confirmacion,
            'status' => 201
        ];
        return responde() ->json($data ,200);
    }
}

