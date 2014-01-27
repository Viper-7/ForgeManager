package net.ForgeManager.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class RestoreCommand extends CommandBase {
	public static String name = "restore";

    @Override
    public String getCommandName() {
            return name;
    }
    
    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
            return "Usage: /restore [chunk|player|world] [player_name|dimension_id] [backup_id]";
    }
    
    @Override
    public final void processCommand(final ICommandSender commandSender, String[] argumentsArray) {
    	
    }
}