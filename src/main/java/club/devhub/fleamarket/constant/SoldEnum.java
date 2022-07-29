package club.devhub.fleamarket.constant;

/**
 * 是否卖出
 */
public enum SoldEnum {
    /**
     * 没有卖出*
     */
    NO(0),
    /**
     * 已经卖出*/
    YES(1);

    private final int state;


    SoldEnum(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
