package ca.concordia.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
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
            if(path!= null) {
            	out.println( "GET "+ path + "?"+ param + " HTTP/1.0" );
            }
            //without parameters
            else {
            	out.println( "GET "+ path + " HTTP/1.0" );
            }
            if(headers == null) {
            	out.println("User-Agent: " + USER_AGENT);
            	out.println("Accept-language: " + ACCEPT_LANG);
            	out.println("Host: " + host);
            }
            else {
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
        
     
    

    public static void main(String[] args) throws IOException {
       /* OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("host", "h"), "EchoServer hostname")
                .withOptionalArg()
                .defaultsTo("httpbin.org");

        parser.acceptsAll(asList("port", "p"), "EchoServer listening port")
                .withOptionalArg()
                .defaultsTo("80");

        OptionSet opts = parser.parse(args);

        String host = (String) opts.valueOf("host");
        System.out.println(host);
        int port = Integer.parseInt((String) opts.valueOf("port"));
*/
    	Map<String,String>headers = null;
        sendGET("http://httpbin.org/get?course=networking&assignment=1", false, headers);
    }
}

