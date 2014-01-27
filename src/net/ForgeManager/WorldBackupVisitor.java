package net.ForgeManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class WorldBackupVisitor extends SimpleFileVisitor<Path> {
	String parentFolder;
	ZipOutputStream archiveStream;
	List<File> archivedFiles;
	
	public WorldBackupVisitor(String parentFolder, ZipOutputStream archiveStream, List<File> archivedFiles) throws IOException {
		this.parentFolder = parentFolder;
		this.archiveStream = archiveStream;
		this.archivedFiles = archivedFiles;
	}
	
	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes aAttrs) throws IOException {
    	// Process File
		
		if(!archivedFiles.contains(file)) {
			String name = file.toFile().getCanonicalPath().replace(parentFolder, "");
			byte[] buffer = new byte[1024];
			ZipEntry entry = new ZipEntry(name);
			FileInputStream source = new FileInputStream(file.toFile().getCanonicalPath());
			
			archiveStream.putNextEntry(entry);
	
			int len;
			while ((len = source.read(buffer)) > 0) {
				archiveStream.write(buffer, 0, len);
			}
	
			source.close();
			archiveStream.closeEntry();
			archivedFiles.add(file);
		}

    	return FileVisitResult.CONTINUE;
    }
	
    @Override
	public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
    	// Process Folder
    	
    	return FileVisitResult.CONTINUE;
    }
}
