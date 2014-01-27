package net.ForgeManager.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class ListCommand extends CommandBase {
	public static String name = "list";

    @Override
    public String getCommandName() {
            return name;
    }
    
    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
            return "Usage: /list [chunk|player|world]";
    }
    
    @Override
    public final void processCommand(final ICommandSender commandSender, String[] argumentsArray) {
    	
    }
}