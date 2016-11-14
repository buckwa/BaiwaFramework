package com.buckwa.web.controller.json;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.buckwa.domain.BuckWaUser;
import com.buckwa.domain.UserSession;
import com.buckwa.domain.common.BuckWaRequest;
import com.buckwa.domain.common.BuckWaResponse;
import com.buckwa.domain.pam.Person;
import com.buckwa.domain.pbp.AcademicKPI;
import com.buckwa.domain.pbp.AcademicKPIAttachFile;
import com.buckwa.domain.pbp.AcademicKPIAttribute;
import com.buckwa.domain.pbp.AcademicKPIAttributeValue;
import com.buckwa.domain.pbp.AcademicKPIUserMapping;
import com.buckwa.domain.pbp.AcademicKPIUserMappingWrapper;
import com.buckwa.domain.pbp.AcademicKPIWrapper;
import com.buckwa.domain.pbp.AcademicUnitWrapper;
import com.buckwa.domain.pbp.AcademicYearWrapper;
import com.buckwa.domain.pbp.AnonymousWrapper;
import com.buckwa.domain.pbp.Department;
import com.buckwa.domain.pbp.MarkRankWrapper;
import com.buckwa.domain.pbp.PBPWorkType;
import com.buckwa.domain.pbp.PBPWorkTypeSub;
import com.buckwa.domain.pbp.PBPWorkTypeWrapper;
import com.buckwa.domain.pbp.report.DepartmentWorkTypeReport;
import com.buckwa.domain.pbp.report.RadarPlotReport;
import com.buckwa.domain.pbp3.ResponseObj;
import com.buckwa.domain.pbp3.WorkSummary;
import com.buckwa.domain.pbp3.WorkType;
import com.buckwa.service.impl.PersonDetailService;
import com.buckwa.service.intf.pam.FileLocationService;
import com.buckwa.service.intf.pam.PersonProfileService;
import com.buckwa.service.intf.pbp.AcademicKPIService;
import com.buckwa.service.intf.pbp.AcademicKPIUserMappingService;
import com.buckwa.service.intf.pbp.AcademicUnitService;
import com.buckwa.service.intf.pbp.AcademicYearService;
import com.buckwa.service.intf.pbp.FacultyService;
import com.buckwa.service.intf.pbp.HeadService;
import com.buckwa.service.intf.pbp.MarkRankService;
import com.buckwa.service.intf.pbp.PBPWorkTypeService;
import com.buckwa.service.intf.util.PathUtil;
import com.buckwa.util.BeanUtils;
import com.buckwa.util.BuckWaConstants;
import com.buckwa.util.BuckWaUtils;
import com.buckwa.util.FileUtils;
import com.buckwa.util.PAMConstants;
import com.buckwa.util.school.SchoolUtil;
import com.buckwa.web.util.AcademicYearUtil;

import baiwa.util.UserLoginUtil;

@RestController
@RequestMapping("/person")
public class JSONPersonController {
	private static Logger logger = LoggerFactory.getLogger(JSONPersonController.class);

	@Autowired
	private PersonProfileService personProfileService;

	@Autowired
	private FacultyService facultyService;

	@Autowired
	private PBPWorkTypeService pBPWorkTypeService;

	@Autowired
	private SchoolUtil schoolUtil;

	@Autowired
	private AcademicYearUtil academicYearUtil;

	@Autowired
	private HeadService headService;

	@Autowired
	private AcademicUnitService academicUnitService;

	@Autowired
	private AcademicKPIService academicKPIService;

	@Autowired
	private AcademicKPIUserMappingService academicKPIUserMappingService;

	@Autowired
	private PersonDetailService personDetailService;

	@Autowired
	private FileLocationService fileLocationService;
	
	@Autowired
	private PathUtil pathUtil;
	
	@Autowired
	private AcademicYearService academicYearService;
	
	@Autowired
	private MarkRankService markRankService;	

	@RequestMapping(value = "/getPersonByAcademicYear/{userName}/{year}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Person getPersonByAcademicYear(HttpServletRequest httpRequest, @PathVariable String userName,
			@PathVariable String year) {

		Person person = new Person();

		try {

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", userName);
			request.put("academicYear", year);
			BuckWaResponse response = personProfileService.getByUsername(request);
			person = (Person) response.getResObj("person");

		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return person;
	}

	@RequestMapping(value = "/getRadarPlotNew/{userName}/{year}/{round}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> radarPlotNew(HttpServletRequest httpRequest, @PathVariable String userName,
			@PathVariable String year, @PathVariable String round) {

		List<RadarPlotReport> returnList = new ArrayList<RadarPlotReport>();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("initPerson");

		try {

			String academicYear = year;

			logger.info("radarPlotNew  username :" + userName + " academicYear:" + academicYear + " round :" + round);

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", userName);
			request.put("academicYear", academicYear);

			BuckWaResponse response = new BuckWaResponse();

			Person person = new Person();

			response = personProfileService.getByUsername(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				person = (Person) response.getResObj("person");

				// user.setFirstLastName(person.getThaiName() + " " +
				// person.getThaiSurname());

				person.setAcademicYear(academicYear);
				person.setAcademicYearList(academicYearUtil.getAcademicYearList());
				person.setEvaluateRound(round);
				// user.setPersonProfile(person);
				mav.addObject("person", person);

				String facultyCode = person.getFacultyCode();

				request.put("academicYear", academicYear);
				request.put("userName", userName);
				request.put("round", person.getEvaluateRound());
				request.put("employeeType", person.getEmployeeType());
				request.put("facultyCode", facultyCode);

				response = pBPWorkTypeService.getRadarPlotPersonMark(request);

				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					returnList = (List<RadarPlotReport>) response.getResObj("radarPlotReportList");

				}
			} else {
				response.setStatus(BuckWaConstants.FAIL);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject(BuckWaConstants.ERROR_CODE, BuckWaConstants.ERROR_E001);
		}

		return returnList;
	}

	@RequestMapping(value = "/getRadarPlotNewByYear/{academicYear}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> radarPlotNewByYear(HttpServletRequest httpRequest, @PathVariable String academicYear) {

		List<RadarPlotReport> returnList = new ArrayList<RadarPlotReport>();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("initPerson");

		try {

			// String academicYear = schoolUtil.getCurrentAcademicYear();
			// logger.info(" Start academicYear:" + academicYear);
			BuckWaUser user = BuckWaUtils.getUserFromContext();
			logger.info("radarPlotNew  username :" + user.getUsername() + " academicYear:" + academicYear);

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", user.getUsername());
			request.put("academicYear", academicYear);

			BuckWaResponse response = new BuckWaResponse();

			Person person = new Person();

			response = personProfileService.getByUsername(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				person = (Person) response.getResObj("person");

				user.setFirstLastName(person.getThaiName() + " " + person.getThaiSurname());

				person.setAcademicYear(academicYear);
				person.setAcademicYearList(academicYearUtil.getAcademicYearList());

				user.setPersonProfile(person);
				mav.addObject("person", person);

				String facultyCode = person.getFacultyCode();

				request.put("academicYear", academicYear);
				request.put("userName", BuckWaUtils.getUserNameFromContext());
				request.put("round", person.getEvaluateRound());
				request.put("employeeType", person.getEmployeeType());
				request.put("facultyCode", facultyCode);

				response = pBPWorkTypeService.getRadarPlotPersonMarkByYear(request);

				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					returnList = (List<RadarPlotReport>) response.getResObj("radarPlotReportList");

				}
			} else {
				response.setStatus(BuckWaConstants.FAIL);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject(BuckWaConstants.ERROR_CODE, BuckWaConstants.ERROR_E001);
		}

		return returnList;
	}

	@RequestMapping(value = "/getRadarPlotNewE", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> radarPlotNewE(HttpServletRequest httpRequest) {

		List<RadarPlotReport> returnList = new ArrayList<RadarPlotReport>();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("initPerson");

		try {

			String academicYear = academicYearUtil.getAcademicYear();
			logger.info(" Start  academicYear:" + academicYear);
			BuckWaUser user = BuckWaUtils.getUserFromContext();
			logger.info("viewUserProfile  username :" + user.getUsername());

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", user.getUsername());
			request.put("academicYear", academicYear);

			BuckWaResponse response = new BuckWaResponse();
			// Person person = (Person)
			// httpRequest.getSession().getAttribute("personProFileSession");
			Person person = new Person();

			response = personProfileService.getByUsername(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				person = (Person) response.getResObj("person");

				user.setFirstLastName(person.getThaiName() + " " + person.getThaiSurname());

				person.setAcademicYear(academicYear);
				person.setAcademicYearList(academicYearUtil.getAcademicYearList());
				person.setEvaluateRound("1");
				user.setPersonProfile(person);
				mav.addObject("person", person);

				String facultyCode = person.getFacultyCode();

				request.put("academicYear", academicYear);
				request.put("userName", BuckWaUtils.getUserNameFromContext());
				request.put("round", person.getEvaluateRound());
				request.put("employeeType", person.getEmployeeType());
				request.put("facultyCode", facultyCode);

				response = pBPWorkTypeService.getRadarPlotPersonMarkE(request);

				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					returnList = (List<RadarPlotReport>) response.getResObj("radarPlotReportList");

				}
			} else {
				response.setStatus(BuckWaConstants.FAIL);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject(BuckWaConstants.ERROR_CODE, BuckWaConstants.ERROR_E001);
		}

		return returnList;
	}

	@RequestMapping(value = "/getRadarPlot", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> radarPlot(HttpServletRequest httpRequest) {

		List<RadarPlotReport> returnList = new ArrayList<RadarPlotReport>();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("initPerson");
		mav.addObject(BuckWaConstants.PAGE_SELECT, BuckWaConstants.PERSON_INIT);
		try {

			String academicYear = academicYearUtil.getAcademicYear();
			logger.info(" Start  academicYear:" + academicYear);
			BuckWaUser user = BuckWaUtils.getUserFromContext();
			logger.info("viewUserProfile  username :" + user.getUsername());

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", user.getUsername());
			request.put("academicYear", academicYear);

			BuckWaResponse response = new BuckWaResponse();
			// Person person = (Person)
			// httpRequest.getSession().getAttribute("personProFileSession");
			Person person = new Person();
			// if(null == person){
			response = personProfileService.getByUsername(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				person = (Person) response.getResObj("person");

				user.setFirstLastName(person.getThaiName() + " " + person.getThaiSurname());

				person.setAcademicYear(academicYear);
				person.setAcademicYearList(academicYearUtil.getAcademicYearList());
				person.setEvaluateRound("1");
				user.setPersonProfile(person);
				mav.addObject("person", person);

				String facultyCode = person.getFacultyCode();

				request.put("academicYear", academicYear);
				request.put("userName", BuckWaUtils.getUserNameFromContext());
				request.put("round", person.getEvaluateRound());
				request.put("employeeType", person.getEmployeeTypeNo());
				request.put("facultyCode", facultyCode);

				// response = pBPWorkTypeService.getByAcademicYear(request);
				response = pBPWorkTypeService.getCalculateByAcademicYear(request);

				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					PBPWorkTypeWrapper pBPWorkTypeWrapper = (PBPWorkTypeWrapper) response
							.getResObj("pBPWorkTypeWrapper");
					pBPWorkTypeWrapper.setAcademicYear(academicYear);
					person.setpBPWorkTypeWrapper(pBPWorkTypeWrapper);
				}

			} else {
				response.setStatus(BuckWaConstants.FAIL);
			}
			// }else{
			// response.setStatus(BuckWaConstants.SUCCESS);
			// }

			if (response.getStatus() == BuckWaConstants.SUCCESS) {

				List<PBPWorkType> pBPWorkTypeList = person.getpBPWorkTypeWrapper().getpBPWorkTypeList();
				int loop = 0;
				for (PBPWorkType typeTmp : pBPWorkTypeList) {

					RadarPlotReport radartmp = new RadarPlotReport();
					logger.info(" loop:" + loop);
					String tempLabel = "";
					StringTokenizer st = new StringTokenizer(typeTmp.getName(), " ");
					int numberOfSt = 1;
					while (st.hasMoreElements()) {
						String stStr = st.nextElement().toString();
						logger.info(" numberOfSt:" + numberOfSt + "  stStr:" + stStr);
						if (numberOfSt == 1) {
							tempLabel = stStr;
						}
						if (numberOfSt == 2) {
							// axisLables = axisLables +" "
							// st.nextElement();
						}
						numberOfSt++;
					}
					radartmp.setAxisName(tempLabel);

					loop++;
					// radartmp.setAxisValue(typeTmp.getTotalInPercentCompareBaseWorkType().setScale(0,
					// BigDecimal.ROUND_UP) + "");
					radartmp.setAxisValue(typeTmp.getTotalInWorkType().setScale(0, BigDecimal.ROUND_UP) + "");
					logger.info(" Label:" + radartmp.getAxisName() + "  Value:" + radartmp.getAxisValue());
					returnList.add(radartmp);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject(BuckWaConstants.ERROR_CODE, BuckWaConstants.ERROR_E001);
		}

		return returnList;
	}

	@RequestMapping(value = "/getBarchart", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> getWorkTypeBarChartReport() {
		System.out.println(" ### getBarchart ###");

		List<RadarPlotReport> returnList = new ArrayList<RadarPlotReport>();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("markDepartment");
		try {
			BuckWaRequest request = new BuckWaRequest();

			String userName = BuckWaUtils.getUserNameFromContext();
			String academicYear = schoolUtil.getCurrentAcademicYear();

			request.put("username", userName);
			request.put("academicYear", academicYear);
			BuckWaResponse response = facultyService.getDepartmentByUserNameandYear(request);

			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				Department department = (Department) response.getResObj("department");
				System.out.println(" department :" + BeanUtils.getBeanString(department));

				if (department != null) {

					request.put("department", department);
					request.put("academicYear", academicYear);
					response = headService.getReportWorkTypeDepartment(request);

					if (response.getStatus() == BuckWaConstants.SUCCESS) {
						List<DepartmentWorkTypeReport> reportWorkTypeDepartmentList = (List<DepartmentWorkTypeReport>) response
								.getResObj("departmentWorkTypeReportList");

						request.put("username", userName);
						request.put("academicYear", academicYear);
						response = personProfileService.getByUsername(request);
						if (response.getStatus() == BuckWaConstants.SUCCESS) {
							Person person = (Person) response.getResObj("person");
							String firstLast = person.getThaiName() + " " + person.getThaiSurname();
							System.out.println(" firstLast :" + firstLast);

							int loopx = 1;
							for (DepartmentWorkTypeReport personTmp : reportWorkTypeDepartmentList) {
								String personName = personTmp.getPersonName();
								RadarPlotReport reportTmp = new RadarPlotReport();
								System.out.println(" firstLast :" + firstLast + "|   personName:" + personName);
								if (!firstLast.equalsIgnoreCase(personName)) {
									reportTmp.setAxisName(" ");
								} else {
									reportTmp.setAxisName(personTmp.getPersonName());
								}

								reportTmp.setAxisValue(personTmp.getMarkTotal());

								reportTmp.setOrderNo(loopx);
								returnList.add(reportTmp);
								reportTmp.setMean("333");
								loopx++;
							}

							mav.addObject("department", department);
						}
					}

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject("errorCode", "E001");
		}
		return returnList;
	}

	/*
	 * @RequestMapping(value = "/getBarchart", method =
	 * RequestMethod.GET,headers="Accept=application/json") public
	 * List<RadarPlotReport> getBarChartReport( ) {
	 * 
	 * List<RadarPlotReport> returnList = new ArrayList(); ModelAndView mav =
	 * new ModelAndView(); mav.setViewName("markDepartment"); try{ BuckWaRequest
	 * request = new BuckWaRequest();
	 * 
	 * String userName = BuckWaUtils.getUserNameFromContext(); String
	 * academicYear =schoolUtil.getCurrentAcademicYear();
	 * 
	 * request.put("userName",BuckWaUtils.getUserNameFromContext());
	 * request.put("academicYear",academicYear); request.put("status","");
	 * BuckWaResponse response = headService.getDepartmentMarkByUser(request);
	 * if(response.getStatus()==BuckWaConstants.SUCCESS){ Department department
	 * = (Department)response.getResObj("department");
	 * department.setAcademicYear(academicYear); request = new BuckWaRequest();
	 * request.put("academicYear",academicYear); response =
	 * pBPWorkTypeService.getByAcademicYear(request);
	 * if(response.getStatus()==BuckWaConstants.SUCCESS){ PBPWorkTypeWrapper
	 * pBPWorkTypeWrapper =
	 * (PBPWorkTypeWrapper)response.getResObj("pBPWorkTypeWrapper");
	 * List<PBPWorkType> pBPWorkTypeList =
	 * pBPWorkTypeWrapper.getpBPWorkTypeList();
	 * 
	 * 
	 * for(PBPWorkType typeTmp:pBPWorkTypeList){ String shortDesc ="";
	 * StringTokenizer st = new StringTokenizer(typeTmp.getName(), " "); int
	 * numberOfSt =1; while (st.hasMoreElements()) {
	 * 
	 * String stStr = st.nextElement().toString();
	 * logger.info(" numberOfSt:"+numberOfSt+"  stStr:"+ stStr);
	 * if(numberOfSt==1){ shortDesc = stStr; } if(numberOfSt==2){ //axisLables =
	 * axisLables +" " //st.nextElement(); } numberOfSt++; }
	 * 
	 * typeTmp.setShortDesc(shortDesc);
	 * 
	 * // Sum total mark List<AcademicPerson> academicPersonListMark =
	 * department.getAcademicPersonList(); BigDecimal totalMark = new
	 * BigDecimal(0.00); for(AcademicPerson personTmp: academicPersonListMark){
	 * 
	 * List<PBPWorkType> pBPWorkTypeListTotalMark =
	 * personTmp.getpBPWorkTypeWrapper().getpBPWorkTypeList();
	 * 
	 * for(PBPWorkType totalMarkTmp:pBPWorkTypeListTotalMark){
	 * System.out.print(" totalMarkTmp id:"+totalMarkTmp.getWorkTypeId());
	 * 
	 * if(typeTmp.getWorkTypeId().intValue()==totalMarkTmp.getWorkTypeId().
	 * intValue ()){
	 * 
	 * 
	 * totalMark =
	 * totalMark.add(totalMarkTmp.getTotalInPercentCompareBaseWorkType());
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * typeTmp.setTotalAllWorkType(totalMark);
	 * typeTmp.setTotalInPercentCompareBaseWorkType(totalMark);
	 * 
	 * }
	 * 
	 * department.setpBPWorkTypeList(pBPWorkTypeList);
	 * 
	 * } List<AcademicPerson> personListtmp =department.getAcademicPersonList();
	 * //BuckWaUser user = BuckWaUtils.getUserFromContext();
	 * request.put("username",userName);
	 * request.put("academicYear",academicYear); response
	 * =personProfileService.getByUsername(request);
	 * if(response.getStatus()==BuckWaConstants.SUCCESS){ Person person =
	 * (Person) response.getResObj("person"); String personRegId =
	 * person.getRegId();
	 * 
	 * int loopx =0; for(AcademicPerson personTmp: personListtmp){ String
	 * tmpRegId = personTmp.getRegId(); RadarPlotReport reportTmp = new
	 * RadarPlotReport(); if(!personRegId.equalsIgnoreCase(tmpRegId)){
	 * reportTmp.setAxisName( " " ); }else{
	 * reportTmp.setAxisName(personTmp.getThaiName()+" "+
	 * personTmp.getThaiSurname()); }
	 * reportTmp.setAxisValue(personTmp.getpBPWorkTypeWrapper
	 * ().getTotalPercentMarkCompareBase()+"");
	 * 
	 * returnList.add(reportTmp);
	 * 
	 * } }
	 * 
	 * 
	 * mav.addObject("department", department); } }catch(Exception ex){
	 * ex.printStackTrace(); mav.addObject("errorCode", "E001"); } return
	 * returnList; }
	 */

	@RequestMapping(value = "/getWorkTypeBarchart/{worktypecode}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RadarPlotReport> getWorkTypeBarChartReport(@PathVariable String worktypecode) {

		List<RadarPlotReport> returnList = new ArrayList();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("markDepartment");
		try {
			BuckWaRequest request = new BuckWaRequest();

			// String userName = BuckWaUtils.getUserNameFromContext();
			// String academicYear = schoolUtil.getCurrentAcademicYear();

			String userName = UserLoginUtil.getCurrentUserLogin();
			String academicYear = "2558";

			request.put("username", userName);
			request.put("academicYear", academicYear);
			BuckWaResponse response = facultyService.getDepartmentByUserNameandYear(request);

			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				Department department = (Department) response.getResObj("department");

				if (department != null) {

					request.put("department", department);
					request.put("academicYear", academicYear);
					response = headService.getReportWorkTypeDepartment(request);

					if (response.getStatus() == BuckWaConstants.SUCCESS) {
						List<DepartmentWorkTypeReport> reportWorkTypeDepartmentList = (List<DepartmentWorkTypeReport>) response
								.getResObj("departmentWorkTypeReportList");

						request.put("username", userName);
						request.put("academicYear", academicYear);
						response = personProfileService.getByUsername(request);
						if (response.getStatus() == BuckWaConstants.SUCCESS) {
							Person person = (Person) response.getResObj("person");
							String firstLast = person.getThaiName() + " " + person.getThaiSurname();

							int loopx = 0;
							for (DepartmentWorkTypeReport personTmp : reportWorkTypeDepartmentList) {
								String tmpRegId = personTmp.getPersonName();
								RadarPlotReport reportTmp = new RadarPlotReport();
								if (!firstLast.equalsIgnoreCase(tmpRegId)) {
									reportTmp.setAxisName(" ");
								} else {
									reportTmp.setAxisName(personTmp.getPersonName());
								}

								if ("1".equals(worktypecode)) {
									reportTmp.setAxisValue(personTmp.getMark1());
								} else if ("2".equals(worktypecode)) {
									reportTmp.setAxisValue(personTmp.getMark2());
								} else if ("3".equals(worktypecode)) {
									reportTmp.setAxisValue(personTmp.getMark3());
								} else if ("4".equals(worktypecode)) {
									reportTmp.setAxisValue(personTmp.getMark4());
								} else if ("5".equals(worktypecode)) {
									reportTmp.setAxisValue(personTmp.getMark5());
								}
								reportTmp.setOrderNo(loopx);
								returnList.add(reportTmp);

							}

							mav.addObject("department", department);
						}
					}

				}
			}
			logger.info(" worktypecode in :" + worktypecode);
		} catch (Exception ex) {
			ex.printStackTrace();
			mav.addObject("errorCode", "E001");
		}
		return returnList;
	}

	@RequestMapping(value = "/getUserSession", method = RequestMethod.GET, headers = "Accept=application/json")
	public UserSession getUserSession(HttpServletRequest httpRequest) {
		UserSession userreturn = new UserSession();
		try {
			logger.info(" getUserSession ");
			// getUsername login.
			String user = UserLoginUtil.getCurrentUserLogin();

			System.out.println("Current UserLogin :" + user);
			// userreturn = BuckWaUtils.getUserFromContext();

			userreturn = personDetailService.GetUserSession(user);

			// userreturn.setUserName(user);
			// userreturn.setFirstName("พิทักษ์ ");
			// userreturn.setLastName("ธรรมวาริน");
			userreturn.setCurrentAcademicYear("2558");
			userreturn.setFacultyCode("01");
			userreturn.setDepartmentCode("05");
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return userreturn;
	}

	@RequestMapping(value = "/getAllWorkList/{academicYear}/{facultyCode}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<WorkType> getAllWorkList(@PathVariable String academicYear, @PathVariable String facultyCode) {
		logger.info(" Start ");

		List<WorkType> returnList = new ArrayList();
		try {
			BuckWaRequest request = new BuckWaRequest();

			request.put("academicYear", academicYear);
			request.put("facultyCode", facultyCode);
			BuckWaResponse response = pBPWorkTypeService.getByAcademicYearFacultyCode(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				PBPWorkTypeWrapper pBPWorkTypeWrapper = (PBPWorkTypeWrapper) response.getResObj("pBPWorkTypeWrapper");
				List<PBPWorkType> workTypeListx = pBPWorkTypeWrapper.getpBPWorkTypeList();
				List<PBPWorkType> workTypeList = workTypeListx;
				if (workTypeList != null && workTypeList.size() > 0) {

					for (PBPWorkType workTmp : workTypeList) {
						String workTypeCodeTmp = workTmp.getCode();
						String workTypeNameTmp = workTmp.getName();
						logger.info(" WorkType: " + workTypeCodeTmp + ":" + workTypeNameTmp);

						WorkType newWork = new WorkType();
						newWork.setWorkTypeName(workTypeNameTmp);

						request.put("workTypeCode", workTypeCodeTmp);
						response = academicKPIService.getByAcademicYearWorkTypeCodeFacultyCode(request);
						if (response.getStatus() == BuckWaConstants.SUCCESS) {
							AcademicKPIWrapper academicKPIWrapper = (AcademicKPIWrapper) response
									.getResObj("academicKPIWrapper");

							newWork.setAcademicKPIList(academicKPIWrapper.getAcademicKPIList());
						}

						returnList.add(newWork);

					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return returnList;
	}

	// @RequestMapping(value="getAcademicWork.htm", method = RequestMethod.GET)
	// public ModelAndView initAcademicWorkGET(HttpServletRequest httpRequest )
	// {
	@RequestMapping(value = "/getAcademicWork/{userName}/{academicYear}/{round}", method = RequestMethod.GET, headers = "Accept=application/json")
	public WorkSummary getAllWorkList(@PathVariable String userName, @PathVariable String academicYear,
			@PathVariable String round) {

		logger.info(" Start ");
		WorkSummary workSummary = new WorkSummary();
		workSummary.setAcademicYear(academicYear);
		try {

			BuckWaRequest request = new BuckWaRequest();
			request.put("username", userName);
			request.put("academicYear", academicYear);

			BuckWaResponse response = personProfileService.getByUsername(request);

			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				Person person = (Person) response.getResObj("person");
				person.setAcademicYear(academicYear);
				person.setEvaluateRound(round);

				// String academicYear =schoolUtil.getCurrentAcademicYear();
				String facultyCode = person.getFacultyCode();
				request.put("academicYear", academicYear);
				request.put("userName", userName);
				request.put("round", round);
				request.put("employeeType", person.getEmployeeTypeNo());
				request.put("facultyCode", facultyCode);

				response = pBPWorkTypeService.getCalculateByAcademicYear(request);

				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					PBPWorkTypeWrapper pBPWorkTypeWrapper = (PBPWorkTypeWrapper) response
							.getResObj("pBPWorkTypeWrapper");
					pBPWorkTypeWrapper.setAcademicYear(academicYear);
					person.setpBPWorkTypeWrapper(pBPWorkTypeWrapper);

					workSummary.setTotalMark(pBPWorkTypeWrapper.getTotalMark() + "");
					workSummary.setpBPWorkTypeList(pBPWorkTypeWrapper.getpBPWorkTypeList());
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return workSummary;
	}

	@RequestMapping(value = "/getAcademicKPI/{academicKPICode}/{facultyCode}/{academicYear}", method = RequestMethod.GET, headers = "Accept=application/json")
	public AcademicKPI getAcademicKPI(@PathVariable String academicKPICode, @PathVariable String facultyCode,
			@PathVariable String academicYear) {

		logger.info(" Start  academicKPICode:" + academicKPICode + " academicYear:" + academicYear);
		AcademicKPI academicKPI = new AcademicKPI();
		try {
			BuckWaRequest request = new BuckWaRequest();

			request.put("academicYear", academicYear);
			request.put("academicKPICode", academicKPICode);
			request.put("facultyCode", facultyCode);
			BuckWaResponse response = academicKPIService.getByCodeAcademicYear(request);

			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				academicKPI = (AcademicKPI) response.getResObj("academicKPI");

				request.put("academicYear", academicYear);
				response = academicUnitService.getByAcademicYear(request);
				if (response.getStatus() == BuckWaConstants.SUCCESS) {
					AcademicUnitWrapper academicUnitWrapper = (AcademicUnitWrapper) response
							.getResObj("academicUnitWrapper");
					academicKPI.setAcademicUnitList(academicUnitWrapper.getAcademicUnitList());
					academicKPI.setRatio(new Integer(100));

					List<AcademicKPIAttribute> ratioList = academicKPI.getAcademicKPIAttributeList();
					for (AcademicKPIAttribute tmp : ratioList) {
						String attributeName = tmp.getName();
						// logger.info(" Attribute Name:"+attributeName+" index
						// of สัดส่วน:"+attributeName.indexOf("สัดส่วน"));

						if (attributeName.indexOf("สัดส่วน") != -1) {
							tmp.setValue("100");
						}

					}
				}
				// academicKPI.setIndex(index);
				// mav.addObject("academicKPI", academicKPI);

				// Delete Temp File
				// File uploadPath = new File(pathUtil.getPBPAttatchFilePath() +
				// "temp/" + BuckWaUtils.getUserIdFromContext());
				// if (uploadPath.exists() && uploadPath.isDirectory()) {
				// FileUtils.deleteDirectory(uploadPath);
				// }

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return academicKPI;
	}

	@RequestMapping(value = "/getImportWork/{kpiUserMappingId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public AcademicKPIUserMapping getImportWork(@PathVariable String kpiUserMappingId) {

		logger.info(" Start  kpiUserMappingId:" + kpiUserMappingId);
		AcademicKPIUserMapping kpiUserMapping = new AcademicKPIUserMapping();
		try {
			BuckWaRequest request = new BuckWaRequest();
			request.put("kpiUserMappingId", kpiUserMappingId);
			BuckWaResponse response = academicKPIUserMappingService.getById(request);
			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				AcademicKPIUserMappingWrapper academicKPIUserMappingWrapper = (AcademicKPIUserMappingWrapper) response
						.getResObj("academicKPIUserMappingWrapper");

				kpiUserMapping = academicKPIUserMappingWrapper.getAcademicKPIUserMapping();
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return kpiUserMapping;
	}

	@RequestMapping(value = "/importwork", method = RequestMethod.POST)
	public ResponseObj jsonImportworkPOST(@RequestBody AcademicKPI academicKPI) {
		logger.info(" Start  ");
		ResponseObj resp = new ResponseObj();
		resp.setStatus("0");
		try {

			// String userName = BuckWaUtils.getUserNameFromContext();
			String academicYear = schoolUtil.getCurrentAcademicYear();

			AcademicKPIUserMapping academicKPIUserMapping = new AcademicKPIUserMapping();
			academicKPIUserMapping.setUserName(UserLoginUtil.getCurrentUserLogin());
			academicKPIUserMapping.setAcademicYear(academicYear);
			academicKPIUserMapping.setAcademicKPICode(academicKPI.getCode());
			academicKPIUserMapping.setAcademicKPIId(academicKPI.getAcademicKPIId());
			academicKPIUserMapping.setWorkTypeCode(academicKPI.getWorkTypeCode());
			academicKPIUserMapping.setName(academicKPI.getName());
			academicKPIUserMapping.setRatio(academicKPI.getRatio());

			List<AcademicKPIAttribute> academicKPIAttributeList = academicKPI.getAcademicKPIAttributeList();

			List<AcademicKPIAttributeValue> academicKPIAttributeValueList = new ArrayList<AcademicKPIAttributeValue>();
			for (AcademicKPIAttribute tmp : academicKPIAttributeList) {

				logger.info("  Name: " + tmp.getName() + " value:" + tmp.getValue());
				AcademicKPIAttributeValue valueTmp = new AcademicKPIAttributeValue();
				valueTmp.setAcademicKPICode(academicKPI.getCode());
				valueTmp.setAcademicYear(academicYear);
				valueTmp.setValue(tmp.getValue());
				valueTmp.setName(tmp.getName());
				// valueTmp.(tmp.getRownum());
				valueTmp.setRatio(tmp.getRatio());
				// logger.info(" Controller attribute name:"+tmp.getName()+"
				// value:"+tmp.getValue());
				academicKPIAttributeValueList.add(valueTmp);
			}

			academicKPIUserMapping.setAcademicKPIAttributeValueList(academicKPIAttributeValueList);

			academicKPIUserMapping.setStatus("CREATE");
			// Save
			BuckWaRequest request = new BuckWaRequest();
			request.put("academicKPIUserMapping", academicKPIUserMapping);
			request.put("tmpFileNameList", academicKPI.getTmpFileNameList());
			BuckWaResponse response = academicKPIService.importwork(request);// ทำตรงนี้

			if (response.getStatus() == BuckWaConstants.SUCCESS) {
				Long academicKPIId = (Long) response.getResObj("academicKPIId");
				academicKPI.setAcademicKPIUserMappingId(academicKPIId);
				resp.setResObj(academicKPIId);
				logger.info("  Save Success academicKPIId: " + academicKPIId);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resp.setStatus("1");
			resp.setDescription(ex.getMessage());
		}
		logger.info(" End  ");
		return resp;
	}

	@RequestMapping(value = "/importwork_file", method = RequestMethod.POST)
	public ResponseObj jsonImportwork_filePOST(MultipartHttpServletRequest request, HttpServletResponse response) {
		logger.info("importwork_file Start  ");
		ResponseObj resp = new ResponseObj();
		resp.setStatus("0");

		try {

			logger.info("---- Wait For Uploading File ----");
			String academicKPIId = request.getParameter("academicKPIId");
			logger.info("---- PersonId : ----" + academicKPIId);

			Iterator<String> itr = request.getFileNames();
			MultipartFile originalfile = request.getFile(itr.next());

			if (originalfile != null && originalfile.getSize() > 0) {
				logger.info(" originalfile size:" + originalfile.getSize() + " File Name:"
						+ originalfile.getOriginalFilename());

				// For Upload File >>>>
				String uploadPath = PAMConstants.rbApp.getString("project.root.dir") + "attatchfile/" + academicKPIId
						+ "/";
				logger.info("## File Size :" + originalfile.getSize());
				logger.info("## File Name Original :" + originalfile.getOriginalFilename());
				logger.info("## Upload Path :" + uploadPath);

				String fileUpload = uploadPath + originalfile.getOriginalFilename();

				logger.info("## File Name + Path :" + fileUpload);

				int step = 1;
				boolean isnext = true;

				while (isnext) {
					switch (step) {
					case 1:
						logger.info("Step : " + step + " >>  Create New Upload Path");
						isnext = FileUtils.createDirectoryIfNotExist(uploadPath);
						if (isnext) {
							step++;
							continue;
						} else {
							isnext = false;
						}
					case 2:
						logger.info("Step : " + step + " >> Save File To Server directory path");

						// boolean isFileNameExist =
						// fileLocationService.checkFileNameServerExist(fileName,BuckWaConstants.WORKPERSON_TABLE);
						// if(!isFileNameExist){
						isnext = FileUtils.saveFileToDirectory(originalfile, fileUpload);
						if (isnext) {
							step++;
							continue;
						} else {
							isnext = false;
						}
						// }else{
						// isnext = false;
						// mav.addObject("errorCode",
						// BuckWaConstants.MSGCODE_FILE_NAME_EXIST);
						// }
					case 3:
						logger.info(" Step : " + step
								+ " >> Insert into File createPBPAttachFile Database (table : academic_kpi_attach_file) For File Upload History");

						AcademicKPIAttachFile academicKPIAttachFile = null;

						academicKPIAttachFile = new AcademicKPIAttachFile();
						academicKPIAttachFile.setKpiUserMappingId(String.valueOf(academicKPIId));
						academicKPIAttachFile.setFullFilePathName(uploadPath + originalfile.getOriginalFilename());
						academicKPIAttachFile.setFileName(originalfile.getOriginalFilename());
						academicKPIAttachFile.setCreateBy(UserLoginUtil.getCurrentUserLogin());
						fileLocationService.createPBPAttachFile(academicKPIAttachFile);

					case 4:
						// person.setPicture(fileUpload);
					default:
						isnext = false;
					}
				}

			} else {
				// mav.addObject("errorCode",
				// BuckWaConstants.MSGCODE_SELECT_FILE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			resp.setStatus("1");
			resp.setDescription(ex.getMessage());
		}
		logger.info(" End  ");
		return resp;
	}

	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
	public void UploadFile(MultipartHttpServletRequest request, HttpServletResponse response) {

		String p_text = request.getParameter("p_data");
		System.out.println("GetParameter :" + p_text);
		System.out.println("555+");
		Iterator<String> itr = request.getFileNames();

		while (itr.hasNext()) {

			MultipartFile file = request.getFile(itr.next());

			String fileName = file.getOriginalFilename();
			System.out.println(fileName);
			logger.info("  File Name: " + fileName);
		}
	}

	@RequestMapping(value = "/getImageFile/{personId}", method = RequestMethod.GET)
	public void getImageFile(@PathVariable String personId, HttpServletResponse response)
			throws IOException, URISyntaxException {
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		System.out.println("-----getImageFile By personID----" + personId);
		try {
			String path = PAMConstants.rbApp.getString("project.root.dir") + "profile_picture/" + personId + ".jpg";
			// String path = personId;
			System.out.println("Path File Image :" + path);
			try {

				BufferedImage image = ImageIO.read(new File(path));
				ImageIO.write(image, "jpeg", jpegOutputStream);

			} catch (IOException e) {

				System.out.println("cannot find image :");
				BufferedImage image2 = ImageIO.read(new File("../WebContent/baiwa/libs/img/default.jpg"));
				ImageIO.write(image2, "jpeg", jpegOutputStream);
			}

		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		byte[] imgByte = jpegOutputStream.toByteArray();

		// response.setHeader("Cache-Control", "no-store");
		// response.setHeader("Pragma", "no-cache");
		// response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		response.setContentLength(imgByte.length);
		System.out.println("imgByte.length: " + imgByte.length);
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(imgByte);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	@RequestMapping(value = "/getAcademicWork_File/{KpiID}", method = RequestMethod.GET)
	public void getFileAcademicWork(@PathVariable String KpiID, HttpServletResponse response)
			throws IOException, URISyntaxException {
		response.setContentType("multipart/x-mixed-replace;boundary=END");
		// logger.info("---- Wait For Uploading File ----");
		// String academicKPIId = request.getParameter("academicKPIId");
		// logger.info("---- PersonId : ----"+academicKPIId);
		ServletOutputStream responseOutputStream = response.getOutputStream();
		// ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		System.out.println("-----getFile By KPIID----" + KpiID);
		try {
			String path = PAMConstants.rbApp.getString("project.root.dir") + "attatchfile/" + "510923" + "/";
			// String path = personId;
			System.out.println("Path File Image :" + path);
			File file = new File(path);
			String[] paths;
			// array of files and directory
			paths = file.list();
			for (int i = 0; i < paths.length; i++) {
				System.out.println(" File kpi path" + paths[i]);
				FileInputStream fis = new FileInputStream(path + paths[i]);
				BufferedInputStream fif = new BufferedInputStream(fis);
				int data = 0;
				byte[] Byte = IOUtils.toByteArray(fif);
				responseOutputStream.write(Byte);
				//responseOutputStream.println("--END");
				// while ((data = fif.read()) != -1) {
				// out.write(data);
				// }
				fif.close();
				//out.println("--END");
				responseOutputStream.flush();
			}

			try {

				BufferedImage image = ImageIO.read(new File(path));
				// ImageIO.write(image, "jpeg", jpegOutputStream);

			} catch (IOException e) {

				System.out.println("cannot find image :");
				// BufferedImage image2 = ImageIO.read(new
				// File("../WebContent/baiwa/libs/img/default.jpg"));
				// ImageIO.write(image2, "jpeg", jpegOutputStream);
			}

		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		// byte[] imgByte = jpegOutputStream.toByteArray();

		// response.setHeader("Cache-Control", "no-store");
		// response.setHeader("Pragma", "no-cache");
		// response.setDateHeader("Expires", 0);
		// response.setContentType("image/jpeg");
		// response.setContentLength(imgByte.length);
		// System.out.println("imgByte.length: "+imgByte.length);
		// ServletOutputStream responseOutputStream =
		// response.getOutputStream();
		// responseOutputStream.write(imgByte);
		// responseOutputStream.flush();
		// responseOutputStream.close();
		responseOutputStream.flush();
		responseOutputStream.println("--END--");
		responseOutputStream.close(); 
	}
	
	@RequestMapping(value="/downloadAttachFile/{attachFileId}", method = RequestMethod.GET)
	public ModelAndView downloadAttachFile(@PathVariable String attachFileId ,HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		logger.info("#####  Start  Download   << "+ attachFileId +" >> #### ");
		
		//ModelAndView mav = new ModelAndView();
		//mav.addObject(BuckWaConstants.PAGE_SELECT, BuckWaConstants.ADMIN_INIT);
		AcademicKPIAttachFile academicKPIAttachFile = new AcademicKPIAttachFile();
		
		try {
			
			academicKPIAttachFile = fileLocationService.findPBPAttachFile(attachFileId);
			
			logger.info("File >>"+academicKPIAttachFile);
			
			//String filePath = fileLocation.getFilePath();
			//String fileName = fileLocation.getFileName()+fileLocation.getFileExtension();
			//String fullPath = filePath+fileName;
			
			InputStream inputStream = new FileInputStream(academicKPIAttachFile.getFullFilePathName());
			
			//httpResponse.setContentType(fileLocation.getFileType());
			//httpResponse.setContentLength(Integer.parseInt(fileLocation.getFileSize()));
			
			// Check For IE OR NOT for Encoder fileName !
			String user_agent = httpRequest.getHeader("user-agent");
			boolean isInternetExplorer = (user_agent.indexOf(BuckWaConstants.BROWSER_MSIE) > -1);
			if (isInternetExplorer) {
				logger.info("Hello You Are IE ");
				httpResponse.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(academicKPIAttachFile.getFileName(), "utf-8") + "\"");
			} else {
				logger.info("Hello You Not IE ");
				httpResponse.setHeader("Content-disposition", "attachment; filename=\"" + MimeUtility.encodeWord(academicKPIAttachFile.getFileName()) + "\"");
			}
			
			FileCopyUtils.copy( inputStream, httpResponse.getOutputStream());
			   
			httpResponse.flushBuffer();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			//mav.addObject("errorCode", BuckWaConstants.ERROR_E001);
		}
		
		return null;

	}
	@RequestMapping(value="/deleteAttachFile/{kpiUserMappingId}/{fileName}/{attachFileId}",method = RequestMethod.GET)
	public ModelAndView deleteAttachFile(@PathVariable String kpiUserMappingId,@PathVariable String attachFileId,@PathVariable String fileName ) {
	 
		logger.info(" kpiUserMappingId:"+kpiUserMappingId+" attachFileId:"+attachFileId+" fileName:" + fileName);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject(BuckWaConstants.PAGE_SELECT, BuckWaConstants.ADMIN_INIT);
		mav.setViewName("viewImportWork"); 
		try{
			BuckWaRequest request = new BuckWaRequest(); 
			request.put("attachFileId",attachFileId);
			fileLocationService.deletePBPAttachFile(request);
			try {
				String path = PAMConstants.rbApp.getString("project.root.dir") + "attatchfile/"+kpiUserMappingId+"/"+fileName;
				System.out.println("Path delete file :" +path);
				File file = new File(path);
				file.delete();
				logger.info("deletefile sucess.");
				
			} catch (Exception ex) {
				logger.info("deletefile error.");
				ex.printStackTrace();
				
			}
			//mav =  viewWork( kpiUserMappingId);
 	 			
		}catch(Exception ex){
			ex.printStackTrace();
			//mav.addObject("errorCode", BuckWaConstants.ERROR_E001); 
		}		
		return null;
		
	}	
	@RequestMapping(value="/anonymous/listKPIByWorktype/{workTypeCode}/{academicYear}", method = RequestMethod.GET ,headers = "Accept=application/json")
	public AcademicKPIWrapper listByWorktype(@PathVariable String workTypeCode,@PathVariable String academicYear,HttpServletResponse response1) {
		logger.info(" Start  ");
		//ModelAndView mav = new ModelAndView();
		//mav.setViewName("anonymousAcademicKPIList");
		AcademicKPIWrapper academicKPIWrapper = null ;
		try{
			BuckWaRequest request = new BuckWaRequest();
			//String facultyCode = BuckWaUtils.getFacultyCodeFromUserContext();
			String facultyCode = "01" ;
			request.put("academicYear",academicYear);
			request.put("workTypeCode",workTypeCode);
			request.put("facultyCode",facultyCode);
			BuckWaResponse response = academicKPIService.getByAcademicYearWorkTypeCodeFacultyCode(request);
			if(response.getStatus()==BuckWaConstants.SUCCESS){	
				academicKPIWrapper = (AcademicKPIWrapper)response.getResObj("academicKPIWrapper");			 
				academicKPIWrapper.setAcademicYear(academicYear);
				
				
				request.put("academicYear",academicYear);
				request.put("facultyCode",facultyCode);
				 response = pBPWorkTypeService.getByAcademicYearFacultyCode(request);
				if(response.getStatus()==BuckWaConstants.SUCCESS){	
					PBPWorkTypeWrapper pBPWorkTypeWrapper = (PBPWorkTypeWrapper)response.getResObj("pBPWorkTypeWrapper");
					academicKPIWrapper.setpBPWorkTypeList(pBPWorkTypeWrapper.getpBPWorkTypeList());
				} 
				request.put("workTypeCode",workTypeCode);
				request.put("facultyCode",facultyCode);
				 response = pBPWorkTypeService.getByCodeAcademicFacultyCode(request);
				if(response.getStatus()==BuckWaConstants.SUCCESS){	
					PBPWorkType pBPWorkType  = (PBPWorkType)response.getResObj("pBPWorkType");
					 
					academicKPIWrapper.setpBPWorkType(pBPWorkType);
					
					
					List<PBPWorkTypeSub> workTypeSubList  =pBPWorkType.getpBPWorkTypeSubList();
					if(workTypeSubList!=null&&workTypeSubList.size()>0){
						// Set
					}
					 
				}				 
				//mav.addObject("academicKPIWrapper", academicKPIWrapper);	
			}	 

		}catch(Exception ex){
			ex.printStackTrace();
			//mav.addObject("errorCode", "E001"); 
		}
		return academicKPIWrapper;
	}
	
	@RequestMapping(value = "/anonymous/init" , method = RequestMethod.GET ,headers = "Accept=application/json")
	public AnonymousWrapper anonymouse(HttpServletResponse response1) {
		logger.info(" # anonymous 0 ");
		ModelAndView mav = new ModelAndView();
		String academicYear ="2558";
		mav.addObject("academicYearStr",academicYear);
		AnonymousWrapper anonymousWrapper = new AnonymousWrapper();
		anonymousWrapper.setAcademicYear(academicYear);
		mav.addObject("anonymousWrapper", anonymousWrapper);	
		try{
			logger.info(" # anonymous 1 ");
			BuckWaRequest request = new BuckWaRequest();	 
			BuckWaResponse response = academicYearService.getCurrentAcademicYear(request);
			if(response.getStatus()==BuckWaConstants.SUCCESS){	
				AcademicYearWrapper academicYearWrapper = (AcademicYearWrapper)response.getResObj("academicYearWrapper"); 
				anonymousWrapper.setAcademicYearWrapper(academicYearWrapper);
			}	
			 
			logger.info(" # anonymous 2");
			String facultyCode = "01";
			logger.info(" # anonymous facultyCode:"+facultyCode);
			request.put("academicYear",academicYear);
			request.put("facultyCode",facultyCode);
			response = pBPWorkTypeService.getByAcademicYearFacultyCode(request);
			if(response.getStatus()==BuckWaConstants.SUCCESS){	
				PBPWorkTypeWrapper pBPWorkTypeWrapper = (PBPWorkTypeWrapper)response.getResObj("pBPWorkTypeWrapper"); 
				pBPWorkTypeWrapper.setAcademicYear(academicYear);
				anonymousWrapper.setpBPWorkTypeWrapper(pBPWorkTypeWrapper);
			}		
			logger.info(" # anonymous 3 ");
 
			response = markRankService.getByRound(request);
			if(response.getStatus()==BuckWaConstants.SUCCESS){	
				MarkRankWrapper markRankWrapper = (MarkRankWrapper)response.getResObj("markRankWrapper");
			 
				markRankWrapper.setAcademicYear(academicYear);
				anonymousWrapper.setMarkRankWrapper(markRankWrapper);
			}	
			
			 response = academicYearService.getCurrentAcademicYear(request);
			if(response.getStatus()==BuckWaConstants.SUCCESS){	
				AcademicYearWrapper academicYearWrapper = (AcademicYearWrapper)response.getResObj("academicYearWrapper");
				mav.addObject("academicYearWrapper", academicYearWrapper);	
				academicYearWrapper.setAcademicYearList(academicYearUtil.getAcademicYearList());
			}
			
			
			//mav.addObject("academicYearList", academicYearUtil.getAcademicYearList()); 
			//mav.addObject("facultyCode", facultyCode); 
			//mav.addObject("facultyDesc", "01"); 
		 
			
		}catch(Exception ex){
			ex.printStackTrace();
			//mav.addObject("errorCode", "E001"); 
		}
		//mav.setViewName("anonymous");
		
		logger.info(" # anonymous success ");
		return anonymousWrapper;
 
	}
	

}
