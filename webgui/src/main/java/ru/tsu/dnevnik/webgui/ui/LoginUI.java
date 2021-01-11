package ru.tsu.dnevnik.webgui.ui;


import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.db.models.Roles;
import ru.tsu.dnevnik.db.models.UserUI;
import ru.tsu.dnevnik.webgui.utils.Auth;
import ru.tsu.dnevnik.db.interfaces.DbStudents;

import javax.sql.DataSource;
//import UserUI;


public class LoginUI extends Panel
        implements View {

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LoginUI.class);

    private TextField login = new TextField("Логин:");
    private PasswordField password = new PasswordField("Пароль:");
    private Label labelError = new Label();
    private Button buttonGoToRegistration = new Button("зарегистрироваться");
    private Label labelGoToRegistration = new Label("Впервые здесь? Тогда приглашаем ");

    private DataSource dataSource;
//    private DbUsers dbUsers;
    private Navigator navigator;
    DbUsers dbUsers;
    private int loginCount = 1;


    public LoginUI(final Navigator navigator, final DbUsers dbUsers) {

        setSizeFull();


        login.setWidth(300f, Unit.PIXELS);
        login.setRequired(true);
        login.setInvalidAllowed(false);

        password.setWidth(300f, Unit.PIXELS);
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");


        labelError.setWidth(300f, Unit.PIXELS);
        labelError.addStyleName("login-error");
        labelError.setVisible(false);

        this.dbUsers = dbUsers;

        // Make it look like a hyperlink
        buttonGoToRegistration.addStyleName(Reindeer.BUTTON_LINK);

// Handle clicks
        buttonGoToRegistration.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (navigator != null) {
                    navigator.navigateTo(Views.RegistrationUI);
                    return;
                }
            }


        });
//        labelError.setValue("<a href=\"" + res[0] + "/\" target=\"_blank\">" + res[1] + "</a>");

        Button loginButton = new Button("Войти", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                loginButtonClick();
            }
        });
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addStyleName(Reindeer.BUTTON_DEFAULT);

        Label labelTitle = new Label("Вход в систему");
        labelTitle.addStyleName(Reindeer.LABEL_H1);

        VerticalLayout layoutFields = new VerticalLayout(labelTitle, login, password, labelError, loginButton, labelGoToRegistration, buttonGoToRegistration);
        layoutFields.setSpacing(true);
        layoutFields.setMargin(new MarginInfo(true, true, true, true));
        layoutFields.setSizeUndefined();
        layoutFields.addStyleName(Reindeer.LAYOUT_BLUE);
        layoutFields.addStyleName("login-box");
        layoutFields.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);


        VerticalLayout viewLayout = new VerticalLayout(layoutFields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(layoutFields, Alignment.MIDDLE_CENTER);

        setContent(viewLayout);
    }

    private void loginButtonClick() {
        StartUI ui = (StartUI)getUI();
        UserUI user;

        WebBrowser b = navigator.getUI().getPage().getWebBrowser();
        String ip = b.getAddress();

        if (loginCount >= 3) {
            Notification.show("Вы трижды ошиблись при вводе логина и пароля!", Notification.Type.ERROR_MESSAGE);
            System.out.println("трижды ошибся при вооде пароля " + login.getValue().toLowerCase() + " " + ip + " " + " нужно банить ");
            getSession().close();
        }
        try{
            user = Auth.login(login.getValue().toLowerCase(), password.getValue().hashCode(), getSession(), ui.dbUsers);
//            dbUsers.setNewTryCount(user.id);
            System.out.println(user.toString() + "LOGIN USER INFO: IP = "+ ip + " LOCALE = " + b.getLocale());
        } catch (Exception ex) {
            System.out.println("login error " + ex);
            logger.error("login error", ex);
            setError("Ошибка получения данных пользователя:\n" + ex.getMessage());
            loginCount++;
            return;
        }

        if(user == null) {
            setError("Неверный логин или пароль");
            return;
        }

        if(navigator != null) {
//            dbUsers.addTryById(user.id); //TODO

            switch (user.role) {
                case Roles.ADMIN: navigator.navigateTo(Views.AdministratorUI); break; // admin
                case Roles.STUDENT: navigator.navigateTo(Views.StudentUI); break; // user
                case Roles.TEACHER: navigator.navigateTo(Views.TeacherUI); break; // user
            }

        }
        else
            logger.error("navigator is null");
    }
    @Override
        public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        navigator = viewChangeEvent.getNavigator();
        password.setValue("");

    }


    private void setError(String error) {
        labelError.setValue(error);
        labelError.setVisible(true);
    }
}
