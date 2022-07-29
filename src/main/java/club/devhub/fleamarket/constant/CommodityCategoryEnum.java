package club.devhub.fleamarket.constant;

/**
 * 留言类别
 */
public enum CommodityCategoryEnum {
    /**
     * 生活用品
     */
    LIFE(0),
    /**
     * 书本
     */
    BOOK(1),
    /**
     * 体育用品
     */
    PHYSICAL(2),
    /**
     * 学习用品
     */
    STUDY(3),
    /**
     * 电子设备
     */
    ELECTRON(4),
    /**
     * 其它
     */
    OTHER(5);
    private final int code;

    CommodityCategoryEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
