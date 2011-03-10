/*
* Copyright (C) 2003-2009 eXo Platform SAS.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.gatein.wci.authentication;

import org.gatein.wci.impl.DefaultServletContainer;
import org.gatein.wci.impl.DefaultServletContainerFactory;
import org.gatein.wci.security.Credentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GenericAuthentication
{
  public static final TicketService TICKET_SERVICE = new TicketService();

  public void login(Credentials credentials, HttpServletRequest request, HttpServletResponse response, long validityMillis) throws IOException
  {
     login(credentials, request, response, validityMillis, null);
  }

  public void login(Credentials credentials, HttpServletRequest request, HttpServletResponse response, long validityMillis, String initialURI) throws IOException
  {
     String ticket = TICKET_SERVICE.createTicket(new Credentials(credentials.getUsername(), credentials.getPassword()), validityMillis);

     request.getSession().removeAttribute(Credentials.CREDENTIALS);

     if (initialURI == null) {
        initialURI = request.getRequestURI();
     }
     String url = "j_security_check?j_username=" + credentials.getUsername() + "&j_password=" + ticket + "&initialURI=" + initialURI;
     url = response.encodeRedirectURL(url);
     response.sendRedirect(url);
     response.flushBuffer();
  }

  public void logout(HttpServletRequest request, HttpServletResponse response)
  {
     request.getSession().invalidate();
  }
}