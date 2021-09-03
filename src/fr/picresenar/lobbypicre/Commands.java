package fr.picresenar.lobbypicre;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.picresenar.lobbypicre.gameoftaupes.Meetup;
import fr.picresenar.lobbypicre.minjeux.TeleportGames;


public class Commands implements CommandExecutor {
	

	public MainLobby main;

	public Commands(MainLobby lobbyPicre) {
		this.main = lobbyPicre;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) {
		
		Meetup m=new Meetup(main);
		TeleportGames tpG=new TeleportGames(main);
		
		Player player=null;	
		
		if(sender instanceof Player && arg3.length==0) {
			player=(Player)sender;
		}else if(arg3.length==1) {
			player=Bukkit.getPlayer(arg3[0]);
			
			if (player==null) {
				sender.sendMessage("Joueur "+arg3[0]+" inconnu");
				return false;
			}

		}
			
		if(cmd.getName().equalsIgnoreCase("adminmeetup")) {
				
			m.giveAdminBook(player);
			return true;
				
		}else if(cmd.getName().equalsIgnoreCase("getbooktp")){
				
			tpG.getBookTeleportGames(player);
			return true;
				
		}
		
	
	
		

		
		return false;
	}

	
	
}
