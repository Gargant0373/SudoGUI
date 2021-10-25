package gargant.sudogui.commands;

import org.bukkit.Material;
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
	 * Container list handling. 
	 */
	@SubcommandInfo(subcommand = "container list", permission = "sudogui.list")
	public void onList(Player p) {
		StringBuilder b = new StringBuilder();
		this.containerManager.getContainers().forEach(c -> {
			b.append(c.getContainerName() + "&7, &f");
		});
		p.sendMessage(b.toString());
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
	 * Container edit handling.
	 */
	@SubcommandInfo(subcommand = "edit", permission = "sudogui.edit")
	public void onEdit(Player p, String container) {
		SerializeableContainer cont = containerManager.getContainerByName(container);
		if (cont == null) {
			lib.getMessagesAPI().sendMessage("container.invalid", p, new Replaceable("%name%", container));
			return;
		}
		this.containerManager.editContainer(p, cont);
	}
	
	
	/**
	 * Container open handling.
	 */
	@SubcommandInfo(subcommand = "open", permission = "sudogui.open")
	public void onOpen(Player p, String name) {
		SerializeableContainer cont = this.containerManager.getContainerByName(name);
		if (cont == null) {
			lib.getMessagesAPI().sendMessage("container.invalid", p, new Replaceable("%name%", name));
			return;
		}
		this.containerManager.openContainer(p, cont);
	}

	/**
	 * Container add handling
	 */
	@SubcommandInfo(subcommand = "add", permission = "sudogui.add")
	public void onAddSizeName(Player p, String name, String sizeArg) {
		int size = -1;

		try {
			size = Integer.parseInt(sizeArg);
		} catch (NumberFormatException e) {
			lib.getMessagesAPI().sendMessage("exception.number-format", p, new Replaceable("%number%", sizeArg));
			return;
		}

		if (size % 9 != 0 || size > 54) {
			lib.getMessagesAPI().sendMessage("exception.invalid-size", p, new Replaceable("%size%", size));
			return;
		}

		SerializeableContainer cont = new SerializeableContainer();
		cont.setContainerName(name);
		cont.setContainerSize(size);

		int itemsRegistered = 0;
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getItem(i) != null && !p.getInventory().getItem(i).getType().equals(Material.AIR)) {
				itemsRegistered++;
				cont.addItem(p.getInventory().getItem(i), null, i);
			}
		}

		this.containerManager.registerContainer(cont);

		lib.getMessagesAPI().sendMessage("container.created", p, new Replaceable("%name%", name),
				new Replaceable("%size%", size),
				new Replaceable("%item-count%", itemsRegistered));
	}

	/**
	 * Delete alias handling.
	 */
	@SubcommandInfo(subcommand = "remove", permission = "sudogui.delete")
	public void onRemove(Player p, String container) {
		this.onDelete(p, container);
	}
}
