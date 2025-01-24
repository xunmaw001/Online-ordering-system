package com.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000120610296";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCI9r4Lf/4m6OiPPFDTc7Pzo+iqIfciFJUSpDLo0V46PalYLy4598FsyQkf819PtPBYTGeFZehgPS2L9Bymm+gRtRiNowUaW3/Tqk6Qshr7bjOT91S+MqH2KXrzbQ3iYDtFDnbmcr2A4mRt4bj4yHy2hmaFJPTwqseLEPzqqqayuGBXepBbpJpqvn1Ev9xIi1xLtwnKPiBfbJXuWIea6IDOQWgv7Mf6mADzRlGvZe/ZVFLjJB2QjDD/Dl5j0BQ/3z3bDig03NhkRyvgmqgu/9Qxhkqaku1LySd0kHPv89BVRd2Qucq5k6KvR9eQPG+TIYUIWIbagz4x5vOaSMdm7rLlAgMBAAECggEAJ9KOGSviyo9nptXjrkdy6AEdUfJEN72CRoRFZRoS6p4pEcHOD/6wV5/wLkEF0lgKlbHJW6jwsqK3Dn0wo2SrShK8i9n9TteRyAo5g6q1W6uEBZ6hHhf8rWUMIaDwq4RNV1vISGLOJJ4ukyBRNsycLjnPryC6swsEr7OFxflONKImMNeHcTrl4k3wCF9zoTL8gAACriIQtyCH5FPg5UQT3b72wvZ/TcQi0/3oDVZGqErOWtzda/s14EqQmltQhi0NtruaK9177KYRIpF7wEWjr9AiaqqEaDIRb73mxlq7jibX8OOswq8juKWIq6B1caebqx3A2M9hbAQGAqaHOgr6YQKBgQDjm3FGX/Qd5uMezAlyB3c0VqJCcxxsQJDv3U0XpIEhJzCwj8u0UFIUFIoUvuRiQq6YnItzS1bStN7JvjfPkZ4PKThBxKet4o6QTVq5UTYZOf6D2Zrg1NltLCECOZxAA6pakOCy5r4ysdyqJheFLrXOYUh8Ql6AWiCmFN4IubeXzQKBgQCaDKKoIy7hICIBMIjKW6T8uW/wI9Bs1Ttl8bfDMGplCtWY7ZBah7aDqRSOy14t6WLTvTj9/A+a4yzlJjGZ54hm129vK96YVZnz0d/bSB6BIzIj0byIFP2LJ//LSsNI/rkNt51CWnImHWdKXUx+WR8Tu4gYXu9jjkRSIjh8pn6/eQKBgAmjMFWxhG2rtBw+uXupk0OVE4PKyf5PcZOmhDgnEAyuUyJ2xEJbUE5nXD1qo9MBTdFKd6EnJIMBg5l5Q3W3jcDIlwg54nu7Zxv3GotUQ/0ndn5wvV0dxiMYRkGI3PPc896n2cvZd15AJ77QpBhQlQQrHF2elc/LgSxnH8uwyXRVAoGBAI/DZPT2yKQuIa3shmwa9LEpua5pLfSzVph/6VfOUixz3Py4D7R83LaoT1gnsH+2Q5WJDKvy1PleMyKy7y1LJz+apBMi5LxTfrZNUdVJy7YGi59j98cjl66LKKum3PK9TSPhDHDFCaxKWn/K7D0pnNgEdjEQP+Yv+dCQwRiLJNQ5AoGBAMlPuqpCCr/1XUdN38vZiOQK0Cjn0JZXiREk/IyvVEZ7sSK/K2YMsmAezU3TAGQKXIsIbC9gls3MsAAvWQlrkK6Gc/Mzk5DGi1G7cr807Y0hjl8kOHAASXG+5L0CI3TttkS8qxdG0knJetHG0Knzwx5rxbQrq1d8hE49IeQWpJHo";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9ZQgbzlC65TbR6Rlw6sVk74EQHaguhwlM2o3w68Vl0l2aBGgNXaESrSmukP8JX6kwTO6gd3NSTWbdSjQyAOrzAfjefwasrCx42lKZFcDGsxTLLhhmw/Ng5FEumbiuP4SMWFGjGhrNWj/oK6eSkj2Dp3FPYMj6vSssRgAgWRAlEkvcRAkGyuVp5sysO+V+S8NMEjW+oeYxKKz8q0d0VGHJmZc7CSNhHVe7HDjrzagSrMdT97Nkh/QOhEfjU5/vwmHAR3fcDi4b4DputidUVyqHMTa9+3U5IC67M0vNrWOEXEVujnkEg4fRYYU7y20Vdn096T+sUctkGwu9d2GUF0YwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://nzpp6r.natappfree.cc/wangshangdingcan/";//需要替换动态名称
                                        //内网穿透地址^^^^^^^^^^^^^^^^^^下面的地址是内网穿透网站和地址
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String return_url = "http://nzpp6r.natappfree.cc/wangshangdingcan/front/pages/";//需要替换动态名称
    public static String return_url = "http://localhost:8080/wangshangdingcan/front/pages/";//需要替换动态名称
                                        //内网穿透地址^^^^^^^^^^^^^^^^^^下面的地址是内网穿透网站和地址
    // 内网穿透网站(https://natapp.cn/) 使用教程（https://natapp.cn/article/natapp_newbie）

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

