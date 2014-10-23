package com.brandwatch.robots.cli.converters;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;

import static com.brandwatch.robots.cli.TestUtils.array;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CharsetConverterDataTest {

    private CharsetConverter converter;

    private final String value;
    private final Charset expected;

    public CharsetConverterDataTest(String value, Charset expected) {
        this.value = checkNotNull(value, "value is null");
        this.expected = checkNotNull(expected, "expected is null");
    }

    @Parameterized.Parameters(name = "{index}: {0} => {1}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()

                .add(array("ISO-8859-1", Charsets.ISO_8859_1))
                .add(array("iso-ir-100", Charsets.ISO_8859_1))
                .add(array("csISOLatin1", Charsets.ISO_8859_1))
                .add(array("latin1", Charsets.ISO_8859_1))
                .add(array("l1", Charsets.ISO_8859_1))
                .add(array("IBM819", Charsets.ISO_8859_1))
                .add(array("CP819", Charsets.ISO_8859_1))

                .add(array("ascii", Charsets.US_ASCII))
                .add(array("ANSI_X3.4-1968", Charsets.US_ASCII))
                .add(array("ISO_646.irv:1991", Charsets.US_ASCII))
                .add(array("cp367", Charsets.US_ASCII))
                .add(array("iso-ir-6", Charsets.US_ASCII))
                .add(array("ASCII", Charsets.US_ASCII))
                .add(array("us", Charsets.US_ASCII))
                .add(array("csASCII", Charsets.US_ASCII))
                .add(array("ANSI_X3.4-1986", Charsets.US_ASCII))
                .add(array("ISO646-US", Charsets.US_ASCII))
                .add(array("IBM367", Charsets.US_ASCII))

                .add(array("UTF8", Charsets.UTF_8))
                .add(array("UTF-8", Charsets.UTF_8))

                .add(array("UTF_16", Charsets.UTF_16))
                .add(array("UTF-16", Charsets.UTF_16))
                .add(array("UTF16", Charsets.UTF_16))
                .add(array("unicode", Charsets.UTF_16))

                .add(array("UTF_16BE", Charsets.UTF_16BE))
                .add(array("UTF-16BE", Charsets.UTF_16BE))

                .add(array("UTF_16LE", Charsets.UTF_16LE))
                .add(array("UTF-16LE", Charsets.UTF_16LE))

                .build();
    }

    @Before
    public void setup() {
        converter = new CharsetConverter();
    }

    @Test
    public void givenLatin1Value_withDash_whenConvert_thenReturnsExpectedCharset() {
        Charset result = converter.convert(value);
        assertThat(result, equalTo(expected));
    }

}
