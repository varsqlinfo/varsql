package com.varsql.web.app.manager.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.varsql.core.exception.PermissionDeniedException;
import com.varsql.web.app.manager.service.BoardMgmtService;
import com.varsql.web.app.manager.service.ManagerCommonServiceImpl;
import com.varsql.web.common.controller.AbstractController;
import com.varsql.web.constants.HttpParamConstants;
import com.varsql.web.constants.VIEW_PAGE;
import com.varsql.web.dto.board.BoardCommentRequestDTO;
import com.varsql.web.dto.board.BoardRequestDTO;
import com.varsql.web.dto.board.BoardResponseDTO;
import com.varsql.web.model.entity.FileBaseEntity;
import com.varsql.web.model.entity.board.BoardFileEntity;
import com.varsql.web.util.FileServiceUtils;
import com.varsql.web.util.MarkdownXssUtils;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.DataMap;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechUtils;



/**
 * board manage
 * 
 * @author ytkim
 *
 */
@Controller
@RequestMapping("/manager/boardMgmt")
public class BoardMgmtController extends AbstractController {

	private final Logger logger = LoggerFactory.getLogger(BoardMgmtController.class);

	private final BoardMgmtService boardMgmtService;
	
	private final ManagerCommonServiceImpl dbnUserServiceImpl;
	
	public BoardMgmtController(BoardMgmtService boardMgmtService, ManagerCommonServiceImpl dbnUserServiceImpl) {
		this.boardMgmtService = boardMgmtService;
		this.dbnUserServiceImpl = dbnUserServiceImpl; 
	}
	
	/**
	 * board mgmt
	 * 
	 * @param req
	 * @param res
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping({"","/"})
	public ModelAndView boardMgmt(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		model.addAttribute("selectMenu", "boardMgmt");
		model.addAttribute("dbList", dbnUserServiceImpl.selectdbList());
		
		return getModelAndView("/boardMgmt", VIEW_PAGE.MANAGER,model);
	}
	
	@GetMapping("/select")
	public ModelAndView boardSelect(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
		ModelMap model = mav.getModelMap();
		
		return getModelAndView("/boardSelect", VIEW_PAGE.MANAGER_BOARD,model);
	}
	
	/**
	 * list
	 * 
	 * @param boardCode
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/list")
	public @ResponseBody ResponseResult list(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, HttpServletRequest req, HttpServletResponse res) throws Exception {

		return boardMgmtService.list(boardCode, HttpUtils.getSearchParameter(req));
	}

	/**
	 * write
	 * 
	 * @param boardCode
	 * @param req
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/write")
	public  ModelAndView write(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, HttpServletRequest req, ModelAndView mav) throws Exception {

		ModelMap model = mav.getModelMap();
		model.addAttribute(HttpParamConstants.BOARD_CODE, HttpUtils.getServletRequestParam(req).get(HttpParamConstants.BOARD_CODE));
		model.addAttribute("articleInfo", "{}");

		return getModelAndView("/boardWrite", VIEW_PAGE.MANAGER_BOARD, model);
	}
	
	/**
	 * content save
	 * 
	 * @param boardCode
	 * @param boardRequestDTO
	 * @param result
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/save")
	public @ResponseBody ResponseResult save(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @Valid BoardRequestDTO boardRequestDTO
			, BindingResult result, HttpServletRequest req) throws Exception {

		if(result.hasErrors()){

			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("### board save validation check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}

		boardRequestDTO.setBoardCode(boardCode);
		return boardMgmtService.saveBoardInfo(boardRequestDTO);
	}

	/**
	 *
	 * @Method Name  : view
	 * @Method 설명 : 글 보기
	 * @작성자   : ytkim
	 * @작성일   : 2021. 7. 2.
	 * @변경이력  :
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/view")
	public  ModelAndView view(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true) long articleId
			, HttpServletRequest req, ModelAndView mav) throws Exception {

		ModelMap model = mav.getModelMap();
		model.addAttribute("param", HttpUtils.getServletRequestParam(req));
		BoardResponseDTO dto = new BoardResponseDTO();
		dto.setBoardCode(boardCode);
		dto.setArticleId(articleId);
		
		model.addAttribute("articleInfo", VartechUtils.objectToJsonString(dto));
		
		return getModelAndView("/boardDetail", VIEW_PAGE.MANAGER_BOARD, model);
	}
	
	/**
	 * xss 방어 컨텐츠 보기 
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/viewContents")
	public  @ResponseBody ResponseResult viewContents(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true) long articleId
			, HttpServletRequest req, ModelAndView mav) throws Exception {
		
		BoardResponseDTO dto = boardMgmtService.viewBoardInfo(boardCode, articleId);
		dto.setContents(MarkdownXssUtils.sanitizeAndSerializeHTML(dto.getContents()));
		
		return ResponseResult.builder().item(dto).build();
	}
	
	/**
	 * 컨텐츠 보기 
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/contents")
	public  @ResponseBody ResponseResult contents(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true) long articleId
			, HttpServletRequest req, ModelAndView mav) throws Exception {
		
		return ResponseResult.builder().item(boardMgmtService.viewBoardInfo(boardCode, articleId)).build();
	}
	
	/**
	 * content delete
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/delete")
	public @ResponseBody ResponseResult delete(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true)  long articleId
			, HttpServletRequest req
			, HttpServletResponse res) throws Exception {
		return boardMgmtService.deleteBoardInfo(VarsqlUtils.getVonnid(req), articleId);
	}

	/**
	 * content modify
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/modify")
	public  ModelAndView modify(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true)  long articleId
			, HttpServletRequest req
			, ModelAndView mav) throws Exception {

		BoardResponseDTO dto = boardMgmtService.viewBoardInfo(boardCode, articleId);

		if(!dto.isModifyAuth()) {
			throw new PermissionDeniedException("no permission");
		}

		ModelMap model = mav.getModelMap();
		model.addAttribute("param", HttpUtils.getServletRequestParam(req));
		
		return getModelAndView("/boardWrite", VIEW_PAGE.MANAGER_BOARD, model);
	}
	
	/**
	 * comment save
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param boardCommentRequestDTO
	 * @param result
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/commentSave")
	public @ResponseBody ResponseResult commentSave(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true)  long articleId
			, @Valid BoardCommentRequestDTO boardCommentRequestDTO
			, BindingResult result, HttpServletRequest req, HttpServletResponse res) throws Exception {

		if(result.hasErrors()){

			for(ObjectError errorVal :result.getAllErrors()){
				logger.warn("###  comment save validation check {}",errorVal.toString());
			}
			return VarsqlUtils.getResponseResultValidItem(result);
		}
		boardCommentRequestDTO.setBoardCode(boardCode);
		boardCommentRequestDTO.setArticleId(articleId);

		return boardMgmtService.commentSave(articleId, boardCommentRequestDTO);
	}
	
	/**
	 * comment list 
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/commentList")
	public @ResponseBody ResponseResult commentList(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			,@RequestParam(value = "articleId" , required = true)  long articleId
			, HttpServletRequest req, HttpServletResponse res) throws Exception {

		return boardMgmtService.commentList(boardCode, articleId, HttpUtils.getSearchParameter(req));
	}
	
	/**
	 * comment delete
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param commentId
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping(value = "/commentDelete")
	public @ResponseBody ResponseResult commentDelete(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode
			, @RequestParam(value = "articleId" , required = true)  long articleId
			,  @RequestParam(value = "commentId" , required = true) long commentId, HttpServletRequest req) throws Exception {

		return boardMgmtService.commentDelete(articleId, commentId);
	}
	
	/**
	 * 첨부파일 다운로드
	 * 
	 * @param boardCode
	 * @param articleId
	 * @param fileId
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "/file", method= {RequestMethod.GET, RequestMethod.POST})
	public void fileDownload(@RequestParam(value = HttpParamConstants.BOARD_CODE, required = true) String boardCode,
			@RequestParam(value = "articleId", required = true) long articleId,
			@RequestParam(value = "fileId", required = true) String fileId, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		logger.debug("fileDownload");

		DataMap param = HttpUtils.getServletRequestParam(req);

		List<BoardFileEntity> fileList = boardMgmtService.findFileList(articleId, Long.parseLong(fileId));

		if (fileList.size() < 1) {
			res.setContentType("text/html");
			res.setStatus(HttpStatus.OK.value());
			try (PrintWriter out = res.getWriter()) {
				out.write("<script>alert('file not found')</script>");
			}
			return;
		}

		String downFileName = "";
		int fileSize = fileList.size();
		if (fileSize == 1) {
			downFileName = fileList.get(0).getFileName();
		} else {
			downFileName = param.getString("downFileName", "downloadFile");
			downFileName = java.net.URLDecoder.decode(downFileName, "UTF-8");
			downFileName = downFileName + ".zip";
		}

		FileServiceUtils.fileDownload(req, res, downFileName, fileList.toArray(new FileBaseEntity[0]));
	}
}
