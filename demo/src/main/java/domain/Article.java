package domain;


/**
 * The Article class is responsible for storing the news articles.
 */
public class Article {
    private final String title;
    private String content;
    private final String link;

    public Article(String title, String content, String link) {
        this.title = title;
        this.content = content;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String toString() {
        return title + "\n" + content + "\n" + link;
    }

    public void appendLineToContent(String line) {
        content += line + "\n";
    }
}
