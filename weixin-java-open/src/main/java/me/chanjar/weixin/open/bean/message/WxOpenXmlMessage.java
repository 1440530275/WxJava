package me.chanjar.weixin.open.bean.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.util.WxOpenCryptUtil;
import me.chanjar.weixin.open.util.xml.XStreamTransformer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Data
@Slf4j
@XStreamAlias("xml")
public class WxOpenXmlMessage implements Serializable {
  private static final long serialVersionUID = -5641769554709507771L;

  @XStreamAlias("AppId")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String appId;

  @XStreamAlias("CreateTime")
  private Long createTime;

  @XStreamAlias("InfoType")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String infoType;

  @XStreamAlias("ComponentVerifyTicket")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String componentVerifyTicket;

  @XStreamAlias("AuthorizerAppid")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String authorizerAppid;

  @XStreamAlias("AuthorizationCode")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String authorizationCode;

  @XStreamAlias("AuthorizationCodeExpiredTime")
  private Long authorizationCodeExpiredTime;

  @XStreamAlias("PreAuthCode")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String preAuthCode;

  // 以下为快速创建小程序接口推送的的信息

  @XStreamAlias("appid")
  private String registAppId;

  @XStreamAlias("status")
  private int status;

  @XStreamAlias("auth_code")
  private String authCode;

  @XStreamAlias("msg")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String msg;

  @XStreamAlias("info")
  private Info info = new Info();

  @XStreamAlias("info")
  @Data
  public static class Info implements Serializable {
    private static final long serialVersionUID = 7706235740094081194L;

    @XStreamAlias("name")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String name;

    @XStreamAlias("code")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String code;

    @XStreamAlias("code_type")
    private int codeType;

    @XStreamAlias("legal_persona_wechat")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String legalPersonaWechat;

    @XStreamAlias("legal_persona_name")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String legalPersonaName;

    @XStreamAlias("component_phone")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String componentPhone;

    // 创建个人小程序审核通知数据
    @XStreamAlias("wxuser")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String wxuser;

    @XStreamAlias("idname")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String idname;

    // 创建试用小程序成功/失败的通知数据
    @XStreamAlias("unique_id")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String uniqueId;

  }

  public static String wxMpOutXmlMessageToEncryptedXml(WxMpXmlOutMessage message, WxOpenConfigStorage wxOpenConfigStorage) {
    String plainXml = message.toXml();
    WxOpenCryptUtil pc = new WxOpenCryptUtil(wxOpenConfigStorage);
    return pc.encrypt(plainXml);
  }

  public static WxOpenXmlMessage fromXml(String xml) {
    //修改微信变态的消息内容格式，方便解析
    xml = xml.replace("</PicList><PicList>", "");
    return XStreamTransformer.fromXml(WxOpenXmlMessage.class, xml);
  }

  public static WxOpenXmlMessage fromXml(InputStream is) {
    return XStreamTransformer.fromXml(WxOpenXmlMessage.class, is);
  }

  /**
   * 从加密字符串转换
   *
   * @param encryptedXml        密文
   * @param wxOpenConfigStorage 配置存储器对象
   * @param timestamp           时间戳
   * @param nonce               随机串
   * @param msgSignature        签名串
   */
  public static WxOpenXmlMessage fromEncryptedXml(String encryptedXml, WxOpenConfigStorage wxOpenConfigStorage,
                                                  String timestamp, String nonce, String msgSignature) {
    WxOpenCryptUtil cryptUtil = new WxOpenCryptUtil(wxOpenConfigStorage);
    String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, encryptedXml);
    log.debug("解密后的原始xml消息内容：{}", plainText);
    return fromXml(plainText);
  }

  public static WxMpXmlMessage fromEncryptedMpXml(String encryptedXml, WxOpenConfigStorage wxOpenConfigStorage,
                                                  String timestamp, String nonce, String msgSignature) {
    WxOpenCryptUtil cryptUtil = new WxOpenCryptUtil(wxOpenConfigStorage);
    String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, encryptedXml);
    return WxMpXmlMessage.fromXml(plainText);
  }

  public static WxOpenXmlMessage fromEncryptedXml(InputStream is, WxOpenConfigStorage wxOpenConfigStorage,
                                                  String timestamp, String nonce, String msgSignature) {
    try {
      return fromEncryptedXml(IOUtils.toString(is, StandardCharsets.UTF_8),
        wxOpenConfigStorage, timestamp, nonce, msgSignature);
    } catch (IOException e) {
      throw new WxRuntimeException(e);
    }
  }
}
