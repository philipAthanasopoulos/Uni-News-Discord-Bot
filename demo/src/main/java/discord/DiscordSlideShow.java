package discord;

import java.util.ArrayList;
import java.util.List;

public class DiscordSlideShow {
    private final List<String> slides;
    private int currentSlideIndex = 0;

    public DiscordSlideShow() {
        slides = new ArrayList<>();
    }

    public void moveToNextSlide() {
        currentSlideIndex++;
        if (currentSlideIndex >= slides.size())
            currentSlideIndex = 0;
    }

    public void moveToPreviousSlide() {
        currentSlideIndex--;
        if (currentSlideIndex < 0)
            currentSlideIndex = slides.size() - 1;
    }

    public void addSlide(String slide) {
        slides.add(slide);
    }

    public String getCurrentSlide() {
        return slides.get(currentSlideIndex);
    }
}
