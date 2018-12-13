package cc.translate.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JSTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//使用管道流，将输出流转为输入流
		PipedReader prd = new PipedReader();
		PipedWriter pwt = new PipedWriter(prd);
		//设置执行结果内容的输出流
		ScriptEngineManager m = new ScriptEngineManager();
		//获取JavaScript执行引擎
		ScriptEngine engine = m.getEngineByName("JavaScript");
		engine.getContext().setWriter(pwt);
		//js文件的路径
		String strFile = Thread.currentThread().getClass().getResource("/tk.js").getPath();
		Reader reader = new FileReader(new File(strFile));
		engine.eval(reader);
		BufferedReader br = new BufferedReader(prd);
		
		engine.eval(Files.newBufferedReader(Paths.get(new File(strFile).toURI()), StandardCharsets.UTF_8));

		Invocable inv = (Invocable) engine;
		// call function from script file
		inv.invokeFunction("calcHash", "param","1.1");
		
		//开始读执行结果数据
		String str = null;
		while ((str = br.readLine()) != null && str.length() > 0) {
		     System.out.println(str);
		}
		br.close();
		pwt.close();
		prd.close();

	}

}
