
package com.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.config.AlipayConfig;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 菜品订单
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/caipinOrder")
public class CaipinOrderController {
    private static final Logger logger = LoggerFactory.getLogger(CaipinOrderController.class);

    private static final String TABLE_NAME = "caipinOrder";

    @Autowired
    private CaipinOrderService caipinOrderService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressService addressService;//收货地址
    @Autowired
    private CaipinService caipinService;//菜品
    @Autowired
    private CaipinCollectionService caipinCollectionService;//菜品收藏
    @Autowired
    private CaipinCommentbackService caipinCommentbackService;//菜品评价
    @Autowired
    private CartService cartService;//购物车
    @Autowired
    private ChatService chatService;//客服聊天
    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private GonggaoService gonggaoService;//公告
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private ZixunService zixunService;//美食资讯
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = caipinOrderService.queryPage(params);

        //字典表数据转换
        List<CaipinOrderView> list =(List<CaipinOrderView>)page.getList();
        for(CaipinOrderView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        CaipinOrderEntity caipinOrder = caipinOrderService.selectById(id);
        if(caipinOrder !=null){
            //entity转view
            CaipinOrderView view = new CaipinOrderView();
            BeanUtils.copyProperties( caipinOrder , view );//把实体数据重构到view中
            //级联表 收货地址
            //级联表
            AddressEntity address = addressService.selectById(caipinOrder.getAddressId());
            if(address != null){
            BeanUtils.copyProperties( address , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setAddressId(address.getId());
            }
            //级联表 菜品
            //级联表
            CaipinEntity caipin = caipinService.selectById(caipinOrder.getCaipinId());
            if(caipin != null){
            BeanUtils.copyProperties( caipin , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setCaipinId(caipin.getId());
            }
            //级联表 用户
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(caipinOrder.getYonghuId());
            if(yonghu != null){
            BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setYonghuId(yonghu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CaipinOrderEntity caipinOrder, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,caipinOrder:{}",this.getClass().getName(),caipinOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("用户".equals(role))
            caipinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        caipinOrder.setCreateTime(new Date());
        caipinOrder.setInsertTime(new Date());
        caipinOrderService.insert(caipinOrder);

        return R.ok();
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody CaipinOrderEntity caipinOrder, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,caipinOrder:{}",this.getClass().getName(),caipinOrder.toString());
        CaipinOrderEntity oldCaipinOrderEntity = caipinOrderService.selectById(caipinOrder.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("用户".equals(role))
//            caipinOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

            caipinOrderService.updateById(caipinOrder);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<CaipinOrderEntity> oldCaipinOrderList =caipinOrderService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        caipinOrderService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<CaipinOrderEntity> caipinOrderList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("../../upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            CaipinOrderEntity caipinOrderEntity = new CaipinOrderEntity();
//                            caipinOrderEntity.setCaipinOrderUuidNumber(data.get(0));                    //订单编号 要改的
//                            caipinOrderEntity.setAddressId(Integer.valueOf(data.get(0)));   //收货地址 要改的
//                            caipinOrderEntity.setCaipinId(Integer.valueOf(data.get(0)));   //菜品 要改的
//                            caipinOrderEntity.setYonghuId(Integer.valueOf(data.get(0)));   //用户 要改的
//                            caipinOrderEntity.setBuyNumber(Integer.valueOf(data.get(0)));   //购买数量 要改的
//                            caipinOrderEntity.setCaipinOrderTime(sdf.parse(data.get(0)));          //预约时间 要改的
//                            caipinOrderEntity.setCaipinOrderTruePrice(data.get(0));                    //实付价格 要改的
//                            caipinOrderEntity.setCaipinOrderCourierName(data.get(0));                    //派送人 要改的
//                            caipinOrderEntity.setCaipinOrderCourierNumber(data.get(0));                    //联系方式 要改的
//                            caipinOrderEntity.setCaipinOrderTypes(Integer.valueOf(data.get(0)));   //订单类型 要改的
//                            caipinOrderEntity.setCaipinOrderPaymentTypes(Integer.valueOf(data.get(0)));   //支付类型 要改的
//                            caipinOrderEntity.setInsertTime(date);//时间
//                            caipinOrderEntity.setCreateTime(date);//时间
                            caipinOrderList.add(caipinOrderEntity);


                            //把要查询是否重复的字段放入map中
                                //订单编号
                                if(seachFields.containsKey("caipinOrderUuidNumber")){
                                    List<String> caipinOrderUuidNumber = seachFields.get("caipinOrderUuidNumber");
                                    caipinOrderUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> caipinOrderUuidNumber = new ArrayList<>();
                                    caipinOrderUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("caipinOrderUuidNumber",caipinOrderUuidNumber);
                                }
                        }

                        //查询是否重复
                         //订单编号
                        List<CaipinOrderEntity> caipinOrderEntities_caipinOrderUuidNumber = caipinOrderService.selectList(new EntityWrapper<CaipinOrderEntity>().in("caipin_order_uuid_number", seachFields.get("caipinOrderUuidNumber")));
                        if(caipinOrderEntities_caipinOrderUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(CaipinOrderEntity s:caipinOrderEntities_caipinOrderUuidNumber){
                                repeatFields.add(s.getCaipinOrderUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [订单编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        caipinOrderService.insertBatch(caipinOrderList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = caipinOrderService.queryPage(params);

        //字典表数据转换
        List<CaipinOrderView> list =(List<CaipinOrderView>)page.getList();
        for(CaipinOrderView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        CaipinOrderEntity caipinOrder = caipinOrderService.selectById(id);
            if(caipinOrder !=null){


                //entity转view
                CaipinOrderView view = new CaipinOrderView();
                BeanUtils.copyProperties( caipinOrder , view );//把实体数据重构到view中

                //级联表
                    AddressEntity address = addressService.selectById(caipinOrder.getAddressId());
                if(address != null){
                    BeanUtils.copyProperties( address , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setAddressId(address.getId());
                }
                //级联表
                    CaipinEntity caipin = caipinService.selectById(caipinOrder.getCaipinId());
                if(caipin != null){
                    BeanUtils.copyProperties( caipin , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setCaipinId(caipin.getId());
                }
                //级联表
                    YonghuEntity yonghu = yonghuService.selectById(caipinOrder.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CaipinOrderEntity caipinOrder, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,caipinOrder:{}",this.getClass().getName(),caipinOrder.toString());
            CaipinEntity caipinEntity = caipinService.selectById(caipinOrder.getCaipinId());
            if(caipinEntity == null){
                return R.error(511,"查不到该菜品");
            }
            // Double caipinNewMoney = caipinEntity.getCaipinNewMoney();

            if(false){
            }
            else if(caipinEntity.getCaipinNewMoney() == null){
                return R.error(511,"现价/份不能为空");
            }
            else if((caipinEntity.getCaipinKucunNumber() -caipinOrder.getBuyNumber())<0){
                return R.error(511,"购买数量不能大于库存数量");
            }

            //计算所获得积分
            Double buyJifen =0.0;
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
                return R.error(511,"用户金额不能为空");
            double balance = yonghuEntity.getNewMoney() - caipinEntity.getCaipinNewMoney()*caipinOrder.getBuyNumber();//余额
            if(balance<0)
                return R.error(511,"余额不够支付");
            caipinOrder.setCaipinOrderTypes(101); //设置订单状态为已支付
            caipinOrder.setCaipinOrderTruePrice(caipinEntity.getCaipinNewMoney()*caipinOrder.getBuyNumber()); //设置实付价格
            caipinOrder.setYonghuId(userId); //设置订单支付人id
            caipinOrder.setCaipinOrderUuidNumber(String.valueOf(new Date().getTime()));
            caipinOrder.setCaipinOrderPaymentTypes(1);
            caipinOrder.setInsertTime(new Date());
            caipinOrder.setCreateTime(new Date());
                caipinEntity.setCaipinKucunNumber( caipinEntity.getCaipinKucunNumber() -caipinOrder.getBuyNumber());
                caipinService.updateById(caipinEntity);
                caipinOrderService.insert(caipinOrder);//新增订单
            //更新第一注册表
            yonghuEntity.setNewMoney(balance);//设置金额
            yonghuService.updateById(yonghuEntity);


            return R.ok();
    }
    /**
     * 添加订单
     */
    @RequestMapping("/order")
    public R add(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
        logger.debug("order方法:,,Controller:{},,params:{}",this.getClass().getName(),params.toString());
        String caipinOrderUuidNumber = String.valueOf(new Date().getTime());

        //获取当前登录用户的id
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer addressId = Integer.valueOf(String.valueOf(params.get("addressId")));

            Integer caipinOrderPaymentTypes = Integer.valueOf(String.valueOf(params.get("caipinOrderPaymentTypes")));//支付类型
        String caipinOrderTime = String.valueOf(params.get("caipinOrderTime"));

        String data = String.valueOf(params.get("caipins"));
        JSONArray jsonArray = JSON.parseArray(data);
        List<Map> caipins = JSON.parseObject(jsonArray.toString(), List.class);
        BigDecimal zongMoney = new BigDecimal(0.0);

        //获取当前登录用户的个人信息
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);

        //当前订单表
        List<CaipinOrderEntity> caipinOrderList = new ArrayList<>();
        //商品表
        List<CaipinEntity> caipinList = new ArrayList<>();
        //购物车ids
        List<Integer> cartIds = new ArrayList<>();

        BigDecimal zhekou = new BigDecimal(1.0);

        //循环取出需要的数据
        for (Map<String, Object> map : caipins) {
           //取值
            Integer caipinId = Integer.valueOf(String.valueOf(map.get("caipinId")));//商品id
            Integer buyNumber = Integer.valueOf(String.valueOf(map.get("buyNumber")));//购买数量
            CaipinEntity caipinEntity = caipinService.selectById(caipinId);//购买的商品
            String id = String.valueOf(map.get("id"));
            if(StringUtil.isNotEmpty(id))
                cartIds.add(Integer.valueOf(id));

            //判断商品的库存是否足够
            if(caipinEntity.getCaipinKucunNumber() < buyNumber){
                //商品库存不足直接返回
                return R.error(caipinEntity.getCaipinName()+"的库存不足");
            }else{
                //商品库存充足就减库存
                caipinEntity.setCaipinKucunNumber(caipinEntity.getCaipinKucunNumber() - buyNumber);
            }

            //订单信息表增加数据
            CaipinOrderEntity caipinOrderEntity = new CaipinOrderEntity<>();

            //赋值订单信息
            caipinOrderEntity.setCaipinOrderUuidNumber(caipinOrderUuidNumber);//订单编号
            caipinOrderEntity.setAddressId(addressId);//收货地址
            caipinOrderEntity.setCaipinId(caipinId);//菜品
            caipinOrderEntity.setYonghuId(userId);//用户
            caipinOrderEntity.setBuyNumber(buyNumber);//购买数量 ？？？？？？

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            caipinOrderEntity.setCaipinOrderTime(sdf.parse(caipinOrderTime));//预约时间 ？？？？？？
            caipinOrderEntity.setCaipinOrderPaymentTypes(caipinOrderPaymentTypes);//支付类型
            caipinOrderEntity.setInsertTime(new Date());//订单创建时间
            caipinOrderEntity.setCreateTime(new Date());//创建时间
            Double money = new BigDecimal(caipinEntity.getCaipinNewMoney()).multiply(new BigDecimal(buyNumber)).multiply(zhekou).doubleValue();
            //判断是什么支付方式 1代表余额 3代表支付宝
            if(caipinOrderPaymentTypes == 1){//余额支付
                //计算金额

                if(yonghuEntity.getNewMoney() - money <0 ){
                    return R.error("余额不足,请充值！！！");
                }else{
                    //计算所获得积分
                    Double buyJifen =0.0;
                    yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() - money); //设置金额
                    caipinOrderEntity.setCaipinOrderTruePrice(money);
                    caipinOrderEntity.setCaipinOrderTruePrice(money);
                    caipinOrderEntity.setCaipinOrderTypes(101);//订单类型
                }
            }else{
                caipinOrderEntity.setCaipinOrderTruePrice(money);
                caipinOrderEntity.setCaipinOrderTypes(100);

                zongMoney=zongMoney.add(new BigDecimal(money));


            }
            caipinOrderList.add(caipinOrderEntity);
            caipinList.add(caipinEntity);

        }
        caipinOrderService.insertBatch(caipinOrderList);
        caipinService.updateBatchById(caipinList);
        yonghuService.updateById(yonghuEntity);
        if(cartIds != null && cartIds.size()>0)
            cartService.deleteBatchIds(cartIds);


        if(caipinOrderPaymentTypes != 1) {//支付宝支付
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");

            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//successZhifu.html
            alipayRequest.setReturnUrl(AlipayConfig.return_url + "caipinOrder" + "/list.html");//(订单表表名称)需要替换动态名称
//            alipayRequest.setReturnUrl(null);//(订单表表名称)需要替换动态名称
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url + "caipinOrder" + "/notify");//(订单表表名称)需要替换动态名称

            String out_trade_no = caipinOrderUuidNumber;
            String total_amount = String.valueOf(zongMoney.doubleValue());
            String subject = new String("购买了菜品");
            String body = "";

            alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                    + "\"total_amount\":\"" + total_amount + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"body\":\"" + body + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

            String form = "";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            return R.ok().put("data", form);
        }else{
            return R.ok();
        }

    }


    /**
    * 退款
    */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request){
        logger.debug("refund方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

            CaipinOrderEntity caipinOrder = caipinOrderService.selectById(id);//当前表service
            Integer buyNumber = caipinOrder.getBuyNumber();
            Integer caipinOrderPaymentTypes = caipinOrder.getCaipinOrderPaymentTypes();
            Integer caipinId = caipinOrder.getCaipinId();
            if(caipinId == null)
                return R.error(511,"查不到该菜品");
            CaipinEntity caipinEntity = caipinService.selectById(caipinId);
            if(caipinEntity == null)
                return R.error(511,"查不到该菜品");
            Double caipinNewMoney = caipinEntity.getCaipinNewMoney();
            if(caipinNewMoney == null)
                return R.error(511,"菜品价格不能为空");

            Integer userId = (Integer) request.getSession().getAttribute("userId");
            YonghuEntity yonghuEntity = yonghuService.selectById(userId);
            if(yonghuEntity == null)
                return R.error(511,"用户不能为空");
            if(yonghuEntity.getNewMoney() == null)
            return R.error(511,"用户金额不能为空");
            Double zhekou = 1.0;

            //判断是什么支付方式 1代表余额 2代表积分
            if(caipinOrderPaymentTypes == 1){//余额支付
                //计算金额
                Double money = caipinEntity.getCaipinNewMoney() * buyNumber  * zhekou;
                //计算所获得积分
                Double buyJifen = 0.0;
                yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() + money); //设置金额


            }

            caipinEntity.setCaipinKucunNumber(caipinEntity.getCaipinKucunNumber() + buyNumber);

            caipinOrder.setCaipinOrderTypes(102);//设置订单状态为已退款
            caipinOrderService.updateAllColumnById(caipinOrder);//根据id更新
            yonghuService.updateById(yonghuEntity);//更新用户信息
            caipinService.updateById(caipinEntity);//更新订单中菜品的信息

            return R.ok();
    }

    /**
    * 评价
    */
    @RequestMapping("/commentback")
    public R commentback(Integer id, String commentbackText, Integer caipinCommentbackPingfenNumber, HttpServletRequest request){
        logger.debug("commentback方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
            CaipinOrderEntity caipinOrder = caipinOrderService.selectById(id);
        if(caipinOrder == null)
            return R.error(511,"查不到该订单");
        Integer caipinId = caipinOrder.getCaipinId();
        if(caipinId == null)
            return R.error(511,"查不到该菜品");

        CaipinCommentbackEntity caipinCommentbackEntity = new CaipinCommentbackEntity();
            caipinCommentbackEntity.setId(id);
            caipinCommentbackEntity.setCaipinId(caipinId);
            caipinCommentbackEntity.setYonghuId((Integer) request.getSession().getAttribute("userId"));
            caipinCommentbackEntity.setCaipinCommentbackText(commentbackText);
            caipinCommentbackEntity.setInsertTime(new Date());
            caipinCommentbackEntity.setReplyText(null);
            caipinCommentbackEntity.setUpdateTime(null);
            caipinCommentbackEntity.setCreateTime(new Date());
            caipinCommentbackService.insert(caipinCommentbackEntity);

            caipinOrder.setCaipinOrderTypes(105);//设置订单状态为已评价
            caipinOrderService.updateById(caipinOrder);//根据id更新
            return R.ok();
    }

    /**
     * 派送
     */
    @RequestMapping("/deliver")
    public R deliver(Integer id ,String caipinOrderCourierNumber, String caipinOrderCourierName , HttpServletRequest request){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        CaipinOrderEntity  caipinOrderEntity = caipinOrderService.selectById(id);
        caipinOrderEntity.setCaipinOrderTypes(103);//设置订单状态为已派送
        caipinOrderEntity.setCaipinOrderCourierNumber(caipinOrderCourierNumber);
        caipinOrderEntity.setCaipinOrderCourierName(caipinOrderCourierName);
        caipinOrderService.updateById( caipinOrderEntity);

        return R.ok();
    }


    /**
     * 收货
     */
    @RequestMapping("/receiving")
    public R receiving(Integer id , HttpServletRequest request){
        logger.debug("refund:,,Controller:{},,ids:{}",this.getClass().getName(),id.toString());
        CaipinOrderEntity  caipinOrderEntity = caipinOrderService.selectById(id);
        caipinOrderEntity.setCaipinOrderTypes(104);//设置订单状态为收货
        caipinOrderService.updateById( caipinOrderEntity);
        return R.ok();
    }




    @RequestMapping("/alipay")
    public R payController(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url+"caipinOrder"+"/list.jsp");//(订单表表名称)需要替换动态名称
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url+"caipinOrder"+"/notify");//(订单表表名称)需要替换动态名称

        String out_trade_no = new String(request.getParameter("caipinOrderUuidNumber"));
        String total_amount = new String(request.getParameter("totalamount").getBytes("ISO-8859-1"),"UTF-8");
        String subject = new String(request.getParameter("subject"));
        String body = "";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.ok().put("data",form);
    }

    @IgnoreAuth
    @RequestMapping("notify")
    public R nofity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* *
         * 功能：支付宝服务器异步通知页面
         *************************页面功能说明*************************
         * 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
         * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
         * 如果没有收到该页面返回的 success
         * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
         */

        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        if(trade_status.equals("TRADE_FINISHED")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        }else if (trade_status.equals("TRADE_SUCCESS")){
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序

            //注意：
            //付款完成后，支付宝系统发送该交易状态通知

            //(修改订单状态)需要替换动态名称
            List<CaipinOrderEntity> list = caipinOrderService.selectList(new EntityWrapper<CaipinOrderEntity>().eq("caipin_order_uuid_number", out_trade_no));
            
            for(CaipinOrderEntity l:list){
                l.setCaipinOrderTypes(101);
            }
            if(list.size()>0){
                caipinOrderService.updateBatchById(list);
            }
        }

        //——请在这里编写您的程序（以上代码仅作参考）——
        return R.ok();
    }

}

