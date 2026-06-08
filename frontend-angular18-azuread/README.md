# Frontend MediAlert - Angular 18 + Azure AD

Frontend local para el sistema de alertas medicas.

Azure AD / Microsoft Entra ID se usa solo para iniciar sesion en Angular. El backend se consume mediante AWS API Gateway.

## Configuracion

Editar `src/environments/environment.dev.ts`:

```ts
clientId: 'CLIENT_ID_DE_AZURE',
authority: 'https://login.microsoftonline.com/TENANT_ID',
loginRequest: {
  scopes: ['openid', 'profile', 'email'],
},
bffBaseUrl: 'https://API_ID.execute-api.us-east-1.amazonaws.com/prod/api/bff'
```

## Ejecutar local

```bash
npm install
npm start
```

Abrir:

```text
http://localhost:4200
```

## Funcionalidades

- Login con Microsoft Entra ID / Azure AD usando MSAL Angular.
- Formulario de alerta medica con validaciones.
- Listado de alertas desde el BFF mediante AWS API Gateway.
- Cambiar estado a ATENDIDA o DESCARTADA.
- Eliminar alerta.
