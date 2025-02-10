<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\EquipoController;
use App\Http\Controllers\Api\ResultadosController;
use App\Http\Controllers\Api\OrdenesController;

Route::get('/equipos', [EquipoController::class, 'index']);
Route::post('/equipos', [EquipoController::class, 'store']);
Route::post('/resultados', [ResultadosController::class, 'procesarResultadosClinicos']);
Route::post('/ordenes', [OrdenesController::class, 'consultarOrdenes']);
