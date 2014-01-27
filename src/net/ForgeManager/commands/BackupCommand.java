package net.ForgeManager.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class BackupCommand extends CommandBase {
	public static String name = "backup";

    @Override
    public String getCommandName() {
            return name;
    }
    
    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
            return "Usage: /backup [dimension_id]";
    }
    
    @Override
    public final void processCommand(final ICommandSender commandSender, String[] argumentsArray) {
    	
    }
}