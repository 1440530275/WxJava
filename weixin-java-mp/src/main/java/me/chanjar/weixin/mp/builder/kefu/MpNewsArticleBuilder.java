package me.chanjar.weixin.mp.builder.kefu;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

/**
 * 微信新图文消息，取消media_id， 取而代之的是article_id
 * <p>
 *   <url>https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html<url/>
 * </p>
 * @author weihaolong
 */
public final class MpNewsArticleBuilder extends BaseBuilder<MpNewsArticleBuilder>{

  private String articleId;

  public MpNewsArticleBuilder() {
    this.msgType = WxConsts.KefuMsgType.MPNEWSARTICLE;
  }

  public MpNewsArticleBuilder articleId(String articleId) {
    this.articleId = articleId;
    return this;
  }

  @Override
  public WxMpKefuMessage build() {
    WxMpKefuMessage m = super.build();
    m.setMpNewsArticleId(this.articleId);
    return m;
  }
}
