package ru.tsu.dnevnik.webgui.ui;


import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import ru.tsu.dnevnik.db.interfaces.DbRoles;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.db.models.Roles;
import ru.tsu.dnevnik.webgui.utils.EmailAdressValidator;
import ru.tsu.dnevnik.webgui.utils.LookupUtil;

import javax.sql.DataSource;

//import UserUI;


public class RegistrationUI extends Panel
        implements View {
    private Navigator navigator;

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RegistrationUI.class);

    private TextField lastName = new TextField("Фамилия:");
    private TextField firstName = new TextField("Имя:");
    private TextField middleName = new TextField("Отчество:");
//    private TextField organizationNameName = new TextField("Название компании, которую Вы представляете:");
    private TextField login = new TextField("Логин (адрес электронной почты):");
    private PasswordField password = new PasswordField("Пароль (не менее 6 символов):");
    private Label labelError = new Label();
    private DataSource dataSource;

    private DbUsers dbUsers;
    private DbRoles dbRoles;
    final String[] roleValue = {""};


    protected RegistrationUI() {
        try (LookupUtil lookup = new LookupUtil()) {
            dataSource = lookup.lookupJNDI("jdbc/dnevnikDs", DataSource.class); //TODO Переделать join

            dbUsers = lookup.lookupBean("DbUsersBean", LookupUtil.NAME_MODULE_DB_1_0,
                    DbUsers.class);
        }

        setSizeFull();






        login.setWidth(300f, Unit.PIXELS);
        login.setRequired(true);
        login.setInvalidAllowed(false);

        lastName.setWidth(300f, Unit.PIXELS);
        lastName.setRequired(true);
        lastName.setInvalidAllowed(false);

        firstName.setWidth(300f, Unit.PIXELS);
        firstName.setRequired(true);
        firstName.setInvalidAllowed(false);

        middleName.setWidth(300f, Unit.PIXELS);
        middleName.setRequired(true);
        middleName.setInvalidAllowed(false);

        password.setWidth(300f, Unit.PIXELS);
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");


        labelError.setWidth(300f, Unit.PIXELS);
        labelError.addStyleName("login-error");
        labelError.setVisible(false);

/*
        organizationNameName.setWidth(300f, Unit.PIXELS);
        organizationNameName.setRequired(true);
        organizationNameName.setInvalidAllowed(false);
        organizationNameName.setVisible(false);
*/

        Button registrationButton = new Button("Зарегистрировать", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                registrationButtonClick();
            }
        });

        registrationButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        registrationButton.addStyleName(Reindeer.BUTTON_DEFAULT);

        Label labelTitle = new Label("Регистрация пользователя");
        labelTitle.addStyleName(Reindeer.LABEL_H1);

        VerticalLayout layoutFields = new VerticalLayout(labelTitle,
                lastName,
                firstName,
                middleName,
                login,
                password,
                //organizationNameName,
                labelError,
                registrationButton);

        layoutFields.setSpacing(true);
        layoutFields.setMargin(new MarginInfo(true, true, true, true));
        layoutFields.setSizeUndefined();
        layoutFields.addStyleName(Reindeer.LAYOUT_WHITE);
        layoutFields.addStyleName("login-box");
        layoutFields.setComponentAlignment(registrationButton, Alignment.MIDDLE_CENTER);


        VerticalLayout viewLayout = new VerticalLayout(layoutFields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(layoutFields, Alignment.MIDDLE_CENTER);

        setContent(viewLayout);
    }

    private void registrationButtonClick() {
        try {String strLogin = login.getValue().toLowerCase();
            if (!EmailAdressValidator.check(strLogin)) {
                setError("Ошибка при регистрации пользователя. В поле \"Логин\" должен быть Ваш email " );
                labelError.setVisible(true);
            }
               if (password.getValue().length() < 6) {
                setError("Ошибка при регистрации пользователя. Поле \"Пароль\"  не может содержать менее 6 символов " );
                labelError.setVisible(true);
            }

            else {
                registration(strLogin, lastName.getValue(), firstName.getValue(), middleName.getValue(), password.getValue(), Roles.STUDENT);
                labelError.setVisible(false);
            }

            // TODO переход на личную страницу
        } catch (Exception e) {
            getSession().setAttribute("addUser", "false");
            setError("Ошибка при регистрации пользователя:\n" + e.getMessage());
        }
    }

    private boolean registration(String login, String lastName, String firstName, String middleName, String password, int role) {
        try{// TODO исправить MAP, не дергать так часто базу!
//        int userIdByUserEmail = dbRoles.getUserIdByUserEmail(login);

//            if (userIdByUserEmail ) {}

            int i = dbUsers.createUser(login, lastName, firstName, middleName, password.hashCode(), role, 1);
            navigator.navigateTo(Views.LoginUI);
            return true;

    } catch (Exception ex){
            labelError.setVisible(true);
            getSession().setAttribute("addUser", "false");
            setError("Ошибка при регистрации пользователя:\n" + ex.getMessage());
        }
        return false;
    }
    private void setError(String error) {
        labelError.setValue(error);
        labelError.setVisible(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        navigator = viewChangeEvent.getNavigator();
        password.setValue("");

    }
}
