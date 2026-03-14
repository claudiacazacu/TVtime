public class WatchEntry
{
    private final Media media;
    private final boolean completed;

    public WatchEntry(Media media, boolean completed)
    {
        this.media = media;
        this.completed = completed;
    }

    public Media getMedia()
    {
        return media;
    }

    public boolean isCompleted()
    {
        return completed;
    }
}
