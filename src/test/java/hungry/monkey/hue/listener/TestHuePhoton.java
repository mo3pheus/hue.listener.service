package hungry.monkey.hue.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import hungry.monkey.hue.listener.domain.HuePhoton;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

public class TestHuePhoton {
    HuePhoton huePhoton;

    @Before
    public void setUp() {
        Float[] xy = new Float[2];
        xy[0] = 0.5425f;
        xy[1] = 0.4196f;
        huePhoton = new HuePhoton(xy, 12750, 200, 42, true);
    }

//    @Test
//    public void testStringRep() {
//        String hueString = huePhoton.toString();
//        String expectedString = "{\"on\":true,\"xy\":[0.5425,0.4196],\"bri\":42,\"hue\":12750,\"sat\":200}";
//        Assert.assertEquals(expectedString, hueString);
//        System.out.println(hueString);
//    }

    @Test
    @SneakyThrows
    public void testObjectParsing() {
        ObjectMapper objectMapper = new ObjectMapper();
        String str1 = objectMapper.writeValueAsString(huePhoton);
        System.out.println(str1);

        HuePhoton huePhoton1 = objectMapper.readValue(str1, HuePhoton.class);
        String str2 = objectMapper.writeValueAsString(huePhoton1);
        System.out.println(str2);
    }
}
