package fr.picresenar.lobbypicre.commons;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.block.Skull;

import fr.picresenar.lobbypicre.MainLobby;


public class Fonctions {
	
	
	
	
	private MainLobby main;


	public Fonctions(MainLobby mainlobby) {
		this.main=mainlobby;
	}


	public void FillBlocks(Double Xmin,Double Ymin,Double Zmin,Double Xmax,Double Ymax,Double Zmax,Material material) {
		Integer x;
		Integer y;
		Integer z;
			
		for (x = (int) Math.round(Xmin); x <= Xmax; x++) {
			for (y = (int) Math.round(Ymin); y <= Ymax; y++) {
				for (z = (int) Math.round(Zmin); z <= Zmax; z++) { 
					
					Location loc=new Location(main.lobbyworld,x,y,z);
					loc.getChunk().load();
					main.lobbyworld.getBlockAt(x, y, z).setType(material);
					
				}
			}
		}
			
	}	
	
	
	
	public boolean isBetweenLocations(Location cible,Location min,Location max) {
		
		
		if(cible.getX()<=max.getX() && cible.getY()<=max.getY() && cible.getZ()<=max.getZ() && cible.getX()>=min.getX() && cible.getY()>=min.getY() && cible.getZ()>=min.getZ()) {
			return true;

		}else {
			return false;
		}
		
	}
	
	
	
	
	public ItemStack getSkull(String owner,String name) {
		
		ItemStack it = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta=(SkullMeta) it.getItemMeta();
		
		meta.setOwner(owner);
		meta.setDisplayName(name);
		it.setItemMeta(meta);
		
		return it;
	}
	
	
	public ItemStack getItem(Material material, String customName,List<String> lore) {
		
		ItemStack it = new ItemStack(material);
		ItemMeta itM = it.getItemMeta();
		
		if(customName!=null) itM.setDisplayName(customName);
		if(lore!=null)itM.setLore(lore);
		it.setItemMeta(itM);
		
		return it;
		
	}
	
	
	public ItemStack getBanner(String customName,DyeColor color) {
		
		ItemStack it = new ItemStack(Material.LEGACY_BANNER);
		BannerMeta bannermeta = (BannerMeta)it.getItemMeta();
		
		if(customName!=null) bannermeta.setDisplayName(customName);	
		if(color!=null) bannermeta.setBaseColor(color);
		it.setItemMeta(bannermeta);
		
		return it;
		
	}
	
	public String InventoryToString (Inventory invInventory)
	    {
	       
	        String serialization = invInventory.getSize() + ";";
	        for (int i = 0; i < invInventory.getSize(); i++)
	        {
	            ItemStack is = invInventory.getItem(i);
	            if (is != null)
	            {
	                String serializedItemStack = new String();
	             
	                String isType = String.valueOf(is.getType());
	                serializedItemStack += "t@" + isType;
	             
	                if (is.getDurability() != 0)
	                {
	                    String isDurability = String.valueOf(is.getDurability());
	                    serializedItemStack += ":d@" + isDurability;
	                }
	             
	                if (is.getAmount() != 1)
	                {
	                    String isAmount = String.valueOf(is.getAmount());
	                    serializedItemStack += ":a@" + isAmount;
	                }
	             
	                Map<Enchantment,Integer> isEnch = is.getEnchantments();
	                if (isEnch.size() > 0)
	                {
	                    for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
	                    {
	                        serializedItemStack += ":e@" + ench.getKey().getName() + "@" + ench.getValue();
	                    }
	                }
	             
	                serialization += i + "#" + serializedItemStack + ";";
	            }
	        }
	        return serialization;
	    }
	
	public Inventory StringToInventory (String invString)
    {
        String[] serializedBlocks = invString.split(";");
        //String invInfo = serializedBlocks[0];
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, 36);
       
        for (int i = 1; i < serializedBlocks.length; i++)
        {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);
           
            if (stackPosition >= deserializedInventory.getSize())
            {
                continue;
            }
           
            ItemStack is = null;
            Boolean createdItemStack = false;
           
            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack)
            {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t"))
                {
                    is = new ItemStack(Material.getMaterial(String.valueOf(itemAttribute[1])));
                    createdItemStack = true;
                }
                else if (itemAttribute[0].equals("d") && createdItemStack)
                {
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                }
                else if (itemAttribute[0].equals("a") && createdItemStack)
                {
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                }
                else if (itemAttribute[0].equals("e") && createdItemStack)
                {
                	
                	System.out.println("Enchant : "+String.valueOf(itemAttribute[1])+" / Value : "+Integer.valueOf(itemAttribute[2]));
                    is.addEnchantment(Enchantment.getByName(String.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                }
            }
            deserializedInventory.setItem(stackPosition, is);
        }
       
        return deserializedInventory;
    }
	
	
	public void RemoveArmor(Player player) {
	    player.getInventory().setHelmet(null);
	    player.getInventory().setChestplate(null);
	    player.getInventory().setLeggings(null);
	    player.getInventory().setBoots(null);
	}
	
	public Inventory showListofPlayers(List<UUID> players,String titre, World world) {
		
		Fonctions f = new Fonctions(main);
		Integer size =  Math.round(players.toArray().length/9)*9+9;
		
		if(players.toArray().length==0)return null;
		Inventory inv = Bukkit.createInventory(null, size,titre);
		
		Integer i=0;
		for(UUID currentP:players) {
			if(world ==null || Bukkit.getPlayer(currentP).getWorld()==world) {
				inv.setItem(i, f.getSkull(Bukkit.getPlayer(currentP).getDisplayName(), Bukkit.getPlayer(currentP).getDisplayName()));
				i++;
			}
		}

		return inv;
	}
	

}
