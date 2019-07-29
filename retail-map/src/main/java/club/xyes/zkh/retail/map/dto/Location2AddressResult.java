package club.xyes.zkh.retail.map.dto;

import lombok.Data;

import java.util.List;

/**
 * Create by 郭文梁 2019/7/17 10:22
 * Location2AddressResult
 *
 * @author 郭文梁
 * @data 2019/7/17 10:22
 */
@Data
public class Location2AddressResult {

    /**
     * status : 1
     * regeocode : {"addressComponent":{"city":[],"province":"北京市","adcode":"110105","district":"朝阳区","towncode":"110105026000","streetNumber":{"number":"6号","location":"116.481977,39.9900489","direction":"东南","distance":"62.165","street":"阜通东大街"},"country":"中国","township":"望京街道","businessAreas":[{"location":"116.470293,39.996171","name":"望京","id":"110105"},{"location":"116.494356,39.971563","name":"酒仙桥","id":"110105"},{"location":"116.492891,39.981321","name":"大山子","id":"110105"}],"building":{"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"},"neighborhood":{"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"},"citycode":"010"},"formatted_address":"北京市朝阳区望京街道方恒国际中心B座方恒国际中心"}
     * info : OK
     * infocode : 10000
     */

    private Integer status;
    private Regeocode regeocode;
    private String info;
    private String infocode;

    @Data
    public static class Regeocode {
        /**
         * addressComponent : {"city":[],"province":"北京市","adcode":"110105","district":"朝阳区","towncode":"110105026000","streetNumber":{"number":"6号","location":"116.481977,39.9900489","direction":"东南","distance":"62.165","street":"阜通东大街"},"country":"中国","township":"望京街道","businessAreas":[{"location":"116.470293,39.996171","name":"望京","id":"110105"},{"location":"116.494356,39.971563","name":"酒仙桥","id":"110105"},{"location":"116.492891,39.981321","name":"大山子","id":"110105"}],"building":{"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"},"neighborhood":{"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"},"citycode":"010"}
         * formatted_address : 北京市朝阳区望京街道方恒国际中心B座方恒国际中心
         */

        private AddressComponent addressComponent;
        private String formatted_address;

        @Data
        public static class AddressComponent {
            /**
             * city : []
             * province : 北京市
             * adcode : 110105
             * district : 朝阳区
             * towncode : 110105026000
             * streetNumber : {"number":"6号","location":"116.481977,39.9900489","direction":"东南","distance":"62.165","street":"阜通东大街"}
             * country : 中国
             * township : 望京街道
             * businessAreas : [{"location":"116.470293,39.996171","name":"望京","id":"110105"},{"location":"116.494356,39.971563","name":"酒仙桥","id":"110105"},{"location":"116.492891,39.981321","name":"大山子","id":"110105"}]
             * building : {"name":"方恒国际中心B座","type":"商务住宅;楼宇;商务写字楼"}
             * neighborhood : {"name":"方恒国际中心","type":"商务住宅;楼宇;商住两用楼宇"}
             * citycode : 010
             */

            private String province;
            private String adcode;
            private String district;
            private String towncode;
            private StreetNumber streetNumber;
            private String country;
            private String township;
            private Building building;
            private Neighborhood neighborhood;
            private String citycode;
            private List<String> city;
            private List<BusinessAreas> businessAreas;

            @Data
            public static class StreetNumber {
                /**
                 * number : 6号
                 * location : 116.481977,39.9900489
                 * direction : 东南
                 * distance : 62.165
                 * street : 阜通东大街
                 */

                private String number;
                private String location;
                private String direction;
                private String distance;
                private String street;

            }

            @Data
            public static class Building {
                /**
                 * name : 方恒国际中心B座
                 * type : 商务住宅;楼宇;商务写字楼
                 */

                private String name;
                private String type;
            }

            @Data
            public static class Neighborhood {
                /**
                 * name : 方恒国际中心
                 * type : 商务住宅;楼宇;商住两用楼宇
                 */

                private String name;
                private String type;
            }

            @Data
            public static class BusinessAreas {
                /**
                 * location : 116.470293,39.996171
                 * name : 望京
                 * id : 110105
                 */

                private String location;
                private String name;
                private String id;
            }
        }
    }
}
