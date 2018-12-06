package cc.translate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
//javax/crypto/Cipher
import javax.crypto.Cipher;
public class MinJre2 {

	public static void main(String[] args) throws IOException {
		
		String loadedClassLogFile=args[0];
		String filePath="cc\\class.txt";
        String content=null;
        content = new String(Files.readAllBytes(Paths.get(filePath)));
        
		String dir="cc\\target\\installer\\rt";
		String outFile="cc\\target\\installer\\jre\\lib\\rt.jar";
		//updateSrcByContent(content);
		//Runtime.getRuntime().exec("\"C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\jar\" -cf "+outFile+" -C "+dir+" .");
		
		//if(true)System.exit(0);
		try {
			content = runCmd(new String[]{"cc\\target\\installer\\jre\\bin\\java"," -verbose:class "," -jar cc\\target\\installer\\cc.jar"});
			System.out.println(content);
			String[] lines =  content.split("\n");
			for(int i=0;i<lines.length;i++){
				String pkgCls = null;
				String line = lines[i];
				if(line.contains("java.lang.NoClassDefFoundError:")){
					String temps[] = line.trim().split("\\s+");
					pkgCls = temps[1];
				}else if(line.contains("Caused by: java.util.MissingResourceException: Can't find bundle for base name")){
					String temps[] = line.trim().split("\\s+");
					pkgCls = "sun.launcher.resources.launcher";
				}
				
				if(pkgCls!=null){
		    		copyRelativeClass(pkgCls.replaceAll(Pattern.quote("."), "\\\\"),line);
		    		Runtime.getRuntime().exec("\"C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\jar\" -cf "+outFile+" -C "+dir+" .");
		    		System.exit(0);
				}

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       //ZipFiles zipFiles = new ZipFiles();
       // zipFiles.zipDirectory(new File(dir), outFile);
		//run();
	}

	private static void updateSrcByContent(String content) throws IOException {
		String[] lines = content.split("\n");
        
        for(String line:lines){
        		
        		if(line.contains("Opened"))continue;
        		
        		if(!line.startsWith("[L"))continue;
            		//System.out.println(line);
            		String f = line.split("\\s")[1].replaceAll(Pattern.quote("."), "/");
            		copyRelativeClass(f,line);

        }
	}
	
	private static void copyRelativeClass(String f,String line) throws IOException {
		String base = "cc\\target\\installer\\org_rt\\"+f+".class";
		String base2="cc\\target\\installer\\rt\\"+f+".class";

		System.out.println(base+" => "+ base2);
		if(new File(base).isFile()){
    		new File(base2).getParentFile().mkdirs();
    		Files.copy(new File(base).toPath(), new File(base2).toPath(),StandardCopyOption.REPLACE_EXISTING);
    		
    		File[] files = new File(base).getParentFile().listFiles();
    		String tmps[] = f.split("[/\\\\]");
    		String fileName = tmps[tmps.length-1];
    		for(File ff:files){
    			if(ff.getName().startsWith(fileName)){
    				
            		Files.copy(ff.toPath(), new File(ff.getAbsolutePath().replace("org_rt", "rt")).toPath(),StandardCopyOption.REPLACE_EXISTING);
    			}
    		}
		
		}else{
			System.out.println(line);
		}
		
	}

	public static String runCmd(String[] commands) throws IOException, InterruptedException{
		//String[] commands = {"system.exe","-get t"};
		
		ProcessBuilder   ps=new ProcessBuilder(commands);
		System.out.println(commands[0]+" "+ commands[1]);

		//From the DOC:  Initially, this property is false, meaning that the 
		//standard output and error output of a subprocess are sent to two 
		//separate streams
		ps.redirectErrorStream(true);

		Process pr = ps.start();  
		StringBuilder sb = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
		    System.out.println(line);
		    sb.append(line+"\n");
		}
		pr.waitFor();

		in.close();

		return sb.toString();
	}
	
}
