package gargant.sudogui.main;

import org.bukkit.plugin.java.JavaPlugin;

import gargant.sudogui.commands.ContainerCommand;
import gargant.sudogui.containers.ContainerEditor;
import gargant.sudogui.containers.ContainerManager;
import gargant.sudogui.containers.SudoContainer;
import masecla.mlib.main.MLib;

public class SudoGUI extends JavaPlugin {
	
	private MLib lib;
	
	private ContainerManager containerManager;
	private SudoContainer sudoContainer;
	private ContainerEditor containerEditor;
	
	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		lib.getConfigurationAPI().requireAll();
		
		this.containerManager = new ContainerManager(lib);

		this.sudoContainer = new SudoContainer(lib);
		this.sudoContainer.register();
		
		this.containerEditor =  new ContainerEditor(lib, containerManager);
		this.containerEditor.register();
		
		this.containerManager.updateDependencies(sudoContainer, containerEditor);
		
		new ContainerCommand(lib, containerManager).register();
	}
}
