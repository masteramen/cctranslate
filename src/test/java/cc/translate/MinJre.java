package cc.translate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class MinJre {

	public static void main(String[] args) throws IOException {
		
		System.out.println(args);
		System.out.println(args[0]);
		String filePath="D:\\Users\\pwcraradmin\\git\\cc\\class.txt";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] lines = content.split("\n");
        String orgPath = args[0];
        String targetPath = args[1];
        for(String line:lines){
        		
        		if(line.contains("Opened"))continue;
        		if(!line.startsWith("["))continue;
            		String f = line.split("\\s")[1].replaceAll(Pattern.quote("."), "\\\\");
            		String base = orgPath+"\\"+f+".class";
            		String base2 = targetPath+"\\"+f+".class";
            
            		if(new File(base).isFile()){
                		new File(base2).getParentFile().mkdirs();
                		Files.copy(new File(base).toPath(), new File(base2).toPath(),StandardCopyOption.REPLACE_EXISTING);
            		
            		}else{
            			System.out.println(line);
            		}


        
        }

       //ZipFiles zipFiles = new ZipFiles();
       // zipFiles.zipDirectory(new File(dir), outFile);
		//run();
	}
	public static void run() throws IOException
	{
	  Manifest manifest = new Manifest();
	  manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		String outFile="D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre\\lib\\rt.jar";
		String dir="D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\rt";

	  JarOutputStream target = new JarOutputStream(new FileOutputStream(outFile), manifest);
	  add(new File(dir), target);
	  target.close();
	}

	private static void add(File source, JarOutputStream target) throws IOException
	{
	  BufferedInputStream in = null;
	  try
	  {
	    if (source.isDirectory())
	    {
	      String name = source.getPath().replace("\\", "/");
	      if (!name.isEmpty())
	      {
	        if (!name.endsWith("/"))
	          name += "/";
	        JarEntry entry = new JarEntry(name);
	        entry.setTime(source.lastModified());
	        target.putNextEntry(entry);
	        target.closeEntry();
	      }
	      for (File nestedFile: source.listFiles())
	        add(nestedFile, target);
	      return;
	    }

	    JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
	    entry.setTime(source.lastModified());
	    target.putNextEntry(entry);
	    in = new BufferedInputStream(new FileInputStream(source));

	    byte[] buffer = new byte[1024];
	    while (true)
	    {
	      int count = in.read(buffer);
	      if (count == -1)
	        break;
	      target.write(buffer, 0, count);
	    }
	    target.closeEntry();
	  }
	  finally
	  {
	    if (in != null)
	      in.close();
	  }
	}
}
