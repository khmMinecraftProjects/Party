package me.khmdev.Partys;

import java.util.Collections;
import java.util.List;

import me.khmdev.APIGames.ListenAPIG.jugador.JugadorEntraEvent;
import me.khmdev.APIGames.Partidas.IPartida;
import me.khmdev.Partys.Gestores.Gestor;
import me.khmdev.Partys.Gestores.Party;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerParty implements Listener{
	private Gestor gestor;

	public ListenerParty(Gestor g){
		gestor=g;
	}
	@EventHandler
	public void logeOut(PlayerJoinEvent e) {
		gestor.salirDeParty(e.getPlayer());
	}

	@EventHandler
	public void jugadorEntra(JugadorEntraEvent e) {
		Party p = gestor.getMyParty(e.getJugador().getPlayer());
		if (p == null) {
			return;
		}
		List<Player> comp = p.getComponentes();
		Collections.shuffle(comp);
		IPartida par = e.getPartida();
		for (Player pl : comp) {
			if (par.getMax() <= par.getNJug()) {
				p.senToPlayer(pl, "La partida esta llena, no has podido unirte con la party");
			} else {
				if (par.nuevoGoJugador(pl)) {
					p.senToPlayer(pl, "Te has unido a una partida");
				} else {
					p.senToPlayer(pl, "No has podido unirte a una partida");
				}
			}
		}
	}
}
