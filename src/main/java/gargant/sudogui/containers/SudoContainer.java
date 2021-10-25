package gargant.sudogui.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class SudoContainer extends ImmutableContainer {

	public SudoContainer(MLib lib) {
		super(lib);
	}

	private Map<UUID, SerializeableContainer> containerViewing = new HashMap<>();

	public void setViewing(Player p, SerializeableContainer c) {
		this.containerViewing.put(p.getUniqueId(), c);
	}

	@Override
	public void onTopClick(InventoryClickEvent ev) {
		Player p = (Player) ev.getWhoClicked();
		ev.setCancelled(true);
		// Deprecated because Matt is coo coo
		@SuppressWarnings("deprecation")
		String command = lib.getNmsAPI().getNBTTagValueString(ev.getCurrentItem(), "command");
		if (command != null)
			if (!command.equals("null"))
				p.performCommand(command);
	}

	@Override
	public Inventory getInventory(Player p) {
		SerializeableContainer cont = this.containerViewing.get(p.getUniqueId());
		Inventory inv = Bukkit.createInventory(p, cont.getContainerSize(),
				ChatColor.translateAlternateColorCodes('&', cont.getContainerName()));
		cont.getItems().forEach((c, v) -> {
			if (c == null)
				return;
			inv.setItem(cont.getPositionForItem(c),
					v == null ? c : lib.getNmsAPI().write().tagString("command", v).applyOn(c));
		});
		return inv;
	}

	@Override
	public int getSize() {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 5;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

}
