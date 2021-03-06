package me.chanjar.weixin.mp.api;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.thoughtworks.xstream.XStream;

import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

public class ApiTestModule implements Module {

  @Override
  public void configure(Binder binder) {
    InputStream is1 = ClassLoader.getSystemResourceAsStream("test-config.xml");
    try {
      WxXmlMpInMemoryConfigStorage config = this
          .fromXml(WxXmlMpInMemoryConfigStorage.class, is1);
      WxMpServiceImpl wxService = new WxMpServiceImpl();
      wxService.setWxMpConfigStorage(config);

      binder.bind(WxMpServiceImpl.class).toInstance(wxService);
      binder.bind(WxMpConfigStorage.class).toInstance(config);
    } finally {
      IOUtils.closeQuietly(is1);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T fromXml(Class<T> clazz, InputStream is) {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.alias("xml", clazz);
    xstream.processAnnotations(clazz);
    return (T) xstream.fromXML(is);
  }

}
