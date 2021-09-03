package fr.picresenar.lobbypicre.minjeux;

import java.util.Arrays;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.picresenar.lobbypicre.MainLobby;
import fr.picresenar.lobbypicre.commons.Fonctions;
import fr.picresenar.lobbypicre.survival.Survival;

public class TeleportGames {
	
	@SuppressWarnings("unused")
	private MainLobby main;

	public TeleportGames(MainLobby main) {
		this.main=main;
	}

	
	public void getBookTeleportGames(Player player) {
		ItemStack LivreGames = new ItemStack(Material.BOOK);
		ItemMeta LivreGamesM = LivreGames.getItemMeta();
		
		LivreGamesM.setDisplayName("§dSe téléporter vers un mini-jeu");
		LivreGamesM.setLore(Arrays.asList("Appartient à " + player.getDisplayName()));
		
		LivreGames.setItemMeta(LivreGamesM);
		
		player.getInventory().setItem(0,LivreGames);
		player.updateInventory();
		
	}


	public Inventory openBookTeleportGames() {
		Fonctions f=new Fonctions(main);
		
		Inventory inv = Bukkit.createInventory(null, 54,"Téléportation vers un mini-jeu");
		
		Integer i=2;
		Integer j=0;
		
		inv.setItem(3, f.getSkull("Moh_", "Mode spectateur"));
		inv.setItem(4, f.getItem(Material.GOLDEN_APPLE, "Retourner au lobby", null));
		inv.setItem(5, f.getSkull("LumberjackFunk", "Configuration Mumble"));
		
		
		for (String zone : main.getConfig().getConfigurationSection("coordonnees.coordonneesjeux").getKeys(false)) {
			
			inv.setItem(i*9, f.getItem(Material.LEGACY_SIGN, ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("coordonnees.coordonneesjeux."+zone+".name")), null));
			
			for (String jeu : main.getConfig().getConfigurationSection("coordonnees.coordonneesjeux."+zone).getKeys(false)) {
				
				Material material = Material.getMaterial(main.getConfig().getString("coordonnees.coordonneesjeux."+zone+"."+jeu+".material"));
				String titre = main.getConfig().getString("coordonnees.coordonneesjeux."+zone+"."+jeu+".name");
				List<String> lore=Arrays.asList(zone,jeu);
				
				
				//if(material==null) System.out.println("Material inconnu : "+main.getConfig().getString("coordonnees.coordonneesjeux."+zone+"."+jeu+".material"));
				if(material!=null && titre!=null && lore!=null )inv.setItem((i*9)+j+1, f.getItem(material, titre,lore));
	
				j++;
			}
			
			j=0;
			i++;
		}
		
		
		return inv;
	}
	
	
	public void tp2Game(Player player,ItemStack item) {
		
		Survival s=new Survival(main);
		
		if(item==null||item.getItemMeta()==null||item.getItemMeta().getLore()==null||item.getItemMeta().getDisplayName()==null) return;
		
		ItemMeta itemM=item.getItemMeta();
		
		String Zone=itemM.getLore().toArray()[0].toString();
		String Jeu=itemM.getLore().toArray()[1].toString();
		
		Double X=main.getConfig().getDouble("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".X");
		Double Y=main.getConfig().getDouble("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".Y");
		Double Z=main.getConfig().getDouble("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".Z");
		
		Float yaw=0.0f;
		
		if(main.getConfig().get("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".yaw")!=null) yaw=(float)main.getConfig().getDouble("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".yaw");
		
		World world=null;
		
		
		//for(World w:Bukkit.getWorlds())System.out.println("World : "+w);
		
		if(main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".gamemode")!=null) {
			
			switch (main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".gamemode")) {
			
				case "SURVIVAL":
					player.setGameMode(GameMode.SURVIVAL);
					break;
					
				case "ADVENTURE":
					player.setGameMode(GameMode.ADVENTURE);
					break;
				
				case "SPECTATOR":
					player.setGameMode(GameMode.SPECTATOR);
					break;
					
				case "CREATIVE":
					player.setGameMode(GameMode.CREATIVE);
					break;				
			
			}
			
		}
		
		if(main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".world")==null) {
			 
			 System.out.println("monde texte " +main.getConfig().getString("lobbyworld"));
			
			 world=main.lobbyworld;
			 
			 System.out.println("World : " + world);
			 
				Location destination=new Location(world,X,Y,Z,yaw,0.0f);
				
				player.teleport(destination);
			 
		}else if (Jeu.equalsIgnoreCase("Survival")) {
			 world=Bukkit.getWorld(main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".world"));
			 

//			 
			 s.onJoinSurvival(player,true);
			 //System.out.println("World : "+ main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".world"));
			 
		}else {
			
			world=Bukkit.getWorld(main.getConfig().getString("coordonnees.coordonneesjeux."+Zone+"."+Jeu+".world"));
		
			Location destination=new Location(world,X,Y,Z,yaw,0.0f);

			player.teleport(destination);
			
		}
		
		

		
		
		
		
	}
	
	
}
