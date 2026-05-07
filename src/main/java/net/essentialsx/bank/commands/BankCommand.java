package net.essentialsx.bank.commands;

import net.essentialsx.bank.BankI18n;
import net.essentialsx.bank.EssentialsBank;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankCommand implements CommandExecutor {

    private final EssentialsBank plugin;

    public BankCommand(EssentialsBank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        BankI18n i18n = plugin.getI18n();
        Economy vault = plugin.getVaultEconomy();
        String bankName = plugin.getBankAccountName();

        if (args.length == 0) {
            sender.sendMessage(i18n.tl("bankHelp", label));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "balance":
            case "estado":
                double balance = vault.getBalance(bankName);
                sender.sendMessage(i18n.tl("bankBalance", vault.format(balance)));
                break;

            case "deposit":
            case "depositar":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(i18n.tl("playerOnly"));
                    return true;
                }
                Player p = (Player) sender;
                if (args.length < 2) {
                    sender.sendMessage(i18n.tl("invalidAmount"));
                    return true;
                }
                double depositAmount;
                try {
                    depositAmount = Double.parseDouble(args[1]);
                    if (depositAmount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    sender.sendMessage(i18n.tl("invalidAmount"));
                    return true;
                }

                if (!vault.has(p, depositAmount)) {
                    sender.sendMessage(i18n.tl("notEnoughMoney"));
                    return true;
                }

                EconomyResponse withdrawResponse = vault.withdrawPlayer(p, depositAmount);
                if (withdrawResponse.transactionSuccess()) {
                    EconomyResponse depositResponse = vault.depositPlayer(bankName, depositAmount);
                    if (depositResponse.transactionSuccess()) {
                        sender.sendMessage(i18n.tl("depositSuccess", vault.format(depositAmount)));
                    } else {
                        // Rollback
                        vault.depositPlayer(p, depositAmount);
                        sender.sendMessage(i18n.tl("transactionFailed", depositResponse.errorMessage));
                    }
                } else {
                    sender.sendMessage(i18n.tl("transactionFailed", withdrawResponse.errorMessage));
                }
                break;

            case "withdraw":
            case "retirar":
                if (!sender.hasPermission("essentialsbank.admin")) {
                    sender.sendMessage(i18n.tl("noPermission"));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(i18n.tl("invalidAmount"));
                    return true;
                }
                double withdrawAmount;
                try {
                    withdrawAmount = Double.parseDouble(args[1]);
                    if (withdrawAmount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    sender.sendMessage(i18n.tl("invalidAmount"));
                    return true;
                }

                if (!vault.has(bankName, withdrawAmount)) {
                    sender.sendMessage(i18n.tl("bankNotEnoughMoney"));
                    return true;
                }

                EconomyResponse bWithdrawResponse = vault.withdrawPlayer(bankName, withdrawAmount);
                if (bWithdrawResponse.transactionSuccess()) {
                    if (sender instanceof Player) {
                        vault.depositPlayer((Player) sender, withdrawAmount);
                    }
                    sender.sendMessage(i18n.tl("withdrawSuccess", vault.format(withdrawAmount)));
                } else {
                    sender.sendMessage(i18n.tl("transactionFailed", bWithdrawResponse.errorMessage));
                }
                break;

            default:
                sender.sendMessage(i18n.tl("bankHelp", label));
                break;
        }

        return true;
    }
}
