# MediAlert Frontend Angular 18 + Microsoft Entra ID

El frontend se ejecuta localmente en `http://localhost:4200/` y consume solamente la URL publica del API Manager de AWS.

## Configuracion

El archivo utilizado es:

`src/environments/environment.ts`

Incluye el Client ID, Tenant/Authority, redirect URI local y la URL publica:

`https://3lj8j5yzki.execute-api.us-east-1.amazonaws.com/prod/api/bff`

No se debe guardar el secreto interno del API Gateway en Angular.

## Ejecutar

```bash
npm install
npm start
```

## Validar compilacion

```bash
npm run build
```
