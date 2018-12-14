package cc.translate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.SSLKeyException;
public class MinJre3 {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		if(args == null ||args.length==0){
			args=new String[]{
					"C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\jar",
					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\repack",
					"D:\\Users\\pwcraradmin\\git\\cc\\external\\jre1.8.0_181",
					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre",
					"D:\\Users\\pwcraradmin\\git\\cc\\target\\cc.jar",
					
					"D:\\Users\\pwcraradmin\\git\\cc\\external\\jre1.8.0_181\\lib\\rt.jar",
					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre\\lib\\rt.jar"
					//"D:\\Users\\pwcraradmin\\git\\cc\\external\\jre1.8.0_181\\lib\\resources.jar",
					//"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre\\lib\\resources.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\external\\jre1.8.0_181\\lib\\charsets.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre\\lib\\charsets.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\external\\jre1.8.0_181\\lib\\jce.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\jre\\lib\\jce.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\target\\cc.jar",
//					"D:\\Users\\pwcraradmin\\git\\cc\\target\\installer\\cc.jar"
			};
		}

		
		String repackLocation=args[1];//"git\\cc\\target\\installer";//args[0];
		String externalJre=args[2];//"git\\cc\\external\\jre1.8.0_181";//args[1];
		String minJre = args[3];//installLocation+"\\jre";  //args[1];
		String targetJarFile=args[4];//"git\\cc\\target\\cc.jar";//args[2];
		String exeJar=args[0];//"C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\jar";
		
		if(true)System.out.println(minJre);
		List<File> files = Files.walk(Paths.get(minJre))
	     .filter(s -> s.toFile().isFile())
	     .map(f->f.toFile())
	     .sorted()
	     .collect(Collectors.toList());
		
		for(File file:files){
			System.out.println(file.getAbsolutePath());
			if(file.getAbsolutePath().endsWith(".delete"))continue;
			if(file.getAbsolutePath().endsWith(".ok"))continue;
			File delFile = new File(file.getAbsolutePath()+".delete");
			File okFile = new File(file.getAbsolutePath()+".ok");

			if(okFile.exists())continue;
			if(delFile.exists())continue;
			if(file.renameTo(delFile)){
				String content="";
				try{
		         content=runCmd(new String[]{minJre+File.separator+"bin"+File.separator+"java","-jar",targetJarFile});
				}catch(Exception e){
					content=e.toString();
				}
		        if(content.contains("Exception") || content.contains("Error:") ||content.contains("Error occurred")){
		        	delFile.renameTo(file);
					okFile.createNewFile();
		        	continue;
		        }	
			}
			

			


		}
		
		Files.walk(Paths.get(minJre))
	     .filter(s -> s.toFile().getAbsolutePath().endsWith(".delete") 
	    		 || s.toFile().getAbsolutePath().endsWith("ok") 
	    		 || s.toFile().isDirectory() && s.toFile().list().length<=0 )
	     .forEach(x->x.toFile().delete());

       //ZipFiles zipFiles = new ZipFiles();
       // zipFiles.zipDirectory(new File(dir), outFile);
		//run();
	}
	public static void match(String glob, String from, String to) throws IOException {
		
		final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
				glob);
		
		Files.walkFileTree(Paths.get(from), new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path path,
					BasicFileAttributes attrs) throws IOException {
				if (pathMatcher.matches(path)) {
					System.out.println(path);
					//path.toFile().getParentFile().mkdirs();
					new File(path.toString().replace(from,to)).getParentFile().mkdirs();
		    		Files.copy(path, new File(path.toString().replace(from,to)).toPath(),StandardCopyOption.REPLACE_EXISTING);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
	private static void minJar(String installLocation, String externalJre, String minJre, String targetJarFile,
			String exeJar, String destJarFile, String srcJarFile) throws IOException, InterruptedException {
		String jarFileName = new File(srcJarFile).getName();
		System.out.println("****"+srcJarFile);
		String minRtFolder = installLocation+File.separator+"new_"+jarFileName;
		String orgRtFolder = installLocation+File.separator+"org_"+jarFileName;

		match("glob:**/*.{so,dll,dylib,js,gif,jpg}", orgRtFolder,minRtFolder);

		
        String content=null;

        content=runCmd(new String[]{externalJre+File.separator+"bin"+File.separator+"java","-verbose:class","-jar",targetJarFile});

		copyFileByAnalyzeContent(orgRtFolder,minRtFolder,content);
		//runCmd(new String[]{exeJar,"-cf",destJarFile,"-C",minRtFolder,"." });

		
		//if(true)System.exit(0);
		
		boolean hasErr = false;
		do{
			try {
				
				 hasErr = false;

				runCmd(new String[]{exeJar,"-cf",destJarFile,"-C",minRtFolder,"." });

				content = runCmd(new String[]{minJre+File.separator+"bin"+File.separator+"java","-verbose:class","-jar",targetJarFile});

				//System.out.println(content);
				String[] lines =  content.split("\n");
				for(int i=0;i<lines.length;i++){
					String pkgCls = null;
					String line = lines[i];
					if(line.contains("java.lang.NoClassDefFoundError:")){
						pkgCls = line.trim().split(Pattern.quote("java.lang.NoClassDefFoundError:"))[1].trim().split("\\s+")[0];
					}else if(line.contains("Caused by: java.util.MissingResourceException: Can't find bundle for base name")){
						String temps[] = line.trim().split("\\s+");
						pkgCls = "sun.launcher.resources.launcher";
					}
					
					if(pkgCls!=null){
			    		copyRelativeClass(orgRtFolder,minRtFolder,pkgCls.replaceAll(Pattern.quote("."), Matcher.quoteReplacement(File.separator)),line);
			    		hasErr = true;
			    		break;
					}
					

				}
				//if(content.contains("Exception in thread"))System.exit(1);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(hasErr);
	}

	private static void copyFileByAnalyzeContent(String orgJarFolder, String newJarFolder,String content) throws IOException {
		String[] lines = content.split("\n");
        
        for(String line:lines){
        		
        		if(line.contains("Opened"))continue;
        		
        		if(!line.startsWith("[L"))continue;
            		//System.out.println(line);
            		String f = line.split("\\s")[1].replaceAll(Pattern.quote("."), "/");
            		copyRelativeClass(orgJarFolder,newJarFolder,f,line);

        }
	}
	
	private static void copyRelativeClass(String orgJarFolder, String newJarFolder,String f,String line) throws IOException {
		String orgClassFile = orgJarFolder+File.separator+f+".class";
		String newClassFile=newJarFolder+File.separator+f+".class";

		if(new File(orgClassFile).isFile()){
    		new File(newClassFile).getParentFile().mkdirs();
    		Files.copy(new File(orgClassFile).toPath(), new File(newClassFile).toPath(),StandardCopyOption.REPLACE_EXISTING);
    		System.out.println("COPY "+ orgClassFile+ "=> "+ newClassFile);
    		File[] files = new File(orgClassFile).getParentFile().listFiles();
    		String tmps[] = f.split(Pattern.quote(File.separator));
    		String fileName = tmps[tmps.length-1];
    		for(File ff:files){
    			if(ff.getName().startsWith(fileName)){
    				
            		Files.copy(ff.toPath(), new File(ff.getAbsolutePath().replace(orgJarFolder, newJarFolder)).toPath(),StandardCopyOption.REPLACE_EXISTING);
    			}
    		}
		
		}else{
			System.out.println(line);
		}
		
	}

	public static String runCmd(String[] commands) throws IOException, InterruptedException{
		//String[] commands = {"system.exe","-get t"};

		ProcessBuilder   ps=new ProcessBuilder(commands);
		
		System.out.println(String.join(" ", commands));

		//From the DOC:  Initially, this property is false, meaning that the 
		//standard output and error output of a subprocess are sent to two 
		//separate streams
		ps.redirectErrorStream(true);

		Process pr = ps.start(); 
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pr.destroy();
				
			}
		}).start();
		StringBuilder sb = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		    sb.append(line+"\n");
		    if(line.contains("Exception:") ||line.contains("Error:") || line.contains("Error occurred"))
		    	pr.destroy();
		}

		pr.waitFor();

		in.close();

		return sb.toString();
	}
	
}