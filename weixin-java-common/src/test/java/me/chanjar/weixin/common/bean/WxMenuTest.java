package me.chanjar.weixin.common.bean;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;
import org.testng.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Test
public class WxMenuTest {

  @Test(dataProvider = "wxReturnMenu")
  public void testFromJson(String json) {
    WxMenu menu = WxMenu.fromJson(json);
    Assert.assertEquals(menu.getButtons().size(), 3);
  }

  @Test(dataProvider = "wxPushMenu")
  public void testToJson(String json) {
    WxMenu menu = new WxMenu();
    WxMenuButton button1 = new WxMenuButton();
    button1.setType("click");
    button1.setName("今日歌曲");
    button1.setKey("V1001_TODAY_MUSIC");

    WxMenuButton button2 = new WxMenuButton();
    button2.setType("click");
    button2.setName("歌手简介");
    button2.setKey("V1001_TODAY_SINGER");

    WxMenuButton button3 = new WxMenuButton();
    button3.setName("菜单");

    menu.getButtons().add(button1);
    menu.getButtons().add(button2);
    menu.getButtons().add(button3);

    WxMenuButton button31 = new WxMenuButton();
    button31.setType("view");
    button31.setName("搜索");
    button31.setUrl("http://www.soso.com/");

    WxMenuButton button32 = new WxMenuButton();
    button32.setType("view");
    button32.setName("视频");
    button32.setUrl("http://v.qq.com/");

    WxMenuButton button33 = new WxMenuButton();
    button33.setType("click");
    button33.setName("赞一下我们");
    button33.setKey("V1001_GOOD");

    button3.getSubButtons().add(button31);
    button3.getSubButtons().add(button32);
    button3.getSubButtons().add(button33);

    Assert.assertEquals(menu.toJson(), json);
  }

  @Test(dataProvider = "wxAddConditionalMenu")
  public void testAddConditionalToJson(String json) {
    WxMenu menu = new WxMenu();
    WxMenuButton button1 = new WxMenuButton();
    button1.setType("click");
    button1.setName("今日歌曲");
    button1.setKey("V1001_TODAY_MUSIC");

    menu.getButtons().add(button1);

    WxMenuRule wxMenuRule = new WxMenuRule();
    wxMenuRule.setTagId("2");
    wxMenuRule.setSex("1");
    wxMenuRule.setCountry("中国");
    wxMenuRule.setProvince("广东");
    wxMenuRule.setCity("广州");
    wxMenuRule.setClientPlatformType("2");
    wxMenuRule.setLanguage("zh_CN");
    menu.setMatchRule(wxMenuRule);

    Assert.assertEquals(menu.toJson(), json);
  }

  @Test(dataProvider = "wxOtherButtonType")
  public void testOtherButtonTypeToJson(String json){
    WxMenu menu = new WxMenu();
    WxMenuButton button1 = new WxMenuButton();
    button1.setName("扫码");
    WxMenuButton subButton1 = new WxMenuButton();
    subButton1.setType("scancode_waitmsg");
    subButton1.setName("扫码带提示");
    subButton1.setKey("rselfmenu_0_0");
    WxMenuButton subButton2 = new WxMenuButton();
    subButton2.setType("scancode_push");
    subButton2.setName("扫码推事件");
    subButton2.setKey("rselfmenu_0_1");
    List<WxMenuButton> subButtons1 = Arrays.asList(subButton1, subButton2);
    button1.setSubButtons(subButtons1);
    WxMenuButton button2 = new WxMenuButton();
    button2.setName("发图");
    WxMenuButton subButton3 = new WxMenuButton();
    subButton3.setType("pic_sysphoto");
    subButton3.setName("系统拍照发图");
    subButton3.setKey("rselfmenu_1_0");
    WxMenuButton subButton4 = new WxMenuButton();
    subButton4.setType("pic_photo_or_album");
    subButton4.setName("拍照或者相册发图");
    subButton4.setKey("rselfmenu_1_1");
    WxMenuButton subButton5 = new WxMenuButton();
    subButton5.setType("pic_weixin");
    subButton5.setName("微信相册发图");
    subButton5.setKey("rselfmenu_1_2");
    List<WxMenuButton> subButtons2 = Arrays.asList(subButton3, subButton4, subButton5);
    button2.setSubButtons(subButtons2);
    WxMenuButton button3 = new WxMenuButton();
    button3.setName("发送位置");
    button3.setType("location_select");
    button3.setKey("rselfmenu_2_0");
    WxMenuButton button4 = new WxMenuButton();
    button4.setType("media_id");
    button4.setName("图片");
    button4.setMediaId("MEDIA_ID1");
    WxMenuButton button5 = new WxMenuButton();
    button5.setType("view_limited");
    button5.setName("图文消息");
    button5.setMediaId("MEDIA_ID2");
    WxMenuButton button6 = new WxMenuButton();
    button6.setType("article_id");
    button6.setName("发布后的图文消息");
    button6.setArticleId("ARTICLE_ID1");
    WxMenuButton button7 = new WxMenuButton();
    button7.setType("article_view_limited");
    button7.setName("发布后的图文消息");
    button7.setArticleId("ARTICLE_ID2");
    List<WxMenuButton> buttons = Arrays.asList(button1, button2, button3, button4, button5, button6, button7);
    menu.setButtons(buttons);

    Assert.assertEquals(menu.toJson(), json);
  }

  @DataProvider
  public Object[][] wxReturnMenu() {
    Object[][] res = menuJson();
    String json = "{ \"menu\" : " + res[0][0] + " }";
    return new Object[][]{
      new Object[]{json}
    };
  }

  @DataProvider(name = "wxPushMenu")
  public Object[][] menuJson() {
    String json =
      "{"
        + "\"button\":["
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"今日歌曲\","
        + "\"key\":\"V1001_TODAY_MUSIC\""
        + "},"
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"歌手简介\","
        + "\"key\":\"V1001_TODAY_SINGER\""
        + "},"
        + "{"
        + "\"name\":\"菜单\","
        + "\"sub_button\":["
        + "{"
        + "\"type\":\"view\","
        + "\"name\":\"搜索\","
        + "\"url\":\"http://www.soso.com/\""
        + "},"
        + "{"
        + "\"type\":\"view\","
        + "\"name\":\"视频\","
        + "\"url\":\"http://v.qq.com/\""
        + "},"
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"赞一下我们\","
        + "\"key\":\"V1001_GOOD\""
        + "}"
        + "]"
        + "}"
        + "]"
        + "}";
    return new Object[][]{
      new Object[]{json}
    };
  }

  @DataProvider(name = "wxAddConditionalMenu")
  public Object[][] addConditionalMenuJson() {
    String json =
      "{"
        + "\"button\":["
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"今日歌曲\","
        + "\"key\":\"V1001_TODAY_MUSIC\""
        + "}"
        + "],"
        + "\"matchrule\":{"
        + "\"group_id\":\"2\","
        + "\"sex\":\"1\","
        + "\"country\":\"中国\","
        + "\"province\":\"广东\","
        + "\"city\":\"广州\","
        + "\"client_platform_type\":\"2\","
        + "\"language\":\"zh_CN\""
        + "}"
        + "}";
    return new Object[][]{
      new Object[]{json}
    };
  }

  @DataProvider(name = "wxOtherButtonType")
  public Object[][] addOtherButtonType(){
    String json = "{" +
      "\"button\":[" +
      "{" +
      "\"name\":\"扫码\"," +
      "\"sub_button\":[" +
      "{" +
      "\"type\":\"scancode_waitmsg\"," +
      "\"name\":\"扫码带提示\"," +
      "\"key\":\"rselfmenu_0_0\"" +
      "}," +
      "{" +
      "\"type\":\"scancode_push\"," +
      "\"name\":\"扫码推事件\"," +
      "\"key\":\"rselfmenu_0_1\"" +
      "}" +
      "]" +
      "}," +
      "{" +
      "\"name\":\"发图\"," +
      "\"sub_button\":[" +
      "{" +
      "\"type\":\"pic_sysphoto\"," +
      "\"name\":\"系统拍照发图\"," +
      "\"key\":\"rselfmenu_1_0\"" +
      "}," +
      "{"+
      "\"type\":\"pic_photo_or_album\"," +
      "\"name\":\"拍照或者相册发图\"," +
      "\"key\":\"rselfmenu_1_1\"" +
      "}," +
      "{" +
      "\"type\":\"pic_weixin\"," +
      "\"name\":\"微信相册发图\"," +
      "\"key\":\"rselfmenu_1_2\"" +
      "}" +
      "]" +
      "}," +
      "{"+
      "\"type\":\"location_select\"," +
      "\"name\":\"发送位置\"," +
      "\"key\":\"rselfmenu_2_0\"" +
      "}," +
      "{" +
      "\"type\":\"media_id\"," +
      "\"name\":\"图片\"," +
      "\"media_id\":\"MEDIA_ID1\"" +
      "}," +
      "{" +
      "\"type\":\"view_limited\"," +
      "\"name\":\"图文消息\"," +
      "\"media_id\":\"MEDIA_ID2\"" +
      "}," +
      "{" +
      "\"type\":\"article_id\"," +
      "\"name\":\"发布后的图文消息\"," +
      "\"article_id\":\"ARTICLE_ID1\"" +
      "}," +
      "{" +
      "\"type\":\"article_view_limited\"," +
      "\"name\":\"发布后的图文消息\"," +
      "\"article_id\":\"ARTICLE_ID2\"" +
      "}" +
      "]" +
      "}";
      return new Object[][]{
        new Object[]{json}
      };
  }

}
