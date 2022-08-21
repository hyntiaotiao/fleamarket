package club.devhub.fleamarket.constant;

/**
 * 用户的性别
 */
public enum UserStatusEnum {

    /**
     * 未封禁
     */
    NOT_BAN(1),
    /**
     * 封禁
     */
    BANNED(0);
    private final int code;

    UserStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
