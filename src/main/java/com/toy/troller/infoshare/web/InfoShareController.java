package com.toy.troller.infoshare.web;

import com.toy.troller.infoshare.model.InfoShareDto;
import com.toy.troller.infoshare.service.InfoShareService;
import com.toy.troller.member.model.LoginUserInfo;
import com.toy.troller.model.PageRequestDto;
import com.toy.troller.model.PageResultDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 2021.08.07. 임현수
 * 정보공유 게시판 controller
 */
@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/infoshare")
public class InfoShareController {
    private static final Logger logger = LoggerFactory.getLogger(InfoShareController.class);
    private InfoShareService infoShareService;

    //정보 공유 게시판 메인페이지
    @RequestMapping(value = "/list")
    public String infoshare_list(ModelMap model, PageRequestDto pageRequestDto, @AuthenticationPrincipal LoginUserInfo user){
        logger.info("pageRequestDto ::: " + pageRequestDto);
        logger.info("Login User :::: " + user);
        String tmp = pageRequestDto.getKeyword();  //키워드 백업

        //카테고리의 경우 db에 코드번호로 저장되어 있으므로 keyword 값을 변경하여 조회한다.
        if(pageRequestDto.getType() != null && pageRequestDto.getType().equals("c")) {
        	switch (pageRequestDto.getKeyword().replaceAll(" ", "")) {
			case "java":
			case "JAVA":
			case "자바":
				pageRequestDto.setKeyword("1");
				break;
			case "javascript":
			case "js":
			case "자바스크립트":
				pageRequestDto.setKeyword("2");
				break;
			case "기타":
				pageRequestDto.setKeyword("3");
				break;
			default:
				pageRequestDto.setKeyword(null);
				break;
			}
        }

        PageResultDto result = infoShareService.findAll(pageRequestDto);
        pageRequestDto.setKeyword(tmp);   //카테고리의 경우 위에서 가공하면 숫자로 출력되기 때문에 키워드를 기억해 뒀다가 다시 세팅해준다.
        model.put("result", result);
        model.put("boardList", result.getDtoList());

        return "./infoshare/infoshare_list";
    }

    //정보 공유 게시판 등록 페이지
    @RequestMapping(value = "/input")
    public String infoshare_input(InfoShareDto dto, ModelMap model, @AuthenticationPrincipal LoginUserInfo user){
        logger.info("===== 개발자 정보공유 게시판 글 등록 페이지 open=====");
        logger.info("Login User :::: "+user);

        if(dto.getNttId() != null) {
            InfoShareDto result = infoShareService.getInfoShareDetail(dto.getNttId());
            result.setTitle(xssReplace(result.getTitle()));
            result.setContent(xssReplace(result.getContent()));
            model.put("dto", result);
        }
        return "./infoshare/infoshare_input";
    }

    //정보 공유 게시판 게시글 등록 proc
    @RequestMapping(value = "/register")
    public String infoshare_register(@ModelAttribute InfoShareDto dto, ModelMap model, HttpServletRequest req, HttpServletResponse resp, @AuthenticationPrincipal LoginUserInfo user){
        logger.info("===== 개발자 정보공유 게시글 작성 proc =====");
        logger.info("gubun : "+ req.getParameter("gubun") + " nttId : " + dto.getNttId());
        logger.info("Login User :::: "+user);

        String gubun = req.getParameter("gubun");

        if(user == null)
            throw new RuntimeException("(정보공유게시판) 로그인 정보가 없습니다.");
        if(dto.getTitle() == null || dto.getTitle().isEmpty())
        	throw new RuntimeException("(정보공유게시판) 제목을 입력하여 주십시오.");
        if(dto.getContent() == null || dto.getContent().isEmpty())
        	throw new RuntimeException("(정보공유게시판) 내용을 입력하여 주십시오.");
        if(dto.getNttGb().isEmpty())
        	dto.setNttGb(null);

        if(gubun.equals("save")) {
            infoShareService.register(dto, user.getUsername());
        }else {
            infoShareService.modify(dto, user.getUsername());
        }
        return "redirect:list";
    }

    //정보 공유 게시판 상세 페이지
    @RequestMapping(value = "/infoShareDetail")
    public String infoshare_infoShareDetail(Long nttId, @ModelAttribute("requestDto") PageRequestDto requestDto, ModelMap model, @AuthenticationPrincipal LoginUserInfo user){
        logger.info("===== 개발자 정보공유 상세 페이지 =====");
        logger.info("nttId : " + nttId);

        InfoShareDto dto = infoShareService.getInfoShareDetail(nttId);
        model.put("dto", dto);
        return "./infoshare/infoshare_infoShareDetail";
    }

    //정보 공유 게시판 게시글 삭제
    @RequestMapping(value = "/remove")
    public String infoshare_remove(Long nttId, ModelMap model, @AuthenticationPrincipal LoginUserInfo user){
        logger.info("===== 개발자 정보공유 게시글 삭제=====");
        logger.info("nttId : " + nttId);

        infoShareService.remove(nttId);
        return "redirect:list";
    }

    //파일 업로드
    @RequestMapping(value = "/fileUpload_ajax")
    @ResponseBody
    public JsonObject fileUpload(@RequestParam("file") MultipartFile multipartFile,
                             HttpServletRequest request){
        logger.info("==== 파일 업로드 ====");
        JsonObject jsonObject = new JsonObject();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");

        //String fileRoot = "C:\\summernote_files\\";                                          //외부 경로로 저장
        //String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");          //내부 경로로 저장
        //String fileRoot = contextRoot+"resources/fileupload/";
        String fileRoot = "resources/fileupload/";
        String originalFileName = multipartFile.getOriginalFilename();                         //원본 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));  	   //파일 확장자
        String saveFileName = format.format(Calendar.getInstance().getTime()) + extension;     //저장할 파일 이름

        File file = new File(fileRoot + saveFileName);
        logger.info("원본 파일 명 : " + originalFileName);
        logger.info("저장할 파일 명 : " + saveFileName);
        logger.info("저장할 Root : " + fileRoot);

        try {
            InputStream inputStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, file);   //파일 저장
            jsonObject.addProperty("url", "/resources/fileupload/"+saveFileName);
            jsonObject.addProperty("responseCode", "success");
        }catch (IOException ie){
            FileUtils.deleteQuietly(file);    //실패시 저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            logger.error("파일 저장 실패 : "+ie.getMessage());
        }

        return jsonObject;
    }

    //첨부파일 다운로드
    @RequestMapping(value = "/fileDownload")
    public void fileDownload(Long nttId, HttpServletRequest req, HttpServletResponse resp) {
        logger.info("첨부파일 다운로드, 게시글 번호 :::: " + nttId);
        InfoShareDto dto = infoShareService.getInfoShareDetail(nttId);

        FileInputStream fis = null;
        OutputStream os = null;

        try {
            String contextRoot = new HttpServletRequestWrapper(req).getRealPath("/");
//            String pathName = contextRoot+"/resources/fileupload/" + dto.getSaveFileName();
            String pathName = "/resources/fileupload/" + dto.getSaveFileName();
            File file = new File(pathName);
            String originFileName = dto.getOriginFileName();
            String header = req.getHeader("User-Agent");
            String docName = "";
            if (header.contains("MSIE")) {
                docName = URLEncoder.encode(originFileName,"UTF-8").replaceAll("\\+", "%20");
                resp.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");
            } else if (header.contains("Firefox")) {
                docName = new String(originFileName.getBytes("UTF-8"), "ISO-8859-1");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
            } else if (header.contains("Opera")) {
                docName = new String(originFileName.getBytes("UTF-8"), "ISO-8859-1");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
            } else if (header.contains("Chrome")) {
                docName = new String(originFileName.getBytes("UTF-8"), "ISO-8859-1");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");
            }
            resp.setHeader("Content-Type", "application/octet-stream");
            resp.setHeader("Content-Transfer-Encoding", "binary;");
            resp.setHeader("Pragma", "no-cache;");
            resp.setHeader("Expires", "-1;");

            //resp.addHeader("Content-Disposition", "attachment;filename=" + originFileName);  //파일이 첨부파일임을 명시
            fis = new FileInputStream(file);
            os = resp.getOutputStream();
            FileCopyUtils.copy(fis, os);   //파일 복사 (생략시 빈 파일 다운로드 됨)

        }catch (Exception e) {
            logger.error("파일 다운로드 중 에러가 발생하였습니다. error ::: " +e.getMessage());
        }finally {
            if(fis != null) {
                try {
                    fis.close();
                    os.flush();
                }catch (Exception e){}
            }
        }
    }

    //첨부파일 삭제
    @RequestMapping(value = "/removeFile")
    @ResponseBody
    public Map<String, String> removeFile(Long nttId, String saveFileName) {
        logger.info("==== 첨부파일 삭제 ====");
        logger.info(String.format("nttId : %d, saveFileName : %s", nttId, saveFileName));

        if(nttId == null || nttId == 0)
            throw new RuntimeException("게시글이 존재하지 않습니다.");

        if(saveFileName == null || saveFileName.isEmpty())
            throw new RuntimeException("첨부파일이 존재하지 않습니다.");

        Map<String, String> map = new HashMap<>();

        long result = infoShareService.removeFile(nttId, saveFileName);
        if(result > 0)
            map.put("result", "success");
        else
            map.put("result", "fail");

        return map;
    }

    //xss방지 문자열 치환
    public String xssReplace(String param) {
        param = param.replaceAll("&", "&amp;");
        param = param.replaceAll("\"", "&quot;");
        param = param.replaceAll("'", "&apos;");
        param = param.replaceAll("<", "&lt;");
        param = param.replaceAll(">", "&gt;");
        param = param.replaceAll("\r", "<br>");
        param = param.replaceAll("\n", "<p>");

        return param;
    }
}
