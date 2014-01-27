package net.ForgeManager;

import java.io.File;
import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.ForgeManager.commands.*;

@Mod(name = "ForgeManager", version = "0.1.0", useMetadata = false, modid = "ForgeManager", acceptedMinecraftVersions = "1.6.4", dependencies = "required-after:Forge@[9.11.1.953,)")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class ForgeManager {
    int worldBackupInterval = 86400;
    int playerBackupInterval = 3600;
    File backupPath;

	public ForgeManager() {
		// Scheduled world backups 
		// Scheduled player backups
		// Scheduled restarts (idle server + working login server)
		
		// Delay restart until login server is online and server is idle
		// Player data restore?
		// Chunk restore?
		// Scheduled mod updates (including changelog)
	}
	
    @Mod.Init
    public void init(FMLInitializationEvent event) {
            MinecraftForge.EVENT_BUS.register(this);

            TickRegistry.registerScheduledTickHandler(new ForgeManagerTickHandler(worldBackupInterval, playerBackupInterval, backupPath), Side.SERVER);
    }
    
    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());
            config.load();
            String GENERAL = Configuration.CATEGORY_GENERAL;

            worldBackupInterval = config.get(GENERAL, "worldBackupInterval", worldBackupInterval, "How long (in seconds) between world backups").getInt();
            playerBackupInterval = config.get(GENERAL, "playerBackupInterval", playerBackupInterval, "How long (in seconds) between player data backups").getInt();
            backupPath = new File(".", "backups");
            try {
				backupPath = new File(config.get(GENERAL, "backupPath", backupPath.getPath(), "Where backups are stored").getString());
			} catch (IOException e) {
				e.printStackTrace();
			}

            config.save();
    }
    
    @Mod.ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
            ServerCommandManager serverCommandManager = (ServerCommandManager) event.getServer().getCommandManager();
            
            serverCommandManager.registerCommand(new BackupCommand());
            serverCommandManager.registerCommand(new ListCommand());
            serverCommandManager.registerCommand(new RestoreCommand());
            serverCommandManager.registerCommand(new TeleportCommand());
    }
}
