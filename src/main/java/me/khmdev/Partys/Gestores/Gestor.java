package me.khmdev.Partys.Gestores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class Gestor {
	protected static HashMap<Player, Party> partys = new HashMap<>();
	protected static HashMap<Player, List<Party>> peticiones = new HashMap<>();

	public HashMap<Player, Party> getPartys() {
		return partys;
	}

	public Party getParty(Player pl) {
		return partys.get(pl);
	}

	public boolean enviarPeticion(Player entrante, Party par) {
		List<Party> party = peticiones.get(entrante);
		if (party == null) {
			party = new ArrayList<>();
			peticiones.put(entrante, party);
		}
		if (party.contains(par)) {
			return false;
		}
		party.add(par);
		par.addPeticion(entrante);
		entrante.sendMessage("Has recibido una invitacion para una party de "
				+ par.getOwn().getName() + "\nAcepta con   /party aceptar "
				+ par.getOwn().getName() + "\nEliminala con /party cancelar "
				+ par.getOwn().getName());
		return true;
	}

	public boolean aceptarPeticion(Player entrante, Player own) {
		List<Party> party = peticiones.get(entrante);
		if (party == null) {
			return false;
		}
		for (Party par : party) {
			if (par.getOwn() == own) {
				par.addComponentes(entrante);
				partys.put(entrante, par);
				par.sendAll(entrante.getName() + " se ha unido a la party");

				return true;
			}
		}

		return false;
	}

	public void crearParty(Player pl) {
		if (partys.containsKey(pl)) {
			salirDeParty(pl);
		}
		partys.put(pl, new Party(pl));
	}

	public void removeParty(Party party) {
		for (Player pl : party.getComponentes()) {
			partys.remove(pl);
			pl.sendMessage("La partida de " + party.getOwn().getName()
					+ " fue eliminada");

		}
		for (Player pl : party.getPeticiones()) {
			peticiones.remove(pl);
			pl.sendMessage("La partida de " + party.getOwn().getName()
					+ " fue eliminada");
		}
	}

	public boolean salirDeParty(Player pl) {
		Party par = getParty(pl);
		if (par == null) {
			return false;
		}
		par.abandonarParty(pl);
		return true;
	}

	public HashMap<Player, List<Party>> getPeticiones() {
		return peticiones;
	}

	public List<Party> getPeticiones(Player pl) {
		List<Party> l = peticiones.get(pl);
		if (l == null) {
			l = new ArrayList<>();
		}
		return l;
	}

	public List<Player> getPeticionesRealizadas(Player emisor) {
		Party par = getParty(emisor);
		if (par == null || par.getOwn() != emisor) {
			return new ArrayList<>();
		}
		return par.getPeticiones();
	}

	public boolean cancelarPeticion(Player entrante, Player own) {
		List<Party> party=peticiones.get(entrante);
		if(party==null){
			return false;
		}
		Party e=null;
		for (Party par : party) {
			if(par.getOwn()==own){
				par.cancelarPeticion(entrante);
				par.sendOwn(entrante.getName()+" ha cancelado la invitacion");
				e=par;
				break;
			}
		}
		if(e!=null){
			party.remove(e);
			return true;
		}else{
		return false;
	}}

	public Party getMyParty(Player player) {
		Party p=getParty(player);
		return p!=null&&p.getOwn()!=player?null:p;
	}
}
