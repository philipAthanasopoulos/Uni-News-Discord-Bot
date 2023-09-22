package Scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class UoiMenuScraper extends Scraper{
    private final String universtityMenusHomeLink = "https://www.uoi.gr/tag/menou-lesxis/";

    public void scrapeLastMonthsMenu(){
        Document page = scrapeSite(universtityMenusHomeLink);
        Element lastMonthsMenu = page.select(".wf-cell iso-item shown")
                                        .select("a[href]")
                                        .first();
        System.out.println(lastMonthsMenu.attr("abs:href"));
//        String lastMonthsMenuLink = lastMonthsMenu.attr("abs:href");
//        Document lastMonthsMenuDocument = scrapeSite(lastMonthsMenuLink);
//        Element lastMonthsMenuDownloadLink = lastMonthsMenuDocument
//                                                .select(".pods-value")
//                                                .select("a[href]")
//                                                .first();
//        System.out.println(lastMonthsMenuDownloadLink.attr("abs:href"));
//        Document lastMonthsMenuDownloadDocument = scrapeSite(lastMonthsMenuDownloadLink.attr("abs:href"));
//        String res = lastMonthsMenuDownloadDocument.toString();
//        System.out.println(res);
    }

    public static void main(String[] args) {
        UoiMenuScraper scraper = new UoiMenuScraper();
        scraper.scrapeLastMonthsMenu();
    }
}
