package mlchallenge.fuegoquasar.service.message;

import mlchallenge.fuegoquasar.FuegoQuasarApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static mlchallenge.fuegoquasar.service.SatelliteTestData.SATELLITE_1;
import static mlchallenge.fuegoquasar.service.SatelliteTestData.SATELLITE_2;
import static mlchallenge.fuegoquasar.service.SatelliteTestData.SATELLITE_3;
import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.MESSAGE_NOT_COMPLETED;
import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.NO_MESSAGE_SENT;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.EMPTY;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.MESSAGE;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.WORD1;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.WORD2;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.WORD3;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.WORD4;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.WORD5;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegoQuasarApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageBuilderTest {

    @Autowired
    MessageBuilder messageBuilder;

    @Test
    public void testSingleMessageOK(){
        final List<String> message1 = Arrays.asList(WORD1, WORD2, WORD3, WORD4, WORD5);
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        messageMap.put(SATELLITE_1, message1);

        messageBuilder.processMessages(messageMap);
        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);
    }

    @Test
    public void testMessageOK(){
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        final List<String> message1 = Arrays.asList(WORD1, WORD2, EMPTY, EMPTY, WORD5);
        messageMap.put(SATELLITE_1, message1);
        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        messageMap.put(SATELLITE_2, message2);
        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        messageMap.put(SATELLITE_3, message3);

        messageBuilder.processMessages(messageMap);
        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);
    }

    @Test
    public void testMessageLongOK(){
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        final List<String> message1 = Arrays.asList(EMPTY, WORD1, WORD2, EMPTY, EMPTY, WORD5);
        messageMap.put(SATELLITE_1, message1);
        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        messageMap.put(SATELLITE_2, message2);
        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        messageMap.put(SATELLITE_3, message3);

        messageBuilder.processMessages(messageMap);
        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);
    }

    @Test
    public void testMessageLongOK2(){
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        final List<String> message1 = Arrays.asList(EMPTY, EMPTY, WORD1, WORD2, EMPTY, EMPTY, WORD5);
        messageMap.put(SATELLITE_1, message1);
        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        messageMap.put(SATELLITE_2, message2);
        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        messageMap.put(SATELLITE_3, message3);

        messageBuilder.processMessages(messageMap);
        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);
    }

    @Test
    public void testMessageError(){
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        final List<String> message1 = Arrays.asList(WORD1, WORD2, EMPTY, EMPTY, WORD5);
        messageMap.put(SATELLITE_1, message1);
        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        messageMap.put(SATELLITE_2, message2);
        final List<String> message3 = Arrays.asList(WORD1, WORD2, EMPTY, EMPTY, EMPTY);
        messageMap.put(SATELLITE_3, message3);

        messageBuilder.processMessages(messageMap);
        assertThatExceptionOfType(MessageException.class).isThrownBy(messageBuilder::buildMessage).withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());
    }

    @Test
    public void testMessageLongError(){
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        final List<String> message1 = Arrays.asList(EMPTY, EMPTY, WORD1, WORD2, EMPTY, EMPTY, WORD5);
        messageMap.put(SATELLITE_1, message1);
        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        messageMap.put(SATELLITE_2, message2);
        final List<String> message3 = Arrays.asList(WORD1, WORD2, EMPTY, EMPTY, EMPTY);
        messageMap.put(SATELLITE_3, message3);

        messageBuilder.processMessages(messageMap);
        assertThatExceptionOfType(MessageException.class).isThrownBy(messageBuilder::buildMessage).withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());
    }

    @Test
    public void testBatchedMessageOK(){
        final List<String> message1 = Arrays.asList(WORD1, WORD2, EMPTY, EMPTY, WORD5);
        final HashMap<String,List<String>> messageMap1= new HashMap<>();
        messageMap1.put(SATELLITE_1, message1);
        messageBuilder.processMessages(messageMap1);

        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        final HashMap<String, List<String>> messageMap2 = new HashMap<>();
        messageMap2.put(SATELLITE_2, message2);
        messageBuilder.processMessages(messageMap2);

        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        final HashMap<String, List<String>> messageMap3 = new HashMap<>();
        messageMap3.put(SATELLITE_3, message3);
        messageBuilder.processMessages(messageMap3);

        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);
    }

    @Test
    public void testBatchedMessageLongOK(){
        final List<String> message1 = Arrays.asList(EMPTY, EMPTY, WORD1, WORD2, EMPTY, EMPTY, WORD5);
        final HashMap<String,List<String>> messageMap1= new HashMap<>();
        messageMap1.put(SATELLITE_1, message1);
        messageBuilder.processMessages(messageMap1);

        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, WORD4, WORD5);
        final HashMap<String, List<String>> messageMap2 = new HashMap<>();
        messageMap2.put(SATELLITE_2, message2);
        messageBuilder.processMessages(messageMap2);

        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        final HashMap<String, List<String>> messageMap3 = new HashMap<>();
        messageMap3.put(SATELLITE_3, message3);
        messageBuilder.processMessages(messageMap3);

        final String buildMessage = messageBuilder.buildMessage();
        Assert.assertEquals(MESSAGE, buildMessage);

    }

    @Test
    public void testBatchedMessageLongError(){
        final List<String> message1 = Arrays.asList(EMPTY, EMPTY, WORD1, WORD2, EMPTY, EMPTY, WORD5);
        final HashMap<String,List<String>> messageMap1= new HashMap<>();
        messageMap1.put(SATELLITE_1, message1);
        messageBuilder.processMessages(messageMap1);

        final List<String> message2 = Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, WORD5);
        messageMap1.put(SATELLITE_2, message2);
        final HashMap<String, List<String>> messageMap2 = new HashMap<>();
        messageBuilder.processMessages(messageMap2);

        final List<String> message3 = Arrays.asList(WORD1, WORD2, WORD3, EMPTY, EMPTY);
        messageMap1.put(SATELLITE_3, message3);
        final HashMap<String, List<String>> messageMap3 = new HashMap<>();
        messageBuilder.processMessages(messageMap3);

        assertThatExceptionOfType(MessageException.class).isThrownBy(messageBuilder::buildMessage).withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());
    }

    @Test
    public void testMessageEmptyError(){
        final List<String> message1 = Arrays.asList(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
        final HashMap<String,List<String>> messageMap= new HashMap<>();
        messageMap.put(SATELLITE_1, message1);

        messageBuilder.processMessages(messageMap);
        assertThatExceptionOfType(MessageException.class).isThrownBy(messageBuilder::buildMessage).withMessageContaining(NO_MESSAGE_SENT.getValue());
    }

    @Test
    public void testNoMessageError(){
        assertThatExceptionOfType(MessageException.class).isThrownBy(messageBuilder::buildMessage).withMessageContaining(NO_MESSAGE_SENT.getValue());
    }
}
