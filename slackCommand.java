//slackCommand.java
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.net.*; //HttpURLConnection
import java.io.File;
import java.lang.*;  //THREAD_SLEEP
import java.net.InetAddress; //IP & HOSTNAME
import java.net.UnknownHostException; //HOSTNAME
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.riversun.slacklet.SlackletSession;
import org.riversun.slacklet.SletDefaultPersistManager;
import org.riversun.slacklet.SletPersistManager;
import org.riversun.xternal.simpleslackapi.SlackAttachment;
//
public class slackCommand {
    //GLOBAL_VARS
    public static String PROC = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    public static String PID = PROC.split("@")[0];
    //
    public static boolean FLAMEON = false;
    //SERVICE_VARS
    public static SlackletService slackService;

    //BOT_VARS
    public static String botToken = "xxxxx";
    public static String channelName = "xxxxx";

    //
    public static long BORED; //KEEP_ALIVE_GLOBAL
    public static long ALIVE; //CONNECTION_TIMEOUT
    public static boolean SLEEP = false; //SLEEP_STATE

    //ALLOWED_COMMANDS
    public static String[] CMDS = {"uname", "id", "who", "whoami", "mount", "ps", "dmidecode", "crontab", "dmesg", "free", "cat /proc/cpuinfo", "ls", "last", "ss", "lsof", "nmap", "netstat", "nslookup", "curl", "whois", "pwd", "hostname", "ifconfig", "ip", "uptime" };//ADD_COMMANDS_HERE

    //REGEX
    public static Pattern p = Pattern.compile("ping.*", Pattern.CASE_INSENSITIVE);
    public static Pattern p2 = Pattern.compile("pong.*", Pattern.CASE_INSENSITIVE);
    public static Pattern p3 = Pattern.compile("do i look fat in this dress\\?", Pattern.CASE_INSENSITIVE);
    public static Pattern p4 = Pattern.compile("\\.\\..*", Pattern.CASE_INSENSITIVE);
    public static Pattern Q = Pattern.compile("\\.\\.\\.", Pattern.CASE_INSENSITIVE);
    public static Pattern batman  = Pattern.compile("batman.*", Pattern.CASE_INSENSITIVE);

    /***** MAIN *****/
    public static void main(String[] args) throws IOException {
        //PRINT_PID
        System.out.println("MY_NAME_IS: "+ PROC);
        System.out.println("MY_PID_IS: "+ PID);
        // changing the name of Main thread
        Thread t = Thread.currentThread(); 
        t.setName("CAC"); 
        System.out.println("Main Thread NAME: " + t.getName()); 
        //PUT_ARRAY_HERE_OR_MAKE_PROPERTIES_FILE
        //REALTIME_CMD_VIA_CHAT
        //START_UP_IMAGE
        startUpImg();
        //START_RTM_API_CONNECTION
        startSlackBot();
        //TEST_NETWORK
        testNet();
        //START_THREAD
        threadKeepAlive KAT1 = new threadKeepAlive("Thread-1", slackService, channelName); KAT1.start(); //START_KEEP_ALIVE_THREAD
    }//END_MAIN


    /***** FUNCTIONS *****/
    //START_UP_SLACK_CONNCTION
    public static void startSlackBot(){
          try{
               if (FLAMEON){
                   slackService.stop(); //STOP
               }
               slackService = new SlackletService(botToken);
               slackService.addSlacklet(slackListener);//END_RTM_API_CONNECTION
               slackService.start(); //START_LOOP
               FLAMEON = true;
               //
               if (ALIVE > 0){
                   long NOW = System.currentTimeMillis();
                   long connUptime = NOW - ALIVE;
                   System.out.printf("Reconnection Interval: %d:ms %d:s %d:m %d:h %d:d %d:y %n", connUptime, connUptime / 1000, connUptime / 60000, connUptime / 60000 / 60, connUptime / 60000 / 60 / 24, connUptime / 60000 / 60 / 24 / 365 ); 
                   String RECONNECT = String.format("Reconnection Interval: %d:ms %d:s %d:m %d:h %d:d %d:y %n", connUptime, connUptime / 1000, connUptime / 60000, connUptime / 60000 / 60, connUptime / 60000 / 60 / 24, connUptime / 60000 / 60 / 24 / 365 ); 
                   slackService.sendMessageTo(channelName, RECONNECT);
               }
               ALIVE = System.currentTimeMillis(); //CONNECTION_ALIVE
               //
            } catch(IOException e){
               System.out.println("ERROR Starting (startSlackBot): " + e);
               threadWait(2000); //wait_2sec
               System.exit(-1); //DIE
            }
    }

    //START_UP_IMAGE
    public static void startUpImg(){
        try{
            //SEND_STARTUP_IMG
            SlackletService slackService_img = new SlackletService(botToken);
            slackService_img.start();
            final String imageUrl = "https://fcw.com/~/media/GIG/FCWNow/Topics/Cybersecurity/cyber_eye_1.jpg";
            //final String imageUrl = "https://i.ytimg.com/vi/0b-rijV98Qw/hqdefault.jpg";
            final String imageUrlFallBack = "https://cdn3.iconfinder.com/data/icons/artificial-intelligence-and-robotics/35/56-512.png";
            final SlackAttachment attchImage = new SlackAttachment();
            attchImage.setTitle("COMMAND AND CONTROL");
            attchImage.setText("Version HAL.9001.0.0000");
            attchImage.setFallback(imageUrlFallBack);
            attchImage.setColor("#ffffff");
            attchImage.setImageUrl(imageUrl);
            slackService_img.sendMessageTo(channelName, "Starting the CaC....",attchImage);
            slackService_img.stop();
        } catch (IOException e){
            System.out.println("ERROR startUpImg (slackService_img): " + e);
            threadWait(2000); //wait_2sec
            System.exit(-1); //DIE
        }
    }

    //CLEAN_UP_REMOVE_MARKDOWN_DATA
    public static String markdown(String mdata){
        //REMOVE_MARKDOWN_TAGS_FROM_SLACK
        System.out.println("Scrubbing data....");
        String MARK = mdata.replaceAll("<(.*)>","@$1@"); //MARK
        System.out.println("MARK: "+MARK);
        String HASH = MARK.replaceAll("@(.*)\\|(.*)@","@$1#$2@"); //HASH
        System.out.println("HASH: "+HASH);
        String FRONT = HASH.replaceAll("@.*\\#",""); //FRONT
        System.out.println("FRONT: "+FRONT);
        String BACK = FRONT.replaceAll("@",""); //BACK
        System.out.println(BACK);
        System.out.println("CLEAN DATA: "+BACK);
        return BACK;
    }

    //THREADWAIT
    public static void threadWait(long timeout){
         try {
            // thread to sleep for 1 sec = 1000 milliseconds 
            java.lang.Thread.sleep(timeout);
         } catch (Exception e) {
            System.out.println(e);
         }
    }

    //TEST NETWORK CONNECTION
    public static boolean testNet(){
        boolean NET = true;
        try {
            Runtime r = Runtime.getRuntime();
            //RUN_COMMAND_AS_ROOT (but only the command not the bot)
            String[] cmd = { "/usr/bin/sudo", "/bin/sh", "-c", "netstat -planet | grep -iw "+PID+"/java | grep -i established" }; //WORKS //remove ".."
            Process p = r.exec(cmd);
            //WAIT_FOR_CMD
            try { 
                p.waitFor(); 
            } catch (InterruptedException e) {
                 System.out.println("CHECK_NET [FAILED]: ");
                 e.printStackTrace();
                 System.exit(-1);
             }
                        //OUTPUT
                        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        if (b.readLine() != null) {
                            String line = "";
                            while ((line = b.readLine()) != null) {
                                System.out.println(line);
                            }
                            NET=true;
                        }else{
                            //System.exit(-1);//DIE?
                            //RESTART_CONNECTION
                            NET=false;
                        }
                        b.close();
                    } catch (IOException e) {
                        System.out.println("[testNet] exception: " +e);
                        //e.printStackTrace();
                        System.exit(-1);//DIE
                    }
         return NET;
    }

    /*** [ THREAD ] KEEP_ALIVE ***/
    //KEEP_ALIVE_TREAD
    public static class threadKeepAlive implements Runnable {
        private Thread t;
        private String threadName;
        private SlackletService _slackService;
        private String _channelName;
        //
        threadKeepAlive (String name, SlackletService slackService, String channelName) {
            threadName = "KeepAlive_t_"+name;
            _slackService = slackService; 
            _channelName = channelName;
            System.out.println("Creating " +  threadName );
        }
        //
        public void run() {
            System.out.println("Running " +  threadName );
            try {
                //THREAD_FUNCTION
                keepAliveLoop(_slackService, _channelName);
            } catch (Exception e) {
                System.out.println("Thread " +  threadName + " interrupted." +e);
            }
            System.out.println("Thread " +  threadName + " exiting.");
       }
       //
       public void start () {
           System.out.println("Starting " +  threadName );
           if (t == null) {
               t = new Thread (this, threadName);
               t.start ();
           }
       }
    }
    //KEEP_ALIVE_FUNCION
    public static void keepAlive(SlackletService slackService, String channelName){
        if (SLEEP){
            //slackService.sendMessageTo(channelName, "Zzzz");
            System.out.println("Zzzz");
            if (!testNet()){
                System.out.println("RESTARTING: Listener");
                startSlackBot();
                System.out.println("STARTED: Listener");
                slackService.sendMessageTo(channelName, "reseting connection.....");
            }
        } else {
            //slackService.sendMessageTo(channelName, "Starting sleep cycle......");
            System.out.println("Starting sleep cycle......");
            SLEEP = true;
        }
    }
    //KEEP_ALIVE_FUNCION_LOOP
    public static void keepAliveLoop(SlackletService slackService, String channelName){
        if (BORED == 0){
            BORED = System.currentTimeMillis(); //KEEP_ALIVE_TIMESTAMP_INIT_LOOP
        }
        while (true){
           //(300000) 5 min in milliseconds / 1 * 60000 (1 min)
            if ((System.currentTimeMillis() - BORED) > 300000 ){
                keepAlive(slackService,channelName);
                BORED = System.currentTimeMillis();
            }
        }
    }

   /*****************************
    * WHERE THE "MAGIC" HAPPENS *
    *****************************/
    //LISTENER
    public static Slacklet slackListener = new Slacklet() { 
            @Override
            public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // user posted message and BOT intercepted it
                // get message content
                String content = req.getContent();
                // reply to the user

                Matcher m = p.matcher(content);  
                if (m.matches()) {
                    System.out.printf("%s matches (PING)%n", content);
                    resp.reply("pong");
                    BORED = System.currentTimeMillis(); //KEEP_ALIVE_RESET (Put This In every Responce) [Or in the first Match]
                    SLEEP = false;
                } else {
                    System.out.printf("%s does not match [PING]%n", content);
                    BORED = System.currentTimeMillis(); //KEEP_ALIVE_RESET (Put This In every Responce) [Or in the first Match]
                    SLEEP = false;
                }

                Matcher m2 = p2.matcher(content);  
                if (m2.matches()) {
                    System.out.printf("%s matches(PONG)%n", content);
                    resp.reply("ping");
                } else {
                    System.out.printf("%s does not match [PONG]%n", content);
                }

                Matcher m3 = p3.matcher(content);  
                if (m3.matches()) {
                    System.out.printf("%s matches(DEVIL IN A BLUE DRESS)%n", content);
                    resp.reply("Yes, that and any dress....");
                } else {
                    System.out.printf("%s does not match [Dress]%n", content);
                }

                Matcher batmanM = batman.matcher(content);  
                if (batmanM.matches()) {
                    System.out.printf("%s matches(BATMAN)%n", content);
                    resp.reply("The Lego Batman Movie - Who's the (Bat)Man (Lyrics) \nhttps://www.youtube.com/watch?v=L9ScWnmjpMk \nhttps://www.youtube.com/results?search_query=The+Lego+Batman+Movie+-+Who%27s+the+%28Bat%29Man+%28Lyrics%29");
                } else {
                    System.out.printf("%s does not match[BatMan]%n", content);
                }


                //COMMANDS_FROM_SLACK: Fliter Commands
                Matcher m4 = p4.matcher(content);  
                if (m4.matches()) {
                    System.out.printf("%s matches (COMMAND)%n", content);  

                    //list command with "..."
                    Matcher QM = Q.matcher(content);  
                    if (QM.matches()) {
                        System.out.println("LIST_CMDS: "+Arrays.asList(CMDS));
                        resp.reply("```"+Arrays.asList(CMDS)+"```");
                    } else {
                        System.out.printf("%s does not match(CMD_LIST)%n", content);
                    }

                    //SHOW_CMD_CHUNKS
                    System.out.println("TRY_COMMAND: "+ content.substring(2).replaceAll(" .*",""));

                    String TRY = content.substring(2);
                    String[] tokens = TRY.split(" ");
                    //int tokenCount = tokens.length;
                    System.out.println(TRY +":"+tokens.length);

                    //STOP ARRAY OUT OF BOUNDS
                    if (tokens.length > 1){
                        System.out.println("TRY_COMMAND_TOKENS: "+tokens[0]+" "+tokens[1]);
                    } else {
                        System.out.println("TRY_COMMAND_TOKENS: "+tokens[0]);
                    }
                    //System.out.println(TRY +":"+tokens.length);


                    //MAKE_FUNCTION
                    if (tokens.length > 1){
                         System.out.println("MATCH_1?: "+ Arrays.asList(CMDS).contains(content.substring(2).replaceAll(" .*","")) +"\n"+content.substring(2).replaceAll(" .*","") );
                         System.out.println("MATCH_2?: "+ Arrays.asList(CMDS).contains(tokens[0]) +"\n"+ tokens[0] );
                         System.out.println("MATCH_A?: "+ Arrays.asList(CMDS).contains(tokens[0]+" "+tokens[1]) +"\n"+tokens[0]+" "+tokens[1] );
                         System.out.println("MATCH_B?: "+ Arrays.asList(CMDS).contains(tokens[0]+" "+tokens[1]) );
                    } else {
                         System.out.println("MATCH_1?: "+ Arrays.asList(CMDS).contains(content.substring(2).replaceAll(" .*","")) +"\n"+content.substring(2).replaceAll(" .*","") );
                         System.out.println("MATCH_2?: "+ Arrays.asList(CMDS).contains(tokens[0]) +"\n"+ tokens[0] );
                    }

                    //READ_CMD_CHUNKS
                    //if (Arrays.asList(CMDS).contains(content.substring(2).replaceAll(" .*",""))){
                    //if ( (Arrays.asList(CMDS).contains(tokens[0])) || (Arrays.asList(CMDS).contains(content.substring(2).replaceAll(" .*",""))) || (Arrays.asList(CMDS).contains(tokens[0]+" "+tokens[1])) ){
                    if ( (Arrays.asList(CMDS).contains(tokens[0])) || (Arrays.asList(CMDS).contains(content.substring(2).replaceAll(" .*",""))) || (Arrays.asList(CMDS).contains(tokens[0]+" "+tokens[1])) || Arrays.asList(CMDS).contains(tokens[0]+" "+tokens[1]) ){

                    System.out.println("CMD OK"); 
                    resp.reply("```OK, please hold...```");
                    //RUN_TIME_START (Slack Rate Limit CTRL)
                    long cmdstart = System.currentTimeMillis();

                    //MAKE_FUNCTION
                    //SHELL
                    try {
                        Runtime r = Runtime.getRuntime();
                        //Process p = r.exec(content.substring(2));//remove ".."
                        //USE_SHELL_INTERPRETER
                        System.out.println("RUN: "+markdown(content.substring(2))+"");
                        //String[] cmd = { "/bin/sh", "-c", ""+markdown(content.substring(2))+"" };//WORKS //remove ".."
                        //String[] cmd = { "/bin/sh", "-c", "ls | head -3" };//THIS_WORKS //remove ".."
                        //RUN_COMMAND_AS_ROOT (but only the command not the bot)
                        String[] cmd = { "/usr/bin/sudo", "/bin/sh", "-c", ""+markdown(content.substring(2))+"" };//WORKS //remove ".."
                        Process p = r.exec(cmd);

                        try { 
                            p.waitFor(); 
                        } catch (InterruptedException e) {
                            System.out.println("exception: ");
                            e.printStackTrace();
                            System.exit(-1);
                        }


                        //MAKE_FUNCTION
                        //RUN_TIME_END (Slack Rate Limit CTRL)
                        long cmdfin = System.currentTimeMillis();
                        long next = cmdfin - cmdstart;
                        long slackwait = 1001 - next; 
                        System.out.println("RUN_TIME (milliseconds): " + next);
                        System.out.println("NEEDED_WAIT_TIME (milliseconds): " + slackwait);
                        if (!(slackwait <= 0)){
                            threadWait(slackwait);
                        }


                        //MAKE_FUNCTION_OLD
                        /********* OLD REAL-TIME OUTPUT RETURN [BUFFER] *****************
                        //OUT
                        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = "";
                        while ((line = b.readLine()) != null) {
                            System.out.println(line);
                            resp.reply("```"+line+"```");
                        }
                        b.close();
                        //ERROR
                        BufferedReader brr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String error = "";
                        while ((error = brr.readLine()) != null) {
                            System.out.println(error);
                            resp.reply("```"+error+"```");
                        }
                        brr.close();
                        ***************************************************************/

                        //MAKE_FUNCTION
                        //NEW_CODE_4K_BUFFER [RETURN MORE INFO TO SLACK AND NOT GET BOCKED]
                        // https://api.slack.com/docs/rate-limits
                        //
                        BufferedReader bout = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        StringBuilder output = new StringBuilder(4001); // creates empty builder, capacity 4001
                        String line = "";
                        while ((line = bout.readLine()) != null) {
                            System.out.println(line);
                            if ( output.length() == 3800 ) {
                                System.out.println("OUTPUT SIZE: "+output.length()); //PRINT_SIZE
                                resp.reply("```"+output+"```"); //Send_output
                                output.setLength(0); //set_to_zero
                                threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            } else if ( (output.length() < 3800) && ((output.length() + line.length()) < 3800 ) ) {
                                output.append(line+"\n"); //PUSH_LINE_TO_output
                                output.length();
                                System.out.println("OUTPUT SIZE: "+output.length());  //PRINT_SIZE
                            } else {
                                System.out.println("OUTPUT SIZE: "+output.length()); //PRINT_SIZE
                                resp.reply("```"+output+"```"); //Send_output
                                output.setLength(0); //set_to_zero
                                threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            }
                        }
                        bout.close();
                        if ( output.length() != 0 ) {
                            threadWait(1001);  //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            System.out.println("OUTPUT SIZE: "+output.length()); //PRINT_SIZE
                            resp.reply("```"+output+"```"); //Send_output
                            output.setLength(0); //set_to_zero
                            threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                        }

                        //MAKE_FUNCTION
                        //NEW_ERROR_4K(~4k)
                        BufferedReader berror = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        StringBuilder errorOutput = new StringBuilder(4001); // creates empty builder, capacity 4001
                        String error = "";
                        while ((error = berror.readLine()) != null) {
                            System.out.println(error);
                            if ( errorOutput.length() == 3800 ) {
                                System.out.println("OUTPUT SIZE: "+errorOutput.length()); //PRINT_SIZE
                                resp.reply("```"+errorOutput+"```"); //Send_output
                                errorOutput.setLength(0); //set_to_zero
                                threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            } else if ( (errorOutput.length() < 3800) && ((errorOutput.length() + error.length()) < 3800 ) ) {
                                errorOutput.append(error+"\n"); //PUSH_LINE_TO_output
                                errorOutput.length();
                                System.out.println("OUTPUT SIZE: "+errorOutput.length());  //PRINT_SIZE
                            } else {
                                System.out.println("OUTPUT SIZE: "+errorOutput.length()); //PRINT_SIZE
                                resp.reply("```"+errorOutput+"```"); //Send_output
                                errorOutput.setLength(0); //set_to_zero
                                threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            }
                        }
                        berror.close();
                        if ( errorOutput.length() != 0 ) {
                            threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                            System.out.println("OUTPUT SIZE: "+errorOutput.length()); //PRINT_SIZE
                            resp.reply("```"+errorOutput+"```"); //Send_output
                            errorOutput.setLength(0); //set_to_zero
                            threadWait(1001); //WAIT_FOR_SLACK_RATE_LIMIT(1000ms)
                        }


                        resp.reply("```..done```"); //Send_output
                    } catch (IOException e) {
                        System.out.println("exception: ");
                        e.printStackTrace();
                        System.exit(-1);
                    }


                    } else { 
                            if (!(QM.matches())){
                                System.out.println("sorry, I can not do that.....");
                                resp.reply("sorry, I can not do that.....");
                            }
                    }//END_OF_SAFE_CMD

                } else {
                            System.out.printf("%s does not match%n", content);
                }//END_COMMANDS_FROM_SLACK
				

            }

            //MAKE_FUNCTION
            //ADD_REMOVE_COMMANDS_HERE (BY ALLOWED USER LIST)
            @Override
            public void onMentionedMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // BOT received message mentioned to the BOT such like "@bot ADD pwd"
                // from user.
                String command = req.getContent();
                // get 'mention' text formatted <@user> of sender user.
                String requester = req.getUserDisp();
                resp.reply("Ok," + requester + "adding cammand"+ command + "..." );
                //LIST COMMANDS
                //ADD_COMMAND_FUNCTION_HERE
                //LIST NEW LIST OF COMMANDS
            }

         }; //Need an end to the statment as this is a block-and-statment for [Slacklet slackListener = new Slacklet(){};]

}
//EOF

