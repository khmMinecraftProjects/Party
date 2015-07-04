package me.khmdev.Partys.Gestores;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.khmdev.APIAuxiliar.Players.AuxPlayer;
import me.khmdev.Friends.Gestores.InventoryFriendItem;
import me.khmdev.Friends.Gestores.InventoryFriendItem.friendType;
import me.khmdev.Friends.Gestores.Factorys.IFactoryFriendItem;
import me.khmdev.Friends.Gestores.Items.IFriendItem;
import me.khmdev.Partys.Base;

public class PartyItem extends IFriendItem implements IFactoryFriendItem{
	private IFriendItem instance;

	public PartyItem() {
		super(AuxPlayer.getItem(Material.IRON_SWORD,"Invitar a party"));
		instance=this;
	}


	@Override
	public void execute(Player sender, InventoryFriendItem inventory) {
		String player=inventory.getPlayer();
		Gestor gestor=Base.getInstance().getGestor();
		Party par = gestor.getParty(sender);
		if (par == null) {
			gestor.crearParty(sender);
			par = gestor.getParty(sender);
		}
		if (par.getOwn() != sender) {
			sender.sendMessage("No tienes permisos para invitar a nadie en esta party");
			return;
		}
		if (sender.getName().equals(player)) {
			sender.sendMessage("No puedes invitarte a ti mismo");
			return;
		}
		Player pl2 = Bukkit.getPlayerExact(player);
		if (pl2 == null) {
			sender.sendMessage(player + " no esta conectado");
			return;
		}
		if (par.esta(pl2)) {
			sender.sendMessage(player + " ya esta en la partida");
			return;
		}
		if (par.tienePeticion(pl2)) {
			sender.sendMessage("Ya se ha enviado una invitacion a "
					+ player);
			return;
		}
		gestor.enviarPeticion(pl2, par);
		sender.sendMessage("Peticion enviada");
		return;
	}


	@Override
	public IFriendItem create(String pl) {
		return instance;
	}


	@Override
	public int getPriority() {
		return 4;
	}


	@Override
	public boolean show(friendType type) {
		return type==friendType.FRIEND;
	}
}
