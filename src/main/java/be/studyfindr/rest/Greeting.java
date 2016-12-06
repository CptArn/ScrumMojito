package be.studyfindr.rest;

/**
 * Represents a greeting object
 */
public class Greeting {
    // privates for id & message content
    private final long id;
    private final String content;

    /**
     * Creates a new message instance
     * @param id id of greeting
     * @param content content of the greeting
     */
    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    /**
     * Returns the id of the greeting
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the content of the message
     * @return
     */
    public String getContent() {
        return content;
    }
}
