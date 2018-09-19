package by.epam.shop.service;

import java.util.ResourceBundle;

public class ConfigManager {

    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("by.epam.shop.service.config");

    public final static String DB_USER = "jdbc.user";
    public final static String DB_PASSWORD = "jdbc.password";
    public final static String DB_DRIVER = "jdbc.driver";
    public final static String DB_URL = "jdbc.url";
    public final static String DB_MAX_NUMBER_OF_CONNECTION = "jdbc.max_con";
    public final static String SQL_LOGIN = "sql.login";
    public final static String SQL_SELECT_ALL_USER = "sql.sel_all_user";
    public final static String SQL_SELECT_ALL_PR = "sql.sel_all_pr";
    public final static String SQL_SELECT_ACT_PR = "sql.sel_act_pr";
    public final static String SQL_SELECT__PR_BY_ID = "sql.sel_pr_by_id";
    public final static String SQL_SELECT_ORDERS_BY_ID = "sql.sel_orders_by_userid";
    public final static String SQL_SELECT_ORDERS = "sql.sel_orders";
    public final static String SQL_SELECT_ORDER_LIST_BY_ORDER_ID = "sql.sel_order_list_by_orderid";
    public final static String SQL_INSERT_USER = "sql.ins_user";
    public final static String SQL_INSERT_PRODUCT = "sql.ins_product";
    public final static String SQL_INSERT_ORDER = "sql.ins_order";
    public final static String SQL_INSERT_ORDER_INFO = "sql.ins_order_info";
    public final static String SQL_UPDATE_AMOUNT_PRODUCT = "sql.upd_product_amount";
    public final static String SQL_UPDATE_PRODUCT = "sql.upd_product";
    public final static String SQL_UPDATE_USER = "sql.upd_user";
    public final static String SQL_UPDATE_STATUS_USER = "sql.upd_user_status";
    public final static String SQL_UPDATE_STATUS_ORDER = "sql.upd_status_order";
    public final static String SQL_CANCEL_ORDER = "sql.cancel_order";
    public final static String SQL_PAY_ORDER = "sql.pay_order";
    public final static String SQL_DEL_PRODUCT_BY_ID = "sql.del_product_by_id";
    public final static String SQL_PRODUCT_CHANGE_STATUS = "sql.upd_product_status";

    private ConfigManager() {
    }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }

}
