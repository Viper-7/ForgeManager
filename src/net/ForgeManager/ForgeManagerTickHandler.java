package net.ForgeManager;

import java.io.File;
import java.util.EnumSet;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ForgeManagerTickHandler implements IScheduledTickHandler {
    private static final EnumSet<TickType> TICKS = EnumSet.of(TickType.SERVER);
    private int worldBackupInterval;
    private int playerBackupInterval;
    private File backupPath;
    
    public boolean playerBackupScheduled, doPlayerBackup;
    public boolean worldBackupScheduled, doWorldBackup;

    public ForgeManagerTickHandler(int worldBackupInterval, int playerBackupInterval, File backupPath) {
    	this.worldBackupInterval = worldBackupInterval;
    	this.playerBackupInterval = playerBackupInterval;
    	this.backupPath = backupPath;
    }

    @Override
    public int nextTickSpacing() {
        // Tick every second    
    	return 20;
    }
    
    @Override
    public EnumSet<TickType> ticks() {
            return EnumSet.of(TickType.SERVER);
    }
    
    @Override
    public void tickStart(final EnumSet<TickType> type, final Object... tickData) {
    	if(doWorldBackup) {
    		BackupZip backup = new BackupZip(backupPath, "world");

    		//for(WorldServer world : MinecraftServer.getServer().worldServers) {
			WorldServer world = MinecraftServer.getServer().worldServers[0];
			String name = world.getWorldInfo().getWorldName();
			String path = world.getSaveHandler().getWorldDirectoryName();
			Long size = world.getWorldInfo().getSizeOnDisk();
			int dimensionID = world.provider.dimensionId;
			FMLLog.info("Performing backup for dimension " + Integer.toString(dimensionID) + " - " + name);
			FMLLog.info("Expected backup size: " + Integer.toString((int)(size / 1024)) + "kb");
			
			world.flush();
			
			try {
				backup.addSummary("Added backup for dimension " + Integer.toString(dimensionID) + " from " + path);
				backup.addFolder(path);
			} catch(Exception e) {
				FMLLog.severe("Backup FAILED for " + path);
			}
    		
    		backup.close();
    		
    		doWorldBackup = false;
    		sendChat("World backup complete - Lag over!");
    	} else if(doPlayerBackup) {
    		BackupZip backup = new BackupZip(backupPath, "player");

    		File worldPath = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectoryName());
    		for(File player : new File(worldPath, "players").listFiles()) {
    			try {
    				backup.addFile(player);
    			} catch(Exception e) {
    				FMLLog.severe("Backup FAILED for " + player.getName());
    			}
    		}
    		
    		backup.close();
    		
    		doPlayerBackup = false;
    		sendChat("Player backup complete!");
    	}

    	
    	if(playerBackupScheduled) {
    		sendChat("Performing player backup");
    		doPlayerBackup = true;
    		playerBackupScheduled = false;
    	} else if(worldBackupScheduled) {
    		sendChat("Performing world backup - BRACE FOR LAG!");
    		doWorldBackup = true;
    		worldBackupScheduled = false;
    	}
    }

    @Override
    public void tickEnd(final EnumSet<TickType> type, final Object... tickData) {
    }
    
    
    public static void sendChat(ICommandSender commandSender, String message) {
        if (commandSender == MinecraftServer.getServer()) {
                FMLLog.info('\n' + message);
                return;
        }
        while (message != null) {
                int nlIndex = message.indexOf('\n');
                String sent;
                if (nlIndex == -1) {
                        sent = message;
                        message = null;
                } else {
                        sent = message.substring(0, nlIndex);
                        message = message.substring(nlIndex + 1);
                }
                commandSender.sendChatToPlayer(new ChatMessageComponent().addText(sent));
       	}
    }
    
    public static void sendChat(String message) {
    	sendChat(MinecraftServer.getServer(), message);
    }
    
    @Override
    public String getLabel() {
            return "ForgeManager Tick Handler";
    }
}
