package gargant.sudogui.commands;

import org.bukkit.entity.Player;

import gargant.sudogui.containers.ContainerManager;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.annotations.SubcommandInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

@RegisterableInfo(command = "sudogui")
@RequiresPlayer
public class ContainerCommand extends Registerable{

	private ContainerManager containerManager;
	
	public ContainerCommand(MLib lib,ContainerManager containerManager) {
		super(lib);
		this.containerManager = containerManager;
	}

	@SubcommandInfo(subcommand = "delete", permission = "sudogui.delete")
	public void onDelete(Player p, String container) {
		
	}
}
