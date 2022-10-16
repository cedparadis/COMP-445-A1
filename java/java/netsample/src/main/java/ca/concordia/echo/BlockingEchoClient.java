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
            if(headers != null) {
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
        
        if(headers != null) {
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
		//list of headers
		List<String> list = opts.valuesOf(headers_arg);
		//hash map where the header keys and values will be added
		HashMap<String, String> headers = null;
		//check if list is not empty
		if (list.size() > 0) {
			//allocate headers to a non null HashMap
			headers = new HashMap<String, String>();
			for (int i = 0; i < list.size(); i++) {
				//check for correct format
				if (!(list.get(i)).contains(":")) {
					System.out.println("Invalid format in header" +list.get(i));
					System.exit(0);
					//if format ok, add the key value pairs to headers
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
		//Defining file_arg and inline_data_arg to help differentiate between the two post methods
		String file_arg = (String) opts.valueOf("file");
		String inline_data_arg = (String) opts.valueOf("inline-data");
		//making sure we can't select f and d at the same time, as specified by assignment
		if (opts.has("f") && opts.has("d")) {
			System.out.println("Options d and f cannot be specified at the same time.");
			System.exit(0);
		}
		boolean verbose = opts.has("v");
		//help
		if (args[0].equals("help")) {
			if(args.length == 1) {
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
			else if(args[1].equals("get")){
				System.out.println("usage: httpc get [-v] [-h key:value] URL\n" +
						"Get executes a HTTP GET request for a given URL.\n" +
						"-v Prints the detail of the response such as protocol, status, and headers.\n" +
						"-h key:value Associates headers to HTTP Request with the format 'key:value'.\n");
				System.exit(0);
			}
			else if(args[1].equals("get")){
		System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n" +
		"Post executes a HTTP POST request for a given URL with inline data or from\n" +
		"file.\n" +
		"-v Prints the detail of the response such as protocol, status, and headers.\n" +
		"-h key:value Associates headers to HTTP Request with the format 'key:value'.\n" +
		"-d string Associates an inline data to the body HTTP POST request.\n" +
		"-f file Associates the content of a file to the body HTTP POST request.\n" +
		"Either [-d] or [-f] can be used but not both.");
				System.exit(0);
			}
			else{
				System.out.println("Unknown command.");
				System.exit(0);
			}
		}

		//URL
		String theURL = args[args.length-1];
		//check if valid URL
		if(theURL.length()<4){
			System.out.println(theURL+ " is not a valid URL");
			System.exit(0);
		}
		else{
			String first4 = theURL.substring(0,4);
			if(!first4.equals("http")) {
				System.out.println(theURL + " is not a valid URL");
				System.exit(0);
			}
		}
		String cmd = "";
		if (args[0].equals("get")) {
			cmd = "get";
		} else if (args[0].equals("post")) {
			cmd = "post";
		} else {
			System.out.println("Invalid command" + args[0]);
			System.exit(0);
		}

		String param = ("{\"Assignment\":1, \"Course\":5}");
		//param = null;

		//GET
		if(cmd.equals("get")){
				sendGET(theURL,verbose, headers);
		}
		//sendGET("http://httpbin.org/get?course=networking&assignment=1", false, headers);
		//http://httpbin.org/post
		//POST
		if (cmd.equals("post")) {
			if (inline_data_arg.isEmpty()) {
				File file = new File(file_arg);
				sendPOST(theURL, verbose, headers, file);
			} else {
				sendPOST(theURL, verbose, headers, inline_data_arg);
			}
		}
	}
}

