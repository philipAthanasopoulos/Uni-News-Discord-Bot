package Discord;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import scraper.UoiScraper;

public class BotListeners extends ListenerAdapter {

    private JDA jda = null;
    private TextChannel generalChannel;
    private TextChannel newsChannel;
    private UoiScraper scraper;

    public BotListeners(JDA jda) throws SchedulerException {
        this.jda = jda;
        this.generalChannel = jda.getTextChannelsByName("general", false).get(0);
        this.newsChannel = jda.getTextChannelsByName("news", false).get(0);
        System.out.println("General channel: " + generalChannel);
        System.out.println("News channel: " + newsChannel);
        this.scraper = new UoiScraper();
        sendDailyNews();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        TextChannel channel = event.getChannel().asTextChannel();
        String message = event.getMessage().getContentRaw();

        switch(message){
            case "!ping":
                channel.sendMessage("Pong!").queue();
                break;
            case "!news":
                scraper.presentNewsForDiscord(channel);
                break;
            case "!latest news":
                scraper.presentLatestNewsForDiscord(channel);
                break;
            case "!help":
                //explain commands
                channel.sendMessage("Commands:\n" +
                        "**!ping** - Pong!\n" +
                        "**!news** - Get all news\n" +
                        "**!latest news** - Get latest news article\n" +
                        "**!help** - Get help")
                        .queue();
                break;
            default:
                break;
        }
    }

    public class NewsJob implements Job{
        public NewsJob(){
            System.out.println("Creating job");
        }
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
            System.out.println("Executing job");
        
        }
    }

    public void sendDailyNews() throws SchedulerException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        JobDetail scrapeNewsJob = JobBuilder.newJob(NewsJob.class)
                .withIdentity("presentNewsForDiscordJob", "group1")
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * ? * * *"))
                .build();

        scheduler.scheduleJob(scrapeNewsJob, trigger);

        //fire immediately
        scheduler.triggerJob(scrapeNewsJob.getKey());
    }
}
