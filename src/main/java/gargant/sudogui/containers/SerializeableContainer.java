package gargant.sudogui.containers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class SerializeableContainer {

	private String containerName = "N/A";
	private int containerSize = 54;

	/**
	 * Map of the item and the command it should run.
	 * Null command means item shouldn't run a command.
	 */
	private Map<ItemStack, String> items = new HashMap<>();
	
	/**
	 * Map of the item and the position it holds in the container.
	 */
	private Map<ItemStack, Integer> itemPositions = new HashMap<>();
	
	public Exception isValid() {
		if(containerName == null)
			return new Exception("Container name is invalid!");
		if(containerSize % 9 != 0)
			return new Exception("Container size is invalid!");
		return null;
	}

	public String getContainerName() {
		return containerName;
	}
	
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public int getContainerSize() {
		return containerSize;
	}

	public void setContainerSize(int containerSize) {
		this.containerSize = containerSize;
	}

	public Map<ItemStack, String> getItems() {
		return items;
	}
	
	public Map<ItemStack, Integer> getItemPositions() {
		return this.itemPositions;
	}
	
	public int getPositionForItem(ItemStack s) {
		return this.itemPositions.get(s);
	}

	public void addItem(ItemStack item, String command, int position) {
		this.items.put(item, command);
		this.itemPositions.put(item, position);
	}
	
	public void updateCommand(ItemStack s, String command) {
		this.items.put(s, command);
	}
}
