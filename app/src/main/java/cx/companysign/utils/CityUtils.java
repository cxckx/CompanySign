package cx.companysign.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by cxcxk on 2016/4/21.
 */
public class CityUtils {

    public static Map<String, String> prvince = new TreeMap<>();
    public static Map<String, List<String>> citys = new TreeMap<>();

    private static List<String> anhui = Arrays.asList("合肥", "巢湖", "蚌埠", "安庆", "六安", "滁州", "马鞍山", "阜阳", "宣城",
            "铜陵", "淮北", "芜湖", "宿州", "淮南", "池州");
    private static List<String> fujian = Arrays.asList("福州", "厦门", "龙岩", "南平", "宁德", "莆田", "泉州", "三明", "漳州");
    private static List<String> guangdong = Arrays.asList("广州", "深圳", "潮州", "韶关", "湛江", "惠州", "清远", "东莞", "江门", "茂名", "肇庆", "汕尾", "河源"
            , "揭阳", "梅州", "中山", "德庆", "阳江", "云浮", "珠海", "汕头", "佛山");
    private static List<String> guizhou = Arrays.asList("贵阳", "安顺", "赤水", "遵义", "铜仁", "六盘水", "毕节", "凯里", "都匀");
    private static List<String> gansu = Arrays.asList("兰州", "白银", "庆阳", "酒泉", "天水", "武威", "张掖", "甘南", "临夏", "平凉", "定西", "金昌");
    private static List<String> guangxi = Arrays.asList("南宁", "桂林", "阳朔", "柳州", "梧州", "玉林", "桂平", "贺州", "钦州", "贵港", "防城港", "百色", "北海", "河池", "来宾", "崇左");
    private static List<String> hebei = Arrays.asList("石家庄", "唐山", "张家口", "廊坊", "邢台", "邯郸", "沧州", "衡水", "承德", "保定", "秦皇岛");
    private static List<String> heilongjiang = Arrays.asList("哈尔滨", "齐齐哈尔", "牡丹江", "大庆", "伊春", "双鸭山", "鹤岗", "鸡西", "佳木斯", "七台河", "黑河", "绥化", "大兴安岭");
    private static List<String> henan = Arrays.asList("郑州", "开封", "洛阳", "平顶山", "焦作", "鹤壁", "新乡", "安阳", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店");
    private static List<String> hubei = Arrays.asList("武汉", "宜昌", "黄冈", "恩施", "荆州", "神农架", "十堰", "咸宁", "襄樊", "孝感", "随州", "黄石", "荆门", "鄂州");
    private static List<String> hunan = Arrays.asList("长沙", "邵阳", "常德", "郴州", "吉首", "株洲", "娄底", "湘潭", "益阳", "永州", "岳阳", "衡阳", "怀化", "韶山", "张家界");
    private static List<String> hainan = Arrays.asList("海口", "三亚", "儋州", "琼海", "万宁", "五指山", "文昌", "东方");
    private static List<String> jilin = Arrays.asList("长春", "吉林", "白山", "白城", "四平", "松原", "辽源", "大安", "通化");
    private static List<String> jiangsu = Arrays.asList("南京", "苏州", "昆山", "南通", "太仓", "吴县", "徐州", "宜兴", "镇江", "淮安", "常熟", "盐城", "泰州", "无锡"
            , "连云港", "扬州", "常州", "宿迁");
    private static List<String> jiangxi = Arrays.asList("南昌", "萍乡", "九江", "上饶", "抚州", "吉安", "鹰潭", "宜春", "新余", "景德镇", "赣州");
    private static List<String> liaoning = Arrays.asList("沈阳", "大连", "葫芦岛", "旅顺", "本溪", "抚顺", "铁岭", "辽阳", "营口", "阜新", "朝阳", "锦州", "丹东", "鞍山");
    private static List<String> neimenggu = Arrays.asList("呼和浩特", "锡林浩特", "包头", "赤峰", "海拉尔", "乌海", "鄂尔多斯", "通辽");
    private static List<String> ningxia = Arrays.asList("银川", "固原", "中卫", "石嘴山", "吴中");
    private static List<String> qinghai = Arrays.asList("西宁", "海北", "海西", "黄南", "果洛", "玉树", "海东", "海南");
    private static List<String> shanxi = Arrays.asList("太原", "阳泉", "晋城", "晋中", "临汾", "运城", "长治", "朔州", "忻州", "大同", "吕梁");
    private static List<String> shandong = Arrays.asList("济南", "青岛", "淄博", "威海", "曲阜", "临沂", "烟台", "枣庄", "聊城", "济宁", "菏泽", "泰安", "日照", "东营", "德州", "滨州", "莱芜", "潍坊");
    private static List<String> sichuan = Arrays.asList("成都", "泸州", "内江", "凉山", "阿坝", "巴中", "广元", "乐山", "绵阳", "德阳", "攀枝花", "雅安", "宜宾", "自贡", "甘孜州", "达州", "资阳", "广安", "遂宁", "眉山", "南充");
    private static List<String> shanxi2 = Arrays.asList("西安", "韩城", "安康", "汉中", "宝鸡", "咸阳", "榆林", "渭南", "商洛", "铜川", "延安");
    private static List<String> taiwan = Arrays.asList("台北", "台南", "高雄", "台中", "新竹", "嘉义");
    private static List<String> xizang = Arrays.asList("拉萨", "阿里", "昌都", "那曲", "日喀则", "山南", "林芝");
    private static List<String> xinjiang = Arrays.asList("乌鲁木齐", "阿勒泰", "阿克苏", "昌吉", "哈密", "和田", "喀什", "克拉玛依", "石河子", "塔城", "库尔勒", "吐鲁番", "伊宁");
    private static List<String> yunnan = Arrays.asList("昆明", "保山", "楚雄", "德宏", "红河", "临沧", "怒江", "曲靖", "思茅", "文山", "玉溪", "邵通", "丽江", "大理");
    private static List<String> zhejiang = Arrays.asList("杭州", "湖州", "金华", "宁波", "丽水", "绍兴", "衢州", "嘉兴", "台州", "舟山", "温州");

    static {
        prvince.put("安徽", "皖");
        prvince.put("福建", "闽");
        prvince.put("广东", "粤");
        prvince.put("贵州", "黔");
        prvince.put("甘肃", "甘");
        prvince.put("广西", "桂");

        prvince.put("河北", "冀");
        prvince.put("黑龙江", "黑");
        prvince.put("河南", "豫");
        prvince.put("湖北", "鄂");
        prvince.put("湖南", "湘");
        prvince.put("海南", "琼");

        prvince.put("吉林", "吉");
        prvince.put("江苏", "苏");
        prvince.put("江西", "赣");

        prvince.put("辽宁", "辽");

        prvince.put("内蒙古", "内");
        prvince.put("宁夏", "宁");

        prvince.put("青海", "青");

        prvince.put("山西", "晋");
        prvince.put("山东", "鲁");
        prvince.put("四川", "川");
        prvince.put("陕西", "陕");

        prvince.put("台湾", "台");

        prvince.put("西藏", "藏");
        prvince.put("新疆", "新");

        prvince.put("云南", "滇");

        prvince.put("浙江", "浙");


        citys.put("安徽", anhui);
        citys.put("福建", fujian);
        citys.put("广东", guangdong);
        citys.put("贵州", guizhou);
        citys.put("甘肃", gansu);
        citys.put("广西", guangxi);

        citys.put("河北", hebei);
        citys.put("黑龙江", heilongjiang);
        citys.put("河南", henan);
        citys.put("湖北", hubei);
        citys.put("湖南", hunan);
        citys.put("海南", hainan);

        citys.put("吉林", jilin);
        citys.put("江苏", jiangsu);
        citys.put("江西", jiangxi);

        citys.put("辽宁", liaoning);

        citys.put("内蒙古", neimenggu);
        citys.put("宁夏", ningxia);

        citys.put("青海", qinghai);

        citys.put("山西", shanxi);
        citys.put("山东", shandong);
        citys.put("四川", sichuan);
        citys.put("陕西", shanxi2);

        citys.put("台湾", taiwan);

        citys.put("西藏", xizang);
        citys.put("新疆", xinjiang);

        citys.put("云南", yunnan);

        citys.put("浙江", zhejiang);
    }
}
