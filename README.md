# EssentialsBank

<p align="center">
  <img src="https://img.shields.io/badge/PaperMC-1.20.x-blue.svg?style=flat-square" alt="PaperMC" />
  <img src="https://img.shields.io/badge/EssentialsX-Compatible-red.svg?style=flat-square" alt="EssentialsX" />
  <img src="https://img.shields.io/badge/Vault-Compatible-green.svg?style=flat-square" alt="Vault" />
</p>

EssentialsBank is a plugin for PaperMC (and its forks like Purpur) designed to integrate **natively** into the **EssentialsX** and **Vault** economy ecosystem. It allows server administrators to have a "Central Bank" account without having to link it to a real player or premium account.

## 🌟 Features
- **Native EssentialsX Integration:** Automatically generates a hidden `NPC` account in EssentialsX that acts as the server bank.
- **Vault Support:** Fully compatible with the Vault API. Any other plugin (e.g., shops, auctions, quests) can deposit or withdraw money from the central bank seamlessly.
- **Translation System (I18n):** Uses a `.properties` system identical to official EssentialsX modules. Includes **English (en)** and **Spanish (es)** languages by default.
- **No Ghost Accounts:** The bank is independent and does not take up a whitelist slot or interfere with real player UUIDs.

## 📦 Requirements
- **Server Software:** PaperMC (or Purpur) 1.13+ (Tested on 1.20.1)
- **Base Economy:** [EssentialsX](https://essentialsx.net/)
- **Economy API:** [Vault](https://github.com/MilkBowl/Vault)

## 🚀 Installation
1. Stop your server.
2. Download or compile the latest `EssentialsBank-x.x.x.jar` file and place it in your server's `plugins/` folder.
3. Make sure you have the `EssentialsX` and `Vault` plugins installed.
4. Start your server.
5. **(Required)** Edit the `config.yml` file generated in `plugins/EssentialsBank/` to configure the internal bank name and set `enabled: true`.
6. Run `/bank reload` or restart your server to activate the plugin.

## 💻 Commands and Permissions

| Command | Description | Permission | Default |
|---|---|---|---|
| `/bank balance` | Shows the current balance of the server bank. | `essentialsbank.use` | True |
| `/bank deposit <amount>` | Deposits money from your balance to the central bank. | `essentialsbank.use` | True |
| `/bank withdraw <amount>` | Withdraws money from the bank to your balance. | `essentialsbank.admin` | OP |
| `/bank set <amount>` | Sets the exact balance of the central bank. | `essentialsbank.admin` | OP |
| `/bank reload` | Reloads the configuration and language file. | `essentialsbank.admin` | OP |

*Supported aliases:* `/banco`, `/serverbank`

💡 *Pro Tip:* EssentialsBank automatically hooks into EssentialsX! Running `/essentials reload` will automatically reload the bank plugin too.

## ⚙️ Configuration

`config.yml`
```yaml
# The language is automatically detected from your EssentialsX config.yml (locale setting).
# Supported by default: 'en' (English), 'es' (Spanish).

# Enable the bank plugin? (Set to true AFTER configuring the bank-account-name)
enabled: false

# Name of the server bank account (internally created as an NPC in Essentials)
bank-account-name: 'ServerBank'
```

## 🛠️ Compiling from source
This project uses Gradle. To compile it yourself:
```bash
git clone https://github.com/xexuu/EssentialsBank.git
cd EssentialsBank
gradle wrapper
./gradlew build
```
The compiled `.jar` file will appear in the `build/libs/` directory.

## 📝 License
This project is open-source. Feel free to clone, modify, and contribute!
