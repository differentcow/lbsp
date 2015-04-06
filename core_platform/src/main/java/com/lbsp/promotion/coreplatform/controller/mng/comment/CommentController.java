package com.lbsp.promotion.coreplatform.controller.mng.comment;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbsp.promotion.core.service.comment.CommentService;
import com.lbsp.promotion.coreplatform.controller.base.BaseController;
import com.lbsp.promotion.entity.base.PageResultRsp;
import com.lbsp.promotion.entity.constants.GenericConstants;
import com.lbsp.promotion.entity.request.CommentReq;
import com.lbsp.promotion.entity.response.CommentRsp;
import com.lbsp.promotion.util.validation.Validation;

/**
 * @author Barry
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentService<com.lbsp.promotion.entity.model.Comment> commentService;

    /**
     * 通过ID获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable(value = "id") Integer id) {
        CommentRsp rsp = commentService.getDetailById(id);
        return this.createBaseResult("查询成功", rsp);
    }

    /**
     * 获取信息集合(分页)
     *
     * @param startRecord
     * @param maxRecords
     * @param name
     * @param title
     * @param type
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = "/lst", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
                       @RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
                       @RequestParam(value = "name", required=false) String name,
                       @RequestParam(value = "title", required=false) String title,
                       @RequestParam(value = "type", required=false) Integer type,
                       @RequestParam(value = "from", required=false) Long from,
                       @RequestParam(value = "to", required=false) Long to) {

        if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
            startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
        if (Validation.isEmpty(maxRecords) || maxRecords < 1)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
        if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

        PageResultRsp page = commentService.getPageList(type, name,title,from,to,startRecord,maxRecords);
        return this.createBaseResult("查询成功", page.getResult(),page.getPageInfo());
    }

    /**
     * 获取信息集合(公共分页)
     *
     * @param startRecord
     * @param maxRecords
     * @param param
     * @param call
     * @param callId
     * @param type
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = "/common", method = RequestMethod.GET)
    @ResponseBody
    public Object commonList(@RequestParam(value = "iDisplayStart", required=false,defaultValue=DEFAULT_LIST_PAGE_INDEX) Integer startRecord,
                       @RequestParam(value = "iDisplayLength", required=false,defaultValue=DEFAULT_LIST_PAGE_SIZE) Integer maxRecords,
                       @RequestParam(value = "param", required=false) String param,
                       @RequestParam(value = "call", required=false) String call,
                       @RequestParam(value = "callId", required=false) Integer callId,
                       @RequestParam(value = "type", required=false) Integer type,
                       @RequestParam(value = "from", required=false) Long from,
                       @RequestParam(value = "to", required=false) Long to) {

        if (Validation.isEmpty(startRecord) || startRecord < GenericConstants.DEFAULT_LIST_PAGE_INDEX)
            startRecord = GenericConstants.DEFAULT_LIST_PAGE_INDEX;
        if (Validation.isEmpty(maxRecords) || maxRecords < 1)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_SIZE;
        if (maxRecords > GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE)
            maxRecords = GenericConstants.DEFAULT_LIST_PAGE_MAX_SIZE;

        PageResultRsp page = commentService.getPageList(call, callId, type, param, from,to,startRecord,maxRecords);
        return this.createBaseResult("查询成功", page.getResult(),page.getPageInfo());
    }

    /**
     * 删除信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delete(@RequestParam("ids")String ids) {
        String[] idStr = ids.split(",");
        List<Integer> idAry = new ArrayList<Integer>();
        for (String id : idStr){
            if(StringUtils.isNumeric(id)){
                idAry.add(Integer.valueOf(id));
            }
        }
        if(commentService.batchDeleteComment(idAry))
            return this.createBaseResult("删除成功", true);
        else
            return this.createBaseResult("删除失败", false);
    }

    /**
     * 批量更新信息状态
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/upt", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateStatus(HttpServletRequest request,@RequestBody CommentReq req) {

        if(commentService.batchUpdateCommentStatus(req.getIds(),req.getStatus(),System.currentTimeMillis(),getSessionUserId(request))) {
            return this.createBaseResult("更新成功", true);
        }else{
            return this.createBaseResult("更新失败", false);
        }
    }

}
