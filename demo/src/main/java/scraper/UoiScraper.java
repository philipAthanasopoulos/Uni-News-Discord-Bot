package scraper;

import app.resources.Unicodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UoiScraper extends Scraper{
    private String CSElink = "https://www.cse.uoi.gr/";

    public void scrapeNews(){
        String newsLink = CSElink + "nea/";
        ArrayList<Document> newsLinks = scrapeNewsLinks();
        System.out.println(presentNews(newsLinks));
    }

    public String scrapeNewsToString(){
        String newsLink = CSElink + "nea/";
        ArrayList<Document> newsLinks = scrapeNewsLinks();
        return presentNews(newsLinks);
    }

    public String scrapeNewsForDiscord(){
        String newsLink = CSElink + "nea/";
        ArrayList<Document> newsLinks = scrapeNewsLinks();
        return presentNewsForDiscord(newsLinks);
    }

    public ArrayList<Document> scrapeNewsLinks(){
        String newsLink = CSElink + "nea/";
        Document doc = scrapeSite(newsLink);
        ArrayList<String> newsLinks = new ArrayList<>();
        Elements links = doc.select(".cs-campus-info").select("h6").select("a[href]");
        for(Element link : links) newsLinks.add(link.attr("abs:href"));
        ArrayList<Document> news = new ArrayList<>();
        for(String link : newsLinks) news.add(scrapeSite(link));
        return news;
    }

    public String presentNews(ArrayList<Document> news){
        StringBuilder sb = new StringBuilder();
            for(Document doc : news){
                Elements title = doc.select(".page-content").select("h1");
                Elements content = doc.select(".page-content").select("p");
                for (int i = 0; i < title.size(); i++) {
                    sb.append(Unicodes.green + title.get(i).text() + Unicodes.reset).append("\n");
                    sb.append(content.get(i).text()).append("\n");
                }
            }
        return sb.toString();
    }

    public String presentNewsForDiscord(ArrayList<Document> news){
        StringBuilder sb = new StringBuilder();
        for(Document doc : news){
            Elements title = doc.select(".page-content").select("h1");
            Elements content = doc.select(".page-content").select("p");
            for (int i = 0; i < title.size() && sb.length() < 1000; i++) {
                sb.append(title.get(i).text()).append("\n");
                sb.append(content.get(i).text()).append("\n");
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        UoiScraper scraper = new UoiScraper();
        scraper.scrapeNews();
    }



}
