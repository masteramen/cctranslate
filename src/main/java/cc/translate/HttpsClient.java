package cc.translate;


import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class HttpsClient{
	
   public static void main(String[] args) throws URISyntaxException
   {
		List <String> times = new ArrayList();
		System.setProperty("java.net.useSystemProxies","true");
		List<Proxy> l = ProxySelector.getDefault().select(new URI("http://java.sun.com/"));
		Proxy theProxy = null;
		for (Iterator iter = l.iterator(); iter.hasNext(); )  {
		  Proxy proxy = (Proxy) iter.next();
		  InetSocketAddress addr = (InetSocketAddress)proxy.address();
		  if(addr!=null){
			  System.out.println("proxy hostname : " + addr.getHostName());
			  System.out.println("proxy port : " + addr.getPort());  
		  }

		}
		System.out.println(theProxy);


		System.setProperty("java.net.useSystemProxies","false");
        new HttpsClient().testIt();
   }
	
   public static void testIt(){

      String https_url = "https://www.google.com/";
      URL url;
      try {

	     url = new URL(https_url);
	     HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			
	     //dumpl all cert info
	     print_https_cert(con);
			
	     //dump all the content
	     print_content(con);
			
      } catch (MalformedURLException e) {
	     e.printStackTrace();
      } catch (IOException e) {
	     e.printStackTrace();
      }

   }
	
   private static void print_https_cert(HttpsURLConnection con){
     
    if(con!=null){
			
      try {
				
	System.out.println("Response Code : " + con.getResponseCode());
	System.out.println("Cipher Suite : " + con.getCipherSuite());
	System.out.println("\n");
				
	Certificate[] certs = con.getServerCertificates();
	for(Certificate cert : certs){
	   System.out.println("Cert Type : " + cert.getType());
	   System.out.println("Cert Hash Code : " + cert.hashCode());
	   System.out.println("Cert Public Key Algorithm : " 
                                    + cert.getPublicKey().getAlgorithm());
	   System.out.println("Cert Public Key Format : " 
                                    + cert.getPublicKey().getFormat());
	   System.out.println("\n");
	}
				
	} catch (SSLPeerUnverifiedException e) {
		e.printStackTrace();
	} catch (IOException e){
		e.printStackTrace();
	}

     }
	
   }
	
   private static void print_content(HttpsURLConnection con){
	if(con!=null){
			
	try {
		
	   System.out.println("****** Content of the URL ********");			
	   BufferedReader br = 
		new BufferedReader(
			new InputStreamReader(con.getInputStream()));
				
	   String input;
				
	   while ((input = br.readLine()) != null){
	      System.out.println(input);
	   }
	   br.close();
				
	} catch (IOException e) {
	   e.printStackTrace();
	}
			
       }
		
   }
	
}