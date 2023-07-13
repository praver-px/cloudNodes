package com.praver.springboot.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户日志表
 * </p>
 *
 * @author praver
 * @since 2023-06-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("z_user_log")
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("时间")
    private Date time;

    @ApiModelProperty("事件")
    private String event;

    @Column("u_id")
    private Integer userId;


}
