package com.wangsheng.httplibrary.base;

/**
 * @description 描述:服务和交易地址
 * @description1 请求码的作用是在请求成功活着失败时对不同的请求进行区分对待
 * @auther wangsheng on 16/4/4 22:09.
 * @email 公司邮箱:wangsheng@csii.com.cn  个人邮箱:jingbei110@163.com
 */
public class HttpHelper {
//    public static String baseUrl = "http://10.4.32.147/";//测试环境
    public static String baseUrl = "https://zhixiao.zjtlcb.com/";//生产环境
//    public static String baseUrl = "http://10.4.32.117/";//回归
//    public static String baseUrl = "http://10.4.12.92:9080/";//宾俊峰
//    public static String baseUrl = "http://10.4.12.19:9080/";//车
//    public static String baseUrl = "http://192.168.1.110:9080/";//童
//    public static String baseUrl = "http://10.4.12.206:9080/";//唐
//    public static String baseUrl = "http://10.4.17.9:9080/";//开发环境
//    public static String baseUrl = "https://10.4.24.56:90/";//手机银行
    public static String Url = baseUrl + "mobile/";
    public static String ImageUrl = "";
//    public static String WebUrl = "http://10.4.32.147/mobile/index.html";//测试环境
    public static String WebUrl = "https://zhixiao.zjtlcb.com/mobile/index.html";//生产环境
//    public static String WebUrl = "http://10.4.32.117/mobile/index.html";//回归
//    public static String WebUrl = "http://10.4.24.56:90/mobile/index.html";//手机银行
//    public static String WebUrl = "http://10.4.12.92/mobile/index.html";//宾俊峰
//    public static String WebUrl = "http://10.4.12.19:9080/index.html";//车
//    public static String WebUrl = "http://192.168.1.110:8888/mobile/index.html";//童
//    public static String WebUrl = "http://10.4.12.206/mobile/index.html";//唐

    public static String Login = "Login.do";//登录
    public static final int LoginRequestCode = 1;//登录请求码

    public static String Recharge = "Recharge.do";//充值
    public static final int RechargeRequestCode = 2;//充值请求码

    public static String Sign = "Sign.do";//签到
    public static final int SignRequestCode = 3;//签到请求码

    public static String RegisterPhone = "RegisterPhone.do";//验证手机号是否注册
    public static final int RegisterPhoneRequestCode = 4;//验证手机号是否注册请求码

    public static String GenPhoneTokenForPublic = "GenPhoneTokenForPublic.do";//短信验证码(未登录状态)
    public static final int GenPhoneTokenForPublicRequestCode = 5;//短信验证码请求码

    public static String GenPhoneToken = "GenPhoneToken.do";//短信验证码(已登录状态)
    public static final int GenPhoneTokenRequestCode = 6;//短信验证码请求码

    public static String Register = "Register.do";//注册
    public static final int RegisterRequestCode = 7;//注册请求码

    public static String CommMeasure = "CommMeasure.do";//测算
    public static final int CommMeasureRequestCode = 8;//我的佣金请求码

    public static String MyComm = "MyComm.do";//我的佣金
    public static final int MyCommRequestCode = 9;//我的佣金请求码

    public static String IntegralExchangeCouponList = "IntegralExchangeCouponList.do";//卡券列表
    public static final int IntegralExchangeCouponListRequestCode = 10;//卡券列表请求码

    public static String PromCode = "PromCode.do";//我的二维码
    public static final int PromCodeRequestCode = 11;//我的二维码请求码

    public static String SignQuery = "SignQuery.do";//签到查询
    public static final int SignQueryRequestCode = 12;//签到查询请求码

    public static String CouponRtxnHist = "CouponRtxnHist.do";//我的卡券
    public static final int CouponRtxnHistRequestCode = 13;//我的卡券请求码

    public static String IncomeWithdraw = "IncomeWithdraw.do";//我的卡券列表中提现
    public static final int IncomeWithdrawCode = 14;//提现请求码


    public static String OldMobilePhoneCheck = "OldMobilePhoneCheck.do";//手机号码修改-校验原手机号码
    public static final int OldMobilePhoneCheckRequestCode = 15;//请求码

    public static String ModifyLonginPasswd = "ModifyLonginPasswd.do";//修改登录密码
    public static final int ModifyLonginPasswdRequestCode = 16;//修改请求码

    public static String CommWithdraw = "CommWithdraw.do";//二度营销佣金提现
    public static final int CommWithdrawRequestCode = 17;//二度营销佣金提现请求码

    public static String MobilePhoneModify = "MobilePhoneModify.do";//手机号码修改
    public static final int MobilePhoneModifyRequestCode = 18;//请求码

    public static String GesturePwdSeting = "GesturePwdSeting.do";//设置手势密码
    public static final int GesturePwdSetingRequestCode = 19;//请求码

    public static String ModifyGesturePwd = "ModifyGesturePwd.do";//修改手势密码
    public static final int ModifyGesturePwdRequestCode = 20;//请求码

    public static String DeleteGesturePwd = "DeleteGesturePwd.do";//删除手势密码
    public static final int DeleteGesturePwdRequestCode = 21;//请求码

    public static String Notice = "Notice.do";//公告列表
    public static final int NoticeRequestCode_home = 22;//首页公告－－－请求码

    public static String Advertisement = "Advertisement.do";//轮播广告
    public static final int AdvertisementRequestCode = 23;//请求码

    public static String TradesDetails = "TradesDetails.do";//交易明细
    public static final int TradesDetailsRequestCode = 24;//请求码

    public static String OpenAccount = "OpenAccount.do";//绑卡
    public static final int OpenAccountRequestCode = 25;//请求码

    public static String Withdrawals = "Withdrawals.do";//提现
    public static final int WithdrawalsRequestCode = 26;//请求码

    public static String BalanceQuery = "BalanceQuery.do";//账户余额查询
    public static final int BalanceQueryRequestCode = 27;//请求码

    public static String GenTokenImg = "GenTokenImg.do";//获取图形验证码
    public static final int GenTokenImgRequestCode = 28;//请求码

    public static String CheckDynamicCode = "CheckDynamicCode.do";//重置登陆密码手机校验码
    public static final int CheckDynamicCodeRequestCode = 29;//请求码

    public static String ResetLonginPwd = "ResetLonginPwd.do";//重置登陆密码
    public static final int ResetLonginPwdRequestCode = 30;//请求码

    public static String ChangeInformation = "ChangeInformation.do";//修改个人信息
    public static final int ChangeInformationRequestCode = 31;//修改个人信息_请求码

    public static String AccountQuery = "AccountQuery.do";//智能存款页面 列表
    public static final int AccountQueryRequestCode = 32;//存入_请求码

    public static String Intelligentdeposit = "Intelligentdeposit.do";//智能存款页面 存入
    public static final int IntelligentdepositRequestCode = 33;//存入_请求码

    public static String IntelligentDraw = "IntelligentDraw.do";//智能存款页面 取出
    public static final int IntelligentDrawRequestCode = 34;//取出_请求码

    public static String CommPertQuery = "CommPertQuery.do";//我是理财师页面顶部小鱼钱包推广比例
    public static final int CommPertQueryRequestCode = 35;//请求码

    public static String IntegralRtxnHist = "IntegralRtxnHist.do";//虾米明细
    public static final int IntegralRtxnHistRequestCode = 36;//请求码

    public static String IntegralBal = "IntegralBal.do";//我的虾米数量
    public static final int IntegralBalRequestCode = 37;//请求码

    public static String IntegralExchangeCoupon = "IntegralExchangeCoupon.do";//虾米兑换卡券
    public static final int IntegralExchangeCouponRequestCode = 38;//请求码

    public static String ProductList = "ProductList.do";//产品列表
    public static final int ProductListRequestCode = 39;//请求码

    public static String FileUpload = "FileUpload.do";//实名认证上传图片
    public static final int FileUploadRequestCode = 40;//请求码

    public static String TotalAssets = "TotalAssets.do";//我的投资
    public static final int TotalAssetsRequestCode = 41;//请求码

    public static String TradePassword = "TradePassword.do";//验证原交易密码
    public static final int TradePasswordRequestCode = 42;//请求码

    public static String TradePasswordModify = "TradePasswordModify.do";//修改交易密码
    public static final int TradePasswordModifyRequestCode = 43;//请求码

    public static String MinnowShowForIndex = "MinnowShowForIndex.do";//首页小鱼钱包
    public static final int MinnowShowForIndexRequestCode = 44;//请求码

    public static String ChangeAccountCard = "ChangeAccountCard.do";//变更绑定卡  已经实名认证  并且账户没有余额
    public static final int ChangeAccountCardRequestCode = 45;//请求码

    public static String ProvinceQuery = "ProvinceQuery.do";//省份查询
    public static final int ProvinceQueryRequestCode = 46;//请求码

    public static String RegionQuery = "RegionQuery.do";//地区查询
    public static final int RegionQueryRequestCode = 47;//请求码

    public static String BankCode = "BankCode.do";//行名行号查询
    public static final int BankCodeRequestCode = 48;//请求码

    public static String PhoneTokenCheckManager = "PhoneTokenCheckManager.do";//校验短信验证码
    public static final int PhoneTokenCheckManagerRequestCode = 49;//请求码

    public static String VersionCheck = "VersionCheck.do";//校验短信验证码
    public static final int VersionCheckRequestCode = 50;//请求码

    public static String BankAmericardQuery = "BankAmericardQuery.do";//获取银行卡名字
    public static final int BankAmericardQueryRequestCode = 51;//请求码

    public static String Logout = "Logout.do";//退出
    public static final int LogoutRequestCode = 52;//请求码

    public static String BankAmericardLimit = "BankAmericardLimit.do";//?Flag=W W提现P充值 限额
    public static final int BankAmericardLimitRequestCode = 53;//请求码

    public static final int NoticeRequestCode_ActivityFragment03 = 54;//虾米规则公告－－－请求码
    public static final int NoticeRequestCode_My2DBarCode = 55;//我的二维码公告－－－请求码
    public static final int NoticeRequestCode_TopUpActivity = 56;//充值公告－－－请求码
    public static final int NoticeRequestCode_WithdrawCashActivity = 57;//提现公告－－－请求码
    public static final int NoticeRequestCode_HomeDialog = 58;//首页弹窗--公告

    public static String GesturePwdVal = "GesturePwdVal.do";//验证手势
    public static final int GesturePwdValRequestCode = 59;//请求码

    public static String TransferStatusQry = "TransferStatusQry.do";//交易明细状态校验
    public static final int TransferStatusQryRequestCode = 60;//请求码

    public static final int CouponRtxnHistRequestCode_web = 61;//我的卡券请求码 web页面

    public static String BindingRelationQuery = "BindingRelationQuery.do"; //三方登录绑定关系查询
    public static final int BindingRelationQueryRequestCode = 63;//请求码

    public static String ThirdPartyLogin = "ThirdPartyLogin.do";//第三方登录
    public static final int ThirdPartyLoginRequestCode = 62;//请求码

    public static final int RegisterPhoneRequestCode_THREE = 63;//第三方登录——验证手机号是否注册请求码

    public static String MyPrizeList = "MyPrizeList.do";//我的奖品列表
    public static final int MyPrizeListRequestCode = 64;//请求码

    public static String UpdatePrizeSend = "UpdatePrizeSend.do";//更新收货信息
    public static final int UpdatePrizeSendRequestCode = 65;//请求码


}
