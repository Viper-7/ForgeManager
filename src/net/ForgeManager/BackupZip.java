package net.ForgeManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLLog;

public class BackupZip {
	private String backupID;
	private File backupPath;
	private File backupTypePath;
	private File targetPath;
	private static String serverBase = null;
	
	private ZipOutputStream archiveStream;

	public List<File> archivedFiles = new ArrayList<File>();
	public File archiveFile;
	public FileOutputStream summary;
	
	public BackupZip(File backupPath, String backupType) {
		this.backupPath = backupPath;
		if(serverBase == null) {
			serverBase = MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectoryName();
		}

		try{
			backupTypePath = new File(backupPath, backupType);
			backupTypePath.mkdir();
			
			this.backupID = getBackupID();

			targetPath = new File(backupTypePath, backupID);
			targetPath.mkdir();
			
			archiveFile = new File(targetPath, "fmbackup.zip");

			this.summary = new FileOutputStream(new File(targetPath, "summary.txt"));
			FileOutputStream fos = new FileOutputStream(archiveFile.getPath());
			this.archiveStream = new ZipOutputStream(fos);
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	private String getBackupID() {
		int id = 0;
		
		for(File file : backupTypePath.listFiles()) {
			int fileID = Integer.parseInt(file.getName());
			
			if(fileID > id) {
				id = fileID;
			}
		}
		
		return Integer.toString(id + 1);
	}
	
	public void addSummary(String msg) {
		try {
			this.summary.write((msg + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFile(File file) throws IOException {
		if(!archivedFiles.contains(file)) {
			byte[] buffer = new byte[1024];
			ZipEntry entry = new ZipEntry(file.getCanonicalPath().replace(serverBase, ""));
			FileInputStream source = new FileInputStream(file.getPath());
			
			archiveStream.putNextEntry(entry);
	
			int len;
			while ((len = source.read(buffer)) > 0) {
				archiveStream.write(buffer, 0, len);
			}
	
			source.close();
			archiveStream.closeEntry();
			archivedFiles.add(file);
		}
	}
	
	public void addFolder(String folder) throws Exception {
		addFolder(new File(folder));
	}
	
	public void addFolder(File folder) throws Exception {
    	FileVisitor<Path> processor = new WorldBackupVisitor(serverBase, archiveStream, archivedFiles);
    	
    	if(!archivedFiles.contains(folder)) {
	    	try {
	    		Files.walkFileTree(folder.toPath(), processor);
	    	} catch(IOException e) {
	    		FMLLog.severe(e.getMessage());
	    	}
    	}
	}
	
	public void close() {
    	try {
    		archiveStream.close();
    	} catch(IOException e) {
    		FMLLog.severe(e.getMessage());
    	}
	}
}