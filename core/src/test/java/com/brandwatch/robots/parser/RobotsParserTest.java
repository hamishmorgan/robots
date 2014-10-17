package com.brandwatch.robots.parser;

import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class RobotsParserTest {

    @Mock
    private RobotsParseHandler handler;

    @Test(expected = NullPointerException.class)
    public void givenNullInputStream_whenNewInstance_thenThrowsNPE() {
        new RobotsParser((InputStream) null);
    }


    @Test
    public void givenEmptyStream_whenParser_thenNoExceptionThrown() throws IOException, ParseException {
        final InputStream inputStream = ByteSource.empty().openStream();
        RobotsParser robotsTxtParser = new RobotsParser(inputStream);
        robotsTxtParser.parse(handler);
    }

    @Test
    public void givenEmptyReader_whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        Reader reader = CharSource.empty().openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
    }

    @Test
    public void givenSingleBlankLine_whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        Reader reader = CharSource.wrap("\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
    }

    @Test
    public void givenSingleCommentLine_whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        Reader reader = CharSource.wrap("# comment line \n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
    }

    @Test
    public void givenTwoCommentLines_whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        Reader reader = CharSource.wrap("# comment line 1 \n# comment line 2 \n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
    }


    @Test
    public void givenLowerCaseUserAgentLine_whenParse_thenHandlerCalledWithExpectedAgent() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }

    @Test
    public void givenUpperCaseUserAgentLine_whenParse_thenHandlerCalledWithExpectedAgent() throws IOException, ParseException {
        Reader reader = CharSource.wrap("USER-AGENT: example-bot\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }

    @Test
    public void givenMixedCaseUserAgentLine_whenParse_thenHandlerCalledWithExpectedAgent() throws IOException, ParseException {
        Reader reader = CharSource.wrap("uSeR-aGeNt: example-bot\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }

    @Test
    public void givenTwoUserAgentLines_whenParse_thenHandlerCalledWithExpectedAgent() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: first\nuser-agent: second\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("first");
        verify(handler).userAgent("second");
    }

    @Test
    public void givenMixedCaseUserAgentLine_whenParse_thenStartEntryCalled() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).startEntry();
    }

    @Test
    public void givenMixedCaseUserAgentLine_whenParse_thenEndEntryCalled() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).endEntry();
    }

    @Test
    public void givenEmpty_whenParse_thenZeroInteractionOnHandler() throws IOException, ParseException {
        Reader reader = CharSource.empty().openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verifyZeroInteractions(handler);
    }

    @Test
    public void givenUserAgentMissingEOL_whenParse_thenEndEntryCalled() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).endEntry();
    }

    @Test
    public void givenEmptyDisallow_whenParse_thenDisallowCalledWithEmptyString() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\ndisallow:\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).disallow("");
    }


    @Test
    public void givenUserAgentWithTrailingSpace_whenParse_thenAgentIsTrimmed() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot \n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }


    @Test
    public void givenUserAgentEndingInComment_whenParse_thenAgentProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot # some comment\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }


    @Test
    public void givenBlankLineSeparatedAgents_whenParse_thenAgentsProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\n\n\n\nuser-agent: naughty-bot").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
        verify(handler).userAgent("naughty-bot");
    }


    @Test
    public void givenTrailingNewLines_whenParse_thenAgentProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: example-bot\n\n\n\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).userAgent("example-bot");
    }

    @Test
    public void givenHostRule_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\nhost: example.com\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).otherDirective("host", "example.com");
    }


    @Test
    public void givenCrawlDelayRule_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\ncrawl-delay: 10\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).otherDirective("crawl-delay", "10");
    }


    @Test
    public void givenUnsupportedRule_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\nCheese-burgers: yummy\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).otherDirective("Cheese-burgers", "yummy");
    }

    @Test
    public void givenMissingFinalEOL_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\nallow: /").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).allow("/");
    }

    @Test
    public void givenMissingPath_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\nallow:\n").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).allow("");
    }

    @Test
    public void givenMissingPathAndEOL_whenParse_thenDirectiveProduced() throws IOException, ParseException {
        Reader reader = CharSource.wrap("user-agent: *\nallow:").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
        verify(handler).allow("");
    }

    @Test
    public void givenCommentMissingEOL_whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        Reader reader = CharSource.wrap("#").openStream();
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        robotsTxtParser.parse(handler);
    }

}
