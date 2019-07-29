package club.xyes.zkh.retail.map.impl;

import club.xyes.zkh.retail.map.dto.Location2AddressResult;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class AMapServiceImplTest {
    @Test
    public void testFormatJson() {
        String json = "{\"status\":\"1\",\"regeocode\":{\"addressComponent\":{\"city\":[],\"province\":\"北京市\",\"adcode\":\"110105\",\"district\":\"朝阳区\",\"towncode\":\"110105026000\",\"streetNumber\":{\"number\":\"6号\",\"location\":\"116.481977,39.9900489\",\"direction\":\"东南\",\"distance\":\"62.165\",\"street\":\"阜通东大街\"},\"country\":\"中国\",\"township\":\"望京街道\",\"businessAreas\":[{\"location\":\"116.470293,39.996171\",\"name\":\"望京\",\"id\":\"110105\"},{\"location\":\"116.494356,39.971563\",\"name\":\"酒仙桥\",\"id\":\"110105\"},{\"location\":\"116.492891,39.981321\",\"name\":\"大山子\",\"id\":\"110105\"}],\"building\":{\"name\":\"方恒国际中心B座\",\"type\":\"商务住宅;楼宇;商务写字楼\"},\"neighborhood\":{\"name\":\"方恒国际中心\",\"type\":\"商务住宅;楼宇;商住两用楼宇\"},\"citycode\":\"010\"},\"formatted_address\":\"北京市朝阳区望京街道方恒国际中心B座方恒国际中心\"},\"info\":\"OK\",\"infocode\":\"10000\"}";
        Location2AddressResult res = JSON.parseObject(json, Location2AddressResult.class);
        System.out.println(res);
    }
}