package fr.picresenar.lobbypicre.gameoftaupes;

import java.util.Arrays;
import java.util.Collection;


import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.picresenar.lobbypicre.MainLobby;
import fr.picresenar.lobbypicre.commons.Fonctions;

public class Meetup {

	
	@SuppressWarnings("unused")
	private MainLobby main;

	public Meetup(MainLobby main) {
		this.main=main;
	}

	public void giveAdminBook(Player player) {
		
		
		ItemStack LivreEquipes = new ItemStack(Material.BOOK);
		ItemMeta LivreEquipesM = LivreEquipes.getItemMeta();
		
		LivreEquipesM.setDisplayName("§cRépartition en équipe");
		LivreEquipesM.setLore(Arrays.asList("Lors du GotMeetup,","permet de parquer les joueurs","comme les animaux qu'ils sont.", "Appartient à " + player.getDisplayName()));
		
		LivreEquipes.setItemMeta(LivreEquipesM);
		
		player.getInventory().setItemInHand(LivreEquipes);		
		//player.updateInventory();
		
		player.sendMessage("§2[Game of Taupes] §aTu peux maintenant gérer le parquage des anim... des joueurs");
		
	}
	
	public Inventory openMeetupBookPlayers() {
		
		Fonctions f=new Fonctions(main);
		
		
		Collection<? extends Player> players=Bukkit.getOnlinePlayers();
		
		
		Integer size =  Math.round(players.toArray().length/9)*9+9;
		
		if(players.toArray().length==0)return null;
		
		Inventory inv = Bukkit.createInventory(null, size,"Choisissez le joueur à déplacer");
		
		Integer i=0;
		for(Player currentP:players) {
			inv.setItem(i, f.getSkull(currentP.getDisplayName(), currentP.getDisplayName()));
			i++;
		}
		
				
		return inv;
		
		
	}
	
	
	public Inventory openMeetupBookTeams(Player player) {
		Fonctions f=new Fonctions(main);
		
		Inventory inv = Bukkit.createInventory(null, 9,"Destination de : " +player.getName());
		
		inv.setItem(0, f.getBanner("Rose", DyeColor.PINK));
		inv.setItem(1, f.getBanner("Jaune", DyeColor.YELLOW));
		inv.setItem(2, f.getBanner("Violette", DyeColor.PURPLE));
		inv.setItem(3, f.getBanner("Cyan", DyeColor.CYAN));
		inv.setItem(4, f.getBanner("Verte", DyeColor.GREEN));
		inv.setItem(5, f.getBanner("Grise", DyeColor.GRAY));
		
		return inv;
	}
	
	
	
	
	
}
