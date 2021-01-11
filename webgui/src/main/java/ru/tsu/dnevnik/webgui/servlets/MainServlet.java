package ru.tsu.dnevnik.webgui.servlets;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import ru.tsu.dnevnik.webgui.ui.MainUI;
import ru.tsu.dnevnik.webgui.ui.StartUI;

import javax.servlet.annotation.WebServlet;

@WebServlet(value = ServletSettings.PATH_MAIN, asyncSupported = true)
@VaadinServletConfiguration(
    productionMode = ru.tsu.dnevnik.webgui.servlets.ServletSettings.PRODUCTION_MODE,
    ui = StartUI.class,
    widgetset = ru.tsu.dnevnik.webgui.servlets.ServletSettings.WIDGETSET)
public class MainServlet extends VaadinServlet {
}
