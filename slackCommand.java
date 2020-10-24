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

public class slackBot {
    public static void main(String[] args) throws IOException {
        //BOT_VARS
        String botToken = "cexb-68939i9373375-d3757399809319-lrkvkPWFtjfldjfdfjldsjlkdf"; //spot.bot
        String channelName = "bot_talk";
        //ALLOWED_COMMANDS
        //PUT_ARRAY_HERE_OR_MAKE_PROPERTIES_FILE
        //SEND_STARTUP_IMG
        SlackletService slackService_img = new SlackletService(botToken);
        slackService_img.start();
        final String imageUrl = "https://www-tc.pbskids.org/fetch/ruff/img/home_hl_getting.png";
        final SlackAttachment attchImage = new SlackAttachment();
        attchImage.setTitle("SPOT BOT");
        attchImage.setText("Version 849208");
        attchImage.setFallback("");
        attchImage.setColor("#ffffff");
        attchImage.setImageUrl(imageUrl);
        slackService_img.sendMessageTo(channelName, "Starting the bot....",attchImage);
        slackService_img.stop();
        //REALTIME_CMD_VIA_CHAT
        //REGEX
        Pattern p = Pattern.compile("ping.*", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("pong.*", Pattern.CASE_INSENSITIVE);
        Pattern p3 = Pattern.compile("do i look fat in this dress\\?", Pattern.CASE_INSENSITIVE);
        Pattern p4 = Pattern.compile("\\.\\..*", Pattern.CASE_INSENSITIVE);

        SlackletService slackService = new SlackletService(botToken);
        slackService.addSlacklet( new Slacklet() { //START_RTM_API_CONNECTION
            @Override
            public void onMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // user posted message and BOT intercepted it
                // get message content
                String content = req.getContent();
                // reply to the user

                Matcher m = p.matcher(content);  
                if (m.matches()) {
                    System.out.printf("%s matches%n", content);
                    resp.reply("pong");
                } else {
                    System.out.printf("%s does not match%n", content);
                }

                Matcher m2 = p2.matcher(content);  
                if (m2.matches()) {
                    System.out.printf("%s matches%n", content);
                    resp.reply("ping");
                } else {
                    System.out.printf("%s does not match%n", content);
                }

                Matcher m3 = p3.matcher(content);  
                if (m3.matches()) {
                    System.out.printf("%s matches%n", content);
                    resp.reply("Yes, that and any dress....");
                } else {
                    System.out.printf("%s does not match%n", content);
                }

                //COMMANDS_FROM_SLACK
                //Fliter Commands
                Matcher m4 = p4.matcher(content);  
                if (m4.matches()) {
                    System.out.printf("%s matches%n", content);  
                    //SAFE_CMD  
                    if (Arrays.asList("uname", "nslookup", "whois", "pwd", "hostname", "ifconfig", "ip", "uptime", "last", "tail", "octopi").contains(content.substring(2).replaceAll(" .*",""))){ 
                    System.out.println("CMD OK"); 
                               
                    try {
                        Runtime r = Runtime.getRuntime();
                        //Process p = r.exec(content.substring(2));//remove ".."
                        //USE_SHELL_INTERPRETER
                        System.out.println("RUN: "+markdown(content.substring(2))+"");
                        String[] cmd = { "/bin/sh", "-c", ""+markdown(content.substring(2))+"" };//WORKS //remove ".."
                        //String[] cmd = { "/bin/sh", "-c", "ls | head -3" };//THIS_WORKS //remove ".."
                        Process p = r.exec(cmd);

                        try { p.waitFor(); } catch (InterruptedException e) {
                            System.out.println("exception: ");
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        //OUT
                        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String error = "";
                        while ((error = b.readLine()) != null) {
                            System.out.println(error);
                            resp.reply("```"+error+"```");
                        }
                        b.close();
                        //ERROR
                        BufferedReader brr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line = "";
                        while ((line = brr.readLine()) != null) {
                            System.out.println(line);
                            resp.reply("```"+line+"```");
                        }
                        brr.close();
                        } catch (IOException e) {
                            System.out.println("exception: ");
                            e.printStackTrace();
                            System.exit(-1);
                        }


                    } else { 
                            System.out.println("sorry, I can not do that.....");
                            resp.reply("sorry, I can not do that.....");
                    }//END_OF_SAFE_CMD

                } else {
                            System.out.printf("%s does not match%n", content);
                }//END_COMMANDS_FROM_SLACK
				
            }

            @Override
            public void onMentionedMessagePosted(SlackletRequest req, SlackletResponse resp) {
                // BOT received message mentioned to the BOT such like "@bot How are you?"
                // from user.
                String content = req.getContent();
                // get 'mention' text formatted <@user> of sender user.
                String mention = req.getUserDisp();
                resp.reply("Hi," + mention + "how can I help you with "+ content + "?" );
            }

            });//END_RTM_API_CONNECTION
            slackService.start(); //START_LOOP
	}

    public static String markdown(String mdata){
    //REMOVE_MARKDOWN_TAGS
        //CHOP_OFF_BAD_DATA
        /*System.out.println("Scrubbing data....");
        String MARK = mdata.replaceFirst("<(.*)>","@$1@"); //FRONT
        String HASH = MARK.replaceFirst("\\|","#"); //FRONT
        String FRONT = HASH.replaceAll("@.*\\#",""); //FRONT
        String BACK = FRONT.replaceAll("@",""); //BACK
        System.out.println(BACK);
        System.out.println("CLEAN DATA: "+BACK);
        return BACK;*/
        //CHOP_OFF_BAD_DATA(BETTER?)
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

}
//EOF


