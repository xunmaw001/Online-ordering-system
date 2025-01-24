package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 菜品订单
 *
 * @author 
 * @email
 */
@TableName("caipin_order")
public class CaipinOrderEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public CaipinOrderEntity() {

	}

	public CaipinOrderEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 订单编号
     */
    @ColumnInfo(comment="订单编号",type="varchar(200)")
    @TableField(value = "caipin_order_uuid_number")

    private String caipinOrderUuidNumber;


    /**
     * 收货地址
     */
    @ColumnInfo(comment="收货地址",type="int(11)")
    @TableField(value = "address_id")

    private Integer addressId;


    /**
     * 菜品
     */
    @ColumnInfo(comment="菜品",type="int(11)")
    @TableField(value = "caipin_id")

    private Integer caipinId;


    /**
     * 用户
     */
    @ColumnInfo(comment="用户",type="int(11)")
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 购买数量
     */
    @ColumnInfo(comment="购买数量",type="int(11)")
    @TableField(value = "buy_number")

    private Integer buyNumber;


    /**
     * 预约时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="预约时间",type="timestamp")
    @TableField(value = "caipin_order_time")

    private Date caipinOrderTime;


    /**
     * 实付价格
     */
    @ColumnInfo(comment="实付价格",type="decimal(10,2)")
    @TableField(value = "caipin_order_true_price")

    private Double caipinOrderTruePrice;


    /**
     * 派送人
     */
    @ColumnInfo(comment="派送人",type="varchar(200)")
    @TableField(value = "caipin_order_courier_name")

    private String caipinOrderCourierName;


    /**
     * 联系方式
     */
    @ColumnInfo(comment="联系方式",type="varchar(200)")
    @TableField(value = "caipin_order_courier_number")

    private String caipinOrderCourierNumber;


    /**
     * 订单类型
     */
    @ColumnInfo(comment="订单类型",type="int(11)")
    @TableField(value = "caipin_order_types")

    private Integer caipinOrderTypes;


    /**
     * 支付类型
     */
    @ColumnInfo(comment="支付类型",type="int(11)")
    @TableField(value = "caipin_order_payment_types")

    private Integer caipinOrderPaymentTypes;


    /**
     * 订单创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="订单创建时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    /**
     * 创建时间  listShow
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：订单编号
	 */
    public String getCaipinOrderUuidNumber() {
        return caipinOrderUuidNumber;
    }
    /**
	 * 设置：订单编号
	 */

    public void setCaipinOrderUuidNumber(String caipinOrderUuidNumber) {
        this.caipinOrderUuidNumber = caipinOrderUuidNumber;
    }
    /**
	 * 获取：收货地址
	 */
    public Integer getAddressId() {
        return addressId;
    }
    /**
	 * 设置：收货地址
	 */

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    /**
	 * 获取：菜品
	 */
    public Integer getCaipinId() {
        return caipinId;
    }
    /**
	 * 设置：菜品
	 */

    public void setCaipinId(Integer caipinId) {
        this.caipinId = caipinId;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }
    /**
	 * 设置：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：购买数量
	 */
    public Integer getBuyNumber() {
        return buyNumber;
    }
    /**
	 * 设置：购买数量
	 */

    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }
    /**
	 * 获取：预约时间
	 */
    public Date getCaipinOrderTime() {
        return caipinOrderTime;
    }
    /**
	 * 设置：预约时间
	 */

    public void setCaipinOrderTime(Date caipinOrderTime) {
        this.caipinOrderTime = caipinOrderTime;
    }
    /**
	 * 获取：实付价格
	 */
    public Double getCaipinOrderTruePrice() {
        return caipinOrderTruePrice;
    }
    /**
	 * 设置：实付价格
	 */

    public void setCaipinOrderTruePrice(Double caipinOrderTruePrice) {
        this.caipinOrderTruePrice = caipinOrderTruePrice;
    }
    /**
	 * 获取：派送人
	 */
    public String getCaipinOrderCourierName() {
        return caipinOrderCourierName;
    }
    /**
	 * 设置：派送人
	 */

    public void setCaipinOrderCourierName(String caipinOrderCourierName) {
        this.caipinOrderCourierName = caipinOrderCourierName;
    }
    /**
	 * 获取：联系方式
	 */
    public String getCaipinOrderCourierNumber() {
        return caipinOrderCourierNumber;
    }
    /**
	 * 设置：联系方式
	 */

    public void setCaipinOrderCourierNumber(String caipinOrderCourierNumber) {
        this.caipinOrderCourierNumber = caipinOrderCourierNumber;
    }
    /**
	 * 获取：订单类型
	 */
    public Integer getCaipinOrderTypes() {
        return caipinOrderTypes;
    }
    /**
	 * 设置：订单类型
	 */

    public void setCaipinOrderTypes(Integer caipinOrderTypes) {
        this.caipinOrderTypes = caipinOrderTypes;
    }
    /**
	 * 获取：支付类型
	 */
    public Integer getCaipinOrderPaymentTypes() {
        return caipinOrderPaymentTypes;
    }
    /**
	 * 设置：支付类型
	 */

    public void setCaipinOrderPaymentTypes(Integer caipinOrderPaymentTypes) {
        this.caipinOrderPaymentTypes = caipinOrderPaymentTypes;
    }
    /**
	 * 获取：订单创建时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }
    /**
	 * 设置：订单创建时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间  listShow
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间  listShow
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CaipinOrder{" +
            ", id=" + id +
            ", caipinOrderUuidNumber=" + caipinOrderUuidNumber +
            ", addressId=" + addressId +
            ", caipinId=" + caipinId +
            ", yonghuId=" + yonghuId +
            ", buyNumber=" + buyNumber +
            ", caipinOrderTime=" + DateUtil.convertString(caipinOrderTime,"yyyy-MM-dd") +
            ", caipinOrderTruePrice=" + caipinOrderTruePrice +
            ", caipinOrderCourierName=" + caipinOrderCourierName +
            ", caipinOrderCourierNumber=" + caipinOrderCourierNumber +
            ", caipinOrderTypes=" + caipinOrderTypes +
            ", caipinOrderPaymentTypes=" + caipinOrderPaymentTypes +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
