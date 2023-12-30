package WebsiteMonitor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class CseWebsiteMonitor{
    private WebDriver driver;
    private final String url = "https://www.cse.uoi.gr/nea/";
    private String lastPageSource = null;

    public CseWebsiteMonitor(){
        System.out.println("CseWebsiteMonitor initialized!");
    }

    public boolean hasWebsiteChanged(){
        driver = new ChromeDriver();
        driver.get(url);
        String pageSource = driver.findElement(By.cssSelector(".page-section ")).getAttribute("innerHTML");
        driver.close();
        if (lastPageSource != null && !lastPageSource.equals(pageSource)) {
            System.out.println("Website changed!");
            return true;
        }
        System.out.println("Last page source: " + lastPageSource);
        System.out.println("Website not changed!");
        lastPageSource = pageSource;

        return false;
    }
}
