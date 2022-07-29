package club.devhub.fleamarket.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果返回模板
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 一共有多少条符合要求
     */
    private Long total;

    /**
     * 上一页的页码（如果当前是第一页，则prev返回-1，表示没有上一页）
     */
    private Integer prev;

    /**
     * 下一页的页码（如果当前是最后一页，则next返回-1，表示没有下一页）
     */
    private Integer next;

    /**
     * 数据列表
     */
    private List<T> list;

}