package gargant.sudogui.containers;

import java.util.ArrayList;
import java.util.List;

import masecla.mlib.main.MLib;

public class ContainerManager {

	private MLib lib;
	
	public ContainerManager(MLib lib) {
		this.lib = lib;
	}
	
	@SuppressWarnings("unchecked")
	public List<SerializeableContainer> getContainers() {
		return (List<SerializeableContainer>) lib.getConfigurationAPI().getConfig("containers").get("guis", new ArrayList<>());
	}
	
	public void registerContainer(SerializeableContainer cont) {
		List<SerializeableContainer> containers = this.getContainers();
		containers.add(cont);
		lib.getConfigurationAPI().getConfig("containers").set("guis", containers);
	}
}
