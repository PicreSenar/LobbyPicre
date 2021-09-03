package fr.picresenar.lobbypicre.survival;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


import fr.picresenar.lobbypicre.MainLobby;
import fr.picresenar.lobbypicre.commons.Fonctions;

public class Survival {

	private MainLobby main;

	public Survival(MainLobby main) {
		this.main=main;
	}
	
	
	@SuppressWarnings("deprecation")
	public void onJoinSurvival(Player player, Boolean tp) {
		
		Fonctions f = new Fonctions(main);
		
		if(player==null) return;
		
		//Init
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.setLevel(0);
		player.setTotalExperience(0);
		for(PotionEffect pte:player.getActivePotionEffects())player.removePotionEffect(pte.getType());	
		
		String playername;
		
		//Verif si joueur sauvegardé
		if(main.configsavesurvie.getString(player.getName())!=null) {
			
			playername=player.getName();
			System.out.println("Joueur trouvé : " + playername);
			
		}else {
			
			playername="DefaultInventory";
			System.out.println("Joueur non trouvé");
			
		}
		
		
			//stats
			player.setHealth(main.configsavesurvie.getInt(playername+".health"));
			
			player.setFoodLevel(main.configsavesurvie.getInt(playername+".food"));
			player.setLevel(main.configsavesurvie.getInt(playername+".level"));
			player.setTotalExperience(main.configsavesurvie.getInt(playername+".xp"));
			player.setFallDistance(main.configsavesurvie.getInt(playername+".falling"));
			
			
			Integer i;
						
			//inventaire
			try {
				player.getInventory().setContents(f.StringToInventory(main.configsavesurvie.getString(playername+".inventory")).getContents());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erreur sur l'inventaire : "+ (main.configsavesurvie.getString(playername+".inventory")));
			}
			
			for(i=0;i<=35;i++) {
				if (main.configsavesurvie.get(playername+".inventory.item"+i)!=null) player.getInventory().setItem(i, (ItemStack)main.configsavesurvie.get(playername+".inventory.item"+i));
			}
			
			player.getInventory().setItemInOffHand((ItemStack)main.configsavesurvie.get(playername+"."+"lefthand"));
	
	        player.getInventory().setHelmet((ItemStack)main.configsavesurvie.get(playername+"."+"armor.Helmet"));
	        player.getInventory().setChestplate((ItemStack)main.configsavesurvie.get(playername+"."+"armor.Chestplate"));
	        player.getInventory().setLeggings((ItemStack)main.configsavesurvie.get(playername+"."+"armor.Leggings"));
	        player.getInventory().setBoots((ItemStack)main.configsavesurvie.get(playername+"."+"armor.Boots"));
	        
	        
	        //effets de potion      
	        for(i=1;i<=20;i++) {
	        	if(main.configsavesurvie.get(playername+"."+"potions.potion"+i)==null)break;
	        	
	        	player.getActivePotionEffects().add((PotionEffect)main.configsavesurvie.get(playername+"."+"potions.potion"+i));
	        	
	        }
	        
	        World monde;
	
	        if (Bukkit.getWorld(main.configsavesurvie.getString(playername + ".location.world"))==null){
	        	
	        	monde=main.survivalworld;
	        	
	        	
	        } else {
	        	
	        	monde = Bukkit.getWorld(main.configsavesurvie.getString(playername + ".location.world"));
	        }
	        
	        //destination
	        Location destination = new Location(monde, 
	        		main.configsavesurvie.getDouble(playername + ".location.X"),
	        		main.configsavesurvie.getDouble(playername + ".location.Y"),
	        		main.configsavesurvie.getDouble(playername + ".location.Z"),
	        		(float)main.configsavesurvie.getDouble(playername + ".location.yaw"),
	        		(float)main.configsavesurvie.getDouble(playername + ".location.pitch"));
	        
   		   	 if(player.getLocation()!=destination&&tp)player.teleport(destination);
	        	
	        	       	
 //old death mechanic if (main.configsavesurvie.getBoolean(playername+".isdead")) {
   		    	 
   		    	
//   		    	player.sendMessage(ChatColor.RED+"Joueur mort... régénération du corps à son dernier checkpoint");
   		    	
//   		     }
	        		
 //  		     main.configsavesurvie.set(playername+".isdead",false);
   		     
   			try {
				main.configsavesurvie.save(main.filesavesurvie);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        		
	 

//        Vector velocity=(Vector)main.configsavesurvie.get(uuid + ".velocity");
//        player.setVelocity(velocity);
		
		
		//player.updateInventory();
		
		
	}
	
	
	@SuppressWarnings("deprecation")
	public void onLeaveSurvival(Player player, Boolean clearAll) {
		
		Fonctions f = new Fonctions(main);
		String playername=player.getName();
				
		//main.configsavesurvie.set(playername+".X",player.getLocation().getX());
		
		//main.configsavesurvie.set(playername.toString(),null);
		//main.configsavesurvie.set(playername+".name",player.getName());
		
		Boolean isdead;
		
		isdead=main.configsavesurvie.getBoolean(playername+".isdead");
		
		
//Old death mechanic		
//		if(isdead) {
//			
//			if(main.configsavesurvie.get(playername+".spawn")!=null) {
//				main.configsavesurvie.set(playername+".location.world",main.getConfig().getString("survivalworld"));
//				main.configsavesurvie.set(playername+".location.X",main.configsavesurvie.getDouble(playername+".spawn.X"));
//				main.configsavesurvie.set(playername+".location.Y",main.configsavesurvie.getDouble(playername+".spawn.Y"));
//				main.configsavesurvie.set(playername+".location.Z",main.configsavesurvie.getDouble(playername+".spawn.Z"));
//			}else {
//				main.configsavesurvie.set(playername+".location.world",main.getConfig().getString("survivalworld"));
//				main.configsavesurvie.set(playername+".location.X", main.getConfig().getDouble("coordonnees.coordonneesjeux.Bree.Survival.X"));
//				main.configsavesurvie.set(playername+".location.Y", main.getConfig().getDouble("coordonnees.coordonneesjeux.Bree.Survival.Y"));
//				main.configsavesurvie.set(playername+".location.Z", main.getConfig().getDouble("coordonnees.coordonneesjeux.Bree.Survival.Z"));
//				
//			}
//		
//			
//			main.configsavesurvie.set(playername+".location.yaw",player.getLocation().getYaw());
//			main.configsavesurvie.set(playername+".location.pitch",player.getLocation().getPitch());
//			
//			main.configsavesurvie.set(playername+".health",player.getMaxHealth());
//			main.configsavesurvie.set(playername+".food",20.0);
//			main.configsavesurvie.set(playername+".level",player.getLevel()/2);
//			main.configsavesurvie.set(playername+".xp",player.getTotalExperience()/2);
//			main.configsavesurvie.set(playername+".falling",0);
//			
//			
		
				
//		} else {
			
			main.configsavesurvie.set(playername+".location.world",player.getLocation().getWorld().getName());
			main.configsavesurvie.set(playername+".location.X",player.getLocation().getBlockX());
			main.configsavesurvie.set(playername+".location.Y",player.getLocation().getBlockY());
			main.configsavesurvie.set(playername+".location.Z",player.getLocation().getBlockZ());
			main.configsavesurvie.set(playername+".location.yaw",player.getLocation().getYaw());
			main.configsavesurvie.set(playername+".location.pitch",player.getLocation().getPitch());
			
			main.configsavesurvie.set(playername+".health",player.getHealth());
			main.configsavesurvie.set(playername+".food",player.getFoodLevel());
			main.configsavesurvie.set(playername+".level",player.getLevel());
			main.configsavesurvie.set(playername+".xp",player.getTotalExperience());
			main.configsavesurvie.set(playername+".falling",player.getFallDistance());
			
		
			
			Integer i=0;
			
			main.configsavesurvie.set(playername+".potions",null);
			
			for(PotionEffect pte:player.getActivePotionEffects()) {
				main.configsavesurvie.set(playername+".potions.potion"+i,pte);	
				i++;
			}
			
			
//		}
		
		
		main.configsavesurvie.set(playername+".inventory",null);
		
		for(i=0;i<=35;i++) {
			
			if(player.getInventory().getItem(i)!=null) main.configsavesurvie.set(playername+".inventory.item"+i,player.getInventory().getItem(i));
			
		}

		
		//main.configsavesurvie.set(playername+".inventory",f.InventoryToString(player.getInventory()));

		main.configsavesurvie.set(playername+".lefthand",player.getInventory().getItemInOffHand());
		
		main.configsavesurvie.set(playername+".armor.Helmet",player.getInventory().getHelmet());
		main.configsavesurvie.set(playername+".armor.Chestplate",player.getInventory().getChestplate());
		main.configsavesurvie.set(playername+".armor.Leggings",player.getInventory().getLeggings());
		main.configsavesurvie.set(playername+".armor.Boots",player.getInventory().getBoots());
		

		//main.configsavesurvie.set(playername+".velocity",player.getVelocity());
		
		
		try {
			main.configsavesurvie.save(main.filesavesurvie);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur à l'enregistrement du fichier");
		}
		
		
		if (clearAll==true) 
		{
			player.getInventory().clear();
			f.RemoveArmor(player);
			
			player.getActivePotionEffects().clear();
			player.setTotalExperience(0);
			player.setLevel(0);
			
			if(player.getHealth()!=0.0)player.setHealth(player.getMaxHealth());
			
			player.setGameMode(GameMode.ADVENTURE);
			
			player.updateInventory();

		}
	}
	
	
//	public void majSave() {
//		
//		
//		
//		
//	}
//	
	
}
