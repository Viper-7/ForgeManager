package net.ForgeManager.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class TeleportCommand extends CommandBase {
	public static String name = "tpo";

    @Override
    public String getCommandName() {
            return name;
    }
    
    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
            return "Usage: /tpo [player_name] [dimension_id] [x y z]";
    }
    
    @Override
    public final void processCommand(final ICommandSender commandSender, String[] argumentsArray) {
    	
    }
}