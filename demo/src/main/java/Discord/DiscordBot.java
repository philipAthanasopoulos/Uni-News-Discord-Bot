package Discord;

import app.Unicodes;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {
    public static String banner = "MMMMMMMMMMMMMMMMMMMMMMMMMMWNXKO0NMMMMMMMMMMMMMMN0OKXNWMMMMMMMMMMMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMMMMMMWNK0kxdolllONWWNNNNNNNNWWNOlclodxk0KNWMMMMMMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMMMWXOdolcccccccclodoooooooooodolcccccccclodOXWMMMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMMWKdccccccccccccccccccccccccccccccccccccccccdKWMMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMW0occcccccccccccccccccccccccccccccccccccccccco0WMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMW0occcccccccccccccccccccccccccccccccccccccccccco0WMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMW0occcccccccccccccccccccccccccccccccccccccccccccco0WMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMKdccccccccccccccccccccccccccccccccccccccccccccccccdKMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMNxccccccccccccccccccccccccccccccccccccccccccccccccccxNMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMWOlcccccccccccclodxdoccccccccccccccodxxolcccccccccccclOWMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMXdccccccccccclxKNWWWXOoccccccccccoOXWWWNKxlcccccccccccdXMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMWOlccccccccccckNMMMMMMWKoccccccccoKWMMMMMMNkccccccccccclOWMMMMMMMMMMM\n" +
            "MMMMMMMMMMMNxcccccccccccl0MMMMMMMMNxccc::cccxNMMMMMMMM0lcccccccccccxNMMMMMMMMMMM\n" +
            "MMMMMMMMMMMXdcccccccccccckNMMMMMMMKoccccccccoKMMMMMMMNkccccccccccccdXMMMMMMMMMMM\n" +
            "MMMMMMMMMMMKocccccccccccclkXWMMWN0dccccccccccd0NWMMWXklccccccccccccoKMMMMMMMMMMM\n" +
            "MMMMMMMMMMM0lccccccccccccccodkkxolccccccccccccloxkkdlccccccccccccccl0MMMMMMMMMMM\n" +
            "MMMMMMMMMMM0lccccccccccccccccccccccccccccccccccccccccccccccccccccccl0MMMMMMMMMMM\n" +
            "MMMMMMMMMMM0lcccccccccccccccccccccccccccccccccccccccccccccccccccccco0MMMMMMMMMMM\n" +
            "MMMMMMMMMMMXxlccccccccccokOkdolcccccccccccccccccclodkOkocccccccccclxXMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMNKkdlccccccccoxkKXXK0OkxxxddddxxxkO0KXX0kxoccccccccldkKNMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMWNKOxoccccccco0WMMMMMMWWWWWWWWMMMMMMW0occcccccoxOKNMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMMMWNKOxdlldKWMMMMMMMMMMMMMMMMMMMMMMWKdlldxOKNWMMMMMMMMMMMMMMMMMM\n" +
            "MMMMMMMMMMMMMMMMMMMMMMWNK0XWMMMMMMMMMMMMMMMMMMMMMMMMWX0KNWMMMMMMMMMMMMMMMMMMMMMM\n";

    public static void main(String[] args) throws InterruptedException {

        JDA jda = JDABuilder.createDefault(DiscordToken.token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("with the UoI website"))
                .build()
                .awaitReady();

        for (char letter : banner.toCharArray()) {
            if (letter == 'c') System.out.print(Unicodes.blue + letter + Unicodes.reset);
            else System.out.print(Unicodes.white + letter + Unicodes.reset);
        }

        BotListeners listeners = new BotListeners(jda);
        jda.addEventListener(listeners);
    }
}
