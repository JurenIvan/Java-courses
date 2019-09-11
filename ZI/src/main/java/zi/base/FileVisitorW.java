package zi.base;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class FileVisitorW extends SimpleFileVisitor<Path>{
	
	public static void main(String[] args) throws IOException {
		String a="C:\\data";
		FileVisitorW fv=new FileVisitorW();
		Files.walkFileTree(Paths.get(a),fv);
		
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		if(!file.toAbsolutePath().toString().endsWith("-in.txt"))
			return FileVisitResult.CONTINUE;
		
		String newName=file.toAbsolutePath().toString().substring(0,file.toAbsolutePath().toString().length()-6).concat("out.txt");
		
		List<String> in=Files.readAllLines(file);
		Calc c=new Calc();
		String out=c.calcOut(in);
		
		
		  FileWriter fw=new FileWriter(newName);    
          fw.write(out);    
          fw.close();    
		
		return FileVisitResult.CONTINUE;
	}
}
