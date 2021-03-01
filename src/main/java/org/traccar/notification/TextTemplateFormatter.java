/*
 * Copyright 2021 Anton Tananaev (anton@traccar.org)
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
package org.traccar.notification;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.Context;
import org.traccar.model.User;
import org.traccar.reports.ReportUtils;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Locale;

public final class TextTemplateFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextTemplateFormatter.class);

    private TextTemplateFormatter() {
    }

    public static VelocityContext prepareContext(User user) {

        VelocityContext velocityContext = new VelocityContext();

        if (user != null) {
            velocityContext.put("user", user);
            velocityContext.put("timezone", ReportUtils.getTimezone(user.getId()));
        }

        velocityContext.put("webUrl", Context.getVelocityEngine().getProperty("web.url"));
        velocityContext.put("dateTool", new DateTool());
        velocityContext.put("numberTool", new NumberTool());
        velocityContext.put("locale", Locale.getDefault());

        return velocityContext;
    }

    public static Template getTemplate(String name, String path) {

        String templateFilePath;
        Template template;

        try {
            templateFilePath = Paths.get(path, name + ".vm").toString();
            template = Context.getVelocityEngine().getTemplate(templateFilePath, StandardCharsets.UTF_8.name());
        } catch (ResourceNotFoundException error) {
            LOGGER.warn("Notification template error", error);
            templateFilePath = Paths.get(path, "unknown.vm").toString();
            template = Context.getVelocityEngine().getTemplate(templateFilePath, StandardCharsets.UTF_8.name());
        }
        return template;
    }

    public static FullMessage formatFullMessage(VelocityContext velocityContext, String name) {
        String formattedMessage = formatMessage(velocityContext, name, "full");
        return new FullMessage((String) velocityContext.get("subject"), formattedMessage);
    }

    public static String formatShortMessage(VelocityContext velocityContext, String name) {
        return formatMessage(velocityContext, name, "short");
    }

    private static String formatMessage(
            VelocityContext velocityContext, String name, String templatePath) {

        StringWriter writer = new StringWriter();
        getTemplate(name, templatePath).merge(velocityContext, writer);
        return writer.toString();
    }

}
