package com.varsql.web.app.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.varsql.web.app.user.service.DetailViewServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.vartech.common.app.beans.ResponseResult;

import lombok.RequiredArgsConstructor;

/**
 * 상세보기 controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/user/detail")
public class DetailViewController extends AbstractController {

    private final Logger logger = LoggerFactory.getLogger(DetailViewController.class);

    private final DetailViewServiceImpl executionHistoryServiceImpl;
    
    /**
	 * execution history detail view
	 * 
	 * @param histSeq
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value="/batch/hist/{histSeq}")
	public @ResponseBody ResponseResult batchHistoryDetail(@PathVariable(required = true, name="histSeq") long histSeq
			, HttpServletRequest req, HttpServletResponse res) throws Exception {
		return executionHistoryServiceImpl.executionHistoryDetail(histSeq);
	}
}
