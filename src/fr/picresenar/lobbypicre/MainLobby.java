package fr.picresenar.lobbypicre;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class MainLobby extends JavaPlugin {
	
	public World lobbyworld;
	public World forbiddenworld;
	public World survivalworld;
	public World survivalworld_nether ;
	public World survivalworld_end ;
	
	public Double Xlobby;
	public Double Ylobby;
	public Double Zlobby;
	
	public File filesavesurvie;
	public FileConfiguration configsavesurvie;
	public String DefaultInventory="58dd1760-6c2a-36ab-96d3-d9219c226858";
	
	public Boolean isSomeoneAsleep=false;
	

	@Override
	public void onEnable() {
		
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getCommand("adminmeetup").setExecutor(new Commands(this));
		getCommand("getbooktp").setExecutor(new Commands(this));
		
		
		getServer().getPluginManager().registerEvents(new LobbyListeners(this), this);
		
//		Bukkit.createWorld(new WorldCreator(getConfig().getString("survivalworld")));
//		Bukkit.createWorld(new WorldCreator(getConfig().getString("survivalworld")+"_nether"));
//		Bukkit.createWorld(new WorldCreator(getConfig().getString("survivalworld")+"_the_end"));
		
		this.filesavesurvie = new File(getDataFolder() + File.separator+"savesurvie.yml");
		
		if(!filesavesurvie.exists()) {
			try {
				filesavesurvie.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		

		this.configsavesurvie=YamlConfiguration.loadConfiguration(filesavesurvie);
		
		//Définition des mondes
		this.lobbyworld=Bukkit.getWorld(getConfig().getString("lobbyworld"));
		this.survivalworld=Bukkit.getWorld(getConfig().getString("survivalworld"));
		this.forbiddenworld=Bukkit.getWorld(getConfig().getString("forbiddenworld"));
		this.survivalworld_nether = Bukkit.getWorld(getConfig().getString("survivalworld")+"_nether");
		this.survivalworld_end = Bukkit.getWorld(getConfig().getString("survivalworld")+"_the_end");
		
		//Définition des coordonnées
		Xlobby=this.getConfig().getDouble("coordonnees.coordonneeslobby.X");
		Ylobby=this.getConfig().getDouble("coordonnees.coordonneeslobby.Y");
		Zlobby=this.getConfig().getDouble("coordonnees.coordonneeslobby.Z");
		
		//Recette Graisse enflamée
		ItemStack carbon = new ItemStack(Material.COAL, 1);
		ItemMeta carbonm = carbon.getItemMeta();
		carbonm.setDisplayName("Graisse inflammable");
		carbon.setItemMeta(carbonm);
		ShapedRecipe coal = new ShapedRecipe(carbon);
		coal.shape("*%*","%*%","*%*");
		coal.setIngredient('*', Material.FEATHER);
		coal.setIngredient('%', Material.ROTTEN_FLESH);
		getServer().addRecipe(coal);
		
		//Recette Spider web
		ItemStack web = new ItemStack(Material.LEGACY_WEB, 1);
		ItemMeta webm = web.getItemMeta();
		web.setItemMeta(webm);
		ShapedRecipe cobweb = new ShapedRecipe(web);
		cobweb.shape("* *"," * ","* *");
		cobweb.setIngredient('*', Material.STRING);
		getServer().addRecipe(cobweb);
		
		System.out.println("+-----------------Lobby Picre-----------------+");
		System.out.println("|         Plugin created by PicreSenar        |");
		System.out.println("|           Initialization complete           |");
		System.out.println("+---------------------------------------------+");	
		
		
	}
	
	@Override
	public void onDisable() {

		System.out.println("+-----------------Lobby Picre-----------------+");
		System.out.println("|         Plugin created by PicreSenar        |");
		System.out.println("|            Deactivation complete            |");
		System.out.println("+---------------------------------------------+");	
		
	}

	
}
