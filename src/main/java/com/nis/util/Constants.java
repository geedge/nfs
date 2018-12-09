package com.nis.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.GsonBuilder;

public final class Constants {
	public static final String GROUP_REUSE_SOURCES=Configurations.getStringProperty("groupReuseSources","groupReuseSources");
	public static Integer APP_SPEC_SERVICE_CODE_MIN_VAL=Configurations.getIntProperty("app_spec_service_code_min_val", 300001);
	public static Integer APP_SPEC_SERVICE_CODE_MAX_VAL=Configurations.getIntProperty("app_spec_service_code_max_val", 268435455);
	public static String APP_CFG_USERREGION_SPLITOR=Configurations.getStringProperty("app_cfg_userregion_splitor", "&");
	/**
	 * MaatConfig 默认值
	 */
	public static Integer MAAT_CFG_DOLOG_DEFAULT=Configurations.getIntProperty("maat_cfg_dolog_default", 2);
	public static Integer MAAT_CFG_DOLOG_DOBLACKLIST_DEFAULT=Configurations.getIntProperty("maat_cfg_dolog_doblacklist_default", 1);
	public static Integer MAAT_CFG_DOLOG_CONFIGPERCENT_DEFAULT=Configurations.getIntProperty("maat_cfg_dolog_configpercent_default", 100);
	public static Integer MAAT_CFG_DOLOG_CONFIGOPTION_DEFAULT=Configurations.getIntProperty("maat_cfg_dolog_configoption_default", 1);
	/**
	 * iP默认值
	 */
	public static String IPV4_DEFAULT_IP_VALUE=Configurations.getStringProperty("ipv4_default_ip_value", "0.0.0.0");
	public static String IPV6_DEFAULT_IP_VALUE=Configurations.getStringProperty("ipv6_default_ip_value", "::");
	public static String IPV4_DEFAULT_IP_SUBNET_VALUE=Configurations.getStringProperty("ipv4_default_ip_subnet_value", "0.0.0.0/32");
	public static String IPV6_DEFAULT_IP_SUBNET_VALUE=Configurations.getStringProperty("ipv6_default_ip_subnet_value", "::/128");
	public static String IPV4_DEFAULT_IP_RANGE_VALUE=Configurations.getStringProperty("ipv4_default_ip_range_value", "0.0.0.0-0.0.0.0");
	public static String IPV6_DEFAULT_IP_RANGE_VALUE=Configurations.getStringProperty("ipv6_default_ip_range_value", "::-::");
	/**
	 * port默认值
	 */
	public static String PORT_DEFAULT=Configurations.getStringProperty("port_default", "0");
	public static String PORT_MASK_DEFAULT=Configurations.getStringProperty("port_mask_default", "0/65535");
	/**
	 *特定服务类型 
	 */
	public static String SPECIFIC_SERVICE_CFG_TYPE_APP=Configurations.getStringProperty("specific_service_cfg_type_app", "social_app");
	public static String SPECIFIC_SERVICE_CFG_TYPE_ENCRYPTED_TUNNEL_BEHAVIOR=Configurations.getStringProperty("specific_service_cfg_type_encrypted_tunnel_behavior", "encrypted_tunnel_behavior");
	public static String SPECIFIC_SERVICE_CFG_TYPE_BASIC_PROTOCOL=Configurations.getStringProperty("specific_service_cfg_type_basic_protocol", "basic_protocol");
	/**
	 * 报表去重连接符
	 */
	public static final String REPORT_TRIM_CONNECTOR="_;_";
	/**
	 * 报表类型
	 */
	public static final int REPORT_TYPE_HOUR=1;
	public static final int REPORT_TYPE_DAY=2;
	public static final int REPORT_TYPE_MONTH=3;
	/**
	 * 配置审核下发的类型 maat类型
	 */
	public static final int MAAT_TYPE=1;
	/**
	 * 配置审核下发的类型 回调类型
	 */
	public static final int CALLBACK_TYPE=2;
	/**
	 * 域名验证正则
	 */
	public static final String DOMAIN_REGEXP=Configurations.getStringProperty("domain_regexp", "*");
	/**
	 * IP验证正则
	 */
	public static final String IPV4_IP_SUBNET_REGEXP=Configurations.getStringProperty("ipv4_ip_subnet_regexp", "*");
	public static final String IPV4_IP_SUBNET_REGEXP_ORIGINAL=Configurations.getStringProperty("ipv4_ip_subnet_regexp_original", "*");
	public static final String IPV6_IP_SUBNET_REGEXP=Configurations.getStringProperty("ipv6_ip_subnet_regexp", "*");
	public static final String IPV4_IP_RANGE_REGEXP=Configurations.getStringProperty("ipv4_ip_range_regexp", "*");
	//0.0.0.1-2这种格式
	public static final String IPV4_IP_RANGE_REGEXP_NEW=Configurations.getStringProperty("ipv4_ip_range_regexp_new", "*");
	public static final String IPV6_IP_RANGE_REGEXP=Configurations.getStringProperty("ipv6_ip_range_regexp", "*");
	public static final String IPV4_IP_REGEXP=Configurations.getStringProperty("ipv4_ip_regexp", "*");
	public static final String IPV6_IP_REGEXP=Configurations.getStringProperty("ipv6_ip_regexp", "*");
	/**
	 * 用户自定义域，限速
	 */
	public static final String USERREGION_RATE_LIMIT=Configurations.getStringProperty("userregion_rate_limit", "RATE_LIMIT");
	/**
	 * 用户自定义域，策略分组号
	 */
	public static final String USERREGION_IR_STRATEGY=Configurations.getStringProperty("userregion_ir_strategy", "IR_STRATEGY");
	/**
	 * 用户自定义域，策略分组类型
	 */
	public static final String USERREGION_IR_TYPE=Configurations.getStringProperty("userregion_ir_type", "IR_TYPE");
	/**
	 * 用户自定义域，域名ID
	 */
	public static final String USERREGION_DOMAIN_ID=Configurations.getStringProperty("userregion_domain_id", "DOMAIN_ID");
	/**
	 * 用户自定义域，域名
	 */
	public static final String USERREGION_DOMAIN_STR=Configurations.getStringProperty("userregion_domain_str", "DOMAIN_STR");
	/**
	 * IP限速业务类型
	 */
	public static final int SERVICE_IP_RATELIMIT = Configurations.getIntProperty("service_ip_ratelimit", 0x421);
	/**
	 * 域名限速业务类型
	 */
	public static final int SERVICE_DOMAIN_RATELIMIT = Configurations.getIntProperty("service_domain_ratelimit", 0x421);
	/**
	 * IP复用业务类型
	 */
	public static final int SERVICE_IP_MULITIPLEX = Configurations.getIntProperty("service_ip_mulitiplex", 768);
	/**
	 * 域名拦截业务类型
	 */
	public static final Integer SERVICE_PXY_DOMAIN_INTERCEPT = Configurations.getIntProperty("service_pxy_domain_intercept",0x201);
	/**
	 * 当前时区
	 */
	public static final int TIME_ZONE = Configurations.getIntProperty("time_zone", 8);
	/**
	 * 区域GK类型
	 */
	public static final int AREA_EFFECTIVE_TYPE_AREA_ISP = 1;
	public static final int AREA_EFFECTIVE_TYPE_AREA_IP = 0;
	/**
	 * 是否区域GK
	 */
	public static final int IS_AREA_EFFECTIVE_YES = 1;
	public static final int IS_AREA_EFFECTIVE_NO = 0;
	/**
	 * 字典表地域运营商
	 */
	public static final int ITEM_TYPE_AREA = 1;
	public static final int ITEM_TYPE_ISP = 2;
	/**
	 * action值
	 */
	public static final int ACTION_DF = 1;
	public static final int ACTION_DJ = 2;
	public static final int ACTION_BMD = 5;
	public static final int ACTION_HMD = 8;
	public static final Integer DROP_ACTION = Configurations.getIntProperty("drop_action",32);
	public static final Integer LOOP_ACTION = Configurations.getIntProperty("loop_action",96);
	public static final Integer RATELIMIT_ACTION = Configurations.getIntProperty("ratelimit_action",64);
	public static final Integer REPLACE_ACTION = Configurations.getIntProperty("replace_action",80);
	public static final Integer MONIT_ACTION = Configurations.getIntProperty("monit_action",1);
	public static final Integer REJECT_ACTION = Configurations.getIntProperty("reject_action",16);
	public static final Integer WHITELIST_ACTION = Configurations.getIntProperty("reject_action",128);
	public static final int CFG_PAGE = 0;
	public static final int AUDIT_PAGE = 1;
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	/**
	 * 表类型
	 */
	public static final int TABLE_TYPE_IP = 1;
	public static final int TABLE_TYPE_STRING = 2;
	public static final int TABLE_TYPE_NUMBER = 3;
	public static final int TABLE_TYPE_COMPLEX = 4;
	/**
	 * 词典数据key
	 */
	public static final String CACHE_DICT_MAP = "dictMap";
	/**
	 * 特征域字典key
	 */
	public static final String CACHE_FEATURES_DICT_MAP = "featuresDictMap";
	
	/**
	 * 功能业务字典key
	 */
	public static final String CACHE_FUNCTION_SERVICE_DICT = "functionServiceDict";
	
	/**
	 * 功能配置域字典key
	 */
	public static final String CACHE_FUNCTION_REGION_DICT = "functionRegionDict";
	/**
	 * DNS 响应策略KEY
	 */
	public static final String CACHE_DNSRESSTRATEGY_DICT = "dnsResStrategyDict";
	/**
	 * DNS 策略组KEY
	 */
	public static final String CACHE_DNSGROUP_DICT = "dnsGroupDict";
	/**
	 *  词典数据分类
	 */
	public static final int ITEM_TYPE_FL  = 1;
	/**
	 *  词典数据性质
	 */
	public static final int ITEM_TYPE_XZ  = 2;
	/**
	 *  词典数据标签
	 */
	public static final int ITEM_TYPE_LABEL  = 3;
	/**
	 * 配置有效标志，有效
	 */
	public static final int VALID_YES=1;
	/**
	 * 配置有效标志，无效
	 */
	public static final int VALID_NO=0;
	/**
	 * 配置有效标志，删除
	 */
	public static final int VALID_DEL=-1;
	/**
	 * 配置审核,未审核
	 */
	public static final int AUDIT_NOT_YET=0;
	/**
	 * 配置审核,审核通过
	 */
	public static final int AUDIT_YES=1;
	/**
	 * 配置审核,审核未通过
	 */
	public static final int AUDIT_NO=2;
	/**
	 * 配置审核,取消审核通过
	 */
	public static final int AUDIT_NOT_YES=3;
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	public static final int LOG_ACCESS_SUCCESS = 1;
	public static final int LOG_ACCESS_EXCEPTION = 0;
	/**
	 * 默认未知方法（未添加词典或未识别）操作类型值为：unknown（8000）
	 */
	public static final int DEFAULT_METHOD_TYPE = 8000;

	public static final String SYS_BUSINESS_MENU_NAME = "信访管理";

	public static final String LABEL_DEFAULT = "label";
	public static final String LABEL_SUCCESS = "label label-success";
	public static final String LABEL_WARNING = "label label-warning";
	public static final String LABEL_IMPORTANT = "label label-important";
	public static final String LABEL_INFO = "label label-info";
	public static final String LABEL_INVERSE = "label label-inverse";
	/**
	 * 生效系统
	 */
	public static final String ACTIVESYS_A = "4";
	public static final String ACTIVESYS_B = "2";
	public static final String ACTIVESYS_C = "1";
	public static final String ACTIVESYS_ALL = "7";
	//A+B版
	public static final String ACTIVESYS_AB = "6";
	/**
	 * 数据库操作
	 */
	public static final String INSERT = "I";
	public static final String UPDATE = "U";
	public static final String DELETE = "D";
	/**
	 * 接口的操作行为opAction
	 */
	public static final int OPACTION_POST = 1;
	public static final int OPACTION_PUT = 2;
	public static final int OPACTION_DELETE = 3;
	public static final int OPACTION_GET = 4;
	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	/**
	* 每页最大显示数
	*/
	public static final int MAX_PAGE_SIZE = Configurations.getIntProperty("maxPageSize", 100000);
	/**
	 * 每次最大导出条数
	 */
	public static final int MAX_EXPORT_SIZE = Configurations.getIntProperty("maxExportSize", 100000);
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	/**
	 * 服务器ip
	 */
	public static String SERVCER_HOST = null;
	/**
	 * oracle redis数据 存储时间
	 */
	public static final int ORACLE_EXPIRE = Configurations.getIntProperty("oracleExpire", 180);
	/**
	 * hive redis数据 存储时间
	 */
	public static final int HIVE_EXPIRE = Configurations.getIntProperty("hiveExpire", 180);
	/**
	 * redis开关
	 */
	public static final boolean IS_OPEN_REDIS = Configurations.getBooleanProperty("isOpenRedis", false);
	/**
	 * es开关
	 */
	public static final boolean IS_USE_ES = Configurations.getBooleanProperty("isUseES", false);

	/**
	 * 数据中心日志redis开关
	 */
	public static final boolean DATACENTER_OPEN_REDIS = Configurations.getBooleanProperty("dataCenterOpenRedis", false);
	/**
	 * 是否使用use soq_log命令
	 */
	// public static final boolean IS_USE_HIVE_DB =
	// Configurations.getBooleanProperty("isUseHiveDb", true);

	/**
	 * 是否获取数据中心查询记录的总条数
	 */

	public static final boolean IS_GET_HIVECOUNT = Configurations.getBooleanProperty("isGetHiveCount", true);
	/**
	 * 是否获取数据中心[神通]查询记录的总条数
	 */
	
	public static final boolean IS_SELECT_CLUSTER = Configurations.getBooleanProperty("isSelectCluster", false);
	
	/**
	 * 神通数据库A的数据最早时间
	 */
	public static final Long CLUSTER_A_START_TIME =  Configurations.getLongProperty("clusterAStartTime", new Date().getTime());

	/**
	 * 神通数据库B的数据最早时间
	 */
	public static final Long CLUSTER_B_START_TIME = Configurations.getLongProperty("clusterBStartTime", new Date().getTime());
	
	/**
	 * 每次获取数据中心多少条数据,咱们在对获取的数据进行分页处理
	 */
	public static final Long EVERY_GETHIVEDATANUM = Configurations.getLongProperty("everyGetHiveDataNum", 10000);

	/**
	 * 是否开启基础校验
	 */
	public static final boolean BASE_VALIDATE = Configurations.getBooleanProperty("baseValidate", true);

	public static final Long DATACENTER_TIME = Configurations.getLongProperty("dataCenterTime", 48);

	/**
	 * 是否开启业务校验
	 */
	public static final boolean SERVICE_VALIDATE = Configurations.getBooleanProperty("serviceValidate", true);
	/**
	 * 日志本地存储时间
	 */
	public static final Long LOG_LOCAL_TIME = Configurations.getLongProperty("logLocalTime", 48);
	/**
	 * 实时统计默认时间
	 */
	public static final Long REPORT_LOCAL_TIME = Configurations.getLongProperty("reportLocalTime", 1);

	/**
	 * 日志是否从hive中查询
	 */
	public static final boolean SEL_FROM_HIVE = Configurations.getBooleanProperty("selFromHive", true);
	public static final boolean ONLY_SEL_FROM_HIVE = Configurations.getBooleanProperty("onlySelFromHive", true);

	/**
	 * 跨域问题解决，允许跨域的url
	 */
	public static final String TARGET_URL = Configurations.getStringProperty("target_url", "*");
	public static final String ACCESS_CONTROL_MAX_AGE = Configurations.getStringProperty("ACCESS_CONTROL_MAX_AGE",
			"3600");
	/**
	 * elasticsearch 检索相关
	 */
	public static final String SEARCH_DATEFORMAT = Configurations.getStringProperty("search.dateformat",
			"yyyy-MM-dd HH:mm:ss");
	public static final String SEARCH_ES_HOSTANDPORT_A = Configurations.getStringProperty("search.eshostandport_A",
			null);
	public static final String SEARCH_ES_HOSTANDPORT_B = Configurations.getStringProperty("search.eshostandport_B",
			null);
	public static final String SEARCH_ES_HOSTANDPORT_C = Configurations.getStringProperty("search.eshostandport_C",
			null);
	/**
	 * 数据中心A版数据库名称,程序中每次查询时使用的数据库名称 use HIVEADBNAME
	 */
	public static final String HIVEADBNAME = Configurations.getStringProperty("jdbc.hive.AName", "xa_dfbhit_hive");
	/**
	 * 数据中心B版数据库名称,程序中每次查询时使用的数据库名称 use HIVEBDBNAME
	 */
	public static final String HIVEBDBNAME = Configurations.getStringProperty("jdbc.hive.BName", "xa_z2_mesalog_hive");
	public static final GsonBuilder gsonbuilder=new GsonBuilder();
	//综合服务接口URL
	public static final String SERVICE_URL = Configurations.getStringProperty("httpUrl","");
	public static final String MAAT_CFG= Configurations.getStringProperty("maatCfg","configSources"); 
	public static final String CALLBACK_CFG = Configurations.getStringProperty("callbackCfg","commonSources"); 
	public static final String FILE_UPLOAD_CFG = Configurations.getStringProperty("fileUploadCfg","fileUploadSources"); 
	public static final String FILE_DIGEST_CFG=Configurations.getStringProperty("fileDigestCfg","fileDigestSources");
	public static final String CONFIG_ID_SOURCES=Configurations.getStringProperty("configIdSources","configPzIdSources");
	//日志查询接口URL
	public static final String LOG_BASE_URL = Configurations.getStringProperty("logBaseUrl","");
	public static final String NTC_SERVICE_REPORT=Configurations.getStringProperty("ntcServiceReport","ntcServiceReport");
	public static final String NTC_ATTRTYPE_REPORT=Configurations.getStringProperty("ntcAttrTypeReport","ntcAttrTypeReport");
	public static final String NTC_TAG_REPORT=Configurations.getStringProperty("ntcTagReport","ntcTagReport");
	public static final String NTC_PZ_REPORT=Configurations.getStringProperty("ntcPzReport","ntcPzReport");
	public static final String NTC_NTC_SRCIP_REPORT=Configurations.getStringProperty("ntcSrcipDomesticReport","ntcSrcipDomesticReport");
	public static final String NTC_NTC_DESTIP_REPORT=Configurations.getStringProperty("ntcDestipCountryReport","ntcDestipCountryReport");
	public static final String NTC_NTC_LWHH_REPORT=Configurations.getStringProperty("ntcLwhhReport","ntcLwhhReport");
	public static final String NTC_NTC_ENTRANCE_REPORT=Configurations.getStringProperty("ntcEntranceReport","ntcEntranceReport");
	public static final String NTC_IP_LOG = Configurations.getStringProperty("ntcIpLog","");
	public static final String NTC_BGP_LOG = Configurations.getStringProperty("ntcBgpLog","");
	public static final String NTC_P2P_LOG = Configurations.getStringProperty("ntcP2pLog","");
	public static final String NTC_HTTP_LOG = Configurations.getStringProperty("ntcHttpLog","");
	public static final String NTC_KEYWORDS_URL_LOG = Configurations.getStringProperty("ntcKeywordsUrlLog","");
	public static final String NTC_DNS_LOG = Configurations.getStringProperty("ntcDnsLog","");
	public static final String NTC_SSL_LOG = Configurations.getStringProperty("ntcSslLog","");
	public static final String NTC_PPTP_LOG = Configurations.getStringProperty("ntcPptpLog","");
	public static final String NTC_L2TP_LOG = Configurations.getStringProperty("ntcL2tpLog","");
	public static final String NTC_OPENVPN_LOG = Configurations.getStringProperty("ntcOpenvpnLog","");
	public static final String NTC_IPSEC_LOG = Configurations.getStringProperty("ntcIpsecLog","");
	public static final String NTC_SSH_LOG = Configurations.getStringProperty("ntcSshLog","");
	public static final String NTC_MAIL_LOG = Configurations.getStringProperty("ntcMailLog","");
	public static final String NTC_FTP_LOG = Configurations.getStringProperty("ntcFtpLog","");
	public static final String NTC_APP_LOG = Configurations.getStringProperty("ntcAppLog", "");
	public static final String NTC_DDOS_LOG = Configurations.getStringProperty("ntcDdosLog", "");
	public static final String NTC_DKBEHAVIOR_LOG = Configurations.getStringProperty("dkBehaviorLog", "");
	public static final String NTC_MMVOIP_LOG = Configurations.getStringProperty("mmVoipLog", "");
	public static final String NTC_MMSAMPLE_VOIP_LOG = Configurations.getStringProperty("mmSampleVoipLog", "");
	public static final String NTC_MMAVIP_LOG = Configurations.getStringProperty("mmAvIpLog", "");
	public static final String NTC_MMFILEDIGEST_LOG = Configurations.getStringProperty("mmFileDigestLog", "");
	public static final String NTC_STREAMMEDIA_LOG = Configurations.getStringProperty("ntcStreamMediaLog", "");
	public static final String NTC_MMPORNAUDIOSAMPLE_LOG = Configurations.getStringProperty("mmPornAudioLevelLog", "");
	public static final String NTC_MMPORNVIDEOSAMPLE_LOG = Configurations.getStringProperty("mmPornVideoLevelLog", "");
	public static final String NTC_MMAVURL_LOG = Configurations.getStringProperty("mmAvUrlLog", "");
	public static final String NTC_MMPICIP_LOG = Configurations.getStringProperty("mmPicIpLog", "");
	public static final String NTC_MMPICURL_LOG = Configurations.getStringProperty("mmPicUrlLog", "");
	public static final String NTC_MMVOIP_IP_LOG = Configurations.getStringProperty("mmVoipIpLog", "");
	public static final String NTC_MMVOIP_ACCOUNT_LOG = Configurations.getStringProperty("mmVoipAccountLog", "");
	public static final String NTC_MMSAMPLEAUDIO_LOG = Configurations.getStringProperty("mmSampleAudioLog", "");
	public static final String NTC_MMSAMPLEVIDEO_LOG = Configurations.getStringProperty("mmSampleVideoLog", "");
	public static final String NTC_MMPORNAUDIOLEVEL_LOG = Configurations.getStringProperty("mmPornAudioLevelLog", "");
	public static final String NTC_MMPORNVIDEOLEVEL_LOG = Configurations.getStringProperty("mmPornVideoLevelLog", "");
	public static final String NTC_MMSAMPLEPIC_LOG = Configurations.getStringProperty("mmSamplePicLog", "");
	public static final String NTC_MMSAMPLESPEAKER_RECOGNIZATION_LOG = Configurations.getStringProperty("mmSpeakerRecognizationLog", "");
	public static final String NTC_MMLOGO_DETECTION_LOG = Configurations.getStringProperty("mmLogoDetectionLog", "");
	public static final String NTC_MMFACE_RECOGNIZATION_LOG = Configurations.getStringProperty("mmFaceRecognizationLog", "");
	public static final String NTC_MMSAMPLEVOIP_LOG = Configurations.getStringProperty("mmSampleVoipLog", "");
	public static final String PXY_HTTP_LOG = Configurations.getStringProperty("pxyHttpLog", "");
	public static final String NTC_COLLECT_VOIP_LOG = Configurations.getStringProperty("ntcCollectVoipLog", "");
	
	//报表类型,1-	配置命中总量业务
	public static final Integer BUSINESSTYPE_CONFIG=Configurations.getIntProperty("businesstype_config", 1);
	//报表类型,2-	配置报表业务
	public static final Integer BUSINESSTYPE_REPORT=Configurations.getIntProperty("businesstype_report", 2);
	//默认日志查询时长(ms)
	public static final String LOG_TIME_START = Configurations.getStringProperty("log_time_start",null);
	public static final String LOG_TIME_END = Configurations.getStringProperty("log_time_end",null);
	//由配置界面跳转日志查询默认查询时长(ms)
	public static final Integer LOG_TIME_RANGE = Configurations.getIntProperty("log_time_range", 300000);
	//默认日志查询时长(ms)
	public static final Integer LOG_TIME_INTERVAL = Configurations.getIntProperty("log_time_interval", 300000);
	
	//日志检索菜单ID
	public static final Integer LOGSEARCH_MENU_ID = Configurations.getIntProperty("logsearch_menu_id", 152);
	/**请求头参数*/
	public static final Map<String,Object> REQUEST_HEADER = new HashMap<String,Object>();
	public static final Integer CLIENT_CONNECT_TIMEOUT = Configurations.getIntProperty("client_connect_timeout",1000);
	public static final Integer CLIENT_READ_TIMEOUT = Configurations.getIntProperty("client_read_timeout",1000);
	public static final Integer CLIENT_SOCKET_TIMEOUT = Configurations.getIntProperty("client_socket_timeout",1000);
	public static final String APP_ID_REGION = Configurations.getStringProperty("app_id_region","APP_ID"); 
	public static final String PROTO_ID_REGION = Configurations.getStringProperty("proto_id_region","APP_ID"); 
	public static final String BEHAV_ID_REGION = Configurations.getStringProperty("behav_id_region","BEHAV_ID"); 
	public static final String RATE_LIMIT_REGION = Configurations.getStringProperty("rate_limit_region","RATE_LIMIT"); 
	public static final String AREA_REGION = Configurations.getStringProperty("area_region","area_ip"); 
	public static final String HTTP_IP_REGION = Configurations.getStringProperty("http_ip_region","http_ip"); 
	public static final String HTTP_REQ_HEAD_REGION = Configurations.getStringProperty("http_req_hdr_region","http_req_hdr"); 
	public static final String HTTP_REQ_BODY_REGION = Configurations.getStringProperty("http_req_body_region","http_req_body");
	public static final String HTTP_RES_HEAD_REGION = Configurations.getStringProperty("http_res_hdr_region","http_res_hdr"); 
	public static final String NTC_SUBSCRIBE_ID = Configurations.getStringProperty("ntc_subscribe_id","NTC_SUBSCRIBE_ID");
	public static final String HTTP_RES_BODY_REGION = Configurations.getStringProperty("http_res_body_region","http_res_body");
	public static final String HTTP_URL_REGION = Configurations.getStringProperty("http_url_region","http_url"); 
	public static final String VOIP_IP_REGION = Configurations.getStringProperty("voip_ip","av_voip_ip"); 
	public static final String VOIP_ACCOUNT_REGION = Configurations.getStringProperty("voip_account","av_voip_account");
	public static final String MAAT_VERSION = Configurations.getStringProperty("maat_version","1.0");
	public static final Integer INSERT_ACTION = Configurations.getIntProperty("insert_action",1);
	public static final Integer UPDATE_ACTION = Configurations.getIntProperty("update_action",2);
	public static final Integer DELETE_ACTION = Configurations.getIntProperty("delete_action",3);
	public static final String SSL_IP_REGION = Configurations.getStringProperty("ssl_ip_region","ssl_ip"); 
	public static final String SSL_SNI_REGION = Configurations.getStringProperty("ssl_sni_region","ssl_sni"); 
	public static final String SSL_SAN_REGION = Configurations.getStringProperty("ssl_san_region","ssl_san");
	public static final String SSL_CA_REGION = Configurations.getStringProperty("ssl_ca_region","ssl_ca"); 
	public static final String PROTOCOL_NUM_REGION = Configurations.getStringProperty("protocol_num_region","protocol_num"); 
	public static final String BGP_IP_REGION = Configurations.getStringProperty("bgp_ip_region","bgp_ip"); 
	public static final String KEYWORD_EXPR = Configurations.getStringProperty("keyword_expr","***and***");
	//用户自定义域分隔符
	public static final String USER_REGION_SPLIT = Configurations.getStringProperty("user_region_split", ";");
	//用户自定义域占位符
	public static final String USER_REGION_PLACEHOLDER = Configurations.getStringProperty("user_region_placeholder", ";;");
	//HTTP REPLACE USER_REGION ZONE KEY 
	public static final String REPLACE_TYPE_KEY = Configurations.getStringProperty("userregion_replace_type_key", "zone");
	//HTTP REPLACE USER_REGION ZONE REQ KEY VALUE
	public static final String REPLACE_REQ_KEY_VALUE = Configurations.getStringProperty("userregion_replace_req_key_value", "http_req_replace_body");
	//HTTP REPLACE USER_REGION ZONE RES KEY VALUE
	public static final String REPLACE_RES_KEY_VALUE = Configurations.getStringProperty("userregion_replace_res_key_value", "http_res_replace_body");
	//HTTP REPLACE USER_REGION REGEX KEY
	public static final String REPLACE_REGEX_KEY = Configurations.getStringProperty("userregion_replace_regex_key", "regex");
	
	//HTTP重定向配置域
	public static final String HTTP_REDIRECT_URL_REGION = Configurations.getStringProperty("http_redirect_url_region","PXY_CTRL_HTTP_URL"); 
	public static final String HTTP_REDIRECT_REQ_HEAD_REGION = Configurations.getStringProperty("http_redirect_req_hdr_region","PXY_CTRL_HTTP_REQ_HDR"); 
	public static final String HTTP_REDIRECT_RES_HEAD_REGION = Configurations.getStringProperty("http_redirect_res_hdr_region","PXY_CTRL_HTTP_RES_HDR");
	public static final String HTTP_REDIRECT_REQ_BODY_REGION = Configurations.getStringProperty("http_redirect_req_body_region","PXY_CTRL_HTTP_REQ_BODY"); 
	public static final String HTTP_REDIRECT_RES_BODY_REGION = Configurations.getStringProperty("http_redirect_res_body_region","PXY_CTRL_HTTP_RES_BODY");
	public static final String HTTP_REDIRECT_IP_REGION = Configurations.getStringProperty("http_redirect_ip_region","PXY_CTRL_HTTP_IP");
	public static final String REDIRECT_RESPONSE_CODE_KEY = Configurations.getStringProperty("redirect_response_code_key","code");
	public static final String REDIRECT_URL_KEY = Configurations.getStringProperty("redirect_url_key","url");
	public static final String REDIRECT_CONTENT_KEY = Configurations.getStringProperty("redirect_content_key","content");
	public static final String CONTENT_FILE_ID_KEY = Configurations.getStringProperty("content_file_id_key","content_file_id");
	public static final String REDIRECT_RESPONSE_CODE_STARTWITH = Configurations.getStringProperty("redirect_response_code_startwith","30");
	public static final String REPLACE_ZONE_KEY = Configurations.getStringProperty("replace_zone_key","zone");
	public static final String REPLACE_SUBSTITUTE_KEY = Configurations.getStringProperty("replace_substitute_key","substitute");
	//P2P配置域
	public static final String P2P_HASH_BIN_REGION = Configurations.getStringProperty("p2p_hash_bin_region","NTC_P2P_HASH_BIN"); 
	public static final String P2P_KEYWORDS_REGION = Configurations.getStringProperty("p2p_keywords_region","NTC_P2P_KEYWORDS");
	public static final String NTC_SUBSCRIBE_ID_REGION = Configurations.getStringProperty("ntc_subscribe_id_region","NTC_SUBSCRIBE_ID");
	public static final String PXY_CRTL_SUBSCRIBE_ID_REGION = Configurations.getStringProperty("pxy_crtl_subscribe_id_region","PXY_CRTL_SUBSCRIBE_ID");
	/**
	 * 样例文件URL关键字
	 */
	public static final String SAMPLE_UPLOAD_URL_KEYWORD = Configurations.getStringProperty("sample_upload_url_keyword","av");
	/**
	 * 摘要文件URL关键字
	 */
	public static final String DIGEST_UPLOAD_URL_KEYWORD = Configurations.getStringProperty("digest_upload_url_keyword","fileTransfer"); 
	/**
	 * 证书文件URL关键字
	 */
	public static final String CERT_UPLOAD_URL_KEYWORD = Configurations.getStringProperty("cert_upload_url_keyword","/intercept/strateagy"); 
	/**
	 * 样例文件限制总文件大小
	 */
	public static final long SAMPLE_TOTAL_FILE_MAX_SIZE = Configurations.getLongProperty("sample_total_file_max_size",52428800l);//50M
	/**
	 * 音频文件支持文件类型
	 */
	public static final String AUDIO_FILE_TYPE = Configurations.getStringProperty("audio_file_type","");
	/**
	 * 音频文件限制单个文件大小
	 */
	public static final long AUDIO_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("audio_single_file_max_size",10485760l);//10M
	/**
	 * 说话人识别音频文件支持文件类型
	 */
	public static final String SPEAKER_FILE_TYPE = Configurations.getStringProperty("speaker_file_type","");
	/**
	 * 说话人识别音频文件限制单个文件大小
	 */
	public static final long SPEAKER_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("speaker_single_file_max_size",10485760l);//10M
	/**
	 * 视频文件支持文件类型
	 */
	public static final String VIDEO_FILE_TYPE = Configurations.getStringProperty("video_file_type","");
	/**
	 * 视频文件限制单个文件大小
	 */
	public static final long VIDEO_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("video_single_file_max_size",10485760l);//10M
	/**
	 *人脸识别视频文件支持文件类型
	 */
	public static final String FACE_FILE_TYPE = Configurations.getStringProperty("face_file_type","");
	/**
	 * 视频文件限制单个文件大小
	 */
	public static final long FACE_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("face_single_file_max_size",10485760l);//10M
	/**
	 * 图片文件支持文件类型
	 */
	public static final String PICTURE_FILE_TYPE = Configurations.getStringProperty("picture_file_type","");
	/**
	 * 图片文件限制单个文件大小
	 */
	public static final long PICTURE_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("picture_single_file_max_size",10485760l);//10M
	/**
	 * 图片文件支持文件类型
	 */
	public static final String LOGO_FILE_TYPE = Configurations.getStringProperty("logo_file_type","");
	/**
	 * 图片文件限制单个文件大小
	 */
	public static final long LOGO_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("logo_single_file_max_size",10485760l);//10M
	/**
	 * voip文件支持文件类型
	 */
	public static final String VOIP_FILE_TYPE = Configurations.getStringProperty("voip_file_type","");
	/**
	 * voip文件限制单个文件大小
	 */
	public static final long VOIP_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("voip_single_file_max_size",10485760l);//10M
	/**
	 * 摘要文件支持文件类型
	 */
	public static final String DIGEST_FILE_TYPE = Configurations.getStringProperty("digest_file_type","");
	/**
	 * 公钥文件支持文件类型
	 */
	public static final String PUBLIC_FILE_TYPE = Configurations.getStringProperty("public_file_type","");
	/**
	 * 私钥文件支持文件类型
	 */
	public static final String PRIVATE_FILE_TYPE = Configurations.getStringProperty("private_file_type","");
	/**
	 * 摘要文件限制单个文件大小
	 */
	public static final long DIGEST_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("digest_single_file_max_size",10485760l);//10M
	/**
	 * 摘要文件限制总文件大小
	 */
	public static final long DIGEST_TOTAL_FILE_MAX_SIZE = Configurations.getLongProperty("digest_total_file_max_size",52428800l);//50M
	/**
	 * 证书文件限制总文件大小
	 */
	public static final long CERT_TOTAL_FILE_MAX_SIZE = Configurations.getLongProperty("cert_total_file_max_size",52428800l);//50M
	
	/**
	 * 公钥文件限制单个文件大小
	 */
	public static final long PUBLIC_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("public_single_file_max_size",10485760l);//10M
	/**
	 * 私钥文件限制单个文件大小
	 */
	public static final long PRIVATE_SINGLE_FILE_MAX_SIZE = Configurations.getLongProperty("private_single_file_max_size",10485760l);//10M
	//YSP文件特征配置相关参数，用于调用外部程序生成特征文件时使用
	public static final String AV_FILE_PATH = Configurations.getStringProperty("av_file_path", "/home/ysp/");
	public static final String AV_SAMPLE_AUDIO_REGION = Configurations.getStringProperty("av_sample_audio_region", "av_sample_audio");
	public static final String AV_SAMPLE_VIDEO_REGION = Configurations.getStringProperty("av_sample_video_region", "av_sample_video");
	public static final String AV_SAMPLE_PICTURE_REGION = Configurations.getStringProperty("av_sample_picture_region", "av_sample_picture");
	public static final String AV_SAMPLE_VOIP_REGION = Configurations.getStringProperty("av_sample_voip_region", "av_sample_voip");
	public static final String AUDIO_SAMPLE_CREATE_PROC = Configurations.getStringProperty("audio_sample_create_proc", "./audio_convert_proc");
	public static final String VIDEO_SAMPLE_CREATE_PROC = Configurations.getStringProperty("video_sample_create_proc", "./video_convert_proc");
	public static final String PICTURE_SAMPLE_CREATE_PROC = Configurations.getStringProperty("picture_sample_create_proc", "./picture_convert_proc");
	public static final boolean AUDIO_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("audio_sample_proc_param_is_quotation", true);
	public static final boolean VIDEO_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("video_sample_proc_param_is_quotation", true);
	public static final boolean PICTURE_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("picture_sample_proc_param_is_quotation", true);
	public static final boolean AUDIO_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("audio_sample_proc_param_is_translation", false);
	public static final boolean VIDEO_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("video_sample_proc_param_is_translation", false);
	public static final boolean PICTURE_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("picture_sample_proc_param_is_translation", false);
	public static final String MM_SPEAKER_RECOGNIZATION_REGION = Configurations.getStringProperty("mm_speaker_recognization_region", "MM_SPEAKER_RECOGNIZATION");
	public static final String MM_LOGO_DETECTION_REGION = Configurations.getStringProperty("mm_logo_detection_region", "MM_LOGO_DETECTION");
	public static final String MM_FACE_RECOGNIZATION_REGION = Configurations.getStringProperty("mm_face_recognization_region", "MM_FACE_RECOGNIZATION");
	public static final String SPEAKER_SAMPLE_CREATE_PROC = Configurations.getStringProperty("speaker_sample_create_proc", "./audio_convert_proc");
	public static final String LOGO_SAMPLE_CREATE_PROC = Configurations.getStringProperty("logo_sample_create_proc", "./picture_convert_proc");
	public static final String FACE_SAMPLE_CREATE_PROC = Configurations.getStringProperty("face_sample_create_proc", "./video_convert_proc");
	public static final boolean SPEAKER_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("speaker_sample_proc_param_is_quotation", true);
	public static final boolean LOGO_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("logo_sample_proc_param_is_quotation", true);
	public static final boolean FACE_SAMPLE_PROC_PARAM_IS_QUOTATION = Configurations.getBooleanProperty("face_sample_proc_param_is_quotation", true);
	public static final boolean SPEAKER_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("speaker_sample_proc_param_is_translation", false);
	public static final boolean LOGO_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("logo_sample_proc_param_is_translation", false);
	public static final boolean FACE_SAMPLE_PROC_PARAM_IS_TRANSLATION = Configurations.getBooleanProperty("face_sample_proc_param_is_translation", false);
	//视频文件生成关键帧图片相关参数
	public static final String VEDIO_TO_PICTURE_PROC = Configurations.getStringProperty("video_to_picture_proc", "./video_to_picture_proc");
	
	//HTTP自定义域相关参数
	public static String HTTP_HEADER_USER_REGION_KEY=Configurations.getStringProperty("http_header_user_region_key", "HTTP_HEADER");
	public static String HTTP_HEADER_DICT_MODULE=Configurations.getStringProperty("http_header_dict_module", "HTTP_HEADER_DISTRICT");
	//DNS自定义域参数
	public static String DNS_STRATEGY_USER_REGION_KEY=Configurations.getStringProperty("dns_strategy_user_region_key", "DNS_STRATEGY");
	//P2P自定义域参数
	public static String P2P_IP_TYPE_USER_REGION_KEY=Configurations.getStringProperty("p2p_ip_type_user_region_key", "P2P_IP_TYPE");
	public static String P2P_HASH_TYPE_USER_REGION_KEY=Configurations.getStringProperty("p2p_hash_type_user_region_key", "P2P_HASH_TYPE");
	//PROXY INTERCEPT IP自定义域参数
	public static String INTERCEPT_IP_MONIT_USER_REGION_KEY=Configurations.getStringProperty("keyring_id", "keyring_id");
	public static String INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY=Configurations.getStringProperty("Droprate", "Droprate");
	public static String INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY=Configurations.getStringProperty("Bandwidth", "Bandwidth");
	public static String INTERCEPT_DOMAN_INTENSITY_USER_REGION_KEY=Configurations.getStringProperty("intercept_domain_intensity", "DOMAIN_INTENSITY");
	//APP VIGNORE
	public static String APP_VIGNORE_USER_REGION_KEY=Configurations.getStringProperty("app_vignore_user_region_key", "VIGNORE");
	
	//大屏统计分析查询接口URL
	public static final String DASHBOARD_URL = Configurations.getStringProperty("dashboardUrl","dashboardUrl");
	public static final String TRAFFIC_IPACTIVE=Configurations.getStringProperty("trafficIpActive","trafficIpActive");
	public static final String TRAFFIC_PROTOCOL=Configurations.getStringProperty("trafficProtocol","trafficProtocol");
	public static final String TRAFFIC_APP=Configurations.getStringProperty("trafficApp","trafficApp");
	public static final String TRAFFIC_OSLIST=Configurations.getStringProperty("trafficOsList","trafficOsList");
	public static final String TRAFFIC_BROWSER_CHART=Configurations.getStringProperty("trafficBrowserChart","trafficBrowserChart");
	public static final String TRAFFIC_BSLISTL=Configurations.getStringProperty("trafficBsList","trafficBsList");
	public static final String TRAFFIC_SYSTEM_CHART=Configurations.getStringProperty("trafficSystemChart","trafficSystemChart");
	public static final String TRAFFIC_WEBSITELIST=Configurations.getStringProperty("trafficWebsiteList","trafficWebsiteList");
	public static final String TRAFFIC_WEBTYPECHART=Configurations.getStringProperty("trafficWebTypeChart","trafficWebTypeChart");
	public static final String TRAFFIC_PORT_ACTIVE=Configurations.getStringProperty("trafficPortActive","trafficPortActive");
	public static final String TRAFFIC_TOPIC_AND_DOMAIN_CHART=Configurations.getStringProperty("trafficTopicAndDomainChart","trafficTopicAndDomainChart");
	public static final String NTC_TOTAL_REPORT=Configurations.getStringProperty("ntcTotalReport","ntcTotalReport");
	public static final String TRAFFIC_IPACTIVE_FIVEMINUTE=Configurations.getStringProperty("trafficIpActiveFiveMinute","trafficIpActiveFiveMinute");
	public static final String TRAFFIC_IPACTIVE_ONEHOUR=Configurations.getStringProperty("trafficIpActiveOneHour","trafficIpActiveOneHour");
	public static final String NTC_RADIUS_REPORT=Configurations.getStringProperty("ntcRadiusReport","ntcRadiusReport");
	public static final String TRAFFIC_BANDWIDTH_TRANS=Configurations.getStringProperty("trafficBandwidthTrans","trafficBandwidthTrans");
	public static final String TRAFFIC_PROTOCOL_LIST=Configurations.getStringProperty("trafficProtocolList","trafficProtocolList");
	public static final String TRAFFIC_APP_LIST=Configurations.getStringProperty("trafficAppList","trafficAppList");
	public static final String NTC_ACTION_ENTRANCE_REPORT=Configurations.getStringProperty("ntcActionEntranceReport","ntcActionEntranceReport");
	/**
	 * httpclient 工具超时时间设置
	 */
	public static final int  HTTP_SOCKET_TIMEOUT= Configurations.getIntProperty("http_socket_timeout", 300000);
	public static final int  HTTP_CONNECT_TIMEOUT= Configurations.getIntProperty("http_connect_timeout", 10000);
	public static final int HTTP_CONNECT_REQUEST_TIMEOUT = Configurations.getIntProperty("http_connect_request_timeout", 50000);
	public static final int HTTP_CONNECT_RETRY_TIMES = Configurations.getIntProperty("http_connect_retry_times", 3);
	
	// 区域/运营商配置下发时tag值
	public static final String AREA_TAG=Configurations.getStringProperty("area_tag","location");
	public static final String ISP_TAG=Configurations.getStringProperty("isp_tag","isp");
	
	//音视频样例时长限制，单位秒
	public static final int AV_DURATION_LIMIT=Configurations.getIntProperty("av_duration_limit", 120);
	//证书文件存放路径
	public static final String CERT_FILE_PATH=Configurations.getStringProperty("cert_file_path", "");
	//证书校验文件
	public static final String CERT_VALIDATE_FILE=Configurations.getStringProperty("cert_validate_file", "x509");
	//证书校验成功关键字
	public static final String CERT_VALIDATE_SUCCESS_INFO=Configurations.getStringProperty("cert_validate_success_info", "x509");
	/**
	 * 一些正则的pattern，预先编译,避免多次编译
	 */
	public static final Pattern IPV4_IP_RANGE_REGEXP_NEW_PATTERN = Pattern.compile(IPV4_IP_RANGE_REGEXP_NEW);
	public static final Pattern RANGE_PATTERN = Pattern.compile("\\d+\\-\\d+");
	public static final Pattern PORT_MASK_PATTERN = Pattern.compile("^[0-9]+/[0-9]+$");
	public static final Pattern PORT_PATTERN = Pattern.compile("^[0-9]+$");
	public static final Pattern IPV4_IP_SUBNET_ORIGINAL_PATTERN = Pattern.compile(IPV4_IP_SUBNET_REGEXP_ORIGINAL);
	public static final Pattern IPV4_IP_SUBNET_PATTERN = Pattern.compile(IPV4_IP_SUBNET_REGEXP);
	public static final Pattern IPV6_IP_SUBNET_PATTERN = Pattern.compile(IPV6_IP_SUBNET_REGEXP);
	public static final Pattern IPV4_IP_RANGE_PATTERN = Pattern.compile(IPV4_IP_RANGE_REGEXP);
	public static final Pattern IPV6_IP_RANGE_PATTERN = Pattern.compile(IPV6_IP_RANGE_REGEXP);
	public static final Pattern IPV4_IP_PATTERN = Pattern.compile(IPV4_IP_REGEXP);
	public static final Pattern IPV6_IP_PATTERN = Pattern.compile(IPV6_IP_REGEXP);
	//IP复用maat json中的ip region单次send 最大个数
	public static final Integer MAAT_JSON_SEND_SIZE=Configurations.getIntProperty("maat_json_send_size", 1000);
	public static final Integer MULITY_THREAD_SIZE=Configurations.getIntProperty("mulity_thread_size", 5);
	public static final Integer SAVE_AND_DEL_THREAD_SIZE=Configurations.getIntProperty("save_and_del_thread_size", 5);
	public static final Integer MAX_ALLOWED_PACKET=Configurations.getIntProperty("max_allowed_packet", 1048576);
	
	//http client连接池相关参数
	public static final Integer HTTP_MAX_CONNECTION=Configurations.getIntProperty("http_max_connection", 1000);
	public static final Integer DEFAULT_MAX_PERROUTE=Configurations.getIntProperty("default_max_perroute", 100);
	public static final Integer POOLCM_SOCKET_TIMEOUT=Configurations.getIntProperty("poolcm_socket_timeout", 3000);
	
	public static final String POLICIES_MENU_ID=Configurations.getStringProperty("policies_menu_id", "86");
	
	public static final String NTC_IP_REUSE_USER_CREATE = Configurations.getStringProperty("userCreate","");
	public static final String NTC_IP_REUSE_USER_PASSWORD = Configurations.getStringProperty("userPasswordSet","");
	public static final String NTC_IP_REUSE_USER_DELETE = Configurations.getStringProperty("userDelete","");
	public static final String NTC_IP_REUSE_USER_GET = Configurations.getStringProperty("userGet","");
	public static final String NTC_IP_REUSE_USER_LIST = Configurations.getStringProperty("userList","");
	
	// IP复用模块调用相关CGI接口URL
	public static final String IP_REUSE_CALL_CGI_URL = Configurations.getStringProperty("ip_reuse_call_cgi_url","");
	public static final String ALL_IP_GET = Configurations.getStringProperty("allIpGet","AllIpGet");
	public static final String IP_NUM_GET = Configurations.getStringProperty("ipNumGet","IpNumGet");
	//日志查询接口URL
	public static final String IR_SNAT_LOG = Configurations.getStringProperty("irSnatLog","");
	public static final String IR_DNAT_LOG = Configurations.getStringProperty("irDnatLog","");
}
