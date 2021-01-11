package ru.tsu.dnevnik.webgui.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import ru.tsu.dnevnik.db.interfaces.DbStudents;
import ru.tsu.dnevnik.webgui.utils.LookupUtil;

import javax.sql.DataSource;
import ru.tsu.dnevnik.db.interfaces.*;

@Theme("runo")
@Title("Dnevnik")
public class MainUI extends UI {
    private static DataSource dataSource;
    public DbStudents dbStudents;

    public MainUI() {
        try (LookupUtil lookup = new LookupUtil()) {
            dataSource = lookup.lookupJNDI("jdbc/dnevnikDs", DataSource.class);
            dbStudents = lookup.lookupBean("DbUsersBean", LookupUtil.NAME_MODULE_DB_1_0,
                    DbStudents.class);

        }
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setNavigator(new Navigator(this, this));
        Navigator navigator = getNavigator();

//        navigator.addView(Views.MainUI, new MainUI(navigator, dbStudents));
//        String login = Auth.getLogin(getSession());
//        if (login == null) {
//
//            navigator.navigateTo(Views.MainUI);
//        } else
//            navigator.navigateTo(Views.LoginUI);
    }

}