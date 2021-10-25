package gargant.sudogui.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.classes.Replaceable;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.AnvilGUI;

public class ContainerEditor extends ImmutableContainer {

	private ContainerManager containerManager;

	public ContainerEditor(MLib lib, ContainerManager containerManager) {
		super(lib);
		this.containerManager = containerManager;
	}

	public void setViewing(Player p, SerializeableContainer cont) {
		this.viewing.put(p.getUniqueId(), cont);
	}

	private Map<UUID, SerializeableContainer> viewing = new HashMap<>();

	@Override
	public void onTopClick(InventoryClickEvent ev) {
		if (ev.getCurrentItem() == null || ev.getCurrentItem().getType().equals(Material.AIR))
			return;

		Player p = (Player) ev.getWhoClicked();
		if (ev.getClick().equals(ClickType.MIDDLE)) {
			new AnvilGUI.Builder().plugin(lib.getPlugin()).text("Enter command!").title("Enter command to run!")
					.itemLeft(new ItemStack(Material.PAPER))
					.onClose(c -> lib.getContainerAPI().openFor(c, ContainerEditor.class)).onComplete((c, r) -> {
						this.viewing.get(c.getUniqueId()).updateCommand(ev.getCurrentItem(), r);
						return AnvilGUI.Response.text("Updated!");
					}).open(p);
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();

		SerializeableContainer cont = this.viewing.get(p.getUniqueId());

		Inventory inv = event.getInventory();

		Map<ItemStack, String> itemCommands = cont.getItems();

		cont.clearItems();
		for (int i = 0; i < inv.getContents().length; i++) {
			if (inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
				cont.addItem(inv.getItem(i), itemCommands.getOrDefault(inv.getItem(i), null), i);
			}
		}

		this.containerManager.deleteContainer(cont);
		this.containerManager.registerContainer(cont);
	}

	@Override
	public Inventory getInventory(Player p) {
		SerializeableContainer cont = this.viewing.get(p.getUniqueId());

		Inventory inv = Bukkit.createInventory(p, cont.getContainerSize(),
				ChatColor.translateAlternateColorCodes('&', cont.getContainerName() + " &7Editor"));

		cont.getItems().forEach((c, v) -> {
			inv.setItem(cont.getPositionForItem(c), c);
		});

		return inv;
	}

	@Override
	public int getSize() {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 0;
	}

	@Override
	public boolean requiresUpdating() {
		return false;
	}

	@Override
	public boolean updateOnClick() {
		return false;
	}

}
