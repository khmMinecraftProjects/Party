package me.khmdev.Partys;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;



public class init extends JavaPlugin{
	private Base base;


	public void onEnable() {
		if (!hasPluging("APIGames")) {
			getLogger().severe(
					getName()
							+ " se desactivo debido ha que no encontro la API");
			setEnabled(false);
			return;
		}
		base=new Base(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (base.onCommand(sender, cmd, label, args)) {
			return true;
		}

		return false;
	}


	private static boolean hasPluging(String s) {
		try {
			return Bukkit.getPluginManager().getPlugin(s).isEnabled();
		} catch (Exception e) {

		}
		return false;
	}
	@Override
	public void onDisable(){

	}

}
