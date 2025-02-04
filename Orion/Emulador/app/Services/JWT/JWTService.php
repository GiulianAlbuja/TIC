<?php

namespace App\Services\JWT;

use Lcobucci\JWT\Configuration;
use Lcobucci\JWT\Signer\Hmac\Sha256;
use Lcobucci\JWT\Signer\Key;
use Carbon\Carbon;
use Lcobucci\JWT\Signer\Key\InMemory;
use Lcobucci\JWT\Validation\Constraint\SignedWith;
use Lcobucci\JWT\Validation\Constraint\ValidAt;
use Lcobucci\Clock\SystemClock;

class JWTService
{
    protected $config;

    public function __construct()
{
    $this->config = Configuration::forSymmetricSigner(
        new Sha256(),
        InMemory::plainText(env('JWT_SECRET'))
    );

    $this->config->setValidationConstraints(
        new SignedWith(new Sha256(), InMemory::plainText(env('JWT_SECRET'))),
        new ValidAt(SystemClock::fromUTC())
    );
}

    public function generateToken(string $clientId): string
    {
        $now = Carbon::now();
        $issuedAt = new \DateTimeImmutable($now->toDateTimeString());
        $expiresAt = new \DateTimeImmutable($now->addMinutes(480)->toDateTimeString());

        $token = $this->config->builder()
            ->issuedAt($issuedAt)
            ->expiresAt($expiresAt)
            ->withClaim('clientId', $clientId)
            ->getToken($this->config->signer(), $this->config->signingKey());

        return $token->toString();
    }

    public function validateToken(string $token, string $expectedClientId): bool
    {
        $parsedToken = $this->config->parser()->parse($token);
        if (!$this->config->validator()->validate($parsedToken, ...$this->config->validationConstraints())) {
            return false;
        }
        $clientId = $parsedToken->claims()->get('clientId');

        if ($clientId !== $expectedClientId) {
            return false;
        }
        return true;
    }
}
