package gargant.sudogui.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gargant.sudogui.containers.ContainerManager;
import gargant.sudogui.containers.SerializeableContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.annotations.SubcommandInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

@RegisterableInfo(command = "sudogui")
@RequiresPlayer
public class ContainerCommand extends Registerable {

	private ContainerManager containerManager;

	public ContainerCommand(MLib lib, ContainerManager containerManager) {
		super(lib);
		this.containerManager = containerManager;
	}

	/**
	 * Fallback command.
	 */
	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		if (!sender.hasPermission("sudogui.help")) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return;
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&c&l&oSudoGUI &7- &f&l&oRunning version " + lib.getPlugin().getDescription().getVersion()));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oWritten with love by Gargant"));
	}

	/**
	 * Delete command handling.
	 */
	@SubcommandInfo(subcommand = "delete", permission = "sudogui.delete")
	public void onDelete(Player p, String container) {
		SerializeableContainer cont = containerManager.getContainerByName(container);
		if (cont == null) {
			lib.getMessagesAPI().sendMessage("container.invalid", p, new Replaceable("%name%", container));
			return;
		}
		boolean removed = containerManager.deleteContainer(cont);
		if (!removed) {
			lib.getMessagesAPI().sendMessage("container.invalid", p, new Replaceable("%name%", container));
			return;
		}
		lib.getMessagesAPI().sendMessage("container.removed", p, new Replaceable("%name%", container));
	}

	/**
	 * Delete alias handling.
	 */
	@SubcommandInfo(subcommand = "remove", permission = "sudogui.delete")
	public void onRemove(Player p, String container) {
		this.onDelete(p, container);
	}
}
