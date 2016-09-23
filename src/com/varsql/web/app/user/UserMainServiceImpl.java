package com.varsql.web.app.user;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsql.web.common.vo.DataCommonVO;

@Service
public class UserMainServiceImpl implements UserMainService{
	
	@Autowired
	UserMainDAO userMainDAO;



}