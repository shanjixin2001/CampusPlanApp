package com.wzit.campusapi.controller;

import com.wzit.campusapi.entity.SubComtInfo;
import com.wzit.campusapi.entity.UserInfo;
import com.wzit.campusapi.service.SubComtInfoService;
import org.springframework.web.bind.annotation.*;
import com.wzit.campusapi.web.BaseController;
import com.wzit.campusapi.web.BaseEntity;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.wzit.campusapi.utils.ResultUtils.*;
import static com.wzit.campusapi.utils.ResultCode.*;
import static com.wzit.campusapi.utils.ResultMsg.*;
import static com.wzit.campusapi.utils.WzitUtils.*;

/**
 * 圈子子任务提交表(SubComtInfo)表控制层
 *
 * @author yyw
 */
@RestController
@RequestMapping("/subComtInfo")
public class SubComtInfoController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SubComtInfoService subComtInfoService;

    /**
     * 添加
     */
    @PostMapping("/insert")
    public Object insert(@RequestBody SubComtInfo subComtInfo) {
        subComtInfo.setCreateTime(new Date());
        if (SaTokenCheck() == 1) {
            SubComtInfo ins = subComtInfoService.insert(subComtInfo);
            if (ins == null) {
                return ResultErr(1);
            }
            return ResultOK(1, ins, 1);
        } else {
            return NoSatoken();
        }
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(@RequestBody SubComtInfo subComtInfo) {
        if (SaTokenCheck() == 1) {
            boolean b = subComtInfoService.delete(subComtInfo);
            if (!b) {
                return ResultErr(2);
            }
            return ResultOK(2, new HashMap<>(), 1);
        } else {
            return NoSatoken();
        }
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Object update(@RequestBody SubComtInfo subComtInfo) {
        if (SaTokenCheck() == 1) {
            boolean b = subComtInfoService.update(subComtInfo);
            if (!b) {
                return ResultErr(3);
            }
            return ResultOK(3, new HashMap<>(), 1);
        } else {
            return NoSatoken();
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/selectpage")
    public Object selectpage(@RequestBody SubComtInfo subComtInfo) {
        int pageNum = subComtInfo.getPageNum();
        List<SubComtInfo> list = subComtInfoService.selectpage(subComtInfo);
        return ResultApi(list, pageNum, subComtInfo.getPageSize());
    }

    /**
     * 查询所有
     */
    @PostMapping("/selectall")
    public Object selectall(@RequestBody SubComtInfo subComtInfo) {
        if (SaTokenCheck() == 1) {
            List<SubComtInfo> list = subComtInfoService.selectall(subComtInfo);
            if (list.size() <= 0) {
                return ResultErr(4);
            }
            return ResultOK(4, list, list.size());
        } else {
            return NoSatoken();
        }
    }

    /**
     * 查询数量
     */
    @PostMapping("/selectcount")
    public Object selectcount(@RequestBody SubComtInfo subComtInfo) {
        if (SaTokenCheck() == 1) {
            Integer count = subComtInfoService.selectcount(subComtInfo);
            if (count == null) {
                return ResultErr(4);
            }
            return ResultOK(count);
        } else {
            return NoSatoken();
        }
    }
}

