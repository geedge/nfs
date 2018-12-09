package com.nis.web.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.eclipse.jetty.util.ajax.JSON;
import org.jets3t.service.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.callback.ProxyObjKeyring;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppFeatureIndex;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.AreaBean;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexStringCfgTemplate;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.configuration.RequestInfo;
import com.nis.domain.configuration.StringCfgTemplate;
import com.nis.domain.configuration.template.AppDomainTemplate;
import com.nis.domain.configuration.template.AsnIpTemplate;
import com.nis.domain.configuration.template.ComplexStringAllTemplate;
import com.nis.domain.configuration.template.DdosIpTemplate;
import com.nis.domain.configuration.template.DnsComplexStringTemplate;
import com.nis.domain.configuration.template.DnsIpTemplate;
import com.nis.domain.configuration.template.DnsResStrategyTemplate;
import com.nis.domain.configuration.template.IpAllTemplate;
import com.nis.domain.configuration.template.IpCfgTemplate;
import com.nis.domain.configuration.template.IpPayloadTemplate;
import com.nis.domain.configuration.template.IpRateLimitTemplate;
import com.nis.domain.configuration.template.P2pHashStringTemplate;
import com.nis.domain.configuration.template.P2pIpTemplate;
import com.nis.domain.configuration.template.StringAllTemplate;
import com.nis.domain.log.BaseLogEntity;
import com.nis.domain.log.SearchReport;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Configurations;
//import com.nis.main.ConvertTool;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.ServiceConfigTemplateUtil;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.util.excel.ImportBigExcel;
import com.nis.util.excel.thread.CheckComplexStringFormatThread;
import com.nis.util.excel.thread.CheckDnsResStrategyFormatThread;
import com.nis.util.excel.thread.CheckIpFormatThread;
import com.nis.util.excel.thread.CheckStringFormatThread;
import com.nis.web.security.UserUtils;
import com.nis.web.service.ArchiveServcie;
import com.nis.web.service.AreaService;
import com.nis.web.service.BaseService;
import com.nis.web.service.CrudService;
import com.nis.web.service.DictService;
import com.nis.web.service.LogService;
import com.nis.web.service.MenuService;
import com.nis.web.service.OfficeService;
import com.nis.web.service.RoleService;
import com.nis.web.service.SystemService;
import com.nis.web.service.UserService;
import com.nis.web.service.basics.AsnIpCfgService;
import com.nis.web.service.basics.IpReuseIpCfgService;
import com.nis.web.service.basics.PolicyGroupInfoService;
import com.nis.web.service.basics.ServiceDictInfoService;
import com.nis.web.service.basics.SysDictInfoService;
import com.nis.web.service.configuration.AppBuiltInFeatureService;
import com.nis.web.service.configuration.AppCfgService;
import com.nis.web.service.configuration.AppMultiFeatureCfgService;
import com.nis.web.service.configuration.AsnPolicyCfgService;
import com.nis.web.service.configuration.AvCfgService;
import com.nis.web.service.configuration.AvContentCfgService;
import com.nis.web.service.configuration.BgpCfgService;
import com.nis.web.service.configuration.CachePolicyService;
import com.nis.web.service.configuration.CommonPolicyService;
import com.nis.web.service.configuration.ComplexStringCfgService;
import com.nis.web.service.configuration.ConfigSynchronizationService;
import com.nis.web.service.configuration.ControlPolicyService;
import com.nis.web.service.configuration.DdosCfgService;
import com.nis.web.service.configuration.DnsIpCfgService;
import com.nis.web.service.configuration.DnsResStrategyService;
import com.nis.web.service.configuration.DomainService;
import com.nis.web.service.configuration.FileTransferCfgService;
import com.nis.web.service.configuration.GroupAreaService;
import com.nis.web.service.configuration.HttpRedirectCfgService;
import com.nis.web.service.configuration.InterceptCfgService;
import com.nis.web.service.configuration.IpAddrPoolCfgService;
import com.nis.web.service.configuration.IpCfgService;
import com.nis.web.service.configuration.IpMultiplexPoolCfgService;
import com.nis.web.service.configuration.MailCfgService;
import com.nis.web.service.configuration.NumCfgService;
import com.nis.web.service.configuration.ProxyFileStrategyService;
import com.nis.web.service.configuration.PxyObjKeyringService;
import com.nis.web.service.configuration.RequestInfoService;
import com.nis.web.service.configuration.UserManageService;
import com.nis.web.service.configuration.WebsiteCfgService;
import com.nis.web.service.configuration.XmppCfgService;
import com.nis.web.service.configuration.statistics.ConfigureStatisticsService;
import com.nis.web.service.specific.ConfigGroupInfoService;
import com.nis.web.service.specific.SpecificServiceCfgService;
import com.nis.web.service.specific.SpecificServiceHostCfgService;
import com.nis.web.service.systemService.ServiceConfigInfoService;

import jersey.repackaged.com.google.common.collect.Maps;
import net.sf.json.JSONObject;

public class BaseController {

	@Autowired
	protected UserService userService;

	@Autowired
	protected SystemService systemService;

	@Autowired
	protected OfficeService officeService;

	@Autowired
	protected AreaService areaService;

	@Autowired
	protected RoleService roleService;

	@Autowired
	protected MenuService menuService;

	@Autowired
	protected LogService logService;

	@Autowired
	protected ArchiveServcie archiveServcie;

	@Autowired
	protected DictService dictService;

	@Autowired
	protected IpCfgService ipCfgService;

	@Autowired
	protected DnsIpCfgService dnsIpCfgService;

	@Autowired
	protected MailCfgService mailCfgService;

	@Autowired
	protected FileTransferCfgService fileTransferCfgService;

	@Autowired
	protected AppCfgService appCfgService;
	@Autowired
	protected ControlPolicyService controlPolicyService;

	@Autowired
	protected ServiceDictInfoService serviceDictInfoService;

	@Autowired
	protected SysDictInfoService sysDictInfoService;

	protected final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	protected ServiceConfigInfoService serviceConfigInfoService;

	@Autowired
	protected RequestInfoService requestInfoService;
	@Autowired
	protected DomainService domainService;
	@Autowired
	protected NumCfgService numCfgService;
	@Autowired
	protected ComplexStringCfgService complexStringCfgService;
	@Autowired
	protected SpecificServiceCfgService specificServiceCfgService;
	@Autowired
	protected SpecificServiceHostCfgService specificServiceHostCfgService;
	@Autowired
	protected AvCfgService avCfgService;
	@Autowired
	protected AvContentCfgService avContentCfgService;
	@Autowired
	protected WebsiteCfgService websiteCfgService;
	@Autowired
	protected BgpCfgService bgpCfgService;
	@Autowired
	protected XmppCfgService xmppCfgService;
	@Autowired
	protected PolicyGroupInfoService policyGroupInfoService;
	@Autowired
	protected DnsResStrategyService dnsResStrategyService;
	@Autowired
	protected HttpRedirectCfgService httpRedirectCfgService;
	@Autowired
	protected DdosCfgService ddosCfgService;
	@Autowired
	protected IpMultiplexPoolCfgService ipMultiplexPoolCfgService;
	@Autowired
	protected ConfigureStatisticsService configureStatisticsService;
	@Autowired
	protected InterceptCfgService interceptCfgService;
	@Autowired
	protected ProxyFileStrategyService proxyFileStrategyService;// 代理文件策略service
	@Autowired
	protected PxyObjKeyringService pxyObjKeyringService;// 拦截策略service
	@Autowired
	protected AppBuiltInFeatureService appBuiltInFeatureService;// 拦截策略service
	@Autowired
	protected AsnIpCfgService asnIpCfgService;// asn ip service
	@Autowired
	protected AsnPolicyCfgService asnPolicyCfgService;// asn 策略 service
	@Autowired
	protected AppMultiFeatureCfgService appMultiFeatureCfgService;
	@Autowired
	protected GroupAreaService groupAreaService;// 分组区域管理 service
	//@Autowired
	//protected ConfigGroupInfoService configGroupInfoService;
	@Autowired
	protected ConfigSynchronizationService configSynchronizationService;
	@Autowired
	protected CachePolicyService cachePolicyService;
	@Autowired
	protected CommonPolicyService commonPolicyService;
	
	@Autowired
	protected UserManageService userManageService;
	@Autowired
	protected IpReuseIpCfgService ipReuseIpCfgService;
	@Autowired
	protected IpAddrPoolCfgService ipAddrPoolCfgService;// 地址池管理service
	@Autowired
	protected ConfigGroupInfoService configGroupInfoService;// 分组复用
	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;
	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 添加Model消息
	 * 
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Model消息 日志用
	 * 
	 * @param message
	 */
	protected void addMessageLog(Model model, String status) {
		// status 接口状态码
		String messages = "search_error";
		if (null != status) {
			if ("400".equals(status)) {
				messages = "data_format_error";
			} else if ("500".equals(status)) {
				messages = "server_internal_error";
			} else if (status.indexOf("request_service_failed") != -1) {
				messages = "server_internal_error";
			}
		}
		model.addAttribute("message", messages);
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param message
	 * @param messageType 消息类型：info、success、warning、error、loading
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,String messageType, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
		redirectAttributes.addFlashAttribute("messageType", messageType);
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	protected void initPageCondition(Model model) {
		List<RequestInfo> requestInfos = requestInfoService.getAllRequestInfo();
		model.addAttribute("requestInfos", requestInfos);
		List<ServiceDictInfo> fls = serviceDictInfoService.findAllFlDict();
		String pNames = "";
		for (ServiceDictInfo serviceDictInfo : fls) {
			pNames = "";
			pNames = serviceDictInfo.getPNames(fls, serviceDictInfo.getParent().getServiceDictId(), pNames);
			serviceDictInfo.setpNames(pNames);
		}
		model.addAttribute("fls", fls);
		List<ServiceDictInfo> xzs = serviceDictInfoService.findAllXzDict();
		for (ServiceDictInfo serviceDictInfo : xzs) {
			pNames = "";
			pNames = serviceDictInfo.getPNames(xzs, serviceDictInfo.getParent().getServiceDictId(), pNames);
			serviceDictInfo.setpNames(pNames);
		}
		model.addAttribute("xzs", xzs);
		List<ServiceDictInfo> lables = serviceDictInfoService.findAllLableDict();
		for (ServiceDictInfo serviceDictInfo : lables) {
			pNames = "";
			pNames = serviceDictInfo.getPNames(lables, serviceDictInfo.getParent().getServiceDictId(), pNames);
			serviceDictInfo.setpNames(pNames);
		}
		model.addAttribute("lables", lables);
	}

	protected void initPageCondition(Model model, BaseCfg cfg) {
		List<RequestInfo> requestInfos = requestInfoService.getAllRequestInfo();
		model.addAttribute("requestInfos", requestInfos);
		List<ServiceDictInfo> fls = serviceDictInfoService.findAllFlDict();
		String pNames = "";
		/*
		 * for (ServiceDictInfo serviceDictInfo : fls) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(fls,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("fls", fls);
		List<ServiceDictInfo> xzs = serviceDictInfoService.findAllXzDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : xzs) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(xzs,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("xzs", xzs);
		List<ServiceDictInfo> lables = serviceDictInfoService.findAllLableDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : lables) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(lables,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("lables", lables);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		model.addAttribute("regionList", regionList);
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(cfg.getFunctionId());
		model.addAttribute("serviceList", serviceList);
	}

	protected void initReportCondition(Model model, SearchReport report) {
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(report.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		if (StringUtils.isBlank(report.getReportTime())) {
			report.setReportTime(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		}
		if (report.getReportType() == null) {
			report.setReportType(1);
		}
	}

	protected void initFormCondition(Model model) {
		List<RequestInfo> requestInfos = requestInfoService.getValidRequestInfo();
		model.addAttribute("requestInfos", requestInfos);
		List<ServiceDictInfo> fls = serviceDictInfoService.findFlDict();
		String pNames = "";
		/*
		 * for (ServiceDictInfo serviceDictInfo : fls) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(fls,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("fls", fls);
		List<ServiceDictInfo> xzs = serviceDictInfoService.findXzDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : xzs) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(xzs,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("xzs", xzs);
		List<ServiceDictInfo> lables = serviceDictInfoService.findLableDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : lables) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(lables,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("lables", lables);
		List<Integer> itTypeList = new ArrayList<Integer>();
		itTypeList.add(Constants.ITEM_TYPE_AREA);
		List<SysDictInfo> areas = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("areas", areas);
		itTypeList.clear();
		itTypeList.add(Constants.ITEM_TYPE_ISP);
		List<SysDictInfo> isps = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("isps", isps);
		// 初始化一个默认的地域ip给界面
		AreaIpCfg areaIpCfg = new AreaIpCfg();
		areaIpCfg.initDefaultValue();
		model.addAttribute("_areaCfg", areaIpCfg);
	}

	protected void initFormCondition(Model model, BaseCfg cfg) {
		List<RequestInfo> requestInfos = requestInfoService.getValidRequestInfo();
		model.addAttribute("requestInfos", requestInfos);
		List<ServiceDictInfo> fls = serviceDictInfoService.findFlDict();
		String pNames = "";
		/*
		 * for (ServiceDictInfo serviceDictInfo : fls) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(fls,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("fls", fls);
		List<ServiceDictInfo> xzs = serviceDictInfoService.findXzDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : xzs) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(xzs,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("xzs", xzs);
		List<ServiceDictInfo> lables = serviceDictInfoService.findLableDict();
		/*
		 * for (ServiceDictInfo serviceDictInfo : lables) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(lables,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("lables", lables);
		List<Integer> itTypeList = new ArrayList<Integer>();
		itTypeList.add(Constants.ITEM_TYPE_AREA);
		List<SysDictInfo> areas = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("areas", areas);
		itTypeList.clear();
		itTypeList.add(Constants.ITEM_TYPE_ISP);
		List<SysDictInfo> isps = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("isps", isps);
		// 初始化一个默认的地域ip给界面
		AreaIpCfg areaIpCfg = new AreaIpCfg();
		areaIpCfg.initDefaultValue();
		model.addAttribute("_areaCfg", areaIpCfg);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		model.addAttribute("regionList", regionList);
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(cfg.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		// ip配置设置protocolId
		if (cfg instanceof BaseIpCfg) {
			for (FunctionServiceDict dict : serviceList) {
				if (cfg.getFunctionId().intValue() == dict.getFunctionId().intValue()) {
					((BaseIpCfg) cfg).setProtocolId(dict.getProtocolId().intValue());
					break;
				}
			}
		}
	}

	protected void initUpdateFormCondition(Model model, BaseCfg cfg) {
		List<Integer> itTypeList = new ArrayList<Integer>();
		itTypeList.add(Constants.ITEM_TYPE_AREA);
		List<SysDictInfo> areas = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("areas", areas);
		itTypeList.clear();
		itTypeList.add(Constants.ITEM_TYPE_ISP);
		List<SysDictInfo> isps = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(), itTypeList, null);
		model.addAttribute("isps", isps);

		// 查询配置对应的地域ip
		if (cfg.getCompileId() != null) {
			List<AreaIpCfg> areaCfgs = ipCfgService.getAreaCfgByCompileId(cfg.getCompileId());
			model.addAttribute("areaCfgs", areaCfgs);

			StringBuffer areaCfgIds = new StringBuffer();
			for (AreaIpCfg areaCfg : areaCfgs) {
				areaCfgIds.append(areaCfg.getCfgId() + ",");
			}
			// update时判断是否有删除地域ip
			model.addAttribute("areaCfgIds", areaCfgIds.toString());
		}
		// 初始化一个默认的地域ip给界面
		AreaIpCfg areaIpCfg = new AreaIpCfg();
		areaIpCfg.initDefaultValue();
		model.addAttribute("_areaCfg", areaIpCfg);
		// 从区域生效ID中获取区域与运营商
		String areaEffectiveIds = cfg.getAreaEffectiveIds();
		List<AreaBean> areaIspList = new ArrayList<AreaBean>();
		getAreaIsps(areaEffectiveIds, areaIspList, isps, areas);
		model.addAttribute("_areaIsps", areaIspList);
		List<RequestInfo> requestInfos = requestInfoService.getValidRequestInfo();// 只查询有效的
		model.addAttribute("requestInfos", requestInfos);
		List<ServiceDictInfo> fls = serviceDictInfoService.findFlDict();// 只查询有效分类字典
		String pNames = "";
		/*
		 * for (ServiceDictInfo serviceDictInfo : fls) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(fls,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("fls", fls);
		List<ServiceDictInfo> xzs = serviceDictInfoService.findXzDict();// 只查询有效性质字典
		/*
		 * for (ServiceDictInfo serviceDictInfo : xzs) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(xzs,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("xzs", xzs);
		List<ServiceDictInfo> lables = serviceDictInfoService.findLableDict();// 只查询有效标签字典
		/*
		 * for (ServiceDictInfo serviceDictInfo : lables) { pNames = ""; pNames =
		 * serviceDictInfo.getPNames(lables,
		 * serviceDictInfo.getParent().getServiceDictId(), pNames);
		 * serviceDictInfo.setpNames(pNames); }
		 */
		model.addAttribute("lables", lables);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		model.addAttribute("regionList", regionList);
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(cfg.getFunctionId());
		model.addAttribute("serviceList", serviceList);
	}

	public List<AreaBean> getAreaIsps(String areaEffectiveIds, List<AreaBean> areaIspList, List<SysDictInfo> isps,
			List<SysDictInfo> areas) {
		Map<String, SysDictInfo> areaMap = new HashMap<String, SysDictInfo>();
		for (SysDictInfo areaDict : areas) {
			areaMap.put(areaDict.getItemCode(), areaDict);
		}
		Map<String, SysDictInfo> ispMap = new HashMap<String, SysDictInfo>();
		for (SysDictInfo ispDict : isps) {
			ispMap.put(ispDict.getItemCode(), ispDict);
		}

		// ： 存在冒号，则冒号前为area后为isp；不存在冒号，判断id是否为isp，否则为area
		if (StringUtils.isNotBlank(areaEffectiveIds)) {
			String[] areaIsps = areaEffectiveIds.split(",");
			for (String areaIsp : areaIsps) {
				AreaBean area = new AreaBean();
				if (areaIsp.contains(":")) {
					area.setArea(areaIsp.split(":")[0]);
					area.setIsp(areaIsp.split(":")[1]);
					area.setAreaName(areaMap.get(areaIsp.split(":")[0]).getItemValue());
					area.setIspName(ispMap.get(areaIsp.split(":")[1]).getItemValue());
				} else {
					if (ispMap.keySet().contains(areaIsp)) {
						area.setIsp(areaIsp);
						area.setIspName(ispMap.get(areaIsp).getItemValue());
						area.setAreaName("");
					} else {
						area.setArea(areaIsp);
						area.setIspName("");
						area.setAreaName(areaMap.get(areaIsp).getItemValue());
					}

				}
				areaIspList.add(area);
			}
		}
		return areaIspList;
	}

	/**
	 * 获取国际化配置文件
	 * 
	 * @return
	 */
	public Properties getMsgProp() {
		Properties msgProp = new Properties();
		try {
			String language = LocaleContextHolder.getLocale().getLanguage();
			if (language.equals("zh_cn") || language.equals("zh")) {
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
			} else if (language.equals("ru")) {
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
			} else {
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			}

		} catch (Exception e) {
			msgProp = null;
			logger.error("未知i18n消息配置文件,请确定文件是否存在!", e);
		}
		return msgProp;
	}
	@Deprecated
	public void importCfgTemplate(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, Integer functionId, Integer cfgRegionCode) {
		Properties msgProp = getMsgProp();
		try {
			List<FunctionRegionDict> dictList = DictUtils.getFunctionRegionDictList(functionId);
			for (FunctionRegionDict dict : dictList) {
				if (dict.getConfigRegionCode() == cfgRegionCode) {
					String fileName = dict.getConfigRegionValue() + ".xlsx";
					if (dict.getRegionType() == 1) {
						List<IpCfgTemplate> list = Lists.newArrayList();
						list.add(new IpCfgTemplate());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null,
								IpCfgTemplate.class, 2).setDataList(msgProp, list, null)
										.write(request, response, fileName).dispose();
					} else if (dict.getRegionType() == 2) {
						List<StringCfgTemplate> list = Lists.newArrayList();
						list.add(new StringCfgTemplate());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null,
								StringCfgTemplate.class, 2).setDataList(msgProp, list, null)
										.write(request, response, fileName).dispose();
					} else if (dict.getRegionType() == 3) {
						List<ComplexStringCfgTemplate> list = Lists.newArrayList();
						list.add(new ComplexStringCfgTemplate());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null,
								ComplexStringCfgTemplate.class, 2).setDataList(msgProp, list, null)
										.write(request, response, fileName).dispose();
					}
				}
			}

		} catch (Exception e) {
			msgProp = null;
			e.printStackTrace();
		}
	}
	@Deprecated
	public void importCfgTemplate(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, Integer functionId, Integer cfgRegionCode, Class clazz) {
		Properties msgProp = getMsgProp();
		try {
			List<FunctionRegionDict> dictList = DictUtils.getFunctionRegionDictList(functionId);
			for (FunctionRegionDict dict : dictList) {
				if (dict.getConfigRegionCode() == cfgRegionCode) {
					String fileName = dict.getConfigRegionValue() + ".xlsx";
					if (dict.getRegionType() == 1) {
						List<IpCfgTemplate> list = Lists.newArrayList();
						list.add((IpCfgTemplate) clazz.newInstance());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null, clazz, 2)
								.setDataList(msgProp, list, null).write(request, response, fileName).dispose();
					} else if (dict.getRegionType() == 2) {
						List<StringCfgTemplate> list = Lists.newArrayList();
						list.add(new StringCfgTemplate());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null, clazz, 2)
								.setDataList(msgProp, list, null).write(request, response, fileName).dispose();
					} else if (dict.getRegionType() == 3) {
						List<ComplexStringCfgTemplate> list = Lists.newArrayList();
						list.add(new ComplexStringCfgTemplate());
						new ExportExcel(new FunctionServiceDict(), new FunctionRegionDict(), msgProp, null, clazz, 2)
								.setDataList(msgProp, list, null).write(request, response, fileName).dispose();
					}
				}
			}

		} catch (Exception e) {
			msgProp = null;
			e.printStackTrace();
		}
	}

	public void setLogAction(BaseLogEntity log, List<FunctionServiceDict> serviceList) {
		if (!StringUtil.isEmpty(serviceList)) {
			for (FunctionServiceDict dict : serviceList) {
				if (dict.getServiceId().intValue() == log.getService()) {
					log.setAction(dict.getAction());
				}
			}
		}
	}

	// 初始化查询值判断
	public void initLogSearchValue(BaseLogEntity entry, Map<String, Object> params) {

		if (StringUtils.isNotBlank(entry.getSearchFoundStartTime())
				&& StringUtils.isNotBlank(entry.getSearchFoundEndTime())) {
			params.put("searchFoundStartTime", entry.getSearchFoundStartTime());
			params.put("searchFoundEndTime", entry.getSearchFoundEndTime());
		} else {
			// 判断是否是从配置界面过来的日志查询
			if (StringUtils.isNotBlank(entry.getIsLogTotalSearch())) {
				Calendar time = Calendar.getInstance();
				time.add(Calendar.MINUTE, -5);
				String searchEndTime = DateUtils.formatDateTime(time.getTime());
				params.put("searchFoundEndTime", searchEndTime);
				entry.setSearchFoundEndTime(searchEndTime);

				time.add(Calendar.MINUTE, -5);
				String searchStartTime = DateUtils.formatDateTime(time.getTime());
				params.put("searchFoundStartTime", searchStartTime);
				entry.setSearchFoundStartTime(searchStartTime);

				// 判断startTime是否有值
				/*
				 * if(StringUtils.isNotBlank(Constants.LOG_TIME_START)) { String endTime =
				 * Constants.LOG_TIME_END; if(StringUtils.isBlank(endTime)) { endTime =
				 * DateUtils.getDateTime(); } params.put("searchFoundStartTime",
				 * Constants.LOG_TIME_START); params.put("searchFoundEndTime",endTime );
				 * entry.setSearchFoundStartTime(Constants.LOG_TIME_START);
				 * entry.setSearchFoundEndTime(endTime); }else { Calendar startTimeCal =
				 * Calendar.getInstance(); if(StringUtils.isNotEmpty(entry.getDate())) {
				 * startTimeCal.setTimeInMillis(Long.parseLong(entry.getDate()));// 同步日志总量查询时间 }
				 * // 时间间隔 int interval = Constants.LOG_TIME_RANGE/1000/60; if(interval < 60) {
				 * // 查询上一个5分钟 startTimeCal.set(Calendar.SECOND, 0); int minute =
				 * startTimeCal.get(Calendar.MINUTE); startTimeCal.set(Calendar.MINUTE,
				 * minute/interval*interval); }else { // 查询上一个1小时
				 * startTimeCal.set(Calendar.SECOND, 0); startTimeCal.set(Calendar.MINUTE, 0); }
				 * Calendar endTimeCal = Calendar.getInstance();
				 * endTimeCal.setTimeInMillis(startTimeCal.getTimeInMillis());
				 * //endTimeCal.add(Calendar.SECOND, -1);
				 * 
				 * startTimeCal.add(Calendar.MINUTE, -interval); //开始时间减去时间间隔
				 * 
				 * String searchStartTime = DateUtils.formatDateTime(startTimeCal.getTime());
				 * String searchEndTime = DateUtils.formatDateTime(endTimeCal.getTime());
				 * params.put("searchFoundStartTime", searchStartTime);
				 * params.put("searchFoundEndTime",searchEndTime);
				 * entry.setSearchFoundStartTime(searchStartTime);
				 * entry.setSearchFoundEndTime(searchEndTime); }
				 */

			} else {
				// 设置默认查询当前时间及前五分钟
				String endTime = DateUtils.getDateTime();
				Date dateStart = new Date(new Date().getTime() - Constants.LOG_TIME_INTERVAL);
				String startTime = DateUtils.formatDateTime(dateStart);
				params.put("searchFoundStartTime", startTime);
				params.put("searchFoundEndTime", endTime);
				entry.setSearchFoundStartTime(startTime);
				entry.setSearchFoundEndTime(endTime);
			}
			logger.info("searchFoundStartTime：" + params.get("searchFoundStartTime"));
			logger.info("searchFoundEndTime：" + params.get("searchFoundEndTime"));
		}

		if (StringUtils.isNotBlank(entry.getTransProto())) {
			params.put("searchTransProto", entry.getTransProto());
		}
		if (entry.getService() != null) {
			params.put("searchService", entry.getService());
		} else if (entry.getAction() != null) {
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entry.getFunctionId());
			for (FunctionServiceDict dict : serviceList) {
				if (dict.getAction().intValue() == entry.getAction().intValue()
						&& dict.getFunctionId().intValue() == entry.getFunctionId().intValue()) {
					entry.setService(dict.getServiceId());
					params.put("searchService", dict.getServiceId());
				}
			}
		}
		if (StringUtils.isNotBlank(entry.getdIp())) {
			params.put("searchDIp", entry.getdIp());
		}
		if (StringUtils.isNotBlank(entry.getsIp())) {
			params.put("searchSIp", entry.getsIp());
		}
		if (StringUtils.isNotBlank(entry.getCapIp())) {
			params.put("searchCapIp", entry.getCapIp());
		}
		if (entry.getDirection() != null) {
			params.put("searchDirection", entry.getDirection());
		}
		if (entry.getEntranceId() != null) {
			params.put("searchEntranceId", entry.getEntranceId());
		}
		if (entry.getCfgId() != null) {
			params.put("searchCfgId", entry.getCfgId());
		}
		if (StringUtils.isNotBlank(entry.getOrderBy())) {
			params.put("orderBy", entry.getOrderBy());
		}
	}
	/**
	 * 多线程验证
	 * @param serviceDict
	 * @param regionDict
	 * @param asnNos
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public BlockingQueue<BaseIpCfg> checkIpCfgMulity(FunctionServiceDict serviceDict, FunctionRegionDict regionDict,List<Map<Long,Integer>> asnNos, BlockingQueue<? extends Object> list) throws ServiceException, InterruptedException, ExecutionException{
		logger.warn("start checkIpCfgMulity ,size "+list.size());
		long start=System.currentTimeMillis();
		BlockingQueue<BaseIpCfg> queue=new ArrayBlockingQueue<>(list.size());
		ExecutorService service=Executors.newFixedThreadPool(Constants.MULITY_THREAD_SIZE);
		List<Future<String>> futures=new ArrayList<>();
		for(int i=0;i<Constants.MULITY_THREAD_SIZE;i++) {
			CheckIpFormatThread t=new CheckIpFormatThread(serviceDict,regionDict, this.getMsgProp(), list, queue);
			t.setAsnNoMaps(asnNos);
			futures.add(service.submit(t));
		}
		service.shutdown();
		while(!service.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Future<String> future:futures) {
			String msg = future.get();
			if(StringUtils.isNotBlank(msg)) {
				throw new ServiceException(msg);
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("checkIpCfgMulity finish,cost:"+(end-start)+",size:"+queue.size());
		return queue;
	}
	/**
	 * 多线程验证
	 * @param serviceDict
	 * @param regionDict
	 * @param list
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ServiceException
	 */
	public BlockingQueue<BaseStringCfg<?>> checkStringCfgMulity(FunctionServiceDict serviceDict, FunctionRegionDict regionDict, BlockingQueue<? extends Object> list) throws InterruptedException, ExecutionException, ServiceException{
		logger.warn("start checkStringCfgMulity ,size "+list.size());
		long start=System.currentTimeMillis();
		BlockingQueue<BaseStringCfg<?>> queue=new ArrayBlockingQueue<>(list.size());
		ExecutorService service=Executors.newFixedThreadPool(Constants.MULITY_THREAD_SIZE);
		List<Future<String>> futures=new ArrayList<>();
		for(int i=0;i<Constants.MULITY_THREAD_SIZE;i++) {
			CheckStringFormatThread t=new CheckStringFormatThread(serviceDict,regionDict, this.getMsgProp(), list, queue);
			futures.add(service.submit(t));
		}
		service.shutdown();
		while(!service.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Future<String> future:futures) {
			String msg = future.get();
			if(StringUtils.isNotBlank(msg)) {
				throw new ServiceException(msg);
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("checkStringCfgMulity finish,cost:"+(end-start)+",size:"+queue.size());
		return queue;
	}
	/**
	 * 多线程验证
	 * @param serviceDict
	 * @param regionDict
	 * @param list
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ServiceException
	 */
	public BlockingQueue<ComplexkeywordCfg> checkComplexStringCfgMulity(FunctionServiceDict serviceDict, FunctionRegionDict regionDict,BlockingQueue<? extends Object> list) throws InterruptedException, ExecutionException, ServiceException{
		logger.warn("start checkComplexStringCfgMulity ,size "+list.size());
		long start=System.currentTimeMillis();
		BlockingQueue<ComplexkeywordCfg> queue=new ArrayBlockingQueue<>(list.size());
		ExecutorService service=Executors.newFixedThreadPool(Constants.MULITY_THREAD_SIZE);
		List<Future<String>> futures=new ArrayList<>();
		for(int i=0;i<Constants.MULITY_THREAD_SIZE;i++) {
			CheckComplexStringFormatThread t=new CheckComplexStringFormatThread(serviceDict,regionDict, this.getMsgProp(), list, queue);
			futures.add(service.submit(t));
		}
		service.shutdown();
		while(!service.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Future<String> future:futures) {
			String msg = future.get();
			if(StringUtils.isNotBlank(msg)) {
				throw new ServiceException(msg);
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("checkComplexStringCfgMulity finish,cost:"+(end-start)+",size:"+queue.size());
		return queue;
	}
	/**
	 * 多线程验证
	 * @param serviceDict
	 * @param regionDict
	 * @param list
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ServiceException
	 */
	public BlockingQueue<DnsResStrategy> checkDnsResStrategyCfgMulity(FunctionServiceDict serviceDict, FunctionRegionDict regionDict,BlockingQueue<? extends Object> list) throws InterruptedException, ExecutionException, ServiceException{
		logger.warn("start checkDnsResStrategyCfgMulity ,size "+list.size());
		long start=System.currentTimeMillis();
		BlockingQueue<DnsResStrategy> queue=new ArrayBlockingQueue<>(list.size());
		ExecutorService service=Executors.newFixedThreadPool(Constants.MULITY_THREAD_SIZE);
		List<Future<String>> futures=new ArrayList<>();
		for(int i=0;i<Constants.MULITY_THREAD_SIZE;i++) {
			CheckDnsResStrategyFormatThread t=new CheckDnsResStrategyFormatThread(serviceDict,regionDict, this.getMsgProp(), list, queue);
			futures.add(service.submit(t));
		}
		service.shutdown();
		while(!service.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Future<String> future:futures) {
			String msg = future.get();
			if(StringUtils.isNotBlank(msg)) {
				throw new ServiceException(msg);
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("checkDnsResStrategyCfgMulity finish,cost:"+(end-start)+",size:"+queue.size());
		return queue;
	}
	
	/**
	 * 
	 * setReportSearchTime(报表查询设置开始时间与结束时间) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param bean
	 * @throws ParseException
	 *             void
	 * @exception @since
	 *                1.0.0
	 */
	public void setReportSearchTime(SearchReport bean) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SEARCH_DATEFORMAT);
		String reportTime = bean.getReportTime();
		if (StringUtils.isNotBlank(reportTime)) {
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(getReportTime(reportTime));
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			startCal.set(Calendar.MILLISECOND, 0);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(getReportTime(reportTime));
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 59);
			endCal.set(Calendar.MILLISECOND, 0);
			if (bean.getReportType() == Constants.REPORT_TYPE_HOUR) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				endCal.set(Calendar.HOUR_OF_DAY, 23);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_DAY) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.DAY_OF_MONTH, 1);
				endCal.set(Calendar.HOUR_OF_DAY, 23);
				DateUtils.setLastDayOfMonth(endCal);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_MONTH) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.DAY_OF_MONTH, 1);
				startCal.set(Calendar.MONTH, 0);
				endCal.set(Calendar.HOUR_OF_DAY, 23);
				DateUtils.setLastDayOfMonth(endCal);
				endCal.set(Calendar.MONTH, 11);
			}
			bean.setReportStartTime(startCal.getTime());
			bean.setReportEndTime(endCal.getTime());
			bean.setSearchReportStartTime(sdf.format(startCal.getTime()));
			bean.setSearchReportEndTime(sdf.format(endCal.getTime()));
		} else {
			Date date = new Date();
			bean.setReportEndTime(date);
			bean.setSearchReportEndTime(sdf.format(date));
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(date);
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			startCal.set(Calendar.MILLISECOND, 0);
			if (bean.getReportType() == Constants.REPORT_TYPE_HOUR) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_DAY) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.DAY_OF_MONTH, 1);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_MONTH) {
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.DAY_OF_MONTH, 1);
				startCal.set(Calendar.MONTH, 0);
			}
			bean.setReportStartTime(startCal.getTime());
			bean.setSearchReportStartTime(sdf.format(startCal.getTime()));

		}
		logger.info("search start time " + bean.getSearchReportStartTime());
		logger.info("search end time " + bean.getSearchReportEndTime());
	}

	public List<String> getDateTitiles(SearchReport bean) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SEARCH_DATEFORMAT);
		List<String> titles = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(bean.getReportStartTime());
		while (cal.getTimeInMillis() < bean.getReportEndTime().getTime()) {// 构造标题
			// 报表时间单位加一
			if (bean.getReportType() == Constants.REPORT_TYPE_HOUR) {
				titles.add(sdf.format(cal.getTime()).substring(0, 13));
				cal.add(Calendar.HOUR_OF_DAY, 1);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_DAY) {
				titles.add(sdf.format(cal.getTime()).substring(0, 10));
				cal.add(Calendar.DAY_OF_MONTH, 1);
			} else if (bean.getReportType() == Constants.REPORT_TYPE_MONTH) {
				titles.add(sdf.format(cal.getTime()).substring(0, 7));
				cal.add(Calendar.MONTH, 1);
			}
		}
		return titles;
	}

	public Date getReportTime(String reportTime) throws ParseException {
		Pattern datePattern = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
		Pattern monthPattern = Pattern.compile("^[0-9]{4}-[0-9]{2}$");
		Pattern yearPattern = Pattern.compile("^[0-9]{4}$");
		Matcher matcher = datePattern.matcher(reportTime);
		if (matcher.matches()) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(reportTime);
		} else {
			matcher = monthPattern.matcher(reportTime);
			if (matcher.matches()) {
				return new SimpleDateFormat("yyyy-MM").parse(reportTime);
			} else {
				matcher = yearPattern.matcher(reportTime);
				if (matcher.matches()) {
					return new SimpleDateFormat("yyyy").parse(reportTime);
				}
			}
		}
		return null;
	}

	/**
	 * 多域导入
	 * 
	 * @param redirectAttributes
	 * @param files
	 * @param serviceDictId
	 * @param regionDictIds
	 * @param requestId
	 */
	public void _import(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,
			MultipartFile[] files, Integer serviceDictId, String regionDictIds, Integer requestId) {
		logger.warn("import start...");
		long start=System.currentTimeMillis();
		ImportBigExcel ei=null;
		try {
			FunctionServiceDict serviceDict = DictUtils.getFunctionServiceDict(serviceDictId);
				BlockingQueue<BaseIpCfg> ipPortCfgs =null;
				BlockingQueue<BaseStringCfg<?>> stringCfgs =null;
				BlockingQueue<ComplexkeywordCfg> complexkeywordCfgs =null;
				BlockingQueue<DnsResStrategy> dnsResStrategies=null;
				List<CfgIndexInfo> cfgIndexInfos = new ArrayList<CfgIndexInfo>();
				List<AppPolicyCfg> appPolicyCfgs = new ArrayList<AppPolicyCfg>();
				List<Map<Long,Integer>> asnNoMaps=Lists.newArrayList();
				Map<Long,Integer> newAsnNoMap=Maps.newConcurrentMap();
				Map<Long,Integer> OldAsnNoMap=Maps.newConcurrentMap();
				asnNoMaps.add(newAsnNoMap);
				asnNoMaps.add(OldAsnNoMap);
				FunctionRegionDict appRegion = null;
				if(serviceDict!=null) {
					String regionCode = serviceDict.getRegionCode();
					for (String code : regionCode.split(",")) {
						if (StringUtils.isNotBlank(code)) {
							FunctionRegionDict d = DictUtils.getFunctionRegionDictByRegionCode(serviceDict.getFunctionId(),
									Integer.parseInt(code));
							if (d != null && d.getConfigServiceType() != null
									&& d.getConfigServiceType().equals("app_policy")) {
								appRegion = d;
								break;
							}
						}
	
					}
				}
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					ei = new ImportBigExcel(file, 0, 1);
					FunctionRegionDict regionDict = DictUtils
							.getFunctionRegionDict(Integer.parseInt(regionDictIds.split(",")[i]));
					//加载模板
					loadTemplate(ei,regionDict, serviceDict);
					//------------------------------------check format start----------------------------
					if (regionDict.getRegionType().equals(1)) {// IP
						if (regionDict.getFunctionId().equals(5)) {
							if (serviceDict!=null&&serviceDict.getAction().equals(64)) {
								BlockingQueue<IpRateLimitTemplate> list = ei.getDataList(IpRateLimitTemplate.class
										);
								ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
							} else {
								BlockingQueue<IpAllTemplate> list = ei.getDataList(IpAllTemplate.class );
								ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
							}
						} else if (regionDict.getFunctionId().equals(7)&&serviceDict!=null&&serviceDict.getAction().intValue()==16) {
							BlockingQueue<DnsIpTemplate> list = ei.getDataList(DnsIpTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						}else if(regionDict.getFunctionId().equals(401)) {
							BlockingQueue<DnsIpTemplate> list = ei.getDataList(DnsIpTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						}else if (regionDict.getFunctionId().equals(212)) {
							BlockingQueue<IpPayloadTemplate> list = ei.getDataList(IpPayloadTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						} else if (regionDict.getFunctionId().equals(510)
								&& "p2p_ip".equals(regionDict.getConfigServiceType())) { // P2p IP
							BlockingQueue<P2pIpTemplate> list = ei.getDataList(P2pIpTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						} else if (regionDict.getFunctionId().equals(600)) {// ASN IP
							//加载asn缓存
							AsnCacheUtils.init(true);
							BlockingQueue<AsnIpTemplate> list = ei.getDataList(AsnIpTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, asnNoMaps, list);
						}	else if (regionDict.getFunctionId().equals(301)) {// DDOS IP
							BlockingQueue<DdosIpTemplate> list = ei.getDataList(DdosIpTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						} else {
							BlockingQueue<IpAllTemplate> list = ei.getDataList(IpAllTemplate.class );
							ipPortCfgs=this.checkIpCfgMulity(serviceDict, regionDict, null, list);
						}
					} else if (regionDict.getRegionType().equals(2)) {// 字符串类
						if (regionDict.getFunctionId().equals(510)
								&& "p2p_hash".equals(regionDict.getConfigServiceType())) { // P2p hash
							BlockingQueue<P2pHashStringTemplate> list = ei.getDataList(P2pHashStringTemplate.class
									);
							stringCfgs=this.checkStringCfgMulity(serviceDict, regionDict, list);
						}else if (regionDict.getFunctionId().equals(403)) {
							BlockingQueue<AppDomainTemplate> list = ei.getDataList(AppDomainTemplate.class
									);
							stringCfgs=this.checkStringCfgMulity(serviceDict, regionDict, list);
						} else {
							BlockingQueue<StringAllTemplate> list = ei.getDataList(StringAllTemplate.class );
							stringCfgs=this.checkStringCfgMulity(serviceDict, regionDict, list);
						}

					} else if (regionDict.getRegionType().equals(3)) {// 增强字符串类
						if (regionDict.getFunctionId().equals(7)&&serviceDict!=null&&serviceDict.getAction().intValue()==16) {
							BlockingQueue<DnsComplexStringTemplate> list = ei.getDataList(DnsComplexStringTemplate.class
									);
							complexkeywordCfgs = this.checkComplexStringCfgMulity(serviceDict, regionDict, list);
						} else {
							BlockingQueue<ComplexStringAllTemplate> list = ei.getDataList(ComplexStringAllTemplate.class
									);
							complexkeywordCfgs = this.checkComplexStringCfgMulity(serviceDict, regionDict, list);
						}

					}else if (regionDict.getRegionType().equals(6)) {// 回调类
						if (regionDict.getFunctionId().equals(400)) {
							BlockingQueue<DnsResStrategyTemplate> list = ei.getDataList(DnsResStrategyTemplate.class
									);
							dnsResStrategies=this.checkDnsResStrategyCfgMulity(serviceDict, regionDict, list);
						} 
					}
					//删除文件
					if(ei.getUploadFile()!=null&&ei.getUploadFile().exists()) {
						ei.getUploadFile().delete();
					}
					//------------------------------------check format end----------------------------
					Date date = new Date();
					String specServiceId = request.getParameter("appId");
					String behaviorId = request.getParameter("behaviorId");
					String asnIpGroup = request.getParameter("asnGroupId");
					String isSend = request.getParameter("isSend")==null?"":request.getParameter("isSend");
					SpecificServiceCfg specificServiceCfg = null;
					if (serviceDict!=null&&(serviceDict.getServiceId().intValue() == 33 || serviceDict.getServiceId().intValue() == 145
							|| serviceDict.getServiceId().intValue() == 35
							|| serviceDict.getServiceId().intValue() == 147
							|| serviceDict.getServiceId().intValue() == 36
							|| serviceDict.getServiceId().intValue() == 148
							|| serviceDict.getServiceId().intValue() == 1028// app ip
							|| serviceDict.getServiceId().intValue() == 1024//app http
							|| serviceDict.getServiceId().intValue() == 1026//app domain
							)) {
						if (StringUtils.isNotBlank(specServiceId)) {
							specificServiceCfg = specificServiceCfgService
									.getBySpecServiceId(Integer.parseInt(specServiceId));
						}
					}
					if (regionDict.getRegionType().equals(1)) {// IP
						if(regionDict.getFunctionId().intValue()==600) {//ans ip
							if(asnNoMaps.get(0).size()>0) {
								try {
									//将未在缓存中找到对应config的asn no分配group ID
									List<Integer> groupIds = ConfigServiceUtil.getId(2,asnNoMaps.get(0).size());
									int ind=0;
									for(Long key:asnNoMaps.get(0).keySet()) {
										asnNoMaps.get(0).put(key, groupIds.get(ind));
										ind++;
									}
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("获取编译ID出错");
									throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
								}
							}
							
							List<SysDataDictionaryItem> isImportAll=DictUtils.getDictList("IS_ASN_IP_IMPORT_ALL");
							//处理组,新的组会在这里保存
							asnIpCfgService.processGroup(asnNoMaps.get(0));
							//全量下发，删除asnNo对应的已有的IP
							if(isImportAll.get(0).getItemCode().equals("1")) {
								logger.warn("Delete and send ip reuse regions start");
								long _start=System.currentTimeMillis();
								asnIpCfgService.deleteIps(asnNoMaps.get(1));
//								List<Future<Throwable>> list=Lists.newArrayList();
//								ExecutorService service=Executors.newFixedThreadPool(Constants.SAVE_AND_DEL_THREAD_SIZE);
//								BlockingQueue<Long> queue=new ArrayBlockingQueue<>(asnNoMaps.get(1).size());
//								queue.addAll(asnNoMaps.get(1).keySet());
//								for(int j=0;j<Constants.SAVE_AND_DEL_THREAD_SIZE;j++) {
//									DeleteAsnIpTread t=new DeleteAsnIpTread(queue);
//									list.add(service.submit(t));
//								}
//								service.shutdown();
//								while(!service.isTerminated()) {
//									Thread.sleep(10);
//								}
//								for(Future<Throwable> e:list) {
//									if(e.get()!=null) {
//										try {
//											throw e.get();
//										} catch (Throwable e1) {
//											// TODO Auto-generated catch block
//											e1.printStackTrace();
//										}
//									}
//								}
								long _end=System.currentTimeMillis();
								logger.warn("Delete and send ip reuse regions end,cost:"+(_end-_start));
							}
							Map<Long,Boolean> fullMap=Maps.newConcurrentMap();
							for(Long asnNo:asnNoMaps.get(0).keySet()) {
								fullMap.put(asnNo, false);
							}
							for(Long asnNo:asnNoMaps.get(1).keySet()) {
								ConfigGroupInfo info=AsnCacheUtils.get(asnNo);
								if(info==null) {
									info=asnIpCfgService.getConfigGroupInfoByAsnNo(asnNo);
								}
								fullMap.put(asnNo, info.getIsIssued().intValue()==1);
							}
//							List<Future<Throwable>> list=Lists.newArrayList();
//							ExecutorService service=Executors.newFixedThreadPool(Constants.SAVE_AND_DEL_THREAD_SIZE);
//							for(int j=0;j<Constants.SAVE_AND_DEL_THREAD_SIZE;j++) {
//								SaveAsnIpThread t=new SaveAsnIpThread(serviceDict, regionDict, requestId, ipPortCfgs);
//								t.setAsnNoMaps(asnNoMaps);
//								t.setFullMap(fullMap);
//								list.add(service.submit(t));
//							}
//							service.shutdown();
//							while(!service.isTerminated()) {
//								Thread.sleep(10);
//							}
//							for(Future<Throwable> e:list) {
//								if(e.get()!=null) {
//									try {
//										throw e.get();
//									} catch (Throwable e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								}
//							}
							List<AsnIpCfg> asnIpCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
							List<BaseIpCfg> _ipPortCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
							while(!ipPortCfgs.isEmpty()) {
								ipPortCfgs.drainTo(_ipPortCfgs, Constants.MAAT_JSON_SEND_SIZE);
								List<Integer> regionIds=Lists.newArrayList();
								try {
									regionIds = ConfigServiceUtil.getId(3,_ipPortCfgs.size());
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("获取域ID出错");
									throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
								}
								int ind=0;
								for (BaseIpCfg cfg : _ipPortCfgs) {
									AsnIpCfg _cfg=new AsnIpCfg();
									BeanUtils.copyProperties(cfg, _cfg,new String[] {"cfgId"});
									_cfg.setTableName(AsnIpCfg.getTablename());
									_cfg.setAction(0);
									_cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
									_cfg.setCfgType(regionDict.getConfigRegionValue());
									_cfg.setCreateTime(date);
									_cfg.setCreatorId(UserUtils.getUser().getId());
									_cfg.setDoLog(2);
									_cfg.setFunctionId(regionDict.getFunctionId());
									_cfg.setIsAudit(0);
									if(fullMap.get(Long.parseLong(_cfg.getUserRegion1()))) {
										_cfg.setIsValid(Constants.VALID_YES);
									}else {
										_cfg.setIsValid(Constants.VALID_NO);
									}
									_cfg.setIsAreaEffective(0);
									_cfg.setAttribute("0");
									_cfg.setClassify("0");
									_cfg.setLable("0");
									_cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
									_cfg.setServiceId(0);
									//设置region id
									if(regionIds!=null&&regionIds.size()==_ipPortCfgs.size()) {
										_cfg.setRegionId(regionIds.get(ind));
									}
									//设置group id
									if(asnNoMaps.get(0).containsKey(Long.parseLong(_cfg.getUserRegion1()))) {
										_cfg.setAsnIpGroup(asnNoMaps.get(0).get(Long.parseLong(_cfg.getUserRegion1())));
									}else if(asnNoMaps.get(1).containsKey(Long.parseLong(_cfg.getUserRegion1()))) {
										_cfg.setAsnIpGroup(asnNoMaps.get(1).get(Long.parseLong(_cfg.getUserRegion1())));
									}else {
										ConfigGroupInfo info=AsnCacheUtils.get(Long.parseLong(_cfg.getUserRegion1()));
										if(info==null) {
											info=asnIpCfgService.getConfigGroupInfoByAsnNo(Long.parseLong(_cfg.getUserRegion1()));
										}
										_cfg.setAsnIpGroup(info.getGroupId());
									}
									asnIpCfgs.add(_cfg);
									ind++;
								}
								ipCfgService.saveAndSend(regionDict, serviceDict, specificServiceCfg, asnIpCfgs, cfgIndexInfos, appPolicyCfgs,isSend.equals("1"));
								cfgIndexInfos.clear();
								appPolicyCfgs.clear();
								_ipPortCfgs.clear();
								asnIpCfgs.clear();
							}
						}else {
							List<BaseIpCfg> _ipPortCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
							while(!ipPortCfgs.isEmpty()) {
								ipPortCfgs.drainTo(_ipPortCfgs, Constants.MAAT_JSON_SEND_SIZE);
								List<Integer> compileIds=Lists.newArrayList();
								List<Integer> regionIds=Lists.newArrayList();
								List<Integer> groupIds=Lists.newArrayList();
//								List<Integer> numRegionGroupIds=Lists.newArrayList();
								List<Integer> numRegionRegionIds=Lists.newArrayList();
								if(!regionDict.getFunctionId().equals(405)) {//app ip compileId 从config_group_info中取
									try {
										 compileIds = ConfigServiceUtil.getId(1,_ipPortCfgs.size());
										 if(isSend.equals("1")) {
											 groupIds = ConfigServiceUtil.getId(2,_ipPortCfgs.size());
											 regionIds = ConfigServiceUtil.getId(3,_ipPortCfgs.size());
											 //需要获取数值域的id
											 if(serviceDict!=null&&serviceDict.getProtocolId()!=null&&serviceDict.getProtocolId()>0) {
//												 numRegionGroupIds = ConfigServiceUtil.getId(2,_ipPortCfgs.size());
												 numRegionRegionIds = ConfigServiceUtil.getId(3,_ipPortCfgs.size());
											 }
										 }
									} catch (Exception e) {
										e.printStackTrace();
										logger.info("获取编译ID出错");
										throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
									}
								}else {
									try {
										regionIds = ConfigServiceUtil.getId(3,_ipPortCfgs.size());
									} catch (Exception e) {
										e.printStackTrace();
										logger.info("获取域ID出错");
										throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
									}
								}
								int ind=0;
								for (BaseIpCfg cfg : _ipPortCfgs) {
									cfg.setAction(serviceDict==null?null:serviceDict.getAction());
									/*cfg.setAuditorId(UserUtils.getUser().getId());
									cfg.setAuditTime(date);*/
									cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
									cfg.setCfgType(regionDict.getConfigRegionValue());
									cfg.setCreateTime(date);
									cfg.setCreatorId(UserUtils.getUser().getId());
									cfg.setDoLog(2);
									cfg.setFunctionId(regionDict.getFunctionId());
									if(isSend.equals("1")) {
										cfg.setIsAudit(Constants.AUDIT_YES);
										cfg.setIsValid(Constants.VALID_YES);
										cfg.setAuditorId(UserUtils.getUser().getId());
										cfg.setAuditTime(date);
										if(groupIds!=null&&groupIds.size()==_ipPortCfgs.size()) {
										cfg.setGroupId(groupIds.get(ind));
										}
										if(regionIds!=null&&regionIds.size()==_ipPortCfgs.size()) {
										cfg.setRegionId(regionIds.get(ind));
										}
										if(serviceDict!=null&&serviceDict.getProtocolId()!=null) {
//											if(numRegionGroupIds!=null&&numRegionGroupIds.size()==_ipPortCfgs.size()) {
//												cfg.setNumberRegionGroupId(numRegionGroupIds.get(ind));
//											}
											if(numRegionRegionIds!=null&&numRegionRegionIds.size()==_ipPortCfgs.size()) {
												cfg.setNumberRegionRegionId(numRegionRegionIds.get(ind));
											}
										 }
									}else {
										cfg.setIsAudit(Constants.AUDIT_NOT_YET);
										cfg.setIsValid(Constants.VALID_NO);
									}
									cfg.setIsAreaEffective(0);
									cfg.setAttribute("0");
									cfg.setClassify("0");
									cfg.setLable("0");
									cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
									cfg.setServiceId(serviceDict==null?null:serviceDict.getServiceId());
									cfg.setTableName("ip_port_cfg");
									if(compileIds.size()==_ipPortCfgs.size()) {
										cfg.setCompileId(compileIds.get(ind));
									}
									if(regionDict.getFunctionId().equals(405)&&regionIds.size()==_ipPortCfgs.size()) {
										cfg.setUserRegion1(regionIds.get(ind).toString());
									}
									if(specificServiceCfg!=null) {
										cfg.setAppCode(specificServiceCfg.getSpecServiceCode());
										cfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
										if(StringUtils.isNotBlank(behaviorId))
										cfg.setBehavCode(Integer.parseInt(behaviorId));
									}
									if (serviceDict!=null&&(serviceDict.getServiceId().intValue() == 33
											|| serviceDict.getServiceId().intValue() == 145
											|| serviceDict.getServiceId().intValue() == 35
											|| serviceDict.getServiceId().intValue() == 147
											|| serviceDict.getServiceId().intValue() == 36
											|| serviceDict.getServiceId().intValue() == 148)) {
										if (appRegion != null) {
											AppPolicyCfg appPolicyCfg = new AppPolicyCfg();
											BeanUtils.copyProperties(cfg, appPolicyCfg,new String[] {"cfgId"});
											//appPolicyCfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
											//appPolicyCfg.setAppCode(specificServiceCfg.getSpecServiceCode());
											appPolicyCfg.setMatchMethod(0);
											appPolicyCfg.setIsHexbin(0);
											appPolicyCfg.setCfgType(appRegion.getConfigRegionValue());
											appPolicyCfg.setCfgRegionCode(appRegion.getConfigRegionCode());
											if (StringUtils.isNotBlank(behaviorId)) {
												//appPolicyCfg.setBehavCode(Integer.parseInt(behaviorId));
												appPolicyCfg.setExprType(1);
											} else {
												appPolicyCfg.setExprType(0);
											}
											appPolicyCfgs.add(appPolicyCfg);
										}
									} else {
										if(regionDict.getFunctionId().intValue()!=600
										&&regionDict.getFunctionId().intValue()!=301
										&&regionDict.getFunctionId().intValue()!=401
										&&regionDict.getFunctionId().intValue()!=405) {
											CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
											BeanUtils.copyProperties(cfg, cfgIndexInfo,new String[] {"cfgId"});
											cfgIndexInfos.add(cfgIndexInfo);
										}
									}
									ind++;
								}
								ipCfgService.saveAndSend(regionDict, serviceDict, specificServiceCfg, _ipPortCfgs, cfgIndexInfos, appPolicyCfgs,isSend.equals("1"));
								cfgIndexInfos.clear();
								appPolicyCfgs.clear();
								_ipPortCfgs.clear();
							}
						}
						
					} else if (regionDict.getRegionType().equals(2)) {
//						List<Future<Throwable>> list=Lists.newArrayList();
//						ExecutorService service=Executors.newFixedThreadPool(Constants.SAVE_AND_DEL_THREAD_SIZE);
//						for(int j=0;j<Constants.SAVE_AND_DEL_THREAD_SIZE;j++) {
//							SaveStringCfgThread t=new SaveStringCfgThread(serviceDict, regionDict, requestId, stringCfgs,isSend.equals("1"));
//							t.setSpecificServiceCfg(specificServiceCfg);
//							t.setAsnIpGroup(asnIpGroup);
//							t.setBehaviorId(behaviorId);
//							t.setSpecServiceId(specServiceId);
//							list.add(service.submit(t));
//						}
//						service.shutdown();
//						while(!service.isTerminated()) {
//							Thread.sleep(10);
//						}
//						for(Future<Throwable> e:list) {
//							if(e.get()!=null) {
//								try {
//									throw e.get();
//								} catch (Throwable e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
//							}
//						}
						List<BaseStringCfg> _stringCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
						while(!stringCfgs.isEmpty()) {
							stringCfgs.drainTo(_stringCfgs, Constants.MAAT_JSON_SEND_SIZE);
							List<Integer> compileIds=Lists.newArrayList();
							List<Integer> groupIds=Lists.newArrayList();
							List<Integer> regionIds=Lists.newArrayList();
							try {
								 compileIds = ConfigServiceUtil.getId(1,_stringCfgs.size());
								 if(isSend.equals("1")) {
									 groupIds = ConfigServiceUtil.getId(2,_stringCfgs.size());
									 regionIds = ConfigServiceUtil.getId(3,_stringCfgs.size());
								 }
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("获取编译ID出错");
								throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
							}
							int ind=0;
							for (BaseStringCfg cfg : _stringCfgs) {
								cfg.setAction(serviceDict.getAction());
								cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
								cfg.setCfgType(regionDict.getConfigRegionValue());
								cfg.setCreateTime(date);
								cfg.setCreatorId(UserUtils.getUser().getId());
								cfg.setDoLog(2);
								cfg.setFunctionId(regionDict.getFunctionId());
								if(isSend.equals("1")) {
									cfg.setIsAudit(Constants.AUDIT_YES);
									cfg.setIsValid(Constants.VALID_YES);
									cfg.setAuditorId(UserUtils.getUser().getId());
									cfg.setAuditTime(date);
									if(groupIds!=null&&groupIds.size()==_stringCfgs.size()) {
									cfg.setGroupId(groupIds.get(ind));
									}
									if(regionIds!=null&&regionIds.size()==_stringCfgs.size()) {
									cfg.setRegionId(regionIds.get(ind));
									}
								}else {
									cfg.setIsAudit(Constants.AUDIT_NOT_YET);
									cfg.setIsValid(Constants.VALID_NO);
								}
								cfg.setIsAreaEffective(0);
								cfg.setAttribute("0");
								cfg.setClassify("0");
								cfg.setLable("0");
								cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
								cfg.setServiceId(serviceDict.getServiceId());
								if(compileIds!=null&&compileIds.size()==_stringCfgs.size()) {
									cfg.setCompileId(compileIds.get(ind));
								}
								if(specificServiceCfg!=null) {
									cfg.setAppCode(specificServiceCfg.getSpecServiceCode());
									cfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
									if(StringUtils.isNotBlank(behaviorId))
									cfg.setBehavCode(Integer.parseInt(behaviorId));
								}
								if (serviceDict.getServiceId().intValue() == 33
										|| serviceDict.getServiceId().intValue() == 145
										|| serviceDict.getServiceId().intValue() == 35
										|| serviceDict.getServiceId().intValue() == 147
										|| serviceDict.getServiceId().intValue() == 36
										|| serviceDict.getServiceId().intValue() == 148) {
									if (appRegion != null) {
										AppPolicyCfg appPolicyCfg = new AppPolicyCfg();
										BeanUtils.copyProperties(cfg, appPolicyCfg,new String[] {"cfgId"});
										//appPolicyCfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
										//appPolicyCfg.setAppCode(specificServiceCfg.getSpecServiceCode());
										appPolicyCfg.setMatchMethod(0);
										appPolicyCfg.setIsHexbin(0);
										appPolicyCfg.setCfgType(appRegion.getConfigRegionValue());
										appPolicyCfg.setCfgRegionCode(appRegion.getConfigRegionCode());
										if (StringUtils.isNotBlank(behaviorId)) {
											//appPolicyCfg.setBehavCode(Integer.parseInt(behaviorId));
											appPolicyCfg.setExprType(1);
										} else {
											appPolicyCfg.setExprType(0);
										}
										appPolicyCfgs.add(appPolicyCfg);
									}
								} else {
									if(!regionDict.getFunctionId().equals(403)) {
										CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
										BeanUtils.copyProperties(cfg, cfgIndexInfo, new String[] {"cfgId"});
										cfgIndexInfos.add(cfgIndexInfo);
									}
									
								}
								ind++;
							}
							ipCfgService.saveAndSend(regionDict, serviceDict, specificServiceCfg, _stringCfgs, cfgIndexInfos, appPolicyCfgs,isSend.equals("1"));
							cfgIndexInfos.clear();
							appPolicyCfgs.clear();
							_stringCfgs.clear();
						}
					} else if (regionDict.getRegionType().equals(3)) {
						List<ComplexkeywordCfg> _complexkeywordCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
						while(!complexkeywordCfgs.isEmpty()) {
							complexkeywordCfgs.drainTo(_complexkeywordCfgs, Constants.MAAT_JSON_SEND_SIZE);
							List<Integer> compileIds=Lists.newArrayList();
							try {
								 compileIds = ConfigServiceUtil.getId(1,_complexkeywordCfgs.size());
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("获取编译ID出错");
								throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
							}
							int ind=0;
							for (ComplexkeywordCfg cfg : _complexkeywordCfgs) {
								cfg.setAction(serviceDict.getAction());
								/*cfg.setAuditorId(UserUtils.getUser().getId());
								cfg.setAuditTime(date);*/
								cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
								cfg.setCfgType(regionDict.getConfigRegionValue());
								cfg.setCreateTime(date);
								cfg.setCreatorId(UserUtils.getUser().getId());
								cfg.setDoLog(2);
								cfg.setFunctionId(regionDict.getFunctionId());
								cfg.setIsAudit(0);
								cfg.setIsValid(0);
								cfg.setIsAreaEffective(0);
								cfg.setAttribute("0");
								cfg.setClassify("0");
								cfg.setLable("0");
								cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
								cfg.setServiceId(serviceDict.getServiceId());
								if(compileIds!=null&&compileIds.size()==_complexkeywordCfgs.size()) {
									cfg.setCompileId(compileIds.get(ind));
								}
								if(specificServiceCfg!=null) {
									cfg.setAppCode(specificServiceCfg.getSpecServiceCode());
									cfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
									if(StringUtils.isNotBlank(behaviorId))
									cfg.setBehavCode(Integer.parseInt(behaviorId));
								}
								if (serviceDict.getServiceId().intValue() == 33
										|| serviceDict.getServiceId().intValue() == 145
										|| serviceDict.getServiceId().intValue() == 35
										|| serviceDict.getServiceId().intValue() == 147
										|| serviceDict.getServiceId().intValue() == 36
										|| serviceDict.getServiceId().intValue() == 148) {
									if (appRegion != null) {
										AppPolicyCfg appPolicyCfg = new AppPolicyCfg();
										BeanUtils.copyProperties(cfg, appPolicyCfg,new String[] {"cfgId"});
										//appPolicyCfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
										//appPolicyCfg.setAppCode(specificServiceCfg.getSpecServiceCode());
										appPolicyCfg.setMatchMethod(0);
										appPolicyCfg.setIsHexbin(0);
										appPolicyCfg.setCfgType(appRegion.getConfigRegionValue());
										appPolicyCfg.setCfgRegionCode(appRegion.getConfigRegionCode());
										if (StringUtils.isNotBlank(behaviorId)) {
											//appPolicyCfg.setBehavCode(Integer.parseInt(behaviorId));
											appPolicyCfg.setExprType(1);
										} else {
											appPolicyCfg.setExprType(0);
										}
										appPolicyCfgs.add(appPolicyCfg);
									}
								} else {
									if(!regionDict.getFunctionId().equals(402)) {
										CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
										BeanUtils.copyProperties(cfg, cfgIndexInfo,new String[] {"cfgId"});
										cfgIndexInfos.add(cfgIndexInfo);
									}
									
								}
								ind++;
							}
							ipCfgService.saveAndSend(regionDict, serviceDict, specificServiceCfg, _complexkeywordCfgs, cfgIndexInfos, appPolicyCfgs,isSend.equals("1"));
							cfgIndexInfos.clear();
							appPolicyCfgs.clear();
							_complexkeywordCfgs.clear();
						}
					}else if(regionDict.getRegionType().equals(6)) {
						if(regionDict.getFunctionId().equals(400)) {
							List<DnsResStrategy> _dnsResStrategies=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
							while(!dnsResStrategies.isEmpty()) {
								dnsResStrategies.drainTo(_dnsResStrategies, Constants.MAAT_JSON_SEND_SIZE);
								List<Integer> compileIds=Lists.newArrayList();
								try {
									 compileIds = ConfigServiceUtil.getId(1,_dnsResStrategies.size());
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("获取编译ID出错");
									throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
								}
								int ind=0;
								for(DnsResStrategy cfg:_dnsResStrategies) {
									cfg.setAction(serviceDict.getAction());
									/*cfg.setAuditorId(UserUtils.getUser().getId());
									cfg.setAuditTime(date);*/
									cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
									cfg.setCfgType(regionDict.getConfigRegionValue());
									cfg.setCreateTime(date);
									cfg.setCreatorId(UserUtils.getUser().getId());
									cfg.setDoLog(2);
									cfg.setFunctionId(regionDict.getFunctionId());
									cfg.setIsAudit(0);
									cfg.setIsValid(0);
									cfg.setIsAreaEffective(0);
									cfg.setAttribute("0");
									cfg.setClassify("0");
									cfg.setLable("0");
									cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
									cfg.setServiceId(serviceDict.getServiceId());
									cfg.setCompileId(compileIds.get(ind));
									ind++;
								}
								ipCfgService.saveAndSend(regionDict, serviceDict, specificServiceCfg, _dnsResStrategies, cfgIndexInfos, appPolicyCfgs,isSend.equals("1"));
								_dnsResStrategies.clear();
								cfgIndexInfos.clear();
								appPolicyCfgs.clear();
							}
						}
					}
				}
		
		} catch (Exception e) {
			if(ei!=null) {
				if(ei.getUploadFile().exists()) {
					ei.getUploadFile().delete();
				}
			}
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else if(e instanceof ServiceException) {
				addMessage(redirectAttributes,"error", e.getMessage());
			}else {
				addMessage(redirectAttributes,"error",  "import_failed");
			}
			e.printStackTrace();
		}
		long end=System.currentTimeMillis();
		logger.warn("import finish,cost:"+(end-start));
	}
	/**
	 * 加载模板
	 * @param ei
	 * @param regionDict
	 * @param serviceDict
	 */
	public void loadTemplate(ImportBigExcel ei,FunctionRegionDict regionDict,FunctionServiceDict serviceDict) {
		if (regionDict.getRegionType().equals(1)) {// IP
			if (regionDict.getFunctionId().equals(5)) {
				if (serviceDict!=null&&serviceDict.getAction().equals(64)) {
					ei.loadInitParams(IpRateLimitTemplate.class, this.getMsgProp(), regionDict, serviceDict);
				} else {
					ei.loadInitParams(IpAllTemplate.class, this.getMsgProp(), regionDict, serviceDict);
				}
			} else if (regionDict.getFunctionId().equals(7)&&serviceDict!=null&&serviceDict.getAction().intValue()==16) {
				ei.loadInitParams(DnsIpTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}else if(regionDict.getFunctionId().equals(401)) {
				ei.loadInitParams(DnsIpTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}else if (regionDict.getFunctionId().equals(212)) {
				ei.loadInitParams(IpPayloadTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} else if (regionDict.getFunctionId().equals(510)
					&& "p2p_ip".equals(regionDict.getConfigServiceType())) { // P2p IP
				ei.loadInitParams(P2pIpTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} else if (regionDict.getFunctionId().equals(600)) {// ASN IP
				ei.loadInitParams(AsnIpTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}	else if (regionDict.getFunctionId().equals(301)) {// DDOS IP
				ei.loadInitParams(DdosIpTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} else {
				ei.loadInitParams(IpAllTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}
		} else if (regionDict.getRegionType().equals(2)) {// 字符串类
			if (regionDict.getFunctionId().equals(510)
					&& "p2p_hash".equals(regionDict.getConfigServiceType())) { // P2p hash
				ei.loadInitParams(P2pHashStringTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}else if (regionDict.getFunctionId().equals(403)) {
				ei.loadInitParams(AppDomainTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} else {
				ei.loadInitParams(StringAllTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}

		} else if (regionDict.getRegionType().equals(3)) {// 增强字符串类
			if (regionDict.getFunctionId().equals(7)&&serviceDict!=null&&serviceDict.getAction().intValue()==16) {
				ei.loadInitParams(DnsComplexStringTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} else {
				ei.loadInitParams(ComplexStringAllTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			}

		}else if (regionDict.getRegionType().equals(6)) {// 回调类
			if (regionDict.getFunctionId().equals(400)) {
				ei.loadInitParams(DnsResStrategyTemplate.class, this.getMsgProp(), regionDict, serviceDict);
			} 
		}
		
	}
	/**
	 * 多域配置导出
	 * 
	 * @param columns
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param ids
	 * @param redirectAttributes
	 */
	public void _export(Model model, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, String functionName, List<String> titleList,
			Map<String, Class<?>> classMap, Map<String, List> dataMap, Map<String, String> noExportMap)
			throws Exception {
		// 获取国际化配置
		Properties msgProp = getMsgProp();
		// 获取分类、性质、标签
		List<ServiceDictInfo> fls = serviceDictInfoService.findAllFlDict();
		List<ServiceDictInfo> xzs = serviceDictInfoService.findAllXzDict();
		List<ServiceDictInfo> labels = serviceDictInfoService.findAllLableDict();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("fls", fls);
		map.put("xzs", xzs);
		map.put("labels", labels);

		String fileName = msgProp.getProperty(functionName, functionName) + "_" + DateUtils.getDate("yyyyMMddHHmmss")
				+ ".xlsx";

		fileName = fileName.replaceAll(" ", "_");
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}
		new ExportExcel(msgProp, titleList, noExportMap, classMap, 1).setDataList(msgProp, dataMap, map)
				.write(response, fileName).dispose();
	}
	
	/**
	 * 批量审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	public void auditAll(Page page,Integer auditType,Object entity)throws Exception {
		page.setPageSize(Constants.MAAT_JSON_SEND_SIZE);
		page.setOrderBy("");
		//携带审核状态信息的BaseCfg
		BaseCfg auditBatchCfg=new BaseCfg();
		//携带审核条件的BaseCfg
		CfgIndexInfo searchCfg=new CfgIndexInfo();
		AppPolicyCfg searchAppCfg=new AppPolicyCfg();
		FileDigestCfg searchDigestCfg=new FileDigestCfg();
		AvFileSampleCfg searchFileSampleCfg=new AvFileSampleCfg();
		PxyObjKeyring searchObjKeyringCfg=new PxyObjKeyring();
		PxyObjTrustedCaCert searchTrustedCaCertCfg=new PxyObjTrustedCaCert();
		PxyObjTrustedCaCrl searchTrustedCaCrlCfg=new PxyObjTrustedCaCrl();
		DnsResStrategy searchDnsResStrategy=new DnsResStrategy();
		DnsIpCfg searchDnsIpCfg=new DnsIpCfg();
		IpPortCfg searchIpPortCfg=new IpPortCfg();
		AvSignSampleCfg searchSignSampleCfg=new AvSignSampleCfg();
		DdosIpCfg searchDdosIpCfg=new DdosIpCfg();
		AppIpCfg searchAppIpCfg=new AppIpCfg();
		AppHttpCfg searchAppHttpCfg=new AppHttpCfg();
		AppDomainCfg searchAppDomainCfg=new AppDomainCfg();
		AppTopicDomainCfg searchAppTopicCfg=new AppTopicDomainCfg();
		AppFeatureIndex searchAppFeatureIndex=new AppFeatureIndex();
		AppSslCertCfg searchAppSslCertCfg=new AppSslCertCfg();
		if(entity != null  && (entity instanceof CfgIndexInfo)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchCfg);
		}
		if(entity != null && (entity instanceof AppPolicyCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppCfg);
		}
		if(entity != null && (entity instanceof FileDigestCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchDigestCfg);
		}
		if(entity != null && (entity instanceof AvFileSampleCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchFileSampleCfg);
		}
		if(entity != null && (entity instanceof PxyObjKeyring)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchObjKeyringCfg);
		}
		if(entity != null && (entity instanceof PxyObjTrustedCaCert)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchTrustedCaCertCfg);
		}
		if(entity != null && (entity instanceof PxyObjTrustedCaCrl)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchTrustedCaCrlCfg);
		}
		if(entity != null && (entity instanceof DnsResStrategy)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchDnsResStrategy);
		}
		if(entity != null && (entity instanceof DnsIpCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchDnsIpCfg);
		}
		if(entity != null && (entity instanceof IpPortCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchIpPortCfg);
		}
		
		if(entity != null && (entity instanceof AvSignSampleCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchSignSampleCfg);
		}
		if(entity != null && (entity instanceof DdosIpCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchDdosIpCfg);
		}
		if(entity != null && (entity instanceof AppIpCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppIpCfg);
		}
		if(entity != null && (entity instanceof AppHttpCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppHttpCfg);
		}
		if(entity != null && (entity instanceof AppDomainCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppDomainCfg);
		}
		if(entity != null && (entity instanceof AppTopicDomainCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppTopicCfg);
		}
		if(entity != null && (entity instanceof AppFeatureIndex)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppFeatureIndex);
		}
		if(entity != null && (entity instanceof AppSslCertCfg)) {
			BeanUtils.copyProperties(entity, auditBatchCfg);
			BeanUtils.copyProperties(entity, searchAppSslCertCfg);
		}
		
		
		
		//全部审核通过，只查询当前条件下的所有未审核的配置
		if(auditType.equals(1)) {
			searchCfg.setIsValid(0);
			searchCfg.setIsAudit(0);
			searchAppCfg.setIsValid(0);
			searchAppCfg.setIsAudit(0);
			searchDigestCfg.setIsValid(0);
			searchDigestCfg.setIsAudit(0);
			searchFileSampleCfg.setIsValid(0);
			searchFileSampleCfg.setIsAudit(0);
			searchObjKeyringCfg.setIsValid(0);
			searchObjKeyringCfg.setIsAudit(0);
			searchTrustedCaCertCfg.setIsValid(0);
			searchTrustedCaCertCfg.setIsAudit(0);
			searchTrustedCaCrlCfg.setIsValid(0);
			searchTrustedCaCrlCfg.setIsAudit(0);
			searchDnsResStrategy.setIsValid(0);
			searchDnsResStrategy.setIsAudit(0);
			searchDnsIpCfg.setIsValid(0);
			searchDnsIpCfg.setIsAudit(0);
			searchIpPortCfg.setIsValid(0);
			searchIpPortCfg.setIsAudit(0);
			searchSignSampleCfg.setIsValid(0);
			searchSignSampleCfg.setIsAudit(0);
			searchDdosIpCfg.setIsValid(0);
			searchDdosIpCfg.setIsAudit(0);
			searchAppIpCfg.setIsValid(0);
			searchAppIpCfg.setIsAudit(0);
			searchAppHttpCfg.setIsValid(0);
			searchAppHttpCfg.setIsAudit(0);
			searchAppDomainCfg.setIsValid(0);
			searchAppDomainCfg.setIsAudit(0);
			searchAppTopicCfg.setIsValid(0);
			searchAppTopicCfg.setIsAudit(0);
			searchAppFeatureIndex.setIsValid(0);
			searchAppFeatureIndex.setIsAudit(0);
			searchAppSslCertCfg.setIsValid(0);
			searchAppSslCertCfg.setIsAudit(0);
			
			auditBatchCfg.setIsAudit(1);
			auditBatchCfg.setIsValid(1);
			auditBatchCfg.setAuditTime(new Date());
			auditBatchCfg.setAuditorId(UserUtils.getUser().getId());
		}else {
		//全部取消通过，只查询当前条件下的所有审核通过的配置
			searchCfg.setIsValid(1);
			searchCfg.setIsAudit(1);
			searchAppCfg.setIsValid(1);
			searchAppCfg.setIsAudit(1);
			searchDigestCfg.setIsValid(1);
			searchDigestCfg.setIsAudit(1);
			searchFileSampleCfg.setIsValid(1);
			searchFileSampleCfg.setIsAudit(1);
			searchObjKeyringCfg.setIsValid(1);
			searchObjKeyringCfg.setIsAudit(1);
			searchTrustedCaCertCfg.setIsValid(1);
			searchTrustedCaCertCfg.setIsAudit(1);
			searchTrustedCaCrlCfg.setIsValid(1);
			searchTrustedCaCrlCfg.setIsAudit(1);
			searchDnsResStrategy.setIsValid(1);
			searchDnsResStrategy.setIsAudit(1);
			searchDnsIpCfg.setIsValid(1);
			searchDnsIpCfg.setIsAudit(1);
			searchIpPortCfg.setIsValid(1);
			searchIpPortCfg.setIsAudit(1);
			searchSignSampleCfg.setIsValid(1);
			searchSignSampleCfg.setIsAudit(1);
			searchDdosIpCfg.setIsValid(1);
			searchDdosIpCfg.setIsAudit(1);
			searchAppIpCfg.setIsValid(1);
			searchAppIpCfg.setIsAudit(1);
			searchAppHttpCfg.setIsValid(1);
			searchAppHttpCfg.setIsAudit(1);
			searchAppDomainCfg.setIsValid(1);
			searchAppDomainCfg.setIsAudit(1);
			searchAppTopicCfg.setIsValid(1);
			searchAppTopicCfg.setIsAudit(1);
			searchAppFeatureIndex.setIsValid(1);
			searchAppFeatureIndex.setIsAudit(1);
			searchAppSslCertCfg.setIsValid(1);
			searchAppSslCertCfg.setIsAudit(1);
			
			auditBatchCfg.setIsAudit(3);
			auditBatchCfg.setIsValid(0);
			auditBatchCfg.setAuditTime(new Date());
			auditBatchCfg.setAuditorId(UserUtils.getUser().getId());
		}
		
		ServiceConfigTemplateUtil serviceTemplate = new ServiceConfigTemplateUtil();
		Integer functionId=0;
		if(auditBatchCfg != null && !StringUtil.isEmpty(auditBatchCfg.getFunctionId())) {
			functionId=auditBatchCfg.getFunctionId();
		}
 		List<Map<String,Object>> serviceList = serviceTemplate.getServiceList(functionId);
		
 		for(Map<String,Object> service:serviceList){
  			String tableNameXml = service.get("tableName").toString();
  			String serviceTypeXml = service.get("serviceType").toString();
			String classNameXml = service.get("className").toString();
			String serviceIdXml = service.get("id").toString();
 			auditBatchCfg.setServiceId(Integer.valueOf(serviceIdXml));
 			auditBatchCfg.setTableName(tableNameXml);
 			searchCfg.setServiceId(Integer.valueOf(serviceIdXml));
 			searchCfg.setTableName(tableNameXml);
			searchAppCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppCfg.setTableName(tableNameXml);
			searchDigestCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchDigestCfg.setTableName(tableNameXml);
			searchFileSampleCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchFileSampleCfg.setTableName(tableNameXml);
			searchObjKeyringCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchObjKeyringCfg.setTableName(tableNameXml);
			searchTrustedCaCertCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchTrustedCaCertCfg.setTableName(tableNameXml);
			searchTrustedCaCrlCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchTrustedCaCrlCfg.setTableName(tableNameXml);
			searchDnsResStrategy.setServiceId(Integer.valueOf(serviceIdXml));
			searchDnsResStrategy.setTableName(tableNameXml);
			searchDnsIpCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchDnsIpCfg.setTableName(tableNameXml);
			searchIpPortCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchIpPortCfg.setTableName(tableNameXml);
			searchSignSampleCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchSignSampleCfg.setTableName(tableNameXml);
			searchDdosIpCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchDdosIpCfg.setTableName(tableNameXml);
			searchAppIpCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppIpCfg.setTableName(tableNameXml);
			searchAppHttpCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppHttpCfg.setTableName(tableNameXml);
			searchAppDomainCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppDomainCfg.setTableName(tableNameXml);
			searchAppTopicCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppTopicCfg.setTableName(tableNameXml);
			searchAppFeatureIndex.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppFeatureIndex.setTableName(tableNameXml);
			searchAppSslCertCfg.setServiceId(Integer.valueOf(serviceIdXml));
			searchAppSslCertCfg.setTableName(tableNameXml);
			
			if("1".equals(serviceTypeXml)){//maat类配置
 				List<Map<String,Object>> cfgList = (List<Map<String, Object>>) service.get("cfgList");
				List<Map<String,Object>> userRegionList = (List<Map<String, Object>>) service.get("userRegionList");
				if(!StringUtil.isEmpty(cfgList)) {
					if("cfg_index_info".equals(tableNameXml)){
						/************************多域maat cfg_index_info 处理****************************************/
	 					boolean hasData = true;
						while(hasData){
	 						List<CfgIndexInfo> list=getDataList(page,searchCfg,null,null,null);
							if(!StringUtil.isEmpty(list)){
								//配置生效处理 复用全量下发
								if(auditBatchCfg.getIsValid().equals(1)) {
									hasData=configSynchronizationService.auditNtcMaatData(cfgList,userRegionList,page,auditBatchCfg,list,hasData,true);
								}else {//配置失效处理 批量处理
									List<BaseCfg> cfgIndexList=new ArrayList<>();
									if(!StringUtil.isEmpty(list)) {
										for (CfgIndexInfo cfg : list) {
											BaseCfg baseCfg=new BaseCfg();
											BeanUtils.copyProperties(cfg, baseCfg);
											cfgIndexList.add(baseCfg);
										}
									}
									hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg, cfgIndexList, hasData);
								}
								if(hasData) {
									page.setPageNo(1);
								}
							}else{
								hasData = false;
							}
						}
					}else if("file_digest_cfg".equals(tableNameXml)){
						/************************多域 maat 摘要处理****************************************/
						boolean hasData = true;
						while(hasData){
							List<FileDigestCfg> list=getDataList(page,null,null,searchDigestCfg,null);
							if(!StringUtil.isEmpty(list)){
								//配置生效处理 复用全量下发
								if(auditBatchCfg.getIsValid().equals(1)) {
									hasData=configSynchronizationService.auditFileDigestData(cfgList, userRegionList, page, auditBatchCfg, list, hasData, true);
								}else {//配置失效处理 批量处理
									List<BaseCfg> baseCfgList=new ArrayList<>();
									if(!StringUtil.isEmpty(list)) {
										for (FileDigestCfg cfg : list) {
											BaseCfg baseCfg=new BaseCfg();
											BeanUtils.copyProperties(cfg, baseCfg);
											baseCfgList.add(baseCfg);
										}
									}
									hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg,baseCfgList, hasData);
								}
								if(hasData) {
									page.setPageNo(1);
								}
							}else{
								hasData = false;
							}
						}
					}else if("app_policy_cfg".equals(tableNameXml)){
						/************************多域 maat app策略处理****************************************/
						boolean hasData = true;
						while(hasData){
							List<AppPolicyCfg> list=getDataList(page,null,searchAppCfg,null,null);
							if(!StringUtil.isEmpty(list)){
								//配置生效处理 复用全量下发
								if(auditBatchCfg.getIsValid().equals(1)) {
									hasData=configSynchronizationService.auditAppPolicyData(cfgList,userRegionList,page,auditBatchCfg,list,hasData,true);
								}else {//配置失效处理 批量处理
									List<BaseCfg> cfgAppList=new ArrayList<>();
									if(!StringUtil.isEmpty(list)) {
										for (AppPolicyCfg cfg : list) {
											BaseCfg baseCfg=new BaseCfg();
											BeanUtils.copyProperties(cfg, baseCfg);
											cfgAppList.add(baseCfg);
										}
									}
									hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg,cfgAppList, hasData);
								}
								if(hasData) {
									page.setPageNo(1);
								}
							}else{
								hasData = false;
							}
						}
					}else if("app_feature_index".equals(tableNameXml)){
						/************************多域 maat app特征处理****************************************/
						boolean hasData = true;
						while(hasData){
							List<AppFeatureIndex> list=getDataList(page,null,null,null,searchAppFeatureIndex);
							if(!StringUtil.isEmpty(list)){
								//配置生效处理 复用全量下发
								if(auditBatchCfg.getIsValid().equals(1)) {
									hasData=configSynchronizationService.auditAppFeatureData(cfgList,userRegionList,page,auditBatchCfg,list,hasData,true);
								}else {//配置失效处理 批量处理
									List<BaseCfg> cfgFeatrureList=new ArrayList<>();
									if(!StringUtil.isEmpty(list)) {
										for (AppFeatureIndex cfg : list) {
											BaseCfg baseCfg=new BaseCfg();
											BeanUtils.copyProperties(cfg, baseCfg);
											cfgFeatrureList.add(baseCfg);
										}
									}
									hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg,cfgFeatrureList, hasData);
								}
								if(hasData) {
									page.setPageNo(1);
								}
							}else{
								hasData = false;
							}
						}
					}else{
						logger.info("未知业务");
					}
				}else {
					/************************单域maat处理****************************************/
					int cfgType = Integer.parseInt(service.get("cfgType").toString());
					if("ddos_ip_cfg".equals(tableNameXml)){
						 boolean hasData = true;
							while(hasData){
								List<DdosIpCfg> list=ddosCfgService.findPage(page, searchDdosIpCfg).getList();
								if(!StringUtil.isEmpty(list)){
									if(auditBatchCfg.getIsValid().equals(1)) {
										hasData=configSynchronizationService.auditDdosMaatData(cfgList,userRegionList,
												page,auditBatchCfg,list,hasData,true);
									}else {
										List<BaseCfg> newList=new ArrayList<>();
										if(!StringUtil.isEmpty(list)) {
											for (Object obj : list) {
												BaseCfg cfg=(BaseCfg)obj;
												newList.add(cfg);
											}
										}
										hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg,newList, hasData);
									}
									if(hasData) {
										page.setPageNo(1);
									}
								}else{
									hasData = false;
								}
							}
					}else{
						//批量下发时，未下发过的app需要下发正常配置;已下发过的app需要下发ip复用配置（为保证事物，分两种方式下发）
						//1028 app_ip_cfg
						if(auditBatchCfg.getServiceId().equals(1028)) {
							if(true) {
								boolean hasData = true;
								while(hasData){
									/************预先获取已下发的groupid和compileid，且每批次重新获取，避免上一批次有新的compileId下发*************/
									Map<Integer, Integer> isIssuedCompileIds=new HashMap<>();
									//分组复用组信息1:app ip，2：内容，3：区域 ,4 asn
									ConfigGroupInfo groupInfo=new ConfigGroupInfo();
									groupInfo.setIsIssued(1);
									groupInfo.setGroupType(1);
									List<ConfigGroupInfo> groupInfos=configGroupInfoService.findAllList(groupInfo);//每一批次下发都取一次最新的group_info信息
									Map<Integer, Integer> compileGroups=new HashMap<>();
									if(!StringUtil.isEmpty(groupInfos)) {
										for (ConfigGroupInfo configGroupInfo : groupInfos) {
											isIssuedCompileIds.put(configGroupInfo.getCompileId(), configGroupInfo.getGroupId());
										}
									}
									if(!StringUtil.isEmpty(isIssuedCompileIds)) {
										auditBatchCfg.setCompileGroupMap(isIssuedCompileIds);
										auditBatchCfg.setCompileIsIssued(1);
										List list = Lists.newArrayList();
										searchAppIpCfg.setCompileGroupMap(isIssuedCompileIds);
										list=appCfgService.findAppIpList(page, searchAppIpCfg).getList();
										if(!StringUtil.isEmpty(list)){
											//APP IP配置生效/失效
											hasData=configSynchronizationService.auditAppIpData(userRegionList,
													page,auditBatchCfg,list,hasData,true);
											if(hasData) {
												page.setPageNo(1);
											}
										}else{
											hasData = false;
										}
									}else {
										hasData = false;
									}
								}
							}
							if(auditBatchCfg.getIsValid().equals(1)) {
								boolean hasData = true;
								while(hasData){
									/************预先获取未下发的groupid和compileid，********************/
									//获取已下发的appcompileid
									Map<Integer, Integer> notIssuedCompileIds=new HashMap<>();
									//分组复用组信息1:app ip，2：内容，3：区域 ,4 asn
									ConfigGroupInfo groupInfo=new ConfigGroupInfo();
									groupInfo.setIsIssued(0);
									groupInfo.setGroupType(1);
									List<ConfigGroupInfo> groupInfos=configGroupInfoService.findAllList(groupInfo);//每一批次下发都取一次最新的group_info信息
									Map<Integer, Integer> compileGroups=new HashMap<>();
									if(!StringUtil.isEmpty(groupInfos)) {
										for (ConfigGroupInfo configGroupInfo : groupInfos) {
											notIssuedCompileIds.put(configGroupInfo.getCompileId(), configGroupInfo.getGroupId());
										}
									}
									if(!StringUtil.isEmpty(notIssuedCompileIds)) {
										auditBatchCfg.setCompileGroupMap(notIssuedCompileIds);
										searchAppIpCfg.setCompileGroupMap(notIssuedCompileIds);
										auditBatchCfg.setCompileIsIssued(0);
										List list = Lists.newArrayList();
										list=appCfgService.findAppIpList(page, searchAppIpCfg).getList();
										if(!StringUtil.isEmpty(list)){
											//APP IP配置生效/失效
											hasData=configSynchronizationService.auditAppIpData(userRegionList,
													page,auditBatchCfg,list,hasData,true);
											if(hasData) {
												page.setPageNo(1);
											}
										}else{
											hasData = false;
										}
									}else {
										hasData = false;
									}
								}
							}
						}else {
							boolean hasData = true;
							while(hasData){
								List list = Lists.newArrayList();
								/*
								1026 app_domain_cfg
								1028 app_ip_cfg
								1029 app_ssl_cert_cfg
								1152 app_topic_domain_cfg
								1024 app_http_cfg
								1026 app_domain_cfg
								*/
								if(auditBatchCfg.getServiceId().equals(1026)) {
									list=appCfgService.findAppDomainList(page, searchAppDomainCfg).getList();
								}
								if(auditBatchCfg.getServiceId().equals(1029)) {
									list=appCfgService.findAppSslList(page, searchAppSslCertCfg).getList();
								}
								if(auditBatchCfg.getServiceId().equals(1152)) {
									list=appCfgService.findAppTopicDomainList(page, searchAppTopicCfg).getList();
								}
								if(auditBatchCfg.getServiceId().equals(1024)) {
									list=appCfgService.findAppHttpList(page, searchAppHttpCfg).getList();
								}
								if(!StringUtil.isEmpty(list)){
									if(auditBatchCfg.getIsValid().equals(1)) {
										hasData=configSynchronizationService.auditSingleMaatData(cfgType,userRegionList,
												page,auditBatchCfg,list,hasData,true);
									}else {
										List<BaseCfg> newList=new ArrayList<>();
										if(!StringUtil.isEmpty(list)) {
											for (Object obj : list) {
												BaseCfg cfg=(BaseCfg)obj;
												newList.add(cfg);
											}
										}
										hasData=commonPolicyService.auditNtcMaatData(cfgList, page, auditBatchCfg,newList, hasData);
									}
									if(hasData) {
										page.setPageNo(1);
									}
								}else{
									hasData = false;
								}
							}
						}
						
					}
				}
			}else if("2".equals(serviceTypeXml)){
				/********************回调类处理生效和失效一个方法【由于每个表检索条件不一样，需要自己处理，无法复用全量下发】*********/
				boolean hasData = true;
				while(hasData){
					hasData=handelCallbackData(classNameXml, page, hasData, auditBatchCfg
												, searchCfg
												, searchAppCfg
												, searchDigestCfg
												, searchFileSampleCfg
												, searchObjKeyringCfg
												, searchTrustedCaCertCfg
												, searchTrustedCaCrlCfg
												, searchDnsResStrategy
												, searchDnsIpCfg
												, searchIpPortCfg
												, searchSignSampleCfg);
					if(hasData){
						page.setPageNo(page.getNext());
					}
				}
			}
		}
	}
	
	
	
	public List getDataList(Page searchPage
							,CfgIndexInfo searchCfg
							,AppPolicyCfg searchAppCfg
							,FileDigestCfg searchDigestCfg
							,AppFeatureIndex searchAppFeatureIndex){
		BaseCfg baseCfg=new BaseCfg<>();
		if(!StringUtil.isEmpty(searchCfg)) {
			BeanUtils.copyProperties(searchCfg, baseCfg);
		}
		if(!StringUtil.isEmpty(searchAppCfg)) {
			BeanUtils.copyProperties(searchAppCfg, baseCfg);
		}
		if(!StringUtil.isEmpty(searchDigestCfg)) {
			BeanUtils.copyProperties(searchDigestCfg, baseCfg);
		}
		if(!StringUtil.isEmpty(searchAppFeatureIndex)) {
			BeanUtils.copyProperties(searchAppFeatureIndex, baseCfg);
		}
		Page pageResult=new Page();
		if(baseCfg.getFunctionId().equals(5)//IP
				|| baseCfg.getFunctionId().equals(3)) { //IP WHITELIST
			pageResult=ipCfgService.getIpCfgList(searchPage,searchCfg);
		}else if(baseCfg.getFunctionId().equals(212) //IP Payload
				|| baseCfg.getFunctionId().equals(7) //DNS
				|| baseCfg.getFunctionId().equals(8)//HTTP ADVANCED
				|| baseCfg.getFunctionId().equals(635) //HTTP 关键字
				|| baseCfg.getFunctionId().equals(37) //MAIL ADDRESS
				|| baseCfg.getFunctionId().equals(35) //MAIL ADVANCED
				|| baseCfg.getFunctionId().equals(34)) {//SSL
			pageResult=websiteCfgService.getWebsiteList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(6)) {//HTTP URL
			pageResult=domainService.getDomainList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(63)//Stream basic 
				|| baseCfg.getFunctionId().equals(407)//Stream app 
				|| baseCfg.getFunctionId().equals(408)) {//Stream behavior 
			pageResult=appCfgService.findAppPolicyList(searchPage, searchAppCfg);
		}else if(baseCfg.getFunctionId().equals(61)) {//BGP
			pageResult=bgpCfgService.getBgpList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(51)) {//FTP
			pageResult=fileTransferCfgService.getFtpList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(510)) {//P2P
			pageResult=fileTransferCfgService.getFtpList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(22)) {//Stream Media
			pageResult=avContentCfgService.findPage(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(24)) {//VOIP
			pageResult=avContentCfgService.findPage(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(52)) {//Digest
			pageResult=fileTransferCfgService.getFileDigestList(searchPage, searchDigestCfg);
		}else if(baseCfg.getFunctionId().equals(200)//intercept ip
				||baseCfg.getFunctionId().equals(201)) {//intercept domain
			pageResult=interceptCfgService.getWebsiteList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(207) //control
				||baseCfg.getFunctionId().equals(208)
				||baseCfg.getFunctionId().equals(209)
				||baseCfg.getFunctionId().equals(210)
				||baseCfg.getFunctionId().equals(211)) {
			pageResult=httpRedirectCfgService.getHttpRedirectList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(213)) {// cache
			pageResult=cachePolicyService.getCachePolicyList(searchPage, searchCfg);
		}else if(baseCfg.getFunctionId().equals(563) //app payload
				||baseCfg.getFunctionId().equals(564)//app dns
				||baseCfg.getFunctionId().equals(565)//app http
				||baseCfg.getFunctionId().equals(566)//app ssl
				||baseCfg.getFunctionId().equals(567)) {//dk_gl
			pageResult=appMultiFeatureCfgService.findAppFeatureIndexList(searchPage,searchAppFeatureIndex);
		}
		return pageResult.getList();
	}
	
	private boolean handelCallbackData(String className,Page page,boolean hasData,BaseCfg entity
								,CfgIndexInfo searchCfg
								,AppPolicyCfg searchAppCfg
								,FileDigestCfg searchDigestCfg
								,AvFileSampleCfg searchFileSampleCfg
								,PxyObjKeyring searchObjKeyringCfg
								,PxyObjTrustedCaCert searchTrustedCaCertCfg
								,PxyObjTrustedCaCrl searchTrustedCaCrlCfg
								,DnsResStrategy searchDnsResStrategy
								,DnsIpCfg searchDnsIpCfg
								,IpPortCfg searchIpPortCfg
								,AvSignSampleCfg searchSignSampleCfg) {	
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List list = new ArrayList();
		List newList = new ArrayList();
		List ids = new ArrayList();
		List<Integer> compileIds = Lists.newArrayList();
		if(entity.getServiceId().equals(3)){//ip drop回调类配置用了主表和子表关系
			list = getDataList(page, searchCfg, null, null,null);
			if(!StringUtil.isEmpty(list)){
				for(int i=0;i<list.size();i++){
					CfgIndexInfo cfg = (CfgIndexInfo) list.get(i);
					compileIds.add(cfg.getCompileId());
				}
				List subList = commonPolicyService.getIpDropList("ip_port_cfg", compileIds);
				for(int i=0;i<subList.size();i++){
					IpPortCfg cfg = (IpPortCfg) subList.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.add(BaseService.convertCallBackIp(cfg,cfg.getGroupId()));
				}
				
			}
		}else{
			if(className.equals("AvFileSampleCfg")){
				list = avCfgService.getAvFileSampleList(page,searchFileSampleCfg).getList();
				for(int i=0;i<list.size();i++){
					BaseCfg cfg = (BaseCfg) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					ids.add(cfg.getCompileId());
					newList.add(cfg);
				}
			}else if(className.equals("AvSignSampleCfg")){
				list = avCfgService.getSignSampleList(searchSignSampleCfg);
				for(int i=0;i<list.size();i++){
					BaseCfg cfg = (BaseCfg) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					ids.add(cfg.getCompileId());
					newList.add(cfg);
				}
			}else if(className.equals("PxyObjKeyring")){
				list = pxyObjKeyringService.findPage(page, searchObjKeyringCfg).getList();
				for(int i=0;i<list.size();i++){
					PxyObjKeyring cfg = (PxyObjKeyring) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.add(BaseService.convertCallBackProxyObjKeyring(cfg));
					ids.add(cfg.getCompileId());
					newList.add(cfg);
				}
			}else if(className.equals("PxyObjTrustedCaCert")){
				list = pxyObjKeyringService.findTrustedCertPage(page,searchTrustedCaCertCfg).getList();
				for(int i=0;i<list.size();i++){
					PxyObjTrustedCaCert cfg = (PxyObjTrustedCaCert) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.addAll(BaseService.convertCallBackProxyObjTrustedCa(cfg,null));
					ids.add(cfg.getCompileId());
				}
				if(!StringUtil.isEmpty(ids)) {
					list=new ArrayList<>();
					list=commonPolicyService.getPxyObjTrustedCrlCfgListByCertId(ids);
					for(int i=0;i<list.size();i++){
						PxyObjTrustedCaCrl cfg = (PxyObjTrustedCaCrl) list.get(i);
						cfg.setIsValid(entity.getIsValid());
						cfg.setAuditTime(entity.getAuditTime());
						newList.addAll(BaseService.convertCallBackProxyObjTrustedCa(null,cfg));
					}
				}
			}/*//批量下发时不需要单独下发CRL
				else if(className.equals("PxyObjTrustedCaCrl")){ 
				list = pxyObjKeyringService;
				for(int i=0;i<list.size();i++){
					PxyObjTrustedCaCrl cfg = (PxyObjTrustedCaCrl) list.get(i);
					newList.add(BaseService.convertCallBackProxyObjTrustedCa(null,cfg));
					ids.add(cfg.getCompileId());
				}
			}*/else if(className.equals("DnsResStrategy")){
				list = dnsResStrategyService.findPage(page,searchDnsResStrategy).getList();
				for(int i=0;i<list.size();i++){
					DnsResStrategy cfg = (DnsResStrategy) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.add(BaseService.convertCallBackDnsResStrategy(cfg));
					ids.add(cfg.getCompileId());
				}
			}else if(className.equals("DnsIpCfg")){
				list = dnsIpCfgService.findPage(page,searchDnsIpCfg).getList();
				for(int i=0;i<list.size();i++){
					DnsIpCfg cfg = (DnsIpCfg) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.add(BaseService.convertCallBackIp(cfg,cfg.getDnsStrategyId()));
					ids.add(cfg.getCompileId());
				}
			}else if(className.equals("IpPortCfg")){
				list = commonPolicyService.getIpPortList(searchIpPortCfg);
				for(int i=0;i<list.size();i++){
					IpPortCfg cfg = (IpPortCfg) list.get(i);
					cfg.setIsValid(entity.getIsValid());
					cfg.setAuditTime(entity.getAuditTime());
					newList.add(BaseService.convertCallBackIp(cfg,cfg.getGroupId()));
					ids.add(cfg.getCompileId());
				}
			}
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		
		if(!StringUtil.isEmpty(newList)) {
			//批量修改和下发回调配置
			commonPolicyService.auditCallBackData(newList, compileIds, ids, entity);
		}else {
			hasData = false;
		}
		
		return hasData;
	}
	
}
