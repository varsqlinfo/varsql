package com.varsql.web.app.user.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.varsql.core.common.code.VarsqlAppCode;
import com.varsql.core.common.constants.LocaleConstants;
import com.varsql.web.common.service.AbstractService;
import com.varsql.web.constants.ResourceConfigConstants;
import com.varsql.web.dto.user.PasswordRequestDTO;
import com.varsql.web.dto.user.QnARequesetDTO;
import com.varsql.web.dto.user.UserModReqeustDTO;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.model.entity.app.QnAEntity;
import com.varsql.web.model.entity.user.UserEntity;
import com.varsql.web.repository.app.QnAEntityRepository;
import com.varsql.web.repository.spec.QnASpec;
import com.varsql.web.repository.user.UserInfoRepository;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.app.beans.SearchParameter;
import com.vartech.common.crypto.EncryptDecryptException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPreferencesServiceImpl extends AbstractService{
	private final Logger logger = LoggerFactory.getLogger(UserPreferencesServiceImpl.class);

	private final QnAEntityRepository qnaEntityRepository;

	private final UserInfoRepository userInfoRepository;

	@Qualifier(ResourceConfigConstants.APP_PASSWORD_ENCODER)
	private final PasswordEncoder passwordEncoder;

	/**
	 *
	 * @Method Name  : selectUserDetail
	 * @Method 설명 : 사용자 정보 상세.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param loginId
	 * @return
	 */
	public UserEntity findUserInfo(String viewid) {
		return userInfoRepository.findByViewid(viewid);
	}

	/**
	 *
	 * @Method Name  : updateUserInfo
	 * @Method 설명 : 사용자 정보 업데이트
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param userForm
	 * @param req
	 * @param res
	 * @return
	 */
	public boolean updateUserInfo(UserModReqeustDTO userForm, HttpServletRequest req, HttpServletResponse res) {

		logger.debug("updateUserInfo : {}" , userForm);

		UserEntity userInfo = userInfoRepository.findByViewid(SecurityUtil.userViewId());

		if(userInfo==null) throw new VarsqlAppException("user infomation not found : " + SecurityUtil.userViewId());

		userInfo.setLang(userForm.getLang());
		userInfo.setUname(userForm.getUname());
		userInfo.setOrgNm(userForm.getOrgNm());
		userInfo.setDeptNm(userForm.getDeptNm());
		userInfo.setMobileNo(userForm.getMobileNo());
		userInfo.setTelNo(userForm.getTelNo());
		userInfo.setEmpNo(userForm.getEmpNo());
		userInfo.setDescription(userForm.getDescription());

		userInfoRepository.save(userInfo);

		// 언어 변경시 처리.
		Locale userLocale= LocaleConstants.parseLocaleString(userForm.getLang());

		if(userLocale != null  && !userLocale.equals(SecurityUtil.loginInfo().getUserLocale())) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(req);
			if (localeResolver == null) {
				throw new IllegalStateException("No LocaleResolver found.");
			}

			if(localeResolver.resolveLocale(req) != userLocale) {
				localeResolver.setLocale(req, res, userLocale);
			}

			SecurityUtil.loginInfo().setUserLocale(userLocale);
		}

		return true;
	}

	/**
	 *
	 * @Method Name  : updatePasswordInfo
	 * @Method 설명 : 비밀번호 변경.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param passwordForm
	 * @param resultObject
	 * @return
	 * @throws EncryptDecryptException
	 */
	public ResponseResult updatePasswordInfo(PasswordRequestDTO passwordForm) throws EncryptDecryptException {

		UserEntity userInfo = userInfoRepository.findByViewid(SecurityUtil.userViewId());

		ResponseResult resultObject = new ResponseResult();

		if(passwordEncoder.matches(passwordForm.getCurrPw(), userInfo.getUpw())){
			userInfo.setUpw(passwordForm.getUpw());
			userInfoRepository.save(userInfo);
			resultObject.setItemOne(1);
		}else{
			resultObject.setResultCode(VarsqlAppCode.COMM_PASSWORD_NOT_VALID);
		}

		return resultObject;
	}

	/**
	 *
	 * @Method Name  : selectQna
	 * @Method 설명 : qna list
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3.
	 * @변경이력  :
	 * @param searchParameter
	 * @return
	 */
	public ResponseResult searchQna(SearchParameter searchParameter) {
		Page<QnAEntity> result = qnaEntityRepository.findAll(
			QnASpec.userQnaSearch(searchParameter)
			, VarsqlUtils.convertSearchInfoToPage(searchParameter)
		);

		return VarsqlUtils.getResponseResult(result, searchParameter);
	}
	/**
	 *
	 * @Method Name  : insertQnaInfo
	 * @Method 설명 : qna 등록.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 29.
	 * @변경이력  :
	 * @param qnaInfo
	 * @param insFlag
	 * @return
	 */
	public ResponseResult saveQnaInfo(QnARequesetDTO qnaInfo, boolean insFlag) {
		QnAEntity entity= qnaInfo.toEntity();
		qnaEntityRepository.save(entity);
		return VarsqlUtils.getResponseResultItemOne(1);
	}

	/**
	 *
	 * @Method Name  : deleteQnaInfo
	 * @Method 설명 : qna 삭제.
	 * @작성자   : ytkim
	 * @작성일   : 2019. 1. 3.
	 * @변경이력  :
	 * @param qnaid
	 * @return
	 */
	@Transactional(value=ResourceConfigConstants.APP_TRANSMANAGER, rollbackFor=Exception.class)
	public ResponseResult deleteQnaInfo(String qnaid) {

		QnAEntity entity = qnaEntityRepository.findByQnaid(qnaid);

		if(entity.getAnswerId() ==null) {
			qnaEntityRepository.delete(entity);
		}

		ResponseResult result = new ResponseResult();
		result.setMessageCode("answer.msg.notdelete");

		return result;
	}

}