package ru.tsu.dnevnik.webgui.components;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import ru.tsu.dnevnik.webgui.ui.Views;
import ru.tsu.dnevnik.*;


/**
 * Created by Alexey on 15.01.2015.
 */
public class UserInfo extends HorizontalLayout {

    private  Label labelLogin;

    public UserInfo(final Navigator navigator) {
        setSizeUndefined();
        Label labelLoginAs = new Label("Вы вошли как: ");
        labelLoginAs.addStyleName("right-margin-4px");
        addComponent(labelLoginAs);
        setComponentAlignment(labelLoginAs, Alignment.MIDDLE_LEFT);
        labelLogin = new Label();
        labelLogin.addStyleName("right-margin-4px");
        addComponent(labelLogin);
        setComponentAlignment(labelLogin, Alignment.MIDDLE_LEFT);

        Button buttonLogout = new Button("Выйти", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                Auth.logout(getSession());
                navigator.navigateTo(Views.MainUI);
            }
        });
        addComponent(buttonLogout);
        setComponentAlignment(buttonLogout, Alignment.MIDDLE_LEFT);
    }

    public void setUser(ru.tsu.dnevnik.db.models.UserUI user) {
        labelLogin.setValue(user.last_name + " " + user.first_name + " " + user.middle_name);
    }
    public void setUser(String user) {
        labelLogin.setValue(user);
    }

}
