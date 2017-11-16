package com.github.richardjwild.blather.io;

import com.github.richardjwild.blather.repo.UserRepository;
import com.github.richardjwild.blather.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.richardjwild.blather.io.BlatherVerb.*;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InputParserShould {

    private static final User
            USER = new User(),
            RECIPIENT = new User(),
            SUBJECT = new User(),
            DUMMY_USER = new User();

    @Mock
    private UserRepository userRepository;
    private InputParser inputParser;

    @Before
    public void initialize() {
        inputParser = new InputParser(userRepository);
        when(userRepository.findByName(any())).thenReturn(DUMMY_USER);
    }

    @Test
    public void reads_a_read_command_verb() {
        String subject = "subject_to_read";
        String readCommand = format("%s", subject);

        BlatherVerb verb = inputParser.readVerb(readCommand);

        assertThat(verb).isEqualTo(BlatherVerb.READ);
    }

    @Test
    public void read_a_read_command_subject() {
        String subject = "subject_to_read";
        String readCommand = format("%s", subject);
        when(userRepository.findByName(subject)).thenReturn(SUBJECT);

        User actualSubject = inputParser.readSubject(readCommand);

        assertThat(actualSubject).isSameAs(SUBJECT);
    }

    @Test
    public void read_a_follow_command_verb() {
        String followCommand = "user follows subject";

        BlatherVerb verb = inputParser.readVerb(followCommand);

        assertThat(verb).isEqualTo(FOLLOW);
    }

    @Test
    public void read_a_follow_command_user() {
        String user = "a_user";
        String followCommand = format("%s follows subject", user);
        when(userRepository.findByName(user)).thenReturn(USER);

        User actualUser = inputParser.readUser(followCommand);

        assertThat(actualUser).isSameAs(USER);
    }

    @Test
    public void read_a_follow_command_subject() {
        String subject = "a_subject";
        String followCommand = format("user follows %s", subject);
        when(userRepository.findByName(subject)).thenReturn(SUBJECT);

        User actualSubject = inputParser.readFollowSubject(followCommand);

        assertThat(actualSubject).isSameAs(SUBJECT);
    }

    @Test
    public void read_a_post_command_verb() {
        String postCommand = "recipient -> message";

        BlatherVerb verb = inputParser.readVerb(postCommand);

        assertThat(verb).isEqualTo(POST);
    }

    @Test
    public void read_a_post_command_recipient() {
        String recipient = "a_recipient";
        String postCommand = format("%s -> message goes here", recipient);
        when(userRepository.findByName(recipient)).thenReturn(RECIPIENT);

        User actualRecipient = inputParser.readPostRecipient(postCommand);

        assertThat(actualRecipient).isSameAs(RECIPIENT);
    }

    @Test
    public void read_a_post_command_message() {
        String message = "this is a message";
        String postCommand = format("recipient -> %s", message);

        String actualMessage = inputParser.readPostMessage(postCommand);

        assertThat(actualMessage).isEqualTo(message);
    }

    @Test
    public void read_a_wall_command_verb() {
        String wallCommand = "subject wall";

        BlatherVerb verb = inputParser.readVerb(wallCommand);

        assertThat(verb).isEqualTo(WALL);
    }

    @Test
    public void read_a_quit_command() {
        BlatherVerb verb = inputParser.readVerb("Quit");

        assertThat(verb).isEqualTo(QUIT);
    }
}