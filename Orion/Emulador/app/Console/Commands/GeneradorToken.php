<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\Services\JWT\JWTService;

class GeneradorToken extends Command
{
    protected $signature = 'generate:token {clientId}';
    protected $description = 'Generar un token JWT para un cliente';

    protected $jwtService;

    public function __construct(JWTService $jwtService)
    {
        parent::__construct();
        $this->jwtService = $jwtService;
    }

    public function handle()
    {
        $clientId = $this->argument('clientId');
        $token = $this->jwtService->generateToken($clientId);
        $this->info("Token generado para cliente '{$clientId}':");
        $this->line($token);
    }
}
