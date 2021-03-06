package org.gatein.wci.test.servletlistener;

import junit.framework.Assert;
import org.gatein.wci.test.AbstractWCITestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public abstract class AbstractServletListenerTestCase extends AbstractWCITestCase
{
   @ArquillianResource
   URL requestDispatchURL;

   protected static String webXml;
   protected static String contextXml;

   @Deployment(name = "servletlistenersapp", order = 2)
   public static WebArchive deployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "servletlistenerapp.war");
      war.addClass(ServletEventCountListener.class);
      war.setWebXML(webXml);
      if(contextXml != null) {
        war.addAsManifestResource(contextXml, "context.xml");
      }
      return war;
   }

   @Test
   @InSequence(0)
   @RunAsClient
   @OperateOnDeployment("servletlistenerwci")
   public void testListener() throws Exception
   {
      HttpURLConnection conn = (HttpURLConnection) requestDispatchURL.openConnection();
      conn.connect();
      Assert.assertEquals(200, conn.getResponseCode());
   }

   @Test
   @InSequence(1)
   @OperateOnDeployment("servletlistenerwci")
   public void testListenerCount() throws Exception
   {
      Assert.assertEquals(1, ServletEventCountListener.initializedRequests);
      Assert.assertEquals(1, ServletEventCountListener.destroyedRequests);
   }
}
