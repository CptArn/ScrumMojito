package be.studyfindr.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The GreetingController defines a test interface for this service.
 * Polling this controller can be used to check the status of the service.
 * @version 1.0
 */
@RestController
public class GreetingController {
    // message placeholder
    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    /**
     * This route can be polled the check the status of the service.
     * @param name string of text you returned as answer.
     * @return message "Hello, [you query message]!"
     */
    @CrossOrigin
    @RequestMapping("/hello")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

}