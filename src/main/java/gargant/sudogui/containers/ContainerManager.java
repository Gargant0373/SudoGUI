package gargant.sudogui.containers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import masecla.mlib.main.MLib;

public class ContainerManager {

	private MLib lib;
	private SudoContainer sudoContainer;
	private ContainerEditor containerEditor;

	public ContainerManager(MLib lib) {
		this.lib = lib;
	}
	
	public void updateDependencies(SudoContainer sudoContainer, ContainerEditor containerEditor) {
		this.sudoContainer = sudoContainer;
		this.containerEditor = containerEditor;
	}

	// TODO: Add SQL support
	/**
	 * Method used to fetch the containers from the configuration file. It has no
	 * safety checks in place for containers so some of them might be corrupted if
	 * config was edited manually.
	 * 
	 * @return a list of containers from config.
	 */
	@SuppressWarnings("unchecked")
	public List<SerializeableContainer> getContainers() {
		return (List<SerializeableContainer>) lib.getConfigurationAPI().getConfig("containers").get("guis",
				new ArrayList<>());
	}

	// TODO: Add SQL support.
	/**
	 * Method used to update the list of containers in the storing prefference.
	 * 
	 * @param Containers - The list of registered containers.
	 */
	private void updateContainerList(List<SerializeableContainer> containers) {
		lib.getConfigurationAPI().getConfig("containers").set("guis", containers);
	}

	/**
	 * Method used to register a container to the config.
	 * 
	 * @param Container - the container you want to register.
	 */
	public void registerContainer(SerializeableContainer cont) {
		List<SerializeableContainer> containers = this.getContainers();
		containers.add(cont);
		this.updateContainerList(containers);
	}

	/**
	 * Method used to find a container by name.
	 * 
	 * @param name - Name of the container.
	 * @return null if no container found or the container that you're looking for
	 */
	public SerializeableContainer getContainerByName(String name) {
		for (SerializeableContainer c : this.getContainers())
			if (c.getContainerName().equalsIgnoreCase(name))
				return c;
		return null;
	}

	/**
	 * Method used to delete a container from the config.
	 * 
	 * @param Container - Container you want to remove.
	 * @return true if container removed, false if it never existed.
	 */
	public boolean deleteContainer(SerializeableContainer cont) {
		if (cont == null)
			return false;

		List<SerializeableContainer> registeredContainers = this.getContainers();

		if (!registeredContainers.contains(cont)) {
			return false;
		}

		registeredContainers.remove(cont);
		this.updateContainerList(registeredContainers);
		return true;
	}

	public void openContainer(Player p, SerializeableContainer cont) {
		this.sudoContainer.setViewing(p, cont);
		lib.getContainerAPI().openFor(p, SudoContainer.class);
	}
	
	public void editContainer(Player p, SerializeableContainer cont) {
		this.containerEditor.setViewing(p, cont);
		lib.getContainerAPI().openFor(p, ContainerEditor.class);
	}
}
