package ru.tsu.dnevnik.webgui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.webgui.utils.Auth;
import ru.tsu.dnevnik.webgui.utils.LookupUtil;

import javax.sql.DataSource;

/**
 * Created by Alexey on 15.01.2015.
 */
@Theme("runo")
@Title("Дневник")
public class StartUI extends UI {
    private static DataSource dataSource;
    public DbUsers dbUsers;

    public StartUI() {
        try (LookupUtil lookup = new LookupUtil()) {
            dataSource = lookup.lookupJNDI("jdbc/dnevnikDs", DataSource.class);
            dbUsers = lookup.lookupBean("DbUsersBean", LookupUtil.NAME_MODULE_DB_1_0,
                    DbUsers.class);

        }
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setNavigator(new Navigator(this, this));
        Navigator navigator = getNavigator();

        navigator.addView(Views.LoginUI, new LoginUI(navigator, dbUsers));
        navigator.addView(Views.StudentUI, new StudentUI(navigator));
        navigator.addView(Views.AdministratorUI, new AdministratorUI(navigator));
        navigator.addView(Views.RegistrationUI, new RegistrationUI());

        String login = Auth.getLogin(getSession());
        if (login == null) {

            navigator.navigateTo(Views.LoginUI);
        } else
            navigator.navigateTo(Views.LoginUI);
    }

}