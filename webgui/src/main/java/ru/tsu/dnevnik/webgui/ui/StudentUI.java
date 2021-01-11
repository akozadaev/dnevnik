package ru.tsu.dnevnik.webgui.ui;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import ru.tsu.dnevnik.db.interfaces.DbRoles;
import ru.tsu.dnevnik.db.interfaces.DbUsers;
import ru.tsu.dnevnik.db.models.RoleUI;
import ru.tsu.dnevnik.db.models.UserUI;
import ru.tsu.dnevnik.webgui.components.UserInfo;
import ru.tsu.dnevnik.webgui.utils.Auth;
import ru.tsu.dnevnik.webgui.utils.EmailAdressValidator;
import ru.tsu.dnevnik.webgui.utils.LookupUtil;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey on 12.11.2016.
 */
public class StudentUI extends Panel
        implements View {
    UserInfo userInfo;
    private Navigator navigator;
    private String strLogin;
    UserUI userUI;
    private Label labelError = new Label();
    private final HorizontalLayout layoutHeader = new HorizontalLayout();
    final TabSheet tabsheet = new TabSheet();
    final PagedTable tableUsers = new PagedTable("Список зарегистрированных пользователей!");
    final Button buttonAddUser = new Button("Добавить пользователя");
    private DbUsers dbUsers;
    private DbRoles dbRoles;
    private DataSource dataSource;


    public StudentUI(final Navigator navigator) {
        try (LookupUtil lookup = new LookupUtil()) {
            dataSource = lookup.lookupJNDI("jdbc/dnevnikDs", DataSource.class); //TODO Переделать join

            dbUsers = lookup.lookupBean("DbUsersBean", LookupUtil.NAME_MODULE_DB_1_0,
                    DbUsers.class);
            dbRoles = lookup.lookupBean("DbRolesBean", LookupUtil.NAME_MODULE_DB_1_0,
                    DbRoles.class);
        }

//        VerticalLayout tabHelpUser = new VerticalLayout();

        buttonAddUser.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    UI CabinetUI = getUI();

                    // Create a sub-window and set the content
                    final Window subWindow = new Window("Добавление нового пользователя"); //TODO Можно не final?
                    VerticalLayout subContent = new VerticalLayout();
                    subContent.setMargin(true);
                    subWindow.setContent(subContent);

                    final TextField lastName = new TextField("Фамилия:");
                    final TextField firstName = new TextField("Имя:");
                    final TextField middleName = new TextField("Отчество:");
                    final TextField login = new TextField("Логин (адрес электронной почты):");
                    final PasswordField password = new PasswordField("Пароль (не менее 6 символов):");
                    final ComboBox roles = new ComboBox("Выберите роль/должность пользователя:");


                    final Map<String, Integer> map = new LinkedHashMap<String, Integer>();
                    List<RoleUI> allRoles = dbRoles.getAllRoles();
                    for (RoleUI allRole : allRoles) {
                        map.put(allRole.roles_rede, allRole.id);
                    }

                    roles.addItems(map.keySet());

                    Button buttonConfirm = new Button("Подтвердить и завершить");
                    buttonConfirm.addListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent clickEvent) {


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

                                    registration(strLogin, lastName.getValue(), firstName.getValue(), middleName.getValue(), password.getValue(),
                                            map.get(roles.getValue()), 3);// User ACTIVE = 3
                                    labelError.setVisible(false);
                                }

                                // TODO переход на личную страницу
                            } catch (Exception e) {
                                getSession().setAttribute("addUser", "false");
                                setError("Ошибка при регистрации пользователя:\n" + e.getMessage());
                            }
                            updateTable();
                            subWindow.close();

                        }

                    });


                    subContent.addComponent(lastName);
                    subContent.addComponent(firstName);
                    subContent.addComponent(middleName);
                    subContent.addComponent(login);
                    subContent.addComponent(password);
                    subContent.addComponent(roles);
                    subContent.addComponent(buttonConfirm);

                    subWindow.center();

                    CabinetUI.addWindow(subWindow);
                } catch (Exception ex) {
                    Notification.show("Ошибка!\nПроверьте правильность заполнения полей", Notification.Type.ERROR_MESSAGE);
                }

            }
        });

        tableUsers.setEditable(false);
        tableUsers.setColumnReorderingAllowed(false);
        tableUsers.setColumnCollapsingAllowed(true);
        tableUsers.setWidth(100.0f, Unit.PERCENTAGE);
        tableUsers.setSelectable(true);

        ///

        IndexedContainer containerMyTasks = new IndexedContainer();

        containerMyTasks.addContainerProperty("Имя пользователя", String.class, null);
        containerMyTasks.addContainerProperty("Роль/должность", String.class, null);
        containerMyTasks.addContainerProperty("Состояние", String.class, null);
        tableUsers.setContainerDataSource(containerMyTasks);

        List<UserUI> allUsers = dbUsers.getAllUsers();
        for (UserUI allUser : allUsers) {
            Item newItem1 = containerMyTasks.getItem(containerMyTasks.addItem());
            newItem1.getItemProperty("Имя пользователя").setValue(allUser.last_name + " " + allUser.first_name + " " + allUser.middle_name);
            newItem1.getItemProperty("Роль/должность").setValue(allUser.roleRede);
            newItem1.getItemProperty("Состояние").setValue(allUser.accountRede);
        }

        ///


        final VerticalLayout tabUsers = new VerticalLayout(
                buttonAddUser,
                tableUsers,
                tableUsers.createControls()

//                tableTasks,
//                tableTasks.createControls()
        );

        tabsheet.addTab(tabUsers, "Пользователи сервиса");

        VerticalLayout viewLayout = new VerticalLayout(layoutHeader, tabsheet);
        viewLayout.setSizeFull();
        setContent(viewLayout);
        layoutHeader.setSizeUndefined();
        layoutHeader.setWidth(100f, Unit.PERCENTAGE);

        userInfo = new UserInfo(navigator);
        layoutHeader.addComponent(userInfo);

    }

    private void setError(String error) {
        labelError.setValue(error);
        labelError.setVisible(true);
    }

    private boolean registration(String login, String lastName, String firstName, String middleName, String password, int role, int status) {
        try{// TODO исправить MAP, не дергать так часто базу!
//        int userIdByUserEmail = dbRoles.getUserIdByUserEmail(login);

//            if (userIdByUserEmail ) {}

            int i = dbUsers.createUser(login, lastName, firstName, middleName, password.hashCode(), role, status);

            /*Notification.show(" Зарегистрирован, номер " + i);
            navigator.navigateTo(Views.LoginUI);*/
            return true;

        } catch (Exception ex){
            labelError.setVisible(true);
            getSession().setAttribute("addUser", "false");
            setError("Ошибка при регистрации пользователя:\n" + ex.getMessage());
        }
        return false;
    }

    private void  updateTable(){
        IndexedContainer containerMyTasks = new IndexedContainer();

        containerMyTasks.addContainerProperty("Имя пользователя", String.class, null);
        containerMyTasks.addContainerProperty("Роль/должность", String.class, null);
        containerMyTasks.addContainerProperty("Состояние", String.class, null);
        tableUsers.setContainerDataSource(containerMyTasks);

        List<UserUI> allUsers = dbUsers.getAllUsers();
        for (UserUI allUser : allUsers) {
            Item newItem1 = containerMyTasks.getItem(containerMyTasks.addItem());
            newItem1.getItemProperty("Имя пользователя").setValue(allUser.last_name + " " + allUser.first_name + " " + allUser.middle_name);
            newItem1.getItemProperty("Роль/должность").setValue(allUser.roleRede);
            newItem1.getItemProperty("Состояние").setValue(allUser.accountRede);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        navigator = viewChangeEvent.getNavigator();
        strLogin = Auth.getLogin(getSession());
        if (strLogin == null) {
            navigator.navigateTo(Views.LoginUI);
            return;
        }

        StartUI mainUI = (StartUI) getUI();

        userUI = mainUI.dbUsers.getUserUI(strLogin);
        userInfo.setUser(userUI);

    }
}
