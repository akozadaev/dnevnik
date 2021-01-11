package ru.tsu.dnevnik.webgui.servlets;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;


@WebServlet(value = ServletSettings.PATH_VAADIN, asyncSupported = true)
@VaadinServletConfiguration(
    productionMode = ServletSettings.PRODUCTION_MODE,
    ui = UI.class,
    widgetset = ServletSettings.WIDGETSET)
public class VaadinResourcesServlet
    extends VaadinServlet {

    public static class UIClass extends UI {

        @Override
        protected void init(VaadinRequest vaadinRequest) {
        }
    }
}

