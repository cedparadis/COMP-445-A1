package ca.concordia.echo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BlockingEchoClient {

    private final static String USER_AGENT = "Mozilla/5.0";
    private final static String ACCEPT_LANG = "en-US,en;q=0.5";
    
    
    /*
     * Send a get request method
     * -open connection
     * -set socket with socket address
     * -read header
     * -read data
     * -close connection
     * 
     */
 

    
    private static void sendGET(String testUrl, boolean verbose, Map<String,String>headers) throws IOException {
    		
    		//put in URL to parse elements
    		URL url = new URL(testUrl);
    		String host = url.getHost();
    		String path = url.getPath();
    		String param = url.getQuery();
    		int port = url.getPort() != -1 ? url.getPort():url.getDefaultPort();
    		//System.out.println("host: " + host + "\nPath: " + param + "\nPort: " + port);
    		
    		//open socket to connect to server 
    		//initialize output buffer to send request
    		//initialize input buffer to receive response
            Socket socket1 = new Socket(host, port);
            PrintStream out = new PrintStream( socket1.getOutputStream() );
            BufferedReader in = new BufferedReader( new InputStreamReader( socket1.getInputStream() ) );
            
            //send the get request with parameters
            if(param!= null) {
            	out.println( "GET "+ path + "?"+ param + " HTTP/1.0" );
            }
            //without parameters
            else {
            	out.println( "POST "+ path + " HTTP/1.0" );
            }
            
            	out.println("User-Agent: " + USER_AGENT);
            	out.println("Accept-language: " + ACCEPT_LANG);
            	out.println("Host: " + host);
            
            if(headers!= null) {
            	for(String key: headers.keySet()) {
            		out.println(key +": " + headers.get(key));
            	}
            }
            out.println();
            out.println();
            String line = in.readLine();
            
            //do not show response header
            if(!verbose) 
            {
            while( line != null )
            {
            	//condition to not show the response header we skip until we reach two empty lines
            	if(line.isEmpty() ) {
            		while(line!= null) {
            			if(line.isEmpty()) {
            				line = in.readLine();
            				continue;
            			}
            			 System.out.println( line );
                         line = in.readLine();
            		}
            	}
            	line = in.readLine();	
               
           } 
            }
            
            //show response header
            else if(verbose){
            	while(line != null) {
            		System.out.println( line );
                    line = in.readLine();
            	}
            }
         // Close our streams
            in.close();
            out.close();
            socket1.close();
       }
    //Post method for inline data
    /*
     * - Parse url for information
     * - Open connection
     * - Set header
     * - Send Post request
     * - Output response
     */
        
    private static void sendPOST(String testUrl, boolean verbose, Map<String,String> headers, String inlineData) throws IOException{
    	//put in URL to parse elements
		URL url = new URL(testUrl);
		String host = url.getHost();
		String path = url.getPath();
		String param = url.getQuery();
		int port = url.getPort() != -1 ? url.getPort():url.getDefaultPort();
		//System.out.println("host: " + host + "\nPath: " + path + "\nPort: " + port);
		
		//open socket to connect to server 
		//initialize output buffer to send request
		//initialize input buffer to receive response
        Socket socket1 = new Socket(host, port);
        PrintStream out = new PrintStream( socket1.getOutputStream() );
        BufferedReader in = new BufferedReader( new InputStreamReader( socket1.getInputStream() ) );
        
        //send the get request with parameters
        	out.println( "POST "+ path + " HTTP/1.0" );
        
        
        	out.println("User-Agent: " + USER_AGENT);
        	out.println("Accept-language: " + ACCEPT_LANG);
        	//out.println("Content-type: " + "text/html");
        	out.println("Host: " + host);
        	
        
        if(headers != null) {
        	for(String key: headers.keySet()) {
        		out.println(key +": " + headers.get(key));
        	}
        }
        
        //set the body of the request
       
        if(inlineData == null) {
        	out.println("Content-length: 0");
        	out.println();
        	out.println("");
        }
        else {
        	out.println("Content-length: " + inlineData.length());
        	out.println();
        	
            out.println(inlineData);
        	
        }
        
        
        out.println();
        out.println();
        String line = in.readLine();
        
        //do not show response header
        if(!verbose) 
        {
        while( line != null )
        {
        	//condition to not show the response header we skip until we reach two empty lines
        	if(line.isEmpty() ) {
        		while(line!= null) {
        			if(line.isEmpty()) {
        				line = in.readLine();
        				continue;
        			}
        			 System.out.println( line );
                     line = in.readLine();
        		}
        	}
        	line = in.readLine();	
           
       } 
        }
        
        //show response header
        else if(verbose){
        	while(line != null) {
        		System.out.println( line );
                line = in.readLine();
        	}
        }
     // Close our streams
        in.close();
        out.close();
        socket1.close();
   }
    //overload of post for file
    private static void sendPOST(String testUrl, boolean verbose, Map<String,String> headers, File file) throws IOException{
    	//put in URL to parse elements
		URL url = new URL(testUrl);
		String host = url.getHost();
		String path = url.getPath();
		String param = url.getQuery();
		int port = url.getPort() != -1 ? url.getPort():url.getDefaultPort();
		//System.out.println("host: " + host + "\nPath: " + path + "\nPort: " + port);
		
		//put the content of the file in a hashmap
		//Map<String,String> body = new HashMap<String,String>();
		String body = "";
		try {
			
			BufferedReader in = new BufferedReader(new FileReader("C:/Users/Cedric Paradis/Documents/postTest.txt"));
			String line ="";
			line = in.readLine();
			while (line!= null) {
				//String parts[] = line.split(":");
				//body.put(parts[0], parts[1]);
				body += line;
				line = in.readLine();
			}
			in.close();
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		
		//open socket to connect to server 
		//initialize output buffer to send request
		//initialize input buffer to receive response
        Socket socket1 = new Socket(host, port);
        PrintStream out = new PrintStream( socket1.getOutputStream() );
        BufferedReader in = new BufferedReader( new InputStreamReader( socket1.getInputStream() ) );
        
        //send the get request with parameters
        	out.println( "POST "+ path + " HTTP/1.0" );
        
       
        	out.println("User-Agent: " + USER_AGENT);
        	out.println("Accept-language: " + ACCEPT_LANG);
        	//out.println("Content-type: " + "application/json");
        	out.println("Host: " + host);
        	
        
        if(headers != null) {
        	
        	for(String key: headers.keySet()) {
        		out.println(key +":" + headers.get(key));
        	}
        	
        }
        
        //set the body of the request
       
       // if(body.keySet().isEmpty()) {
        if(body.length()==0) {
        	out.println("Content-length: 0");
        	out.println();
        	out.println("");
        }
        
        else {
        	out.println("Content-length: " + body.length());
        	out.println();
        	out.println(body);
        	/*
        	for(String key: body.keySet()) {
        		out.println(key +":" + body.get(key));
        	}
           	*/
        }
        
        
        out.println();
        out.println();
        
        //Output the response
        String line = in.readLine();
        
        //do not show response header
        if(!verbose) 
        {
        while( line != null )
        {
        	//condition to not show the response header we skip until we reach two empty lines
        	if(line.isEmpty() ) {
        		while(line!= null) {
        			if(line.isEmpty()) {
        				line = in.readLine();
        				continue;
        			}
        			 System.out.println( line );
                     line = in.readLine();
        		}
        	}
        	line = in.readLine();	
           
       } 
        }
        
        //show response header
        else if(verbose){
        	while(line != null) {
        		System.out.println( line );
                line = in.readLine();
        	}
        }
     // Close our streams
        in.close();
        out.close();
        socket1.close();
   }
		
    
    

    public static void main(String[] args) throws IOException {
      
    	Map<String,String>headers = new HashMap<String,String>();
    	
    	headers.put("Accept-Language", "fr");
    	
    	String param =("{\"Assignment\":1, \"Course\":5}");
    	//param = null;
        sendGET("http://httpbin.org/get?course=networking&assignment=1", false, headers);
    	File file = new File("C:/Users/Cedric Paradis/Documents/postTest.txt");
        sendPOST("http://httpbin.org/post", true, headers, file);
    }
}

