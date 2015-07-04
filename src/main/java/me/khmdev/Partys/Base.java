package me.khmdev.Partys;

import java.util.List;

import me.khmdev.Partys.Gestores.Gestor;
import me.khmdev.Partys.Gestores.Party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Base  {
	private Gestor gestor;
	private static Base instance;

	public Base(JavaPlugin pl) {
		instance = this;
		gestor = new Gestor();
		Bukkit.getServer().getPluginManager().registerEvents(
				new ListenerParty(gestor), pl);
	}

	

	public static String peticiones(List<String> p) {
		if (p.size() == 0) {
			return "";
		}
		String s = "Tienes peticiones de amistad de: ";
		for (String ss : p) {
			s += ss + " ";
		}
		s += "\nAcepta la peticion con  /amigos aceptar <usuario>";
		s += "\nCancela la peticion con /amigos cancelar <usuario>";
		return s;
	}

	private static String help() {
		String s = "";
		s += "/party crear         crea una party\n";
		s += "/party salir         sal de la party\n";
		s += "/party componentes   lista todos los componentes de la party\n";
		s += "/party invitaciones  lista todas las invitaciones pendientes\n";
		s += "/party invEnviadas   lista todas las invitaciones enviadas\n";

		s += "/party invitar <Usuario>   agrega un usuario a la party\n";
		s += "/party aceptar <Usuario>  acepta una invitacion a una party\n";
		s += "/party cancelar <Usuario> rechaza una solicitud de amistad\n";
		return s;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("party")) {
			Player pl = Bukkit.getPlayerExact(sender.getName());
			if (pl == null) {
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(help());
				return true;
			}
			if (args[0].equalsIgnoreCase("crear")) {
				Party par = gestor.getParty(pl);
				if (par != null) {
					sender.sendMessage("Ya estas en una party");
					return true;
				}
				gestor.crearParty(pl);
				sender.sendMessage("Party creada");
				return true;
			}
			if (args[0].equalsIgnoreCase("salir")) {
				Party par = gestor.getParty(pl);
				if (par == null) {
					sender.sendMessage("No esta en ninguna party");
					return true;
				}
				gestor.salirDeParty(pl);
				sender.sendMessage("Has salido de la party");
				return true;

			} else if (args[0].equalsIgnoreCase("componentes")) {
				Party par = gestor.getParty(pl);
				if (par == null) {
					sender.sendMessage("No esta en ninguna party");
					return true;
				}
				List<Player> l = par.getComponentes();
				if (l.size() == 0) {
					sender.sendMessage("Aun no hay nadie en la party");
					return true;
				}
				if (par.getComponentes().size() == 0) {
					sender.sendMessage("Aun no hay nadie en la party");
					return true;
				}
				sender.sendMessage("Componentes: ");
				for (Player pla : l) {
					sender.sendMessage("\n" + pla.getName());
				}
				return true;
			} else if (args[0].equalsIgnoreCase("invEnviadas")) {
				List<Player> l = gestor.getPeticionesRealizadas(pl);
				if (l.size() == 0) {
					sender.sendMessage("No has enviado invitaciones");
					return true;
				}
				sender.sendMessage("Invitaciones enviadas:");
				for (Player pla : l) {
					sender.sendMessage("\n" + pla.getName());
				}
				return true;
			} else if (args[0].equalsIgnoreCase("invitaciones")) {
				List<Party> l = gestor.getPeticiones(pl);
				if (l.size() == 0) {
					sender.sendMessage("No has recibido invitaciones");
					return true;
				}
				sender.sendMessage("Invitaciones:");
				for (Party pla : l) {
					sender.sendMessage("\n" + pla.getOwn().getName());
				}
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(help());
				return true;
			}
			if (args[0].equalsIgnoreCase("invitar")) {
				Party par = gestor.getParty(pl);
				if (par == null) {
					gestor.crearParty(pl);
					par = gestor.getParty(pl);	
				}
				if (par.getOwn() != pl) {
					sender.sendMessage("No tienes permisos para invitar a nadie en esta party");
					return true;
				}
				if (sender.getName().equals(args[1])) {
					sender.sendMessage("No puedes invitarte a ti mismo");
					return true;
				}
				Player pl2 = Bukkit.getPlayerExact(args[1]);
				if (pl2 == null) {
					sender.sendMessage(args[1] + " no esta conectado");
					return true;
				}
				if (par.esta(pl2)) {
					sender.sendMessage(args[1] + " ya esta en la party");
					return true;
				}
				if (par.tienePeticion(pl2)) {
					sender.sendMessage("Ya se ha enviado una invitacion a "
							+ args[1]);
					return true;
				}
				gestor.enviarPeticion(pl2, par);
				sender.sendMessage("Peticion enviada");
				return true;
			} else if (args[0].equalsIgnoreCase("aceptar")) {
				Player pl2 = Bukkit.getPlayerExact(args[1]);
				if (pl2 == null) {
					sender.sendMessage(args[1] + " no esta conectado");
					return true;
				}
				if (gestor.aceptarPeticion(pl, pl2)) {
					sender.sendMessage("Te has unido a la party de " + args[1]);
				} else {
					sender.sendMessage("No ha recibido ninguna invitacion de "
							+ args[1]);
				}

				return true;
			} else if (args[0].equalsIgnoreCase("cancelar")) {
				Player pl2 = Bukkit.getPlayerExact(args[1]);
				if (pl2 == null) {
					sender.sendMessage(args[1] + " no esta conectado");
					return true;
				}
				if (gestor.cancelarPeticion(pl, pl2)) {
					sender.sendMessage("Has cancelado la invitacion de "
							+ args[1]);
				} else {
					sender.sendMessage("No ha recibido ninguna invitacion de "
							+ args[1]);
				}
				return true;
			}
			sender.sendMessage(help());
			return true;
		}
		return false;
	}

	public static Base getInstance() {
		return instance;
	}

	public Gestor getGestor() {
		return gestor;
	}
}
