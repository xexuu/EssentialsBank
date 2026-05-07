# EssentialsBank

<p align="center">
  <img src="https://img.shields.io/badge/PaperMC-1.20.x-blue.svg?style=flat-square" alt="PaperMC" />
  <img src="https://img.shields.io/badge/EssentialsX-Compatible-red.svg?style=flat-square" alt="EssentialsX" />
  <img src="https://img.shields.io/badge/Vault-Compatible-green.svg?style=flat-square" alt="Vault" />
</p>

EssentialsBank es un plugin para PaperMC (y sus forks como Purpur) diseñado para integrarse de manera **nativa** en el ecosistema económico de **EssentialsX** y **Vault**. Permite a los administradores del servidor disponer de una cuenta de "Banco Central" sin tener que vincularla a un jugador real o cuenta premium.

## 🌟 Características
- **Integración Nativa con EssentialsX:** Genera automáticamente una cuenta `NPC` oculta en EssentialsX que actúa como el banco del servidor.
- **Soporte Vault:** Totalmente compatible con la API de Vault. Cualquier otro plugin (ej. de tiendas, subastas, misiones) puede depositar o retirar dinero del banco central sin problemas.
- **Sistema de Traducciones (I18n):** Utiliza un sistema `.properties` idéntico al de los módulos oficiales de EssentialsX. Incluye idiomas **Español (es)** e **Inglés (en)** por defecto.
- **Sin cuentas fantasma:** El banco es independiente y no ocupa un slot en la whitelist ni interfiere con los UUIDs reales de los jugadores.

## 📦 Requisitos
- **Software del Servidor:** PaperMC (o Purpur) 1.13+ (Testeado en la 1.20.1)
- **Economía Base:** [EssentialsX](https://essentialsx.net/)
- **API de Economía:** [Vault](https://github.com/MilkBowl/Vault)

## 🚀 Instalación
1. Apaga tu servidor.
2. Descarga o compila el archivo `EssentialsBank-1.0.0.jar` y colócalo en la carpeta `plugins/` de tu servidor.
3. Asegúrate de tener los plugins `EssentialsX` y `Vault` instalados.
4. Enciende tu servidor.
5. *(Opcional)* Edita el archivo `config.yml` generado en `plugins/EssentialsBank/` para cambiar el idioma o el nombre interno de la cuenta bancaria.

## 💻 Comandos y Permisos

| Comando | Descripción | Permiso | Por defecto |
|---|---|---|---|
| `/bank balance` | Muestra el saldo actual del banco del servidor. | `essentialsbank.use` | True |
| `/bank deposit <cantidad>` | Deposita dinero de tu bolsillo al banco central. | `essentialsbank.use` | True |
| `/bank withdraw <cantidad>` | Retira dinero de las arcas del banco a tu bolsillo. | `essentialsbank.admin` | OP |

*Alias soportados:* `/banco`, `/serverbank`

## ⚙️ Configuración

`config.yml`
```yaml
# Configura el idioma. Soporte nativo para: 'es' (español), 'en' (inglés).
locale: 'es'

# Nombre de la cuenta bancaria del servidor (creada internamente como NPC en Essentials)
bank-account-name: 'ServerBank'
```

## 🛠️ Compilar desde el código fuente
Este proyecto utiliza Gradle. Para compilarlo tú mismo:
```bash
git clone https://github.com/xexuu/EssentialsBank.git
cd EssentialsBank
gradle wrapper
./gradlew build
```
El archivo `.jar` compilado aparecerá en el directorio `build/libs/`.

## 📝 Licencia
Este proyecto es de código abierto. ¡Siéntete libre de clonarlo, modificarlo y contribuir!
