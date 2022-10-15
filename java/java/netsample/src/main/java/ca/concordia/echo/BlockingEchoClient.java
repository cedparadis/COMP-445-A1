package main.java.ca.concordia.echo;
import static java.util.Arrays.asList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;


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
		System.out.println("host: " + host + "\nPath: " + path + "\nPort: " + port);
		
		//open socket to connect to server 
		//initialize output buffer to send request
		//initialize input buffer to receive response
        Socket socket1 = new Socket(host, port);
        PrintStream out = new PrintStream( socket1.getOutputStream() );
        BufferedReader in = new BufferedReader( new InputStreamReader( socket1.getInputStream() ) );
        
        //send the get request with parameters
        	out.println( "POST "+ path + " HTTP/1.0" );
        
        if(headers == null) {
        	out.println("User-Agent: " + USER_AGENT);
        	out.println("Accept-language: " + ACCEPT_LANG);
        	out.println("Content-type: " + "text/html");
        	out.println("Host: " + host);
        	
        }
        else {
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
		System.out.println("host: " + host + "\nPath: " + path + "\nPort: " + port);
		
		//put the content of the file in a hashmap
		Map<String,String> body = new HashMap<String,String>();
			try {

				BufferedReader in = new BufferedReader(new FileReader(file));
				String line = "";
				line = in.readLine();
				while (line != null) {
					String parts[] = line.split(":");
					body.put(parts[0], parts[1]);
					line = in.readLine();
				}
				in.close();

			} catch (Exception e) {
				//System.err.println("File does not exist");
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
        
        if(headers == null) {
        	out.println("User-Agent: " + USER_AGENT);
        	out.println("Accept-language: " + ACCEPT_LANG);
        	out.println("Content-type: " + "application/json");
        	out.println("Host: " + host);
        	
        }
        else {
        	out.println("{");
        	for(String key: headers.keySet()) {
        		out.println(key +":" + headers.get(key));
        	}
        	out.println("}");
        }
        
        //set the body of the request
       
        if(body.keySet().isEmpty()) {
        	out.println("Content-length: 0");
        	out.println();
        	out.println("");
        }
        
        else {
        	out.println("Content-length: 20");
        	out.println();
        	for(String key: body.keySet()) {
        		out.println(key +":" + body.get(key));
        	}
           	
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
		//Create a parser and parse all the required options
        OptionParser parser = new OptionParser();
		//file
        parser.acceptsAll(asList("file", "f"), "File")
                .withOptionalArg();
		//help
		parser.acceptsAll(asList("h", "help"), "Help").forHelp();
		//verbose
		parser.acceptsAll(asList("v", "verbose"), "Verbose");
		//header (key val)
		OptionSpec<String> headers_arg = parser.accepts("h").withRequiredArg();
		//inline data
		parser.acceptsAll(asList("d", "inline-data"), "Inline data").withRequiredArg();
		OptionSet opts = parser.parse(args);
		List<String> list=opts.valuesOf(headers_arg);
		HashMap<String,String> headers = null;
		if(list.size()>0) {
			headers = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++) {
				if (!(list.get(i)).contains(":")) {
					System.out.println("Invalid format.");
				} else {
					System.out.println(list.get(i));
					String str = list.get(i);
					String[] arrOfString = str.split(":", 2);
					String key = arrOfString[0];
					String value = arrOfString[1];
					headers.put(key, value);
				}
			}
		}
		System.out.println(opts.valuesOf(headers_arg));
		String file_arg  = (String) opts.valueOf("file");
		String inline_data_arg = (String) opts.valueOf("inline-data");
		if(opts.has("f") && opts.has("d")){
			System.out.println("Options d and f cannot be specified at the same time.");
			System.exit(0);
		}
		boolean verbose = opts.has("v");

		if(args.length == 1 && args[0].equals("help")){
			System.out.println("httpc is a curl like application but supports HTTP protocol only");
			System.out.println("Usage:");
			System.out.println("httpc command [arguments]");
			System.out.println("The commands are:");
			System.out.println("get executes a HTTP GET request and prints the response.");
			System.out.println("post executes a HTTP POST request and prints the response.");
			System.out.println("help prints this screen.");
			System.out.println("Use httpc help [command] for more information about a command.");
			System.exit(0);
		}
        /*parser.acceptsAll(asList("port", "p"), "EchoServer listening port")
                .withOptionalArg()
                .defaultsTo("80");




        System.out.println(host);
        int port = Integer.parseInt((String) opts.valueOf("port"));*/


    	String param =("{\"Assignment\":1, \"Course\":5}");
    	//param = null;
        //sendGET("http://httpbin.org/get?course=networking&assignment=1", false, headers);

		if(inline_data_arg.isEmpty()) {
			File file = new File(file_arg);
			sendPOST("http://httpbin.org/post", verbose, headers, file);
		}
		else{
			sendPOST("http://httpbin.org/post", verbose, headers, inline_data_arg);
		}
    }
}

