/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.api.resource;

import org.traccar.Context;
import org.traccar.api.BaseResource;
import org.traccar.helper.DataConverter;
import org.traccar.helper.ServletHelper;
import org.traccar.helper.LogAction;
import org.traccar.model.User;

import javax.annotation.security.PermitAll;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@Path("session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class SessionResource extends BaseResource {

    public static final String USER_ID_KEY = "userId";
    public static final String USER_COOKIE_KEY = "user";
    public static final String PASS_COOKIE_KEY = "password";

    @javax.ws.rs.core.Context
    private HttpServletRequest request;

    @PermitAll
    @GET
    public User get(@QueryParam("token") String token) throws SQLException, UnsupportedEncodingException {
        Long userId = (Long) request.getSession().getAttribute(USER_ID_KEY);
        if (userId == null) {
            Cookie[] cookies = request.getCookies();
            String email = null, password = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(USER_COOKIE_KEY)) {
                        byte[] emailBytes = DataConverter.parseBase64(
                                URLDecoder.decode(cookie.getValue(), StandardCharsets.US_ASCII.name()));
                        email = new String(emailBytes, StandardCharsets.UTF_8);
                    } else if (cookie.getName().equals(PASS_COOKIE_KEY)) {
                        byte[] passwordBytes = DataConverter.parseBase64(
                                URLDecoder.decode(cookie.getValue(), StandardCharsets.US_ASCII.name()));
                        password = new String(passwordBytes, StandardCharsets.UTF_8);
                    }
                }
            }
            if (email != null && password != null) {
                User user = Context.getPermissionsManager().login(email, password);
                if (user != null) {
                    userId = user.getId();
                    request.getSession().setAttribute(USER_ID_KEY, userId);
                }
            } else if (token != null) {
                User user = Context.getUsersManager().getUserByToken(token);
                if (user != null) {
                    userId = user.getId();
                    request.getSession().setAttribute(USER_ID_KEY, userId);
                }
            }
        }

        if (userId != null) {
            Context.getPermissionsManager().checkUserEnabled(userId);
            return Context.getPermissionsManager().getUser(userId);
        } else {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
        }
    }

    @PermitAll
    @POST
    public User add(
            @FormParam("email") String email, @FormParam("password") String password,
            @FormParam("code") String code) throws SQLException {
        User user = Context.getPermissionsManager().login(email, password);
        if (user != null) {
            if (!user.isTotpAuthCodeValid(code)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            request.getSession().setAttribute(USER_ID_KEY, user.getId());
            LogAction.login(user.getId());
            return user;
        } else {
            LogAction.failedLogin(ServletHelper.retrieveRemoteAddress(request));
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    @DELETE
    public Response remove() {
        LogAction.logout(getUserId());
        request.getSession().removeAttribute(USER_ID_KEY);
        return Response.noContent().build();
    }

}
