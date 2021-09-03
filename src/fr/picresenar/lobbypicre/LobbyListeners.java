package fr.picresenar.lobbypicre;


import java.beans.BeanDescriptor;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.picresenar.lobbypicre.commons.Fonctions;
import fr.picresenar.lobbypicre.gameoftaupes.Meetup;
import fr.picresenar.lobbypicre.minjeux.TeleportGames;
import fr.picresenar.lobbypicre.survival.Survival;




public class LobbyListeners implements Listener {
	
	
	@SuppressWarnings("unused")
	private MainLobby main;
	
	public LobbyListeners(MainLobby lobbyPicre) {
		this.main=lobbyPicre;
	}

	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {
		
		Player player = event.getPlayer();
		if(player.getWorld()==main.forbiddenworld) return;
		
		Location destination=event.getRespawnLocation();
		Fonctions f=new Fonctions(main);
		TeleportGames tpG=new TeleportGames(main);
		//Survival s=new Survival(main);
		
		Double Xlobby=main.Xlobby;
		Double Ylobby=main.Ylobby;
		Double Zlobby=main.Zlobby;
		World world=main.lobbyworld;
		
		Location min=new Location(world,Xlobby-50,0.0,Zlobby-50);
		Location max=new Location(world,Xlobby+50,Ylobby+50,Zlobby+50);
		
		if(destination.getWorld()==Bukkit.getWorld(main.getConfig().getString("lobbyworld"))&&f.isBetweenLocations(destination, min, max)) {
			tpG.getBookTeleportGames(player);
		}

	}
	
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if(player.getWorld()!=main.lobbyworld) return;
		
		Entity entity=event.getRightClicked();
		
		if(entity.getType()==EntityType.ITEM_FRAME&&player.getGameMode()!=GameMode.CREATIVE) {
			ItemFrame itemframe=(ItemFrame)entity;
			if(itemframe.getItem().getType()==Material.FILLED_MAP) {
				//player.sendMessage("Impossible de modifier ce panneau");
				event.setCancelled(true);
			}
		}
		
	}
	
	
	@EventHandler
	public void OnDamageOnEntity(EntityDamageByEntityEvent event) {
			
		Entity entity=event.getEntity();
		if(entity.getWorld()!=main.lobbyworld) return;
		
		if(event.getDamager() instanceof Player) {
			
			Player player = (Player)event.getDamager();
			
			if(entity.getType()==EntityType.ITEM_FRAME&& player.getGameMode()!=GameMode.CREATIVE) {
				ItemFrame itemframe=(ItemFrame)entity;
				if(itemframe.getItem().getType()==Material.FILLED_MAP) {
					event.setCancelled(true);
				}
			}

		}else if(entity instanceof ItemFrame) {
				event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		if(player.getWorld()==main.forbiddenworld) return;
		
		Action action = event.getAction();
		ItemStack it = event.getItem();
		//Material block=event.getMaterial();
		
		Meetup m=new Meetup(main);
		TeleportGames tpG=new TeleportGames(main);
		

		if(it!=null) {
			ItemMeta itM=it.getItemMeta();
		
			//Livres de commandes
			if((action==Action.RIGHT_CLICK_AIR||action==Action.RIGHT_CLICK_BLOCK)&& it.getType()==Material.BOOK && itM.getDisplayName()!=null) {
				if(itM.getDisplayName().equalsIgnoreCase("§cRépartition en équipe")) {
					
					Inventory inv=m.openMeetupBookPlayers();
					player.openInventory(inv);
					
					return;
					
				}else if(itM.getDisplayName().equalsIgnoreCase("§dSe téléporter vers un mini-jeu")) {
					
					Inventory inv=tpG.openBookTeleportGames();
					player.openInventory(inv);
					
					return;
					
				}
				
			}
		}
		
		if (event.getAction().equals(Action.PHYSICAL)&&player.getWorld()==main.lobbyworld)
	    {
	        if (event.getClickedBlock().getType() == Material.FARMLAND) {
	        	event.setCancelled(true);
	            if (player != null) player.sendTitle("§cSortez de mon champ !!!","");
	        }
	    }
		
		
		
		if(player.getWorld()==main.survivalworld){
			
			if(event.getClickedBlock()==null)return;
			
			//Sauvegarde des checkpoints
			if(event.getClickedBlock().getType().toString().contains("_BED")&& event.getAction()==Action.RIGHT_CLICK_BLOCK){
				
				
				main.configsavesurvie.set(player.getName()+".spawn.X",player.getLocation().getBlockX());
				main.configsavesurvie.set(player.getName()+".spawn.Y",player.getLocation().getBlockY());
				main.configsavesurvie.set(player.getName()+".spawn.Z",player.getLocation().getBlockZ());
				main.configsavesurvie.set(player.getName()+".spawn.yaw",player.getLocation().getYaw());
				main.configsavesurvie.set(player.getName()+".spawn.pitch",player.getLocation().getPitch());
//				player.sendMessage(ChatColor.AQUA+"Checkpoint enregistré");
				
				try {
					main.configsavesurvie.save(main.filesavesurvie);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(player.getWorld().getTime()>12500&&main.isSomeoneAsleep==false) {
					main.isSomeoneAsleep=true;
					for(Player OnlinePlayer:Bukkit.getOnlinePlayers()) {
						OnlinePlayer.sendMessage(ChatColor.YELLOW+ player.getName() + " est allé se coucher. Bonne nuit !");
					}
					
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					    @Override
					    public void run() {
					    	player.getWorld().setTime(23999);
					    	main.isSomeoneAsleep=false;
					    }
					}, 60L);
					
				}
				
			}
		}
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		Player player =(Player) event.getWhoClicked();
		if(player.getWorld()!=main.lobbyworld) return;
		ItemStack current = event.getCurrentItem();

		Fonctions f= new Fonctions(main);
		Meetup m=new Meetup(main);
		TeleportGames tpG=new TeleportGames(main);
		
		if(current==null) return;
		ItemMeta currentM =current.getItemMeta();
		
		if(event.getView().getTitle().equalsIgnoreCase("Choisissez le joueur à déplacer")) {
			
			event.setCancelled(true);

			if(current.getType()==Material.PLAYER_HEAD) {
				
				String NomJoueurSelect=currentM.getDisplayName();
				
				if(NomJoueurSelect==null) return;
				
				@SuppressWarnings("deprecation")
				Player JoueurSelect=Bukkit.getPlayer(NomJoueurSelect);
					
				if (JoueurSelect==null) return;	
				
				player.openInventory(m.openMeetupBookTeams(JoueurSelect));
				
			}
		
		}else if(event.getView().getTitle().equalsIgnoreCase("Téléportation vers un mini-jeu")) {
			
			event.setCancelled(true);
			if(currentM==null||currentM.getDisplayName()==null) return;
			
			if(currentM.getDisplayName().equalsIgnoreCase("Retourner au lobby")) {
				
				Double Xlobby=main.Xlobby;
				Double Ylobby=main.Ylobby;
				Double Zlobby=main.Zlobby;
				World world=main.lobbyworld;
				
				Location destination=new Location(world,Xlobby,Ylobby,Zlobby);
				
				
				player.teleport(destination);
				
				player.getActivePotionEffects().clear();
				player.getInventory().clear();
				tpG.getBookTeleportGames(player);
				
				
				
				return;
				
			}else if(currentM.getDisplayName().equalsIgnoreCase("Mode spectateur")) {
				
				player.setGameMode(GameMode.SPECTATOR);
				player.sendMessage("§dVous êtes maintenant spectateur, déconnectez-vous puis reconnectez vous pour revenir à la normale");
				return;
				
			}else if(currentM.getDisplayName().equalsIgnoreCase("Configuration Mumble")) {
				
				World world=main.lobbyworld;
				
				Location destination=new Location(world,-8377.0,22.0,-10598.0);
				
				player.teleport(destination);
				
				return;
				
			}

			
			tpG.tp2Game(player, current);
			return;
			
			

		}else if(event.getView().getTitle()!=null) {

			if(current.getType()==Material.BLACK_BANNER) {
				
				Player JoueurSelect=null;
				
								
				
				for(Player pl:Bukkit.getOnlinePlayers()) {
				
					if(event.getView().getTitle().equalsIgnoreCase("Destination de : "+pl.getName())) {
						JoueurSelect=pl;
						break;
					}
					
				}
				
				if (JoueurSelect==null) return;
				
				if(currentM.getDisplayName()==null) return;
				
				event.setCancelled(true);
				
				String Team=currentM.getDisplayName();
				
				Double X=main.getConfig().getDouble("coordonnees.coordonneescages."+Team+".X");
				Double Y=main.getConfig().getDouble("coordonnees.coordonneescages."+Team+".Y");
				Double Z=main.getConfig().getDouble("coordonnees.coordonneescages."+Team+".Z");
				
				World world=main.lobbyworld;
				
				Location destination=new Location(world,X,Y,Z);
				
				JoueurSelect.teleport(destination);
				
			}
		
		
		
		}
		
		
		
	}
	
	@EventHandler
	public void onTP(PlayerTeleportEvent event) {
		
		Location destination=event.getTo();
		
		Player player=event.getPlayer();
		if(player.getWorld()==main.forbiddenworld) return;
		
		Fonctions f=new Fonctions(main);
		TeleportGames tpG=new TeleportGames(main);
		Survival s=new Survival(main);
		
		World worldlobby= main.lobbyworld;
		World worldsurvie = main.survivalworld;
		World worldsurvienether = main.survivalworld_nether;
		World worldsurvieend = main.survivalworld_end;

		Boolean clearAll=false;
		
		
				
		if(event.getFrom().getWorld()==worldlobby&&(event.getTo().getWorld()==worldsurvie || event.getTo().getWorld()==worldsurvienether ||event.getTo().getWorld()==worldsurvieend)) {
			player.sendMessage(ChatColor.YELLOW+"Téléportation vers le monde survie");
			s.onJoinSurvival(player,false);
		}
		
		
		if(player.getWorld()==main.lobbyworld) {
			Double Xlobby=main.Xlobby;
			Double Ylobby=main.Ylobby;
			Double Zlobby=main.Zlobby;
			
			World world=main.lobbyworld;
			Location min=new Location(world,Xlobby-10,0.0,Zlobby-10);
			Location max=new Location(world,Xlobby+10,Ylobby+100,Zlobby+10);
			
			
			if(f.isBetweenLocations(destination, min, max)) {
				tpG.getBookTeleportGames(player);
			}
			
			Double Xcage=main.getConfig().getDouble("coordonnees.coordonneescages.Principale.X");
			Double Ycage=main.getConfig().getDouble("coordonnees.coordonneescages.Principale.Y");
			Double Zcage=main.getConfig().getDouble("coordonnees.coordonneescages.Principale.Z");
			
			min=new Location(world,Xcage-5,Ycage-5,Zcage-5);
			max=new Location(world,Xcage+5,Ycage+5,Zcage+5);
			
			if(f.isBetweenLocations(destination, min, max)) {
				
				player.getInventory().clear();
				player.getInventory().setItem(0, f.getItem(Material.BLACK_BANNER, ChatColor.GOLD+"Choisir son equipe", null));
				
			}
			
			
			
			
		}
		

	}
	
	@EventHandler
	public void OnDamage(EntityDamageEvent event) {
		
		if(event.getEntity().getWorld()==main.forbiddenworld)return;
		
		if (event.getEntity() instanceof Player) {
		
			Player player=(Player)event.getEntity();
			Fonctions f=new Fonctions(main);
			TeleportGames tpG=new TeleportGames(main);
			Survival s=new Survival(main);
				
			Double Xlobby=main.Xlobby;
			Double Ylobby=main.Ylobby;
			Double Zlobby=main.Zlobby;
			
			World world=main.lobbyworld;
			
			Location min=new Location(world,Xlobby-50,0.0,Zlobby-50);
			Location max=new Location(world,Xlobby+50,Ylobby+50,Zlobby+50);
			
//			
//			World worldsurvie=main.survivalworld;
//			World worldsurvie_nether=main.survivalworld_nether;
//			World worldsurvie_the_end=main.survivalworld_end;
			
//			if(player.getWorld()==worldsurvie || player.getWorld()==worldsurvie_nether || player.getWorld()==worldsurvie_the_end ){ 
//				
//					if(event.getFinalDamage()>=player.getHealth()&&!(player.getInventory().getItemInMainHand().getType()==Material.TOTEM_OF_UNDYING||player.getInventory().getItemInOffHand().getType()==Material.TOTEM_OF_UNDYING)) {
//				
//						event.setCancelled(true);
//						
////old death mechanic	main.configsavesurvie.set(player.getName()+".isdead",true);
//						
//						for(Player OnlinePlayer:Bukkit.getOnlinePlayers()) {
//							if (OnlinePlayer!=player) OnlinePlayer.sendMessage(ChatColor.RED + player.getName() + " est mort sur le serveur survie ("+event.getCause()+")");
//							if (OnlinePlayer==player) player.sendTitle("§cYOU DIED","");
//						}
//					
////old death mechanic	Location destination=new Location(world,Xlobby,Ylobby,Zlobby);
//						
//						String playername=player.getName();
//						
//						player.setHealth(20.0);
//						player.setFoodLevel(20);
//						player.setLevel(player.getLevel()/2);
//						player.setFallDistance(0);
//						player.getActivePotionEffects().clear();
//
////old death mechanic	s.onLeaveSurvival(player, true);
//						Location destination=new Location(worldsurvie,main.configsavesurvie.getDouble("DefaultInventory.location.X"),main.configsavesurvie.getDouble("DefaultInventory.location.Y"),main.configsavesurvie.getDouble("DefaultInventory.location.Z"));
//						
//						//Checkpoint detection
//						if(main.configsavesurvie.get(playername+".spawn")!=null) {
//							
//							player.sendMessage(ChatColor.RED+"Régénération du corps à son dernier checkpoint");
//							destination=new Location(worldsurvie,main.configsavesurvie.getDouble(playername+".spawn.X"),main.configsavesurvie.getDouble(playername+".spawn.Y"),main.configsavesurvie.getDouble(playername+".spawn.Z"));
//							
//						}else {
//							
//							player.sendMessage(ChatColor.RED+"Aucun checkpoint détecté : Régénération du corps à l'emplacement par défaut");
//	
//						}
//						
//						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60, 20));
//						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60, 20));
//						player.playSound(player.getLocation(),Sound.ENTITY_WITHER_DEATH , 100F, 0.01F);
//						
//						player.teleport(destination);
//						
////						try {
////							main.configsavesurvie.save(main.filesavesurvie);
////						} catch (IOException e) {
////							e.printStackTrace();
////						}
//						
//					}
//				
//			}
			
			
			if(event.getCause()== DamageCause.FALL && player.getWorld()==main.lobbyworld&&f.isBetweenLocations(player.getLocation(), min, max)) {
				
				tpG.getBookTeleportGames(player);
				event.setCancelled(true);
			
			}
		
		}

		
		
		
	}
	
		
	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		
		Player player = event.getPlayer();
		
		if(player.getWorld()==main.lobbyworld||player.getWorld()==main.forbiddenworld)return;	

		String playername = player.getName();
		
		if(player.getWorld()==main.survivalworld_end) {
			
			event.setCancelled(true);
			
			Location destination=new Location(main.survivalworld,main.configsavesurvie.getDouble("DefaultInventory.location.X"),main.configsavesurvie.getDouble("DefaultInventory.location.Y"),main.configsavesurvie.getDouble("DefaultInventory.location.Z"));
			
			//Checkpoint detection
			if(main.configsavesurvie.get(playername+".spawn")!=null) {
				
				destination=new Location(main.survivalworld,main.configsavesurvie.getDouble(playername+".spawn.X"),main.configsavesurvie.getDouble(playername+".spawn.Y"),main.configsavesurvie.getDouble(playername+".spawn.Z"));
				
			}
			
			event.setTo(destination);
			
			
		}
		
		
//		Double X=player.getLocation().getX();
//		Double Y=player.getLocation().getY();
//		Double Z=player.getLocation().getZ();
//		
//		Double x;
//		Double y;
//		Double z;
//		
//		
//		
//		
//		for(x=X-5;x<=X+5;x++) {
//			for(y=Y-5;y<=Y+5;y++) {
//				for(z=Z-5;z<=Z+5;z++) {
//					
//					Location blocscan=new Location(player.getWorld(),x,y,z);
//
//					if(player.getWorld().getBlockAt(blocscan).getType()==Material.END_PORTAL) {
//						if(player.getWorld()==worldsurvie) {
////							player.teleport(new Location(worldsurvie_the_end, 
////					        		100.0,
////					        		53.0,
////					        		0.0,
////					        		(float)main.configsavesurvie.getDouble(playername + ".location.yaw"),
////					        		(float)main.configsavesurvie.getDouble(playername + ".location.pitch")));
//						}else if(player.getWorld()==main.survivalworld_end) {
//							
//							event.setCancelled(true);
//							
//							Location destination=new Location(worldsurvie,main.configsavesurvie.getDouble("DefaultInventory.location.X"),main.configsavesurvie.getDouble("DefaultInventory.location.Y"),main.configsavesurvie.getDouble("DefaultInventory.location.Z"));
//							
//							//Checkpoint detection
//							if(main.configsavesurvie.get(playername+".spawn")!=null) {
//								
////								player.sendMessage(ChatColor.RED+"Régénération du corps à son dernier checkpoint");
//								destination=new Location(worldsurvie,main.configsavesurvie.getDouble(playername+".spawn.X"),main.configsavesurvie.getDouble(playername+".spawn.Y"),main.configsavesurvie.getDouble(playername+".spawn.Z"));
//								
//							}
//							
//							player.teleport(destination);
//							
//							
//						}
//						
//					} else if(player.getWorld().getBlockAt(blocscan).getType()==Material.NETHER_PORTAL){
//						
//					}
//				}
//			}
//		}
	}
	
	
	
	@EventHandler
	public void onBlockDamage(BlockBreakEvent event) {
		
      Block block = event.getBlock();
      Player player = event.getPlayer();
      
      if (player.getWorld()!=main.lobbyworld||player.isOp()) return;
      
      if(block.getType()==Material.BEDROCK||block.getType()==Material.BARRIER) event.setCancelled(true);
        	
    }
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
      ItemStack it = event.getItem();
      Player player = event.getPlayer();
//      World forbiddenworld=main.forbiddenworld;
      
      if(it==null||event.getAction()==null||it.getType()==null)return;
      
      if (player.getWorld()!=main.lobbyworld||player.isOp())return;
      
      if(event.getAction()==Action.RIGHT_CLICK_BLOCK&&(it.getType()==Material.BEDROCK||it.getType()==Material.BARRIER)) event.setCancelled(true);

    }
	
	
	
	@EventHandler
	public void onPlayerDisconnection(PlayerQuitEvent event) {
		
		Player p = event.getPlayer();
		Survival s=new Survival(main);
		
		World worldsurvie=main.survivalworld;
		World worldsurvie_nether=main.survivalworld_nether;
		World worldsurvie_the_end=main.survivalworld_end;
		
		if(p.getWorld()==worldsurvie||p.getWorld()==worldsurvie_nether||p.getWorld()==worldsurvie_the_end) {	
			s.onLeaveSurvival(p,true);
		}
		
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		
		if(event.getPlayer().getWorld()!=main.survivalworld&&event.getPlayer().getWorld()!=main.survivalworld_nether&&event.getPlayer().getWorld()!=main.survivalworld_end)return;
		
			Player player=(Player)event.getPlayer();
			Fonctions f=new Fonctions(main);
			TeleportGames tpG=new TeleportGames(main);
			Survival s=new Survival(main);
				
			
			World worldsurvie=main.survivalworld;
			World worldsurvie_nether=main.survivalworld_nether;
			World worldsurvie_the_end=main.survivalworld_end;
		
				
				String playername=player.getName();
				Location destination=new Location(worldsurvie,main.configsavesurvie.getDouble("DefaultInventory.location.X"),main.configsavesurvie.getDouble("DefaultInventory.location.Y"),main.configsavesurvie.getDouble("DefaultInventory.location.Z"));
				
				//Checkpoint detection
				if(main.configsavesurvie.get(playername+".spawn")!=null) {
					player.sendMessage(ChatColor.RED+"Régénération du corps à son dernier checkpoint");
					destination=new Location(worldsurvie,main.configsavesurvie.getDouble(playername+".spawn.X"),main.configsavesurvie.getDouble(playername+".spawn.Y"),main.configsavesurvie.getDouble(playername+".spawn.Z"));
				}else {
					player.sendMessage(ChatColor.RED+"Aucun checkpoint détecté : Régénération du corps à l'emplacement par défaut");
				}
				
				player.setLevel(player.getLevel()/2);
				event.setRespawnLocation(destination);
				


		
	
	}

	
	
}
