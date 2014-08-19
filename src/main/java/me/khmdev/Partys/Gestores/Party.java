package me.khmdev.Partys.Gestores;

import java.util.ArrayList;
import java.util.List;

import me.khmdev.Partys.Base;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Party {
	private Player own;
	private List<Player> componentes=new ArrayList<>(),
			peticiones=new ArrayList<>();
	public Party(Player pl){
		own=pl;
	}
	
	public List<Player> getComponentes() {
		return componentes;
	}
	public void addComponentes(Player pl) {
		componentes.add(pl);
		peticiones.remove(pl);
	}
	
	public List<Player> getPeticiones() {
		return peticiones;
	}
	public void addPeticion(Player pl) {
		peticiones.add(pl);
	}
	public void cancelarPeticion(Player pl) {
		peticiones.remove(pl);
	}
	
	public Player getOwn() {
		return own;
	}
	public void setOwn(Player own) {
		this.own = own;
	}
	public void sendAll(String string) {
		sendOwn(string);
		for (Player pl : componentes) {
			pl.sendMessage(ChatColor.DARK_PURPLE+"<Party de "+own.getName()+">"
		+ChatColor.WHITE+" "+string);
		}
	}
	public void abandonarParty(Player pl) {
		if(own==pl){
			Base.getInstance().getGestor().removeParty(this);
		}else{
			componentes.remove(pl);
		}
	}

	public boolean esta(Player pl2) {
		return own==pl2||componentes.contains(pl2);
	}
	
	public boolean tienePeticion(Player pl2) {
		return peticiones.contains(pl2);
	}

	public void sendOwn(String string) {
		own.sendMessage(ChatColor.DARK_PURPLE+"<Party>"
				+ChatColor.WHITE+" "+string);
	}

	public void senToPlayer(Player pl, String string) {
		pl.sendMessage(ChatColor.DARK_PURPLE+"<Party de "+own.getName()+">"
				+ChatColor.WHITE+" "+string);
	}
}
